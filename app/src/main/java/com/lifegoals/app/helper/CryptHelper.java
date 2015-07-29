package com.lifegoals.app.helper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptHelper {
	private static final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public final static String encrypt(String target) {
		try {
			MessageDigest sh = MessageDigest.getInstance("SHA-512");
			sh.update(target.getBytes());

			return new String(encodeHex(sh.digest(), DIGITS_LOWER));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	private static char[] encodeHex(final byte[] data, final char[] toDigits) {

		// source
		// http://commons.apache.org/proper/commons-codec/apidocs/src-html/org/apache/commons/codec/binary/Hex.html#line.77
		final int l = data.length;
		final char[] out = new char[l << 1];
		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
			out[j++] = toDigits[0x0F & data[i]];
		}
		return out;
	}
}
