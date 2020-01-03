/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.ejb.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 *
 * @author Root
 */
public class Security {

	private static final char[] HEXADECIMAL = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	static final String UCAL = "123456789ABCDEFGHJKLMNPQRSTUVWXYZ";
	static final String LCAL = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz";
	private static SecureRandom random = new SecureRandom();

	private static String randomStringPart(String characters) {
		StringBuilder sb = new StringBuilder();
		BigInteger bi = new BigInteger(130, random);
		BigInteger mod = BigInteger.valueOf(characters.length());
		while (bi.compareTo(BigInteger.ZERO) > 0) {
			BigInteger ival = bi.mod(mod);
			sb.append(characters.charAt(ival.intValue()));
			bi = bi.subtract(ival).divide(mod);
		}
		return sb.toString();
	}

	public static String randPassUCAL(int len) {
		StringBuilder sb = new StringBuilder();
		do {
			sb.append(randomStringPart(UCAL));
		} while (sb.length() <= len);
		return sb.toString().substring(0, len);
	}

	public static String randPass(int len) {
		StringBuilder sb = new StringBuilder();
		do {
			sb.append(randomStringPart(LCAL));
		} while (sb.length() <= len);
		return sb.toString().substring(0, len);
	}

	public static String hexEncode(byte[] bytes) {
		StringBuilder sb = new StringBuilder(2 * bytes.length);
		for (int i = 0; i < bytes.length; i++) {
			int low = (int) (bytes[i] & 0x0f);
			int high = (int) ((bytes[i] & 0xf0) >> 4);
			sb.append(HEXADECIMAL[high]);
			sb.append(HEXADECIMAL[low]);
		}
		return sb.toString();

	}

	public static String md5(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes("UTF-8"));
			return hexEncode(md.digest());
		} catch (Exception ex) {
			return null;
		}
	}
}
