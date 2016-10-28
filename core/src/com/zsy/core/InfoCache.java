package com.zsy.core;

import com.google.gson.Gson;
import com.zsy.core.bean.LoginBean;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

/**
 * 
 * @description: 非图片数据缓存类
 * @date: 2015-7-3 上午10:27:02
 * @author: wangqing
 * @version 1.0.0
 */
public class InfoCache {
	private static InfoCache infoCache;
	private LoginBean mLoadInfo;
	private String mCookieInfo;
	private SharedPreferences mSp;
	private Editor mEditor;
	private Context con;
	private String latitude = "";
	private String longtitude = "";

	private InfoCache() {

	}

	public static InfoCache getInstance() {
		if (infoCache == null) {
			infoCache = new InfoCache();
		}
		return infoCache;
	}
	
	public void init(Context con){
		
		this.con = con;
		mSp = con.getSharedPreferences("xjbcore",Context.MODE_PRIVATE);
		mEditor = mSp.edit();
		
	}
	
	public void setUserLoadInfo(LoginBean info){
		
		if(info==null||info.equals("")){
			return;
		}
		this.mLoadInfo = info;
		Gson gson = new Gson();
		String str = gson.toJson(info);
		String strBase64 = new String(Base64.encode(str.getBytes(), Base64.DEFAULT));
		mEditor.putString("loadInfo", strBase64);
		mEditor.commit();
		
	}
	
	    
	// 对base64加密后的数据进行解密
	public LoginBean getUserLoadInfo(){
		if(mLoadInfo==null||mLoadInfo.equals("")){
			Gson gson = new Gson();
			String info = mSp.getString("loadInfo", "");
			if(info.equals("")){
				return null;
			}
			try {
				info = new String(Base64.decode(info.getBytes(), Base64.DEFAULT));
				mLoadInfo = gson.fromJson(info, LoginBean.class);
			} catch (Exception e) {
				e.printStackTrace();
				clearUserLoadInfo();
				return null;
			}
			
		}
		return mLoadInfo;
	}
	
	public void clearUserLoadInfo(){
		mLoadInfo = null;
		mEditor.putString("loadInfo","");
		mEditor.putString("cookie", "");
		mEditor.commit();
	}
	
	/**
	 * 设置cookie信息
	 * @param t
	 */
	public void setCookie(String cookie) {
		if(cookie==null||cookie.equals("")){
			return;
		}
		this.mCookieInfo = cookie;
		mEditor.putString("cookie",cookie);
		mEditor.commit();
		
	}
	
	/**
	 * 获取cookie
	 * @return
	 */
	public  String getCookie(){
		if (mCookieInfo == null||mCookieInfo.equals("")) {
			mCookieInfo = mSp.getString("cookie", "");
		}
		return mCookieInfo;
		
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(String longtitude) {
		this.longtitude = longtitude;
	}
	
	

	

}