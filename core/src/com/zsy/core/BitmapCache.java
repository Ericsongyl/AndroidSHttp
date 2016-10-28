package com.zsy.core;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.xjb.volley.toobox.ImageLoader.ImageCache;


/**
 * 
 * @description: 图片缓存
 * @date: 2015-7-3 上午10:25:57
 * @author: wangqing
 * @version 1.0.0
 */
@SuppressLint("NewApi")
public class BitmapCache implements ImageCache{

    private LruCache<String, Bitmap> mCache;
    private static BitmapCache cache;
    private  BitmapCache() {
        int maxSize = 4* 1024 * 1024;
        mCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }
    
    public static BitmapCache getInstance(){
    	if(cache ==null){
    		cache = new BitmapCache();
    	}
    	return cache;
    }
    
	public void clean(){
    	if(mCache!=null)
    		mCache.evictAll();
    }

    @Override
    public Bitmap getBitmap(String url) {
    	return mCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
    	mCache.put(url, bitmap);
    }
    

}