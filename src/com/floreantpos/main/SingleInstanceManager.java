package com.floreantpos.main;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Window;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.floreantpos.IconFactory;

/**
 * Enforces a strict single-instance application lock for Reness POS.
 * Prevents multiple windows/processes from running concurrently,
 * prevents database conflicts, brings existing instance to focus,
 * and displays an informational message when a second instance is launched.
 */
public class SingleInstanceManager {

	private static final int PREFERRED_PORT = 51966;
	private static final String LOCK_FILE_NAME = "app.lock"; //$NON-NLS-1$
	private static final String PORT_FILE_NAME = "app.port"; //$NON-NLS-1$

	private static File lockFile;
	private static File portFile;
	private static RandomAccessFile lockRaf;
	private static FileChannel lockChannel;
	private static FileLock fileLock;
	private static ServerSocket serverSocket;
	private static Thread ipcListenerThread;

	private static volatile boolean restorePending = false;
	private static PosWindow mainWindow;

	/**
	 * Attempts to acquire the single-instance lock.
	 *
	 * @return true if this process successfully acquired the lock (first instance),
	 *         false if an instance is already running (second instance).
	 */
	public static synchronized boolean acquireLock() {
		if (fileLock != null && fileLock.isValid()) {
			return true;
		}

		File lockDir = getLockDirectory();
		File lFile = new File(lockDir, LOCK_FILE_NAME);
		File pFile = new File(lockDir, PORT_FILE_NAME);

		// If another instance is actively responding on the IPC port, it is running!
		int activePort = readPortFromFile(pFile);
		if (activePort > 0 && isInstanceAlive(activePort)) {
			return false;
		}

		// Attempt to acquire the OS file lock.
		// If a previous instance just crashed or exited, the OS may take a few hundred ms to finish
		// releasing file handles, so we retry opening and locking up to 4 times.
		RandomAccessFile candidateRaf = null;
		FileChannel candidateChannel = null;
		FileLock candidateLock = null;

		for (int attempt = 0; attempt < 5; attempt++) {
			try {
				candidateRaf = new RandomAccessFile(lFile, "rw"); //$NON-NLS-1$
				candidateChannel = candidateRaf.getChannel();
				candidateLock = candidateChannel.tryLock();
				if (candidateLock != null) {
					break;
				}
			} catch (OverlappingFileLockException e) {
				candidateLock = null;
			} catch (IOException e) {
				// Sharing violation while Windows kernel finishes handle cleanup
				candidateLock = null;
			}

			closeSilently(candidateLock);
			closeSilently(candidateChannel);
			closeSilently(candidateRaf);
			candidateLock = null;
			candidateChannel = null;
			candidateRaf = null;

			// If active instance became responsive, abort immediately
			if (activePort > 0 && isInstanceAlive(activePort)) {
				return false;
			}

			try {
				Thread.sleep(250);
			} catch (InterruptedException ignored) {}
		}

		if (candidateLock == null) {
			closeSilently(candidateChannel);
			closeSilently(candidateRaf);
			return false;
		}

		// Lock acquired! Save to static fields, start IPC server, and register shutdown cleanup.
		lockFile = lFile;
		portFile = pFile;
		lockRaf = candidateRaf;
		lockChannel = candidateChannel;
		fileLock = candidateLock;

		startIpcServer();
		registerShutdownHook();

		return true;
	}

