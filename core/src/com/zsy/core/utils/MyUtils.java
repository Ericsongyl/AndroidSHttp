package com.zsy.core.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * 
 * @description: 应用屏幕版本等
 * @date: 2015-7-3 上午10:30:48
 * @author: wangqing
 * @version 1.0.0
 */
public class MyUtils {
	private static float sPixelDensity = 1;
	
	public static String versionName = "1.0.0";
	
	public static int versionCode = 1;

	private static int sWidth;

	private static int sHeight;
	
	public static void init(Context context){
		try {
			PackageInfo mPackageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			versionCode = mPackageInfo.versionCode;
			versionName = mPackageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void getScreenSize(Context context) {
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metrics);
		sPixelDensity = metrics.density;
		sWidth = metrics.widthPixels < metrics.heightPixels ? metrics.widthPixels : metrics.heightPixels;
		sHeight = metrics.heightPixels > metrics.widthPixels ? metrics.heightPixels : metrics.widthPixels;

	}

	public static Point getScreenResolution(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display d = wm.getDefaultDisplay();
		Integer widthPixels = getScreenWidth(context);
		Integer heightPixels = getScreenHeight(context);
		if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17) {
			try {
				widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
				heightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
			} catch (Exception ignored) {
				ignored.printStackTrace();
			}
		} else if (Build.VERSION.SDK_INT >= 17) {
			try {
				Point realSize = new Point();
				Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
				widthPixels = realSize.x;
				heightPixels = realSize.y;
			} catch (Exception ignored) {
				ignored.printStackTrace();
			}
		}

		return new Point(widthPixels, heightPixels);
	}

	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	public static int dp2px(Context context, int dp) {
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		return (int) ((dp * displayMetrics.density) + 0.5);
	}

	public static int dpToPixel(Context context, int dp) {
		getScreenSize(context);
		return Math.round(sPixelDensity * dp);
	}

	public static int pixelToDp(Context context, int pixel) {
		getScreenSize(context);
		return Math.round(pixel / sPixelDensity);
	}

	public static int getScreenWidth(Context context) {
		getScreenSize(context);
		return sWidth;
	}

	public static int getScreenHeight(Context context) {
		getScreenSize(context);
		return sHeight;
	}

	public static float getPixelDensity() {
		return sPixelDensity;
	}

	public static float getScreenSizePhysics() {
		float size = (float) (Math.sqrt(Math.pow(sWidth, 2) + Math.pow(sHeight, 2)) / (sPixelDensity * 160));
		return size;
	}

	/**
	 * 获取应用CODE
	 * @param context
	 * @return
	 * Administrator 2015年11月25日
	 */
	public static int getVersionCode(Context context) {
		if(versionCode == 0){
			return getVersionCodeIfNull(context);
		}
		return versionCode;
    }
	/**
	 * 当静态变量被回收了 重新去取
	 * @param context
	 * @return
	 */
	public static int getVersionCodeIfNull(Context context){
		int version = 0;
		try {
			PackageInfo mPackageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			version = mPackageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}
	/**
	 * 当静态变量被回收了 重新去取
	 * @param context
	 * @return
	 */
	public static String getVersionIfNull(Context context){
		String version = "1.0.0";
		try {
			PackageInfo mPackageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			version = mPackageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}
    
	/**
	 * 获取应用版本号
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		if(versionName ==null){
			return getVersionIfNull(context);
		}
		return versionName;
	}
	
	

	/**
	 * 获取系统 imei号
	 * 
	 * @param act
	 * @return
	 */
	public static String getDeviceId(Context act) {
		TelephonyManager mTelephonyMgr = (TelephonyManager) act.getSystemService(Context.TELEPHONY_SERVICE);
		return mTelephonyMgr.getDeviceId();
	}

	// 获取时间戳
	public static String getCurrentDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		return formatter.format(new Date());
	}
	
	public static String getUid(Context con){
		String uid = null;
		ActivityManager am = (ActivityManager)con.getSystemService(Context.ACTIVITY_SERVICE);
		ApplicationInfo appinfo = con.getApplicationInfo();
		List<RunningAppProcessInfo> run = am.getRunningAppProcesses();
		for (RunningAppProcessInfo runningProcess : run) {
			if ((runningProcess.processName != null) && runningProcess.processName.equals(appinfo.processName)) {
				uid = String.valueOf(runningProcess.uid); 
				break;            
				}         
			} 
		uid = (getCurrentDate()+"_"+uid).replaceAll("-", "");
		
		return uid;
	}
	

	/**
	 * hashmap转json
	 * 
	 * @param map
	 * @return
	 */
	public static String hashMapToJson(HashMap map) {
		String string = "{";
		for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
			Entry e = (Entry) it.next();
			string += "'" + e.getKey() + "':";
			string += "'" + e.getValue() + "',";
		}
		string = string.substring(0, string.lastIndexOf(","));
		string += "}";
		return string;
	}

	public static String getMac() {
		String macSerial = "";
		String str = "";
		try {
			Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);

			for (; null != str;) {
				str = input.readLine();
				if (str != null) {
					macSerial = str.trim();// 去空格
					break;
				}
			}
		} catch (IOException ex) {
			// 赋予默认值
			ex.printStackTrace();
		}
		return macSerial;
	}

	/**
	 * 
	 * @param content
	 * @param fileName
	 * @return
	 */
	public static boolean saveFile(String content, String filePath, String fileName) {
		if (!TextUtils.isEmpty(content)) {
			try {
				return saveFile(content.getBytes("UTF-8"), filePath, fileName);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 指定文件名称保存文本内容到SD卡
	 * 
	 * @param bytes
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	public static boolean saveFile(byte[] bytes, String filePath, String fileName) {
		FileOutputStream output = null;
		try {
			if (bytes != null) {
				File filesDir = new File(filePath);
				if (!filesDir.exists()) {
					filesDir.mkdirs();
				}
				File file = new File(filePath, fileName);
				if (!file.exists()) {
					file.createNewFile();
				}
				output = new FileOutputStream(file);
				output.write(bytes);
				output.flush();
				return true;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (output != null)
					output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 按照指定文本读取文件
	 * 
	 * @param filePath
	 * @return
	 */
	public static String readFile(String filePath, String charset) {

		StringBuilder sb = new StringBuilder();
		try {
			FileInputStream fis = new FileInputStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis, charset));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 获取当前ip地址
	 * 
	 * @param context
	 * @return
	 */
	public static String getHostIp() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> ipAddr = intf.getInetAddresses(); ipAddr.hasMoreElements();) {
					InetAddress inetAddress = ipAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * getOtherScreenPXFromBase
	 * 
	 * @param context
	 * @param basePx
	 *            720*1080的相素值
	 * @return 返回其他屏幕的相素值 int chenpenglong
	 * @since 1.0.0
	 */
	public static int getOtherScreenPXFromBase(Context context, int basePx) {
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metrics);
		float desity = metrics.densityDpi;
		int dp = (int) (basePx / (desity / 160));
		return Math.round(metrics.density * dp);
	}
}
