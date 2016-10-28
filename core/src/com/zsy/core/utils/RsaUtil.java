package com.zsy.core.utils;

import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import android.util.Base64;

import com.zsy.core.XjbHttpClient;

public class RsaUtil {

	public static final String defalutKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDUG9N5HzUmtxmUPG8ybEVUIQBHINR6S7RDY/sXusrwCap7hMBy6yGvU9Bda6rzhvkMP1ivA0HC0LCU0E6MkBxyv2RVa2QyJ3iZDeL1OeS+NKMAasOGEu1fGLceI70rGJTULyclUvx0/31o0l0Py5Mix4kZk8dHhzxTZcC4WlxLJQIDAQAB";
	public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

	public static PublicKey getPulicKey() throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		String publicK = PreferencesManager.getInstance(XjbHttpClient.getInstance().con).get("RSA", defalutKey);
		byte[] publicKeyBuffer = Base64.decode(publicK, Base64.DEFAULT);
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBuffer);
		PublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
		return publicKey;
	}

	public static String RSAEncrypt(final String plain) {
		return plain;
	}

	public static String encryptWithRsaOld(final String plain) {
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, getPulicKey());
			byte[] encryptedBytes = cipher.doFinal(plain.getBytes());
			byte[] encode = Base64.encode(encryptedBytes, Base64.NO_PADDING | Base64.NO_WRAP);
			String encryptedDataStr = new String(encode);
			return encryptedDataStr;
		} catch (Exception e) {
			e.printStackTrace();
			return plain;
		}
	}

	public static String encryptWithRsa(String plaintext) {
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, getPulicKey());
			byte[] plainBytes = plaintext.getBytes("utf-8");
			byte[] encryptedBytes = null;
			for (int i = 0; i < plainBytes.length; i += 100) {
				byte[] doFinal = cipher.doFinal(subarray(plainBytes, i, i + 100));
				encryptedBytes = addAll(encryptedBytes, doFinal);
			}
			return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] getBody(String encodedParams, String paramsEncoding) {
		try {
			return ("_e_=" + URLEncoder.encode(RsaUtil.encryptWithRsa(encodedParams), paramsEncoding))
					.getBytes(paramsEncoding);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getBodyStr(String encodedParams, String paramsEncoding) {
		try {
			return ("_e_=" + URLEncoder.encode(RsaUtil.encryptWithRsa(encodedParams), paramsEncoding));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] subarray(final byte[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return null;
		}
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive > array.length) {
			endIndexExclusive = array.length;
		}
		final int newSize = endIndexExclusive - startIndexInclusive;
		if (newSize <= 0) {
			return EMPTY_BYTE_ARRAY;
		}

		final byte[] subarray = new byte[newSize];
		System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
		return subarray;
	}

	public static byte[] addAll(final byte[] array1, final byte... array2) {
		if (array1 == null) {
			return clone(array2);
		} else if (array2 == null) {
			return clone(array1);
		}
		final byte[] joinedArray = new byte[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	public static byte[] clone(final byte[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

}
