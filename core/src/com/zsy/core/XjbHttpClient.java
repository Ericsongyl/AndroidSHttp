package com.zsy.core;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.widget.ImageView;
import cn.com.higi.dfp.DeviceUtil;

import com.xjb.volley.Cache;
import com.xjb.volley.DefaultRetryPolicy;
import com.xjb.volley.Request;
import com.xjb.volley.RequestQueue;
import com.xjb.volley.Response;
import com.xjb.volley.Response.ErrorListener;
import com.xjb.volley.Response.Listener;
import com.xjb.volley.Response.ServerError;
import com.xjb.volley.VolleyError;
import com.xjb.volley.toobox.ImageLoader;
import com.xjb.volley.toobox.ImageLoader.ImageCache;
import com.xjb.volley.toobox.ImageRequest;
import com.xjb.volley.toobox.NetworkImageView;
import com.xjb.volley.toobox.Volley;
import com.zsy.core.utils.Msg;
import com.zsy.core.utils.MyUtils;
import com.zsy.core.utils.RsaUtil;

/**
 * 
 * @description: 联网控制器 添加联网请求 以及图片缓存处理工具
 * @date: 2015-7-3 上午10:29:43
 * @author: wangqing
 * @version 1.0.0
 */

public class XjbHttpClient {
	private static XjbHttpClient mHttpClient;
	public RequestQueue mRequestQueue;// 请求队列
	public BitmapCache mBitmapCache = null;// 网络下载图片缓存容器
	public ImageLoader mImageLoader = null;// 网络图片下载器
	public Context con;

	public interface LoadCallBack {
		void success();

		void failed();

		void success(Bitmap bitmap);
	}

	public static XjbHttpClient getInstance() {
		if (mHttpClient == null) {
			mHttpClient = new XjbHttpClient();
		}
		return mHttpClient;
	}

	private XjbHttpClient() {
		super();
	}

	/**
	 * 
	 * @Title: @Description: 初始化HttpCLient @param: @param con @return: void @throws
	 */
	public void init(Context con) {
			this.con = con;
			mRequestQueue = Volley.newRequestQueue(con);
			mBitmapCache = BitmapCache.getInstance();
			mImageLoader = new ImageLoader(mRequestQueue, (ImageCache) mBitmapCache);
			mRequestQueue.start();
	}

	/**
	 * 清理硬盘缓存 cleanDiskCache
	 * 
	 * 
	 * void
	 * 
	 * 841306
	 * 
	 * @since 1.0.0
	 */
	public void cleanDiskCache() {
		mRequestQueue.getCache().clear();
	}

	/**
	 * 清理应用内存缓存 cleanLruCache
	 * 
	 * 
	 * void
	 * 
	 * 841306
	 * 
	 * @since 1.0.0
	 */
	public void cleanLruCache() {
		mBitmapCache.clean();
	}

	/**
	 * 断开一条请求 cancelRequest
	 * 
	 * @param tag
	 * 
	 *            void
	 * 
	 *            841306
	 * 
	 * @since 1.0.0
	 */
	public void cancelRequest(String tag) {
		mRequestQueue.cancelAll(tag);
	}
	
