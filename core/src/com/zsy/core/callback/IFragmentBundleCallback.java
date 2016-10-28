package com.zsy.core.callback;

import android.os.Bundle;
/**
 * 
 * @description: fragment 之间数据共享接口
 * @date: 2015-7-3 上午10:06:47
 * @author: wangqing
 * @version 1.0.0
 */
public interface IFragmentBundleCallback {
	public void setBundle(String key, Bundle bundle);

	public void setJSON(String key, String json);
	
	public void setString(String key, String str);

	public void setObject(String key, Object object);

	public Bundle getBundle(String key);

	public String getJSON(String key);
	
	public String getString(String key);

	public Object getObject(String key);
	

}
