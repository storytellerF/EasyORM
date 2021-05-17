package com.storyteller.util;

import java.util.*;

public class EasyCache {
    private static final EasyCache easyCache = new EasyCache();
    private final Hashtable<String, List<?>> cache = new Hashtable<>(100);
    private final List<String> working = new Vector<>();//值正在工作的项目

    private EasyCache() {

    }

    public static EasyCache getEasyCache() {
        return easyCache;
    }

    /**
     * 通知将要执行的操作
     */
    public List<?> request(String request) throws InterruptedException {
        if (cache.containsKey(request)) {
            return cache.get(request);
        } else {
            while (working.contains(request)) {//等待，直到工作目录完成工作
                this.wait();
            }
            //当前不属于正在工作，也不再缓存中，所以返回null
            return cache.getOrDefault(request, null);
        }
    }

    public void tellWork(String parse) {
        working.add(parse);
    }

    public <RETURN_TYPE> void workDone(String parse, List<RETURN_TYPE> list) {
        cache.put(parse, list);
        working.remove(parse);
        synchronized (this) {

            this.notifyAll();
        }
    }
}
