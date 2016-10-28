package com.zsy.core;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

import com.xjb.volley.ParseError;
import com.xjb.volley.Response;
import com.xjb.volley.Response.ErrorListener;
import com.xjb.volley.Response.Listener;
import com.xjb.volley.VolleyError;

/**
 * 
 * @description: 防止更换网络连接库时不需要各个界面修改网络连接
 * @date: 2015-7-3 上午10:26:24
 * @author: wangqing
 * @version 1.0.0
 */
public class HostHelper {
	public static void sendGetCommand(Context context, String url, HostCallBack<? extends Object> callBack) {
		requestGetData(context, url, null, callBack);
	}

	public static void sendGetCommand(Context context, String url, Map<String, String> params,
			HostCallBack<? extends Object> callBack) {
		requestGetData(context, url, params, callBack);
	}

	public static void sendPostCommand(Context context, String url, Map<String, String> params,
			HostCallBack<? extends Object> callBack) {
		requestPostData(context, url, params, callBack);
	}

	public static <T> void requestGetData(final Context context, String url, final HostCallBack<T> callBack) {
		requestGetData(context, url, null, callBack);
	}

	public static <T> void requestGetData(final Context context, String url, Map<String, String> params,
			final HostCallBack<T> callBack) {
		XjbHttpClient.getInstance().getWithURL(url, null, params, new Listener<T>() {
			@Override
			public void onResponse(T response) {
				try {
					JSONObject jsonObj = new JSONObject((String) response);
					int code = jsonObj.getInt("code");
					if (code == 0) {
						callBack.onSuccess(response);
					} else {
						String desc = jsonObj.getString("message");
						Toast.makeText(context, desc, Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					try {
						Toast.makeText(context, "parse data error", Toast.LENGTH_SHORT).show();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if((error) instanceof ParseError){
					
				}
				try {
					if (error != null) {
						Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				callBack.onErrorResponse(error);
			}
		},new Response.ServerError<T>() {

			@Override
			public void onResponse(T arg0) {
				
			}
		});
	}

	public static <T> void requestPostData(final Context context, String url, Map<String, String> params,
			final HostCallBack<T> callBack) {
		XjbHttpClient.getInstance().postWithURL(url, null, params, new Listener<T>() {
			@Override
			public void onResponse(T response) {
				try {
					JSONObject jsonObj = new JSONObject((String) response);
					int code = jsonObj.getInt("code");
					if (code == 0) {
						callBack.onSuccess(response);
					} else {
						String desc = jsonObj.getString("message");
						Toast.makeText(context, desc, Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					try {
						Toast.makeText(context, "parse data error", Toast.LENGTH_SHORT).show();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				try {
					if (error != null) {
						Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				callBack.onErrorResponse(error);
			}
		},new Response.ServerError<T>() {

			@Override
			public void onResponse(T arg0) {
				// TODO Auto-generated method stub
				
			}
		});

	}

	public static class HostCallBack<T> {
		public void onFailure(Throwable t, int errorNo, String strMsg) {
		}

		public void onErrorResponse(VolleyError error) {
		}

		public void onSuccess(T response) {
		}

		public void onStart() {
		}

		public void onLoading(long count, long current) {
		}
	}
}
