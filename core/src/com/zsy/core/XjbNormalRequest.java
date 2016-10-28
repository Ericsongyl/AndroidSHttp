package com.zsy.core;

import java.util.Map;

import com.google.gson.Gson;
import com.xjb.volley.AuthFailureError;
import com.xjb.volley.DefaultRetryPolicy;
import com.xjb.volley.NetworkResponse;
import com.xjb.volley.ParseError;
import com.xjb.volley.Request;
import com.xjb.volley.Response;
import com.xjb.volley.toobox.HttpHeaderParser;

/**
 * 
 * @description:request请求类 
 * @date: 2015-7-3 上午10:29:57
 * @author: wangqing
 * @version 1.0.0
 */
public class XjbNormalRequest<T> extends EncryptRequest<T> {
    private final Response.Listener<T> mListener;
    private final Map<String, String> mParams;
    private Class<?> clazz;
    private  Map<String,String> mHeaders;
    private static final int cookie_timeout = 6;
    private boolean checkcookie = true;
    private String mUrl;

    public XjbNormalRequest(int method,Class<?> clazz, String url, Map<String, String> params,Map<String,String> headers,
                         Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mUrl = url;
        mListener = listener;
        mParams = params;
        this.clazz = clazz;
        this.mHeaders = headers;
        setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 0, 1.0f));//设置重试超时10秒
        setTag(url);//设置url为标记
        setShouldCache(false);

    }
    
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
    	if(mHeaders == null){
    		return super.getHeaders();
    	}else{
    		return mHeaders;
    	}
    }
    
    public void setCheckCookie(boolean b){
    	checkcookie = b;
    }
    

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }
    
	@SuppressWarnings("unchecked")
	@Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
		Gson gson = new Gson();
		try {
			Map<String, String> responseHeaders = response.headers;
			String rawCookies = responseHeaders.get("Set-Cookie");
			InfoCache.getInstance().setCookie(rawCookies);
			String jsonString = new String(response.data, "UTF-8");
			if (clazz == null) {
				return (Response<T>) Response.success(jsonString,
						HttpHeaderParser.parseCacheHeaders(response));
			} else {
				return (Response<T>) Response.success(
						gson.fromJson(jsonString, clazz),
						HttpHeaderParser.parseCacheHeaders(response));
			}

		} catch (Exception e) {
			return Response.error(new ParseError(e));
		}
    }

	@Override
	protected void deliverResponse(T response) {
		mListener.onResponse(response);
	}

	@Override
	protected void deliverServerError(T arg0) {
		// TODO Auto-generated method stub
		
	}

}
