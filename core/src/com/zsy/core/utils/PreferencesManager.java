package com.zsy.core.utils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

/**
 * 
 * @description: PreferencesManager管理类，提供get和put方法来重写SharedPreferences所提供的方法，更为实用和便捷
 * @date: 2015-7-3 上午10:30:38
 * @author: wangqing
 * @version 1.0.0
 */
@SuppressLint({ "SdCardPath", "DefaultLocale", "NewApi" })
public class PreferencesManager {
	private Context mContext;
	private SharedPreferences preferences;
	private String DATA_URL = "/data/data/";  
	private String SHARED_PREFS = "/shared_prefs";  
	private static String shareName = "SHARE_DATA";
	
	private static PreferencesManager instance;

	/**
	 * 构造方法
	 * 
	 * @param context
	 */
	private PreferencesManager(Context context) {
		this(context, shareName);
	}

	/**
	 * 构造方法
	 * 
	 * @param context
	 * @param shareName
	 */
	private PreferencesManager(Context context, String shareName) {
		mContext = context;
		preferences = context.getSharedPreferences(shareName,
				Context.MODE_PRIVATE);
	}

	/**
	 * 得到单例模式的PreferencesManager对象
	 * 
	 * @param context
	 *            上下文
	 * @return
	 */
	public static PreferencesManager getInstance(Context context) {
		return getInstance(context, shareName);
	}

	/**
	 * 得到单例模式的PreferencesManager对象
	 * 
	 * @param context
	 *            上下文
	 * @param shareName
	 *            文件名称
	 * @return
	 */
	public static PreferencesManager getInstance(Context context,
			String shareName) {
		if (instance == null) {
			synchronized (PreferencesManager.class) {
				if (instance == null) {
					instance = new PreferencesManager(context, shareName);
				}
			}
		}
		return instance;
	}

	public void put(String key, boolean value) {
		Editor edit = preferences.edit();
		if (edit != null) {
			if(!TextUtils.isEmpty(key)){
				key = key.toLowerCase();
			}
			edit.putBoolean(key, value);
			edit.commit();
		}
	}

	public void put(String key, String value) {
		Editor edit = preferences.edit();
		if (edit != null) {
			if(!TextUtils.isEmpty(key)){
				key = key.toLowerCase();
			}
			edit.putString(key, value);
			edit.commit();
		}
	}

	public void put(String key, int value) {
		Editor edit = preferences.edit();
		if (edit != null) {
			if(!TextUtils.isEmpty(key)){
				key = key.toLowerCase();
			}
			edit.putInt(key, value);
			edit.commit();
		}
	}

	public void put(String key, float value) {
		Editor edit = preferences.edit();
		if (edit != null) {
			if(!TextUtils.isEmpty(key)){
				key = key.toLowerCase();
			}
			edit.putFloat(key, value);
			edit.commit();
		}
	}

	public void put(String key, long value) {
		Editor edit = preferences.edit();
		if (edit != null) {
			if(!TextUtils.isEmpty(key)){
				key = key.toLowerCase();
			}
			edit.putLong(key, value);
			edit.commit();
		}
	}

	public void put(String key, Set<String> value) {
		Editor edit = preferences.edit();
		if (edit != null) {
			if(!TextUtils.isEmpty(key)){
				key = key.toLowerCase();
			}
			edit.putStringSet(key, value);
			edit.commit();
		}
	}

	/**
	 * 直接存放对象，反射将根据对象的属性作为key，并将对应的值保存。
	 * 
	 * @param t
	 */
	@SuppressWarnings("rawtypes")
	public <T> void put(T t) {
		try {
			String methodName = "";
			String savekey = "";
			String saveValue = "";
			Editor edit = preferences.edit();
			Class cls = t.getClass();

			if (edit != null) {
				Method[] methods = cls.getDeclaredMethods();

				for (Method method : methods) {

					methodName = method.getName();
					if (methodName != null && methodName.startsWith("get")) {

						Object value = method.invoke(t);
						if (!TextUtils.isEmpty(String.valueOf(value))) {
							saveValue = String.valueOf(value);
						}

						savekey = methodName.replace("get", "");
						savekey = savekey.toLowerCase();
						edit.putString(savekey, saveValue);
					}
				}
				edit.commit();
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public String get(String key) {
		if(!TextUtils.isEmpty(key)){
			key = key.toLowerCase();
		}
		return preferences.getString(key, "");
	}

	public String get(String key, String defValue) {
		if(!TextUtils.isEmpty(key)){
			key = key.toLowerCase();
		}
		return preferences.getString(key, defValue);
	}

	public boolean get(String key, boolean defValue) {
		if(!TextUtils.isEmpty(key)){
			key = key.toLowerCase();
		}
		return preferences.getBoolean(key, defValue);
	}

	public int get(String key, int defValue) {
		if(!TextUtils.isEmpty(key)){
			key = key.toLowerCase();
		}
		return preferences.getInt(key, defValue);
	}

	public float get(String key, float defValue) {
		if(!TextUtils.isEmpty(key)){
			key = key.toLowerCase();
		}
		return preferences.getFloat(key, defValue);
	}

	public long get(String key, long defValue) {
		if(!TextUtils.isEmpty(key)){
			key = key.toLowerCase();
		}
		return preferences.getLong(key, defValue);
	}

	@SuppressLint("NewApi")
	public Set<String> get(String key, Set<String> defValue) {
		if(!TextUtils.isEmpty(key)){
			key = key.toLowerCase();
		}
		return preferences.getStringSet(key, defValue);
	}

	/**
	 * 获取整个对象，跟put(T t)对应使用， 利用反射得到对象的属性，然后从preferences获取
	 * 
	 * @param cls
	 * @return
	 */
	public <T> Object get(Class<T> cls) {
		Object obj = null;
		String fieldName = "";
		try {
			obj = cls.newInstance();
			Field[] fields = cls.getDeclaredFields();
			for (Field f : fields) {
				fieldName = f.getName();
				if (!"serialVersionUID".equals(fieldName)) {
					f.setAccessible(true);
					f.set(obj, get(f.getName()));
				}
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return obj;
	}

	public void clearAll() {
		try {
			String fileName = shareName+".xml";
			StringBuilder path = new StringBuilder(DATA_URL).append(mContext.getPackageName()).append(SHARED_PREFS);
			File file = new File(path.toString(), fileName);
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