	/**
	 * Determines the directory to place lock and port files.
	 */
	private static File getLockDirectory() {
		File dir = new File(System.getProperty("user.home"), ".floreantpos"); //$NON-NLS-1$ //$NON-NLS-2$
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				dir = new File(System.getProperty("java.io.tmpdir"), ".floreantpos"); //$NON-NLS-1$ //$NON-NLS-2$
				dir.mkdirs();
			}
		}
		return dir;
	}

	/**
	 * Reads the port number from app.port.
	 */
	private static int readPortFromFile(File file) {
		if (file == null || !file.exists()) {
			return -1;
		}
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line = reader.readLine();
			if (line != null) {
				return Integer.parseInt(line.trim());
			}
		} catch (Exception ignored) {}
		return -1;
	}

	/**
	 * Tests if an instance is listening and responds on the given port.
	 */
	private static boolean isInstanceAlive(int port) {
		if (port <= 0) {
			return false;
		}
		try (Socket socket = new Socket()) {
			socket.connect(new InetSocketAddress("127.0.0.1", port), 400); //$NON-NLS-1$
			socket.setSoTimeout(400);
			OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8"); //$NON-NLS-1$
			writer.write("PING\n"); //$NON-NLS-1$
			writer.flush();

			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8")); //$NON-NLS-1$
			String response = reader.readLine();
			return "PONG".equalsIgnoreCase(response) || "OK".equalsIgnoreCase(response); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Starts the local loopback IPC server.
	 */
	private static void startIpcServer() {
		try {
			try {
				serverSocket = new ServerSocket(PREFERRED_PORT, 10, InetAddress.getByName("127.0.0.1")); //$NON-NLS-1$
			} catch (IOException ex) {
				serverSocket = new ServerSocket(0, 10, InetAddress.getByName("127.0.0.1")); //$NON-NLS-1$
			}

			int port = serverSocket.getLocalPort();
			try (FileOutputStream fos = new FileOutputStream(portFile)) {
				fos.write(String.valueOf(port).getBytes("UTF-8")); //$NON-NLS-1$
				fos.flush();
			}

			ipcListenerThread = new Thread(new Runnable() {
				@Override
				public void run() {
					listenForRequests();
				}
			}, "SingleInstanceIpcListener"); //$NON-NLS-1$
			ipcListenerThread.setDaemon(true);
			ipcListenerThread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Background listener loop for IPC commands from secondary instances.
	 */
	private static void listenForRequests() {
		while (serverSocket != null && !serverSocket.isClosed()) {
			try {
				Socket client = serverSocket.accept();
				client.setSoTimeout(3000);
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8")); //$NON-NLS-1$
					 OutputStreamWriter writer = new OutputStreamWriter(client.getOutputStream(), "UTF-8")) { //$NON-NLS-1$

					String command = reader.readLine();
					if ("PING".equalsIgnoreCase(command)) { //$NON-NLS-1$
						writer.write("PONG\n"); //$NON-NLS-1$
						writer.flush();
					} else if ("RESTORE".equalsIgnoreCase(command)) { //$NON-NLS-1$
						writer.write("OK\n"); //$NON-NLS-1$
						writer.flush();
						restoreExistingInstance();
					}
				} catch (Exception ignored) {
				} finally {
					try {
						client.close();
					} catch (Exception ignored) {}
				}
			} catch (Exception e) {
				if (serverSocket == null || serverSocket.isClosed()) {
					break;
				}
			}
		}
	}

	/**
	 * Called by the second instance to notify the primary instance to restore and focus its window.
	 */
	public static void notifyAndFocusExistingInstance() {
		try {
			File lockDir = getLockDirectory();
			File pFile = new File(lockDir, PORT_FILE_NAME);
			int port = readPortFromFile(pFile);
			if (port > 0) {
				try (Socket socket = new Socket()) {
					socket.connect(new InetSocketAddress("127.0.0.1", port), 2000); //$NON-NLS-1$
					socket.setSoTimeout(2000);
					OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8"); //$NON-NLS-1$
					writer.write("RESTORE\n"); //$NON-NLS-1$
					writer.flush();

					BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8")); //$NON-NLS-1$
					reader.readLine();
				}
			}
		} catch (Exception ignored) {}

		// Native Windows activation fallback as an additional guarantee
		activateWindowWindowsNative("Reness POS"); //$NON-NLS-1$
	}

	/**
	 * Restores and focuses the existing running application window.
	 */
	public static void restoreExistingInstance() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					PosWindow posWindow = (mainWindow != null) ? mainWindow : Application.getPosWindow();
					if (posWindow == null) {
						restorePending = true;
					} else {
						posWindow.restoreAndBringToFront();
					}

					// Un-minimize and focus any other open windows/dialogs
					Window[] windows = Window.getWindows();
					if (windows != null) {
						for (Window w : windows) {
							if (w.isDisplayable()) {
								if (w instanceof Frame) {
									Frame f = (Frame) w;
									int state = f.getExtendedState();
									if ((state & Frame.ICONIFIED) != 0) {
										f.setExtendedState(state & ~Frame.ICONIFIED);
									}
								}
							}
						}
						// Bring the active modal dialog to front if one exists
						for (int i = windows.length - 1; i >= 0; i--) {
							Window w = windows[i];
							if (w.isVisible() && w instanceof Dialog && ((Dialog) w).isModal()) {
								w.toFront();
								w.requestFocus();
								break;
							}
						}
					}
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		});
	}

	/**
	 * Called when PosWindow is instantiated and visible.
	 */
	public static void setMainWindow(PosWindow window) {
		mainWindow = window;
		if (restorePending) {
			restorePending = false;
			if (mainWindow != null) {
				mainWindow.restoreAndBringToFront();
			}
		}
	}

	public static boolean isRestorePending() {
		return restorePending;
	}

	/**
	 * Displays a dialog message informing the user that the application is already running.
	 */
	public static void showAlreadyRunningDialog() {
		try {
			try {
				com.formdev.flatlaf.FlatLightLaf.setup();
			} catch (Throwable ignored) {}

			ImageIcon icon = null;
			try {
				icon = IconFactory.getIcon("/ui_icons/", "icon.png"); //$NON-NLS-1$ //$NON-NLS-2$
			} catch (Throwable ignored) {}

			JOptionPane optionPane = new JOptionPane(
				"The application is already running.", //$NON-NLS-1$
				JOptionPane.INFORMATION_MESSAGE,
				JOptionPane.DEFAULT_OPTION,
				icon
			);

			// Style button height for POS touch ergonomics
			Object[] comps = optionPane.getComponents();
			if (comps != null) {
				for (Object obj : comps) {
					if (obj instanceof JPanel) {
						for (Component comp : ((JPanel) obj).getComponents()) {
							if (comp instanceof JButton) {
								comp.setPreferredSize(new Dimension(comp.getPreferredSize().width + 30, 45));
							}
						}
					}
				}
			}

			JDialog dialog = optionPane.createDialog(null, "Reness POS"); //$NON-NLS-1$
			dialog.setAlwaysOnTop(true);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			if (icon != null) {
				dialog.setIconImage(icon.getImage());
			}
			dialog.setLocationRelativeTo(null);
			dialog.setVisible(true);
		} catch (Throwable t) {
			JOptionPane.showMessageDialog(
				null,
				"The application is already running.", //$NON-NLS-1$
				"Reness POS", //$NON-NLS-1$
				JOptionPane.INFORMATION_MESSAGE
			);
		}
	}

	/**
	 * Releases lock files and socket upon shutdown.
	 */
	public static synchronized void releaseLock() {
		try {
			try {
				com.floreantpos.util.DatabaseBackupUtil.shutdownDerby();
			} catch (Throwable ignored) {}

			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (Exception ignored) {}
				serverSocket = null;
			}
			if (fileLock != null) {
				try {
					fileLock.release();
				} catch (Exception ignored) {}
				fileLock = null;
			}
			if (lockChannel != null) {
				try {
					lockChannel.close();
				} catch (Exception ignored) {}
				lockChannel = null;
			}
			if (lockRaf != null) {
				try {
					lockRaf.close();
				} catch (Exception ignored) {}
				lockRaf = null;
			}
			if (portFile != null && portFile.exists()) {
				portFile.delete();
				portFile = null;
			}
			if (lockFile != null) {
				lockFile = null;
			}
		} catch (Exception ignored) {}
	}

	private static void closeSilently(AutoCloseable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception ignored) {}
		}
	}

	private static void closeSilently(FileLock lock) {
		if (lock != null) {
			try {
				lock.release();
			} catch (Exception ignored) {}
		}
	}

	private static void registerShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				releaseLock();
			}
		}, "SingleInstanceCleanup")); //$NON-NLS-1$
	}

	private static void activateWindowWindowsNative(String windowTitlePattern) {
		try {
			String os = System.getProperty("os.name", "").toLowerCase(); //$NON-NLS-1$ //$NON-NLS-2$
			if (os.contains("win")) { //$NON-NLS-1$
				ProcessBuilder pb = new ProcessBuilder(
					"powershell", "-NoProfile", "-NonInteractive", "-Command", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					"(New-Object -ComObject WScript.Shell).AppActivate('" + windowTitlePattern + "')" //$NON-NLS-1$ //$NON-NLS-2$
				);
				pb.redirectErrorStream(true);
				Process p = pb.start();
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(1500);
							if (p.isAlive()) {
								p.destroy();
							}
						} catch (Exception ignored) {}
					}
				}).start();
			}
		} catch (Throwable ignored) {}
	}
}
