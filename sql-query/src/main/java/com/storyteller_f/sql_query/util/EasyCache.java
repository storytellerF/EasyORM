package com.storyteller_f.sql_query.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EasyCache {
    private static EasyCache easyCache = new EasyCache();
    private HashMap<String, List<?>> cache = new HashMap<>(100);
    private ArrayList<String> working=new ArrayList<>();//值正在工作的项目

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
            if (cache.containsKey(request)) {
                return cache.get(request);
            } else {
                return null;//当前不属于正在工作，也不再缓存中，所以返回null
            }
        }
    }

    public void tellWork(String parse) {
        working.add(parse);
    }

    public <RETURN_TYPE> void workDone(String parse, List<RETURN_TYPE> list) {
        cache.put(parse, list);
        working.remove(parse);
    }
}
