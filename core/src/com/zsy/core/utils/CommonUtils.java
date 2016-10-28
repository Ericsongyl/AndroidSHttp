package com.zsy.core.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Properties;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;


/**
 * 
 * @description: 
 * @date: 2015-7-3 上午10:08:34
 * @author: wangqing
 * @version 1.0.0
 */
public class CommonUtils {

	private static final String tag = CommonUtils.class.getSimpleName();
	
	/** 网络类型 **/ 
	public static final int NETTYPE_WIFI = 0x01;
	public static final int NETTYPE_CMWAP = 0x02;
	public static final int NETTYPE_CMNET = 0x03;
	
	/**
	 * 根据key获取config.properties里面的boolean值
	 * @param context
	 * @param key
	 * @return
	 */
	public static boolean getBooleProperty(Context context, String key){
		try {
			Properties props = new Properties();
			InputStream input = context.getAssets().open("config.properties");
			if (input != null) {
				props.load(input);
				if(props.getProperty(key).equals("true")){
					return true;
				}else{
					return false;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * 根据key获取config.properties里面的值
	 * @param context
	 * @param key
	 * @return
	 */
	public static String getProperty(Context context, String key){
		try {
			Properties props = new Properties();
			InputStream input = context.getAssets().open("config.properties");
			if (input != null) {
				props.load(input);
				return props.getProperty(key);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 检测网络是否可用
	 * 
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}
	
	/**
	 * 获取当前网络类型
	 * 
	 * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
	 */
	public static int getNetworkType(Context context) {
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			String extraInfo = networkInfo.getExtraInfo();
			if (!TextUtils.isEmpty(extraInfo)) {
				if (extraInfo.toLowerCase(Locale.getDefault()).equals("cmnet")) {
					netType = NETTYPE_CMNET;
				} else {
					netType = NETTYPE_CMWAP;
				}
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NETTYPE_WIFI;
		}
		return netType;
	}
	
	
	/**
	 * 判断SDCard是否存在,并可写
	 * 
	 * @return
	 */
	public static boolean checkSDCard(){
		String  flag = Environment.getExternalStorageState();
		if(android.os.Environment.MEDIA_MOUNTED.equals(flag)){
			return true;
		}
		return false;
	}
	
	/**
	 * 获取屏幕宽度
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context){
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm.widthPixels;
	}
	
	/**
	 * 获取屏幕高度
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context){
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm.heightPixels;
	}
	
	/**
	 * 获取屏幕显示信息对象
	 * @param context
	 * @return
	 */
	public static DisplayMetrics getDisplayMetrics(Context context){
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm;
	}
	
	/**
	 * dp转pixel
     */
	public static float dpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }

    /**
     * pixel转dp
     */
    public static float pixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return px / (metrics.densityDpi / 160f);
    }
    
	
	/**
	 * 短信分享
	 * 
	 * @param mContext
	 * @param smstext 短信分享内容
	 * @return
	 */
	public static Boolean sendSms(Context mContext, String smstext){
		Uri smsToUri = Uri.parse("smsto:");
		Intent mIntent = new Intent(android.content.Intent.ACTION_SENDTO, smsToUri);
		mIntent.putExtra("sms_body", smstext);
		mContext.startActivity(mIntent);
		return null;
	}
	
	/**
	 * 邮件分享
	 * 
	 * @param mContext
	 * @param title 邮件的标题
	 * @param text 邮件的内容
	 * @return
	 */
	public static void sendMail(Context mContext, String title, String text){
		// 调用系统发邮件
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		// 设置文本格式
		emailIntent.setType("text/plain");
		// 设置对方邮件地址
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, "");
		// 设置标题内容
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
		// 设置邮件文本内容
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
		mContext.startActivity(Intent.createChooser(emailIntent, "Choose Email Client"));
	}
	
	/**
	 * 隐藏软键盘
	 * @param activity
	 */
	public static void hideKeyboard(Activity activity) {
		if(activity != null){
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			if(imm!=null&&imm.isActive()){
				imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
			}
		}
	}
	
	/**
	 * 显示软键盘
	 * @param activity
	 */
	public static void showKeyboard(Activity activity) {
		if(activity != null){
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			if(!imm.isActive()){
				imm.showSoftInputFromInputMethod(activity.getCurrentFocus().getWindowToken(), 0);
			}
		}
	}
	
	/**
	 * 是否横屏
	 * @param context
	 * @return true为横屏，false为竖屏
	 */
	public static boolean isLandscape(Context context){
		if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){  
			return true;
		}  
		return false;
	}
	
	/**
	 * 判断是否是平板
	 * 这个方法是从 Google I/O App for Android 的源码里找来的，非常准确。
	 * @param context
	 * @return
	 */
	public static boolean isTablet(Context context) {       
		return (context.getResources().getConfiguration().screenLayout 
				& Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE; 
	} 

	/**
	 * 是否需要改变布局
	 * 说明：针对我们的项目，如果是平板横屏才显示最右边fragment区域
	 * 平板竖屏以及手持设备都不显示最右边fragment区域
	 * @param context
	 * @return
	 */
	public static boolean isChangeLayout(Context context){
		if(isTablet(context) && isLandscape(context)){
			return true;
		}
		return false;
	}
	
	/**
	 * 安装APK
	 * @param context
	 * @param storePath
	 * @param fileName
	 */
	public static void archiveAPK(Context context, String storePath, String fileName){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 加这个标志，安装完毕后才会提示已安装成功，否则不会提示。chenwenhan 20141101
		intent.setDataAndType(Uri.fromFile(new File(storePath, fileName)), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}
	
	/**
	 * 安装APK
	 * @param context
	 * @param filePath
	 */
	public static void archiveAPK(Context context, String filePath){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);	// 加这个标志，安装完毕后才会提示已安装成功，否则不会提示。chenwenhan 20141101
		intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}
	
	/**
	 * 静默卸载
	 * @param packageName
	 * @return result
	 */
	public static int uninstallAPKSilently(String packageName) {
		int result = -1;
		Process process = null;

		try {			
			process = Runtime.getRuntime().exec("per-up");
			DataOutputStream dataOutputStream = new DataOutputStream(process.getOutputStream());			
			dataOutputStream.writeBytes("LD_LIBRARY_PATH=/vendor/lib:/system/lib pm uninstall " +
					packageName);	
			dataOutputStream.flush();
			dataOutputStream.close();
			BufferedReader successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			String s;
			while ((s = successResult.readLine()) != null) {
				if (s.contains("Success")){
					result = 0;
				}
			}
			while ((s = errorResult.readLine()) != null) {
				if (s.contains("Failure")){
					result = -1;	
				}
			}
			process.waitFor();		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
