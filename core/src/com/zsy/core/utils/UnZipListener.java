package com.zsy.core.utils;

public interface UnZipListener {
	public void start();
	public void progress(int step, int total);
	public void finished();
	public void error(int code, Throwable error);
}
