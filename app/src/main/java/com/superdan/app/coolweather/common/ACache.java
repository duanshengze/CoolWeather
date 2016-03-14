package com.superdan.app.coolweather.common;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Administrator on 2016/3/14.
 */
public class ACache {
    public static final int TIME_HOUR=60*60;
    public  static final int TIME_DAY=TIME_HOUR*24;
    private static  final int MAX_SIZE=1000*1000*50;//50mb
    private static  final int MAX_COUNT=Integer.MAX_VALUE;//不限制存放个数
    private static Map<String,ACache> mInstanceMap=new HashMap<>();

    private  ACacheManager mCache;


    /**
     *@author duanshengze
     * @version 1.0
     * @title 缓存管理器
     */
    private class ACacheManager{
        private final AtomicLong cacheSize;
        private final AtomicInteger cacheCount;
        private final long sizeLimit;
        private final int countLimit;
        private final Map<File,Long>lastUsageDates= Collections.synchronizedMap(new HashMap<File, Long>());
        protected  File cacheDir;

        private ACacheManager(File cacheDir,long sizeLimit,int countLimit){
            this.cacheDir=cacheDir;
            this.sizeLimit=sizeLimit;
            this.countLimit=countLimit;
            cacheSize=new AtomicLong();
            cacheCount=new AtomicInteger();
            calculateCacheSizeAndCacheCount();
        }
        /**
         * 计算 cacheSize 和cacheCount
         */
        private  void  calculateCacheSizeAndCacheCount(){




        }


    }


}
