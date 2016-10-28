package com.zsy.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Content:计算文件MD5值工具 
 * File: ModuleMD5.java
 * Date: 2015年7月2日 下午2:32:30
 * Author: bichuanfeng
 * @version 1.0.0
 */
public class MD5Utils {
	/**
	 * 获取单个文件的MD5值！
	 * 
	 * @param file
	 * @return 返回字符串
	 */
	public static String getFileMD5(File file) {
		if (!file.isFile()) {
			return null;
		}
		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = new byte[1024];
		int len;
		try {
			digest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}

	/**
	 * 获取文件夹中文件的MD5值
	 * 
	 * @param file
	 * @param listChild
	 *            ;true递归子目录中的文件
	 * @return
	 */
	public static Map<String, String> getDirMD5(File file, boolean listChild) {
		if (!file.isDirectory()) {
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		String md5;
		File files[] = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (f.isDirectory() && listChild) {
				map.putAll(getDirMD5(f, listChild));
			} else {
				md5 = getFileMD5(f);
				if (md5 != null) {
					map.put(f.getPath(), md5);
				}
			}
		}
		return map;
	}

	public static String getMd5(String str) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		byte[] bs = md5.digest(str.getBytes());
		StringBuilder sb = new StringBuilder(40);
		for(byte x:bs) {
			if((x & 0xff)>>4 == 0) {
				sb.append("0").append(Integer.toHexString(x & 0xff));
			} else {
				sb.append(Integer.toHexString(x & 0xff));
			}
		}
		return sb.toString();
	}
}
