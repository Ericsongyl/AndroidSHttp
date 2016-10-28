package com.zsy.core;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import com.xjb.volley.NetworkResponse;
import com.xjb.volley.Request;
import com.xjb.volley.Response;
import com.xjb.volley.Response.ErrorListener;
import com.zsy.core.utils.Msg;
import com.zsy.core.utils.RsaUtil;

public class EncryptRequest<T> extends Request<T> {

	public EncryptRequest(int method, String url, ErrorListener listener) {
		super(method, url, listener);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void deliverResponse(T arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void deliverServerError(T arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	protected byte[] encodeParameters(Map<String, String> params, String paramsEncoding) {
		StringBuilder encodedParams = new StringBuilder();
		try {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
				encodedParams.append('=');
				encodedParams.append(URLEncoder.encode(entry.getValue() == null ? "" : entry.getValue(), paramsEncoding));
				encodedParams.append('&');
			}
//          return encodedParams.toString().getBytes(paramsEncoding);
           return RsaUtil.getBody(encodedParams.toString(), paramsEncoding);
		} catch (UnsupportedEncodingException uee) {
			throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
		}
	}

}