	/**
	 * 从缓存获取数据   如果没有则返回null
	 * @param url
	 * @return
	 */
	public String findCache(String url){
		Cache.Entry entry = mRequestQueue.getCache().get(url);
		try {
			if(entry ==null){
				return null;
			}
			String jsonString =
			            new String(entry.data, "UTF-8");
			return jsonString;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

	// 添加一个request请求
	public void addRequest(Request req) {
		mRequestQueue.add(req);
	}

	/**
	 * Converts <code>params</code> into an application/x-www-form-urlencoded
	 * encoded string.
	 */
	private String encodeParameters(Map<String, String> params, String paramsEncoding) {
		StringBuilder encodedParams = new StringBuilder();
		try {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
				encodedParams.append('=');
				encodedParams.append(URLEncoder.encode(entry.getValue() == null ? "" : entry.getValue(), paramsEncoding));
				encodedParams.append('&');
			}
//          return encodedParams.toString();
           return RsaUtil.getBodyStr(encodedParams.toString(), paramsEncoding);
		} catch (UnsupportedEncodingException uee) {
			throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
		}
	}

	/**
	 * 普通的json get请求 getWithURL
	 * 
	 * @param url
	 * @param class1
	 * @param params
	 * @param listener
	 * @param errorListener
	 * 
	 *            void
	 * 
	 *            841306
	 * 
	 * @since 1.0.0
	 */
	
	
	
	public <T> void getWithURL(String url, Class<?> class1, Map<String, String> params, Listener<T> listener,
			ErrorListener errorListener, ServerError<T> serverError) {
		getWithURL(url, class1, params, listener, errorListener,serverError, 60, 0, false);
	}
	
	public <T> void getWithURL(String url, Class<?> class1, Map<String, String> params, Listener<T> listener,
            ErrorListener errorListener, ServerError<T> serverError, boolean setCookie) {
        getWithURL(url, class1, params, listener, errorListener,serverError, 60, 0, false, setCookie);
    }

	/**
	 * 带超时时间和 重试次数的 是否缓存数据的请求 getWithURL
	 * 
	 * @param url
	 * @param class1
	 * @param params
	 * @param listener
	 * @param errorListener
	 * @param timeout
	 *            void 841306
	 * @since 1.0.0
	 */
	public <T> void getWithURL(String url, Class<?> class1, Map<String, String> params, Listener<T> listener,
			ErrorListener errorListener, ServerError<T> serverError,int timeout, int retrytime, boolean cached) {
		if (params != null && params.size() > 0) {
			if (url.contains("?")) {
				url = url + "&" + encodeParameters(params, "UTF-8");
			} else {
				url = url + "?" + encodeParameters(params, "UTF-8");
			}
		}
		cancelRequest(url);
		XjbJsonRequest jsObjRequest = new XjbJsonRequest(Request.Method.GET, class1, url, params, getHeaders(con),
				listener, errorListener,serverError);
		jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(timeout * 1000, retrytime, 0.0f));
		jsObjRequest.setShouldCache(cached);
		this.addRequest(jsObjRequest);
	}
	
