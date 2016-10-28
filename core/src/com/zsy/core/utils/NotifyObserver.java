package com.zsy.core.utils;


/**
 *
 * @description:
 * @date: 2015年11月09日 下午10:02:14
 * @author: chenpenglong
 * @version 1.0.0
 */
public interface NotifyObserver {
    void update(NotifyObservable observable, Object data);
}
