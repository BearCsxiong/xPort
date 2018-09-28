package me.csxiong.library.utils;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;


public class DigestUtils {

    /**
     * Used to build output as Hex
     */
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f'                         };

	private DigestUtils() {
		throw new IllegalStateException("you can't instantiate me!");
	}

    /**
     * encode By MD5
     * 
     * @param string
     * @return String
     */
	public static String md5_32(String string) {
		if(string==null)
			return "";
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10) hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}

	/**
	 * encode By MD5
	 *
	 * @param string
	 * @return String
	 */
	public static String md5_16(String string) {
		return md5_32(string).substring(8, 24);
	}

    /**
     * Converts an array of bytes into an array of characters representing the hexadecimal values of each byte in order.
     * The returned array will be double the length of the passed array, as it takes two characters to represent any
     * given byte.
     * 
     * @param data a byte[] to convert to Hex characters
     * @return A char[] containing hexadecimal characters
     */
    protected static char[] encodeHex(final byte[] data) {
        final int l = data.length;
        final char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_LOWER[0x0F & data[i]];
        }
        return out;
    }
    


	public static String bytes2Hex(byte[] bytes) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < bytes.length; n++) {
			stmp = (Integer.toHexString(bytes[n] & 0XFF));
			if (stmp.length() == 1) {
				hs += "0" + stmp;
			} else {
				hs += stmp;
			}
		}
		return hs.toLowerCase(Locale.CHINESE);
	}

	public static String getSHA1(String text) {
		String sha1 = null;
		if (TextUtils.isEmpty(text)) {
			return sha1;
		}
		MessageDigest sha1Digest = null;
		try {
			sha1Digest = MessageDigest.getInstance("SHA-1");
		} catch (Exception e) {
			return sha1;
		}
		byte[] textBytes = text.getBytes();
		sha1Digest.update(textBytes, 0, text.length());
		byte[] sha1hash = sha1Digest.digest();
		return bytes2Hex(sha1hash);
	}


}
