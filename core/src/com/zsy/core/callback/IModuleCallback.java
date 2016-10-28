package com.zsy.core.callback;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * 
 * @description: 子模块页面切换接口
 * @date: 2015-7-3 上午10:07:14
 * @author: wangqing
 * @version 1.0.0
 */
public interface IModuleCallback {
	public void replaceFragment(Fragment f);
	public void replaceFragment(Fragment f,boolean setBack);
	public void replaceFragment(Fragment f,Bundle b,boolean setBack);
	public int onBackPress();
}
