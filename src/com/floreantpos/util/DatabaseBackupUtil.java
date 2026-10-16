package com.floreantpos.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.floreantpos.PosLog;

public class DatabaseBackupUtil {
	private static final int MAX_BACKUPS_TO_RETAIN = 7;
	private static boolean backupAttemptedToday = false;

	/**
	 * Initiates an asynchronous, non-blocking daily database backup if one hasn't been performed today.
	 */
	public static synchronized void triggerDailyAutoBackup() {
		if (backupAttemptedToday) {
			return;
		}
		backupAttemptedToday = true;

		Thread backupThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// Wait a few seconds after startup so UI initialization is completely smooth
					Thread.sleep(4000);
					performBackup();
				} catch (Throwable t) {
					PosLog.error(DatabaseBackupUtil.class, "Database auto-backup failed: " + t.getMessage());
				}
			}
		}, "DatabaseAutoBackupThread");

		backupThread.setDaemon(true);
		backupThread.start();
	}

	private static void performBackup() {
		File dbDir = findActiveDerbyDirectory();
		if (dbDir == null || !dbDir.exists() || !dbDir.isDirectory()) {
			return;
		}

		File backupDir = new File("database/backup");
		if (!backupDir.exists()) {
			backupDir.mkdirs();
		}

		String dateStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		File targetZip = new File(backupDir, "posdb-backup-" + dateStamp + ".zip");
		if (targetZip.exists()) {
			// Backup already exists for today
			return;
		}

		File tempZip = new File(backupDir, "posdb-backup-" + dateStamp + ".zip.tmp");

		try (FileOutputStream fos = new FileOutputStream(tempZip);
		     ZipOutputStream zos = new ZipOutputStream(fos)) {
			zipDirectory(dbDir, dbDir.getName(), zos);
			zos.finish();
		} catch (Exception e) {
			PosLog.error(DatabaseBackupUtil.class, "Failed writing database backup: " + e.getMessage());
			if (tempZip.exists()) {
				tempZip.delete();
			}
			return;
		}

		if (tempZip.renameTo(targetZip)) {
			PosLog.info(DatabaseBackupUtil.class, "Database auto-backup created successfully: " + targetZip.getAbsolutePath());
			purgeOldBackups(backupDir);
		} else {
			if (tempZip.exists()) {
				tempZip.delete();
			}
		}
	}

	private static File findActiveDerbyDirectory() {
		File singleDb = new File("database/derby-single/posdb");
		if (singleDb.exists() && singleDb.isDirectory()) {
			return singleDb;
		}
		File serverDb = new File("database/derby-server/posdb");
		if (serverDb.exists() && serverDb.isDirectory()) {
			return serverDb;
		}
		return null;
	}

	private static void zipDirectory(File folderToZip, String parentFolder, ZipOutputStream zos) throws IOException {
		File[] files = folderToZip.listFiles();
		if (files == null) {
			return;
		}

		byte[] buffer = new byte[8192];
		for (File file : files) {
			String entryName = parentFolder + "/" + file.getName();
			if (file.isDirectory()) {
				zipDirectory(file, entryName, zos);
			} else {
				// Skip temporary locks or active log locks if in-flight
				if (file.getName().endsWith(".lck")) {
					continue;
				}
				try (FileInputStream fis = new FileInputStream(file)) {
					zos.putNextEntry(new ZipEntry(entryName));
					int length;
					while ((length = fis.read(buffer)) > 0) {
						zos.write(buffer, 0, length);
					}
					zos.closeEntry();
				} catch (IOException ignored) {
					// In case a temporary file was deleted during copy, skip gracefully
				}
			}
		}
	}

	private static void purgeOldBackups(File backupDir) {
		File[] backups = backupDir.listFiles((dir, name) -> name.startsWith("posdb-backup-") && name.endsWith(".zip"));
		if (backups == null || backups.length <= MAX_BACKUPS_TO_RETAIN) {
			return;
		}

		Arrays.sort(backups, Comparator.comparingLong(File::lastModified));
		int toDelete = backups.length - MAX_BACKUPS_TO_RETAIN;
		for (int i = 0; i < toDelete; i++) {
			backups[i].delete();
		}
	}

	/**
	 * Cleanly shuts down the embedded Apache Derby engine to commit all pending transactions
	 * and release file handles.
	 */
	public static void shutdownDerby() {
		try {
			DriverManager.getConnection("jdbc:derby:;shutdown=true");
		} catch (SQLException se) {
			// SQLState 08006 or XJ015 confirms clean, expected shutdown in Derby
			String sqlState = se.getSQLState();
			if ("08006".equals(sqlState) || "XJ015".equals(sqlState)) {
				PosLog.info(DatabaseBackupUtil.class, "Apache Derby engine cleanly shut down.");
			}
		} catch (Throwable ignored) {
		}
	}
}
