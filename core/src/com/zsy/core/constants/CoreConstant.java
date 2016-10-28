package com.zsy.core.constants;

import com.zsy.core.callback.IFragmentBundleCallback;
import com.zsy.core.callback.IFragmentCallback;
import com.zsy.core.callback.IModuleCallback;


/**
 * 
 * @description: 
 * @date: 2015-7-3 上午10:07:26
 * @author: wangqing
 * @version 1.0.0
 */
public class CoreConstant {
	public final static String DOWN_LOAD_MODULE = "com.myhome.broadcast.start_download_module";
	public final static String DOWN_LOAD_MODULE_CANCEL = "com.myhome.broadcast.cancel_download_module";
	public final static String NEED_RELOAD = "com.myhome.broadcast.need_reload";
	public final static String SET_GESTURE_PWD = "com.myhome.broadcast.set_gesture_pwd";
	public final static String MODIFY_GESTURE_PWD = "com.myhome.broadcast.modify_gesture_pwd";
	public final static String ERROR_MESSAGE = "com.myhome.broadcast.error_message";
	
	public final static String MODULE_KEY = "ModuleConfigKey";
	public final static String USER_INFO_KEY = "UserInfoKey";
	public final static String GENERAL_KEY = "GeneralKey";
	public static boolean isSetPwd=false;
	
	private static IModuleCallback moduleCallback;

	private static IFragmentBundleCallback bundleCallback;
	
	private static IFragmentCallback iFragmentCallback;
	
	public static IFragmentCallback getiFragmentCallback() {
		return iFragmentCallback;
	}

	public static void setiFragmentCallback(IFragmentCallback iFragmentCallback) {
		CoreConstant.iFragmentCallback = iFragmentCallback;
	}

	public static IModuleCallback getModuleCallback() {
		return moduleCallback;
	}

	public static void setModuleCallback(IModuleCallback moduleCallbacks) {
		moduleCallback = moduleCallbacks;
	}
	

	public static IFragmentBundleCallback getBundleCallback() {
		return bundleCallback;
	}

	public static void setBundleCallback(IFragmentBundleCallback bundleCallback) {
		CoreConstant.bundleCallback = bundleCallback;
	}

}
