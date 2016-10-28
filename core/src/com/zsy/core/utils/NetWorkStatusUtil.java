package com.zsy.core.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
/**
 * 检测wifi是否需要登陆
 * 实现方式来至Android4.0.1AOSP code
 * @link http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/4.0.1_r1/android/net/wifi/WifiWatchdogStateMachine.java#WifiWatchdogStateMachine.isWalledGardenConnection%28%29
 */
public class NetWorkStatusUtil extends AsyncTask<Integer, Integer, Integer>{
	public static final int NET_AVAILABLE = 0;//网络可用
	public static final int NET_NOT_AVAILABLE = 1;//网络不可用
	public static final int NET_NEED_LOGIN = 2;//网络需要授权登录
	private Context mContext;
    private NetWorkStatusCallBack callBack;
    
    public NetWorkStatusUtil(Context context,NetWorkStatusCallBack callBack) {
        super();
        this.callBack = callBack;
        mContext = context;
    }

    @Override
    protected Integer doInBackground(Integer... params) {
        return isWifiSetPortal(mContext);
    }

    @Override
    protected void onPostExecute(Integer result) {
        if (callBack != null) {
            callBack.netStatus(result);
        }
    }

    private int isWifiSetPortal(Context context) {  
    	ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
 		NetworkInfo info = cm.getActiveNetworkInfo();
 		if (info == null || !info.isConnected()) {
 			return NET_NOT_AVAILABLE;
 		}
        final String mWalledGardenUrl = "http://connect.rom.miui.com/generate_204";  
        final int WALLED_GARDEN_SOCKET_TIMEOUT_MS = 3000;  

        HttpURLConnection urlConnection = null;  
        try {  
            URL url = new URL(mWalledGardenUrl);  
            urlConnection = (HttpURLConnection) url.openConnection();  
            urlConnection.setInstanceFollowRedirects(false);  
            urlConnection.setConnectTimeout(WALLED_GARDEN_SOCKET_TIMEOUT_MS);  
            urlConnection.setReadTimeout(WALLED_GARDEN_SOCKET_TIMEOUT_MS);  
            urlConnection.setUseCaches(false);  
            urlConnection.getInputStream();  
            if(urlConnection.getResponseCode() == 204)
            	return NET_AVAILABLE;
            else if(urlConnection.getResponseCode() == 200)
            	return NET_NEED_LOGIN;
            else 
                return NET_NOT_AVAILABLE;  
        } catch (IOException e) {  
            return NET_NOT_AVAILABLE;  
        } finally {  
            if (urlConnection != null) {  
                urlConnection.disconnect();  
            }  
        }  
    }  

    /**
     * 检测网络状态,0:网络可用 1:网络不可用 2：网络需要授权登录
     * @param context
     * @param callBack
     * Administrator 2016年1月7日
     */
    public static void networkCheck(Context context,NetWorkStatusCallBack callBack) {
        new NetWorkStatusUtil(context,callBack).execute();
    }

    public interface NetWorkStatusCallBack{
        void netStatus(int netStatus);
    }
}