	public <T> void getWithURL(String url, Class<?> class1, Map<String, String> params, Listener<T> listener,
            ErrorListener errorListener, ServerError<T> serverError,int timeout, int retrytime, boolean cached, boolean setCookie) {
        if (params != null && params.size() > 0) {
            if (url.contains("?")) {
                url = url + "&" + encodeParameters(params, "UTF-8");
            } else {
                url = url + "?" + encodeParameters(params, "UTF-8");
            }
        }
        cancelRequest(url);
        XjbJsonRequest jsObjRequest = new XjbJsonRequest(Request.Method.GET, class1, url, params, getHeaders(con),
                listener, errorListener,serverError);
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(timeout * 1000, retrytime, 0.0f));
        jsObjRequest.setShouldCache(cached);
        jsObjRequest.isSetCookie(setCookie);
        this.addRequest(jsObjRequest);
    }

	/**
	 * 
	 * getNoCheckCookie 返回数据时不检查cookie失效否
	 * 
	 * @param url
	 * @param class1
	 * @param params
	 * @param listener
	 * @param errorListener
	 *            void 841306
	 * @since 1.0.0
	 */
	public <T> void getNoCheckCookie(String url, Class<?> class1, Map<String, String> params, Listener<T> listener,
			ErrorListener errorListener,ServerError<T> serverError) {
		if (params != null && params.size() > 0) {
			if (url.contains("?")) {
				url = url + "&" + encodeParameters(params, "UTF-8");
			} else {
				url = url + "?" + encodeParameters(params, "UTF-8");
			}
		}
		cancelRequest(url);
		XjbJsonRequest jsObjRequest = new XjbJsonRequest(Request.Method.GET, class1, url, params, getHeaders(con),
				listener, errorListener,serverError);
		jsObjRequest.setCheckCookie(false);
		jsObjRequest.setShouldCache(false);
		this.addRequest(jsObjRequest);
	}

	/**
	 * 拼装cookie 以及其他header信息 getHeaders
	 * 
	 * @param con
	 * @return
	 * 
	 *         HashMap<String,String>
	 * 
	 *         841306
	 * 
	 * @since 1.0.0
	 */
	public HashMap<String, String> getHeaders(Context con) {
		HashMap<String, String> hs = new HashMap<String, String>();
		String imei = MyUtils.getDeviceId(con);
		if (imei == null || imei.equals("")) {
			imei = MyUtils.getMac().hashCode() + "12345";
		}
		hs.put("ihome-imei", imei);
		hs.put("ihome-os", "1");
		hs.put("ihome-version", MyUtils.getVersionName(con));
		hs.put("ihome-timestamp", MyUtils.getCurrentDate());
		hs.put("ihome-lng", InfoCache.getInstance().getLongtitude());
		hs.put("ihome-lat", InfoCache.getInstance().getLatitude());
		try{
		hs.put("ihome-deviceFinger", DeviceUtil.getDevicesInfo(con));
		}catch(Exception e){
			
		}
		String cookie = InfoCache.getInstance().getCookie();
		if (cookie != null && !cookie.equals("")) {
			hs.put("Cookie", cookie);
		}

		hs.put("uid", MyUtils.getUid(con));
		hs.put("appversion", MyUtils.getVersionName(con));// 目前网易灰度发布不能带android_
		return hs;
	}

	/**
	 * 普通的json post请求 postWithURL
	 * 
	 * @param url
	 * @param clazz
	 * @param params
	 * @param listener
	 * @param errorListener
	 * 
	 *            void
	 * 
	 *            841306
	 * 
	 * @since 1.0.0
	 */
	public <T> void postWithURL(String url, Class<?> clazz, Map<String, String> params, Response.Listener<T> listener,
			Response.ErrorListener errorListener,ServerError<T> serverError) {
		postWithURL(url, clazz, params, listener, errorListener,serverError, 60, 0, false);
	}
	
	/**
	 * post请求是否设置cookie值
	 * @param url
	 * @param clazz
	 * @param params
	 * @param listener
	 * @param errorListener
	 * @param serverError
	 * @param setCookie
	 */
	public <T> void postWithURL(String url, Class<?> clazz, Map<String, String> params, Response.Listener<T> listener,
            Response.ErrorListener errorListener,ServerError<T> serverError, boolean setCookie) {
        postWithURL(url, clazz, params, listener, errorListener,serverError, 60, 0, false, setCookie);
    }
	
	/**
	 * 带tag的请求
	 * @param tag
	 * @param url
	 * @param clazz
	 * @param params
	 * @param listener
	 * @param errorListener
	 * @param serverError
	 */
	public <T> void postWithURL(String tag,String url, Class<?> clazz, Map<String, String> params, Response.Listener<T> listener,
			Response.ErrorListener errorListener,ServerError<T> serverError) {
		postWithURL(tag,url, clazz, params, listener, errorListener,serverError, 60, 0, false);
	}
	
	/**
	 * 
	 * @param url
	 * @param clazz
	 * @param params
	 * @param listener
	 * @param errorListener
	 * @param timeout
	 * @param retrytime
	 * @param cached
	 *            void 841306
	 * @since 1.0.0
	 */
	public <T> void postWithURL(String tag,String url, Class<?> clazz, Map<String, String> params, Response.Listener<T> listener,
			Response.ErrorListener errorListener, ServerError<T> serverError,int timeout, int retrytime, boolean cached) {
		cancelRequest(tag);
		XjbJsonRequest jsObjRequest = new XjbJsonRequest(tag,Request.Method.POST, clazz, url, params, getHeaders(con),
				listener, errorListener,serverError);
		jsObjRequest.setShouldCache(cached);
		jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(timeout * 1000, retrytime, 0.0f));
		this.addRequest(jsObjRequest);
	}
	

	/**
	 * 带超时时间和 重试次数的 是否缓存数据的请求 postWithURL
	 * 
	 * @param url
	 * @param clazz
	 * @param params
	 * @param listener
	 * @param errorListener
	 * @param timeout
	 * @param retrytime
	 * @param cached
	 *            void 841306
	 * @since 1.0.0
	 */
	public <T> void postWithURL(String url, Class<?> clazz, Map<String, String> params, Response.Listener<T> listener,
			Response.ErrorListener errorListener, ServerError<T> serverError,int timeout, int retrytime, boolean cached) {
		cancelRequest(url);
		XjbJsonRequest jsObjRequest = new XjbJsonRequest(Request.Method.POST, clazz, url, params, getHeaders(con),
				listener, errorListener,serverError);
		jsObjRequest.setShouldCache(cached);
		jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(timeout * 1000, retrytime, 0.0f));
		this.addRequest(jsObjRequest);
	}
	
	/**
	 * post请求设置是否重新设置cookie值
	 * @param url
	 * @param clazz
	 * @param params
	 * @param listener
	 * @param errorListener
	 * @param serverError
	 * @param timeout
	 * @param retrytime
	 * @param cached
	 * @param setCookie
	 */
	public <T> void postWithURL(String url, Class<?> clazz, Map<String, String> params, Response.Listener<T> listener,
            Response.ErrorListener errorListener, ServerError<T> serverError,int timeout, int retrytime, boolean cached, boolean setCookie) {
        cancelRequest(url);
        XjbJsonRequest jsObjRequest = new XjbJsonRequest(Request.Method.POST, clazz, url, params, getHeaders(con),
                listener, errorListener,serverError);
        jsObjRequest.setShouldCache(cached);
        jsObjRequest.isSetCookie(setCookie);
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(timeout * 1000, retrytime, 0.0f));
        this.addRequest(jsObjRequest);
    }
	
	/**
	 * normal request 不对请求返回数据做任何处理 带回原始的数据
	 * @param url
	 * @param clazz
	 * @param params
	 * @param listener
	 * @param errorListener
	 * @param serverError
	 * @param timeout
	 * @param retrytime
	 * @param cached
	 */
	public <T> void postNormalURL(String url, Class<?> clazz, Map<String, String> params, Response.Listener<T> listener,
			Response.ErrorListener errorListenerr,int timeout, int retrytime, boolean cached) {
		cancelRequest(url);
		XjbNormalRequest jsObjRequest = new XjbNormalRequest(Request.Method.POST, clazz, url, params, getHeaders(con),listener, errorListenerr);
		jsObjRequest.setShouldCache(cached);
		jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(timeout * 1000, retrytime, 0.0f));
		this.addRequest(jsObjRequest);
	}
	
	public <T> void getNormalURL(String url, Class<?> clazz, Map<String, String> params, Response.Listener<T> listener,
			Response.ErrorListener errorListenerr,int timeout, int retrytime, boolean cached) {
		if (params != null && params.size() > 0) {
			if (url.contains("?")) {
				url = url + "&" + encodeParameters(params, "UTF-8");
			} else {
				url = url + "?" + encodeParameters(params, "UTF-8");
			}
		}
		cancelRequest(url);
		XjbNormalRequest jsObjRequest = new XjbNormalRequest(Request.Method.GET, clazz, url, params, getHeaders(con),listener, errorListenerr);
		jsObjRequest.setShouldCache(cached);
		jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(timeout * 1000, retrytime, 0.0f));
		this.addRequest(jsObjRequest);
	}


	/**
	 * 
	 * postNoCheckCookie 不检查cookie的post请求
	 * 
	 * @param url
	 * @param clazz
	 * @param params
	 * @param listener
	 * @param errorListener
	 *            void 841306
	 * @since 1.0.0
	 */
	public <T> void postNoCheckCookie(String url, Class<?> clazz, Map<String, String> params,
			Response.Listener<T> listener, Response.ErrorListener errorListener,ServerError<T> serverError) {
		cancelRequest(url);
		XjbJsonRequest jsObjRequest = new XjbJsonRequest(Request.Method.POST, clazz, url, params, getHeaders(con),
				listener, errorListener,serverError);
		jsObjRequest.setCheckCookie(false);
		jsObjRequest.setShouldCache(false);
		this.addRequest(jsObjRequest);
	}

	/**
	 * 图片联网请求 for NetworkImageView getImageForNIView
	 * 
	 * @param url
	 * @param imageView
	 * @param id
	 * 
	 *            void
	 * 
	 *            841306
	 * 
	 * @since 1.0.0
	 */
	public void getImageForNIView(String url, NetworkImageView imageView, int id) {
		imageView.setDefaultImageResId(id);
		imageView.setErrorImageResId(id);
		imageView.setImageUrl(url, mImageLoader);
	}

	/**
	 * 图片联网请求 for ImageView loadImageView
	 * 
	 * @param url
	 * @param imageView
	 * 
	 *            void
	 * 
	 *            841306
	 * 
	 * @since 1.0.0
	 */
	public void loadImageView(final String url, final ImageView imageView) {
		ImageRequest imgRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
			@Override
			public void onResponse(Bitmap bmp) {
				imageView.setImageBitmap(bmp);
			}
		}, 480, 360, Config.ARGB_4444, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {

			}
		});
		mRequestQueue.add(imgRequest);
	}

	public void loadImageView(final String url, final ImageView imageView, final LoadCallBack callback) {
		ImageRequest imgRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
			@Override
			public void onResponse(Bitmap bmp) {
				imageView.setImageBitmap(bmp);
				callback.success();
			}
		}, 480, 360, Config.ARGB_4444, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				callback.failed();
			}
		});
		mRequestQueue.add(imgRequest);
	}

	public void loadImageView(final String url, final LoadCallBack callback) {
		ImageRequest imgRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
			@Override
			public void onResponse(Bitmap bmp) {
				callback.success(bmp);
			}
		}, 480, 360, Config.ARGB_4444, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				callback.failed();
			}
		});
		mRequestQueue.add(imgRequest);
	}

}