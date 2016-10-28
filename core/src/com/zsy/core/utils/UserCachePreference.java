package com.zsy.core.utils;

import com.zsy.core.InfoCache;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * 自动区分用户的SharePreference工具类
 * 
 * @description:
 * @date: 2015年12月18日 上午10:04:45
 * @author: Administrator
 */
public class UserCachePreference {
	private static String shareName = "MODULE_DATA";
	private static SharedPreferences preferences;

	public static void cachePreference(Context context) {
		if (InfoCache.getInstance().getUserLoadInfo() != null)
			shareName = InfoCache.getInstance().getUserLoadInfo().data.customerId;
		else
			shareName = "DEFAULT_USER";
		preferences = context.getSharedPreferences(shareName, Context.MODE_PRIVATE);
	}

	public static void put(Context context, String key, String value) {
		cachePreference(context);
		SharedPreferences.Editor edit = preferences.edit();
		if (edit != null) {
			if (!TextUtils.isEmpty(key)) {
				key = key.toLowerCase();
			}
			edit.putString(key, value);
			edit.commit();
		}
	}

	public static String get(Context context, String key) {
		cachePreference(context);
		if (!TextUtils.isEmpty(key)) {
			key = key.toLowerCase();
		}
		return preferences.getString(key, "");
	}

	public static String get(Context context, String key, String defValue) {
		cachePreference(context);
		if (!TextUtils.isEmpty(key)) {
			key = key.toLowerCase();
		}
		return preferences.getString(key, defValue);
	}

}
