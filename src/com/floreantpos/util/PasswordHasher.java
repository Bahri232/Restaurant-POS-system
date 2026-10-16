/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
package com.floreantpos.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import org.apache.commons.codec.binary.Hex;

import com.floreantpos.PosLog;

public class PasswordHasher {
	private static final String APP_SALT = "RenessPOS_Secured_2026_"; //$NON-NLS-1$

	public static void main(String[] args) throws Exception {
		PosLog.info(PasswordHasher.class, hashPassword("123")); //$NON-NLS-1$
	}

	/**
	 * Hashes password using salted SHA-256 for cryptographic strength.
	 */
	public static String hashPassword(String password) {
		if (password == null) {
			password = "";
		}
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256"); //$NON-NLS-1$
			md.update(APP_SALT.getBytes(StandardCharsets.UTF_8));
			byte[] hashBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
			return Hex.encodeHexString(hashBytes);
		} catch (Exception e) {
			PosLog.error(PasswordHasher.class, e.getMessage());
			return hashPasswordSha1(password);
		}
	}

	/**
	 * Legacy SHA-1 hash generator used for verifying pre-existing legacy hashes.
	 */
	public static String hashPasswordSha1(String password) {
		if (password == null) {
			password = "";
		}
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1"); //$NON-NLS-1$
			byte[] hashBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
			return Hex.encodeHexString(hashBytes);
		} catch (Exception e) {
			PosLog.error(PasswordHasher.class, e.getMessage());
			return "";
		}
	}

	/**
	 * Verifies a candidate raw password against a stored hash, supporting both
	 * modern salted SHA-256 and legacy SHA-1 hashes.
	 */
	public static boolean matchPassword(String storedHash, String rawPassword) {
		if (storedHash == null || rawPassword == null) {
			return false;
		}
		if (storedHash.equalsIgnoreCase(hashPassword(rawPassword))) {
			return true;
		}
		if (storedHash.equalsIgnoreCase(hashPasswordSha1(rawPassword))) {
			return true;
		}
		return false;
	}
}