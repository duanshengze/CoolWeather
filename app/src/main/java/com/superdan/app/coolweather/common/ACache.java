package com.superdan.app.coolweather.common;

import android.content.Context;

import com.superdan.app.coolweather.base.BaseApplication;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.Buffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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



    public static ACache get(Context cts){
        return get(cts,"Data");
    }

    public static  ACache get(Context cts,String cacheName){
        File  f=new File(BaseApplication.cacheDir,cacheName);
        return get(f,MAX_SIZE,MAX_COUNT);
    }

    public static ACache get(Context ctx,long max_size,int  max_count){
        File f=new File(BaseApplication.cacheDir,"Data");
        return get(f,max_size,max_count);
    }

    public static  ACache get(File f,long max_size,int max_count){

        ACache aCache=mInstanceMap.get(f.getAbsoluteFile()+myPid());
        if(aCache==null){

            aCache=new ACache(f,max_size,max_count);
            mInstanceMap.put(f.getAbsolutePath()+myPid(),aCache);
        }
        return  aCache;
    }

    private  static  String myPid(){

        return"-"+android.os.Process.myPid();

    }

    private ACache(File cacheDir,long max_size,int max_count){
        if (!cacheDir.exists()&&!cacheDir.mkdirs()){
            throw  new RuntimeException("can't make dirs in"+cacheDir.getAbsolutePath());
        }
        mCache=new ACacheManager(cacheDir,max_size,max_count);
    }

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
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int size=0;
                    int count=0;
                    File[]cacheFiles=cacheDir.listFiles();
                    if(cacheFiles!=null){
                        for(File cacheeFile:cacheFiles){

                            size+=calculateSize(cacheeFile);
                            count+=1;
                            lastUsageDates.put(cacheeFile,cacheeFile.lastModified());

                        }
                        cacheSize.set(size);
                        cacheCount.set(count);
                    }

                }
            }).start();
        }

        private long calculateSize(File file){
            return file.length();
        }

        private void put(File file){
            int curCacheCount=cacheCount.get();
            while(curCacheCount+1>countLimit){
                long freedSize=removeNext();
                cacheSize.addAndGet(-freedSize);
                curCacheCount=cacheCount.addAndGet(-1);
            }
            cacheCount.addAndGet(1);

            long valueSize=calculateSize(file);
            long curCacheSize=cacheSize.get();
            while (curCacheSize+valueSize>countLimit){
                long freesSize=removeNext();
                curCacheSize=cacheSize.addAndGet(-freesSize);
            }
            cacheSize.addAndGet(valueSize);
            Long currentTime=System.currentTimeMillis();
            file.setLastModified(currentTime);
            lastUsageDates.put(file,currentTime);


        }

        /**
         *移除旧文件
         */
        private  long removeNext(){

            if(lastUsageDates.isEmpty()){
                return 0;
            }

            Long oldestUsage=null;
            File mostLongUsedFile=null;
            Set<Map.Entry<File,Long>>entries=lastUsageDates.entrySet();

                synchronized (lastUsageDates){
                    for(Map.Entry<File,Long>entry:entries){

                        if(mostLongUsedFile==null){
                            mostLongUsedFile=entry.getKey();
                            oldestUsage=entry.getValue();

                        }else {
                            Long  lastValueUsage=entry.getValue();
                            if(oldestUsage>lastValueUsage){

                                oldestUsage=lastValueUsage;
                                mostLongUsedFile=entry.getKey();
                            }


                        }


                    }
                }

            long fileSize=calculateSize(mostLongUsedFile);
            if (mostLongUsedFile.delete()){
                lastUsageDates.remove(fileSize);
            }
            return fileSize;

        }


        private File newFile(String key){
            return  new File(cacheDir,key.hashCode()+"");
        }

        private void clear(){
            lastUsageDates.clear();
            cacheSize.set(0);
            File[] file=cacheDir.listFiles();
            if(file!=null){
                for(int i=0;i<file.length;i++){
                    file[i].delete();
                }
            }
        }


    }



    //===========================
    //===String 数据 读写=========
    //============================
    public void put(String key,String value){

        File file=mCache.newFile(key);
        BufferedWriter out=null;
        try{
            out=new BufferedWriter(new FileWriter(file));
            out.write(value);
        }catch (Exception e){
            e.printStackTrace();

        }finally {
            if(out!=null){
                try {
                    out.flush();
                    out.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            mCache.put(file);
        }

    }
    /**
     * 保存String数据到缓存中
     * @param key 保存的key
     * @param value 保存的String数据
     * @param saveTime 保存的时间，单位秒
     */
    public  void put(String key,String value,int saveTime){
        put(key);


    }












    //========================
    //===============序列化 数据 读写============
    //=======================

    /**
     *保存Serializable数据到缓存中
     * @param  key 保存的key
     * @param  value 保存的value
     * @param  saveTime 保存的时间，单位：秒
     */
    public  void put(String key,Serializable value,int saveTime){
        ByteArrayOutputStream baos=null;
        ObjectOutputStream oos=null;

        try {
            baos=new ByteArrayOutputStream();
            oos=new ObjectOutputStream(baos);
            oos.writeObject(value);
            byte[]data=baos.toByteArray();

            if(saveTime!=-1){


            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                oos.close();
            }catch (IOException e){
                e.printStackTrace();
            }

        }


    }


    /**
     * 保存String数据到缓存中
     *
     * @param key 保存的key
     * @param value 保存的String数据
     * @param saveTime 保存的时间，单位:秒
     */
    public void put(String key,String value,int saveTime){
        put(Utils.newStringWithDateInfo(saveTime,value));
    }


    /**
     *@author duanshengze
     * @version 1.0
     * @title 时间计算工具
     */
    private static class Utils{






    }


}
