package com.zsy.core.callback;

import android.os.Bundle;

/**
 * 
 * @description: activity 模块跳转接口
 * @date: 2015-7-3 上午10:07:00
 * @author: wangqing
 * @version 1.0.0
 */
public interface IFragmentCallback {
	public void startActivity(String clazz);
	public void startActivity(String clazz,Bundle bundle);
	public void startActivity(String clazz,Bundle bundle,String bundleName);
	public void showToast(String message);
	public void closeActivity();
}
