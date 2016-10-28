package com.zsy.core.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0.0
 * @description:基于java Observable 添加了一些函数和判断条件
 * @date: 2015年11月09日 下午10:02:14
 * @author: chenpenglong
 */
public class NotifyObservable {
    private static NotifyObservable messageObservable = null;

    List<NotifyObserver> observers = new ArrayList<NotifyObserver>();

    boolean changed = false;
    /**
     * 获取单例对象
     */
    public static NotifyObservable getInstance() {
        if (messageObservable == null) {
            synchronized (NotifyObservable.class) {
                if (messageObservable == null) {
                    messageObservable = new NotifyObservable();
                }
            }
        }
        return messageObservable;
    }

    /**
     * 新添加方法
     * 无参数的通知所有NotifyObserver
     */
    public void notifyAllObservers() {
        setChanged();
        notifyObservers();
    }

    /**
     * 新添加方法
     * 无参数的通知所有NotifyObserver
     */
    public void notifyAllObservers(Object data) {
        setChanged();
        notifyObservers(data);
    }

    private void notifyObservers() {
        notifyObservers(null);
    }

    @SuppressWarnings("unchecked")
    private void notifyObservers(Object data) {
        int size = 0;
        NotifyObserver[] arrays = null;
        synchronized (this) {
            if (hasChanged()) {
                clearChanged();
                size = observers.size();
                arrays = new NotifyObserver[size];
                observers.toArray(arrays);
            }
        }
        if (arrays != null) {
            for (NotifyObserver observer : arrays) {
                //此处做非空判断，以防对象被回收，特别是按Home键退出应用
                if (observer != null) {
                    observer.update(this, data);
                }
            }
        }
    }

    public void addNotifyObserver(NotifyObserver observer) {
        if (observer == null) {
            throw new NullPointerException("observer == null");
        }
        synchronized (this) {
            if (!observers.contains(observer))
                observers.add(observer);
        }
    }

    public synchronized void deleteNotifyObserver(NotifyObserver observer) {
        observers.remove(observer);
    }

    public synchronized void deleteNotifyObservers() {
        observers.clear();
    }

    protected void clearChanged() {
        changed = false;
    }

    public int countNotifyObservers() {
        return observers.size();
    }

    public boolean hasChanged() {
        return changed;
    }


    protected void setChanged() {
        changed = true;
    }

    private NotifyObservable() {
    }

}


