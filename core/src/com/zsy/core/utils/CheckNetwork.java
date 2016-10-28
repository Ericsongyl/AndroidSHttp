package com.zsy.core.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 检测网络连接状态
 * @author sfhq965
 *
 */
public class CheckNetwork {
	
	/**
	 * 检测当前网络是否可以上网
	 * @param context
	 * @return
	 */
	public static boolean checkInternet(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			return true;
		} else {
			return false;
		}
	}
}
