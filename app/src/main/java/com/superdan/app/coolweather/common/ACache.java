package com.superdan.app.coolweather.common;

import android.content.Context;

import com.superdan.app.coolweather.base.BaseApplication;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
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

    private  ACacheManager mACacheManager;



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
        mACacheManager =new ACacheManager(cacheDir,max_size,max_count);
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



        private File get(String key){
            File file=new File(key);
            Long currentTime=System.currentTimeMillis();
            file.setLastModified(currentTime);

            lastUsageDates.put(file, currentTime);
            return file;



        }

        private boolean remove(String key){
            File file=get(key);
            return file.delete();
        }


    }

    public boolean remove(String key){


        return mACacheManager.remove(key);
    }


    public void clear(){

        mACacheManager.clear();

    }


    //===========================
    //===String 数据 读写=========
    //============================
    public void put(String key,String value){

        File file= mACacheManager.newFile(key);
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
            mACacheManager.put(file);
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
        put(key,Utils.newStringWithDateInfo(saveTime,value));
    }



    public String getAsString(String key){
        File file= mACacheManager.get(key);
        if(!file.exists())return  null;
        boolean removeFile=false;
        BufferedReader in=null;

        try {

            in=new BufferedReader(new FileReader(file));
            String readString ="";
            String currentLine;
            while ((currentLine=in.readLine())!=null){
                readString +=currentLine;
            }

            if(!Utils.isDue(readString)){

                return Utils.clearDateInfo(readString);

            }else {
                removeFile=true;
                return null;


            }

        }catch (IOException e){
            e.printStackTrace();
            return null;
        }finally {
            if(in!=null){
                try{

                    in.close();


                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            if(removeFile) {

                remove(key);
            }

        }




    }



   //======================
   // ======JSONObject 数据 读写======
   // =====================
    /**
     * 保存JSONObject数据到缓存中
     * @param key 保存的key
     * @param value 保存的JSON数据
     *
     */
    public void put(String key,JSONObject value){

        put(key,value.toString());

    }

    /**
      *
      *@author duanshengze
      *created at 16/3/20 下午5:34
      *@param key 保存的key
     * @param value 保存的 JSONObject数据
     * @param saveTime 保存的时间，单位：秒
      */
   public void put(String key,JSONObject value,int saveTime){

       put(key,value.toString(),saveTime);

   }

    public  JSONObject getAsJSONObject(String key){

        String JSONString=getAsString(key);
        try{
            JSONObject obj=new JSONObject(JSONString);
            return obj;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }


    //========================
    //===============JSONArray 数据 读写============
    //=======================

    /**
      *
      *@author duanshengze
      *created at 16/3/20 下午5:38
      *@params key保存的key value 保存的值
      */
    public  void put (String key,JSONArray value){


        put(key,value.toString());
    }


    /**
      *
      *@author duanshengze
      *created at 16/3/20 下午5:40
      *@params
      */
    public void put(String key,JSONArray value,int saveTime){


        put(key,value.toString(),saveTime);


    }


    public  JSONArray getAsJSONArray(String key){

        String JSONString=getAsString(key);

        try{

            JSONArray obj=new JSONArray(JSONString);
            return  obj;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;

    }

    //========================
    //===============byte 数据 读写============
    //=======================
    /**
      *
      *@author duanshengze
      *created at 16/3/20 下午7:07
      *@params key 保存的key value 保存的数据
     *
      */
    public void put(String key,byte[]value){


        File file=mACacheManager.newFile(key);
        FileOutputStream out=null;
        try{
            out=new FileOutputStream(file);
            out.write(value);


        }catch (IOException e){
            e.printStackTrace();

        }finally {
            if (out!=null){
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            mACacheManager.put(file);
        }


    }
    
    
    
    /**
      *保存byte数据 到 缓存 中
      *@author duanshengze
      *created at 16/3/20 下午7:28
      *@params key 保存的key
     * value 保存的数据
     * saveTime 保存的时间,单位:秒
      */
    public  void put(String key,byte[]value,int saveTime){
        put(key,Utils.newByteArrayWithDateInfo(saveTime, value));


    }


    /**
      *
      *@author duanshengze
      *created at 16/3/20 下午11:59
      *@params
     * @return byte数据
      */

    public byte[]getAsBinary(String key){
        RandomAccessFile rAFile=null;
        boolean removeFile=flase;
        try{
            File file=mACacheManager.get(key);
            if(!file.exists())return null;
            rAFile=new RandomAccessFile(file,"r");
            byte[]byteArray=new byte[rAFile.length()];
            rAFile.read(byteArray);
            if(!Utils.isDue(byteArray)){
                return Utils.clearDateInfo(byteArray);
            }else {
                removeFile=true;
                return null;

            }


        }catch (Exception e){
            e.printStackTrace();
            return  null;

        }finally {
            if(rAFile!=null){
                try {
                    rAFile.close();
                }catch (IOException e){

                    e.printStackTrace();
                }


            }
            if(removeFile) remove(key);

        }


    }


    /**
      *
      *@author duanshengze
      *created at 16/3/20 下午5:43
      *@params
      */

    //========================
    //===============序列化 数据 读写============
    //=======================


    public void put(String key,Serializable value){
        put(key,value,-1);

    }
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

              put(key,value,saveTime);
            }else {
                put(key,data);
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

    public  Object getAsObject(String key){
        byte[]data=getAsBinary(key);
        if(data!=null){
            ByteArrayInputStream bais=null;
            ObjectInputStream ois=null'
            try{
                bais=new ByteArrayInputStream(data);
                ois=new ObjectInputStream(bais);
                Object reObject=ois.readObject();
                return  reObject;
            }catch (Exception e){

                e.printStackTrace();
                return  null;
            }finally {
                if(bais!=null){
                    try{
                        bais.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }


                }
                if(ois!=null){

                    try{
                        ois.close();
                    }catch (IOException e){


                        e.printStackTrace();
                    }

                }



            }





        }

        return null;
    }


    /**
     *@author duanshengze
     * @version 1.0
     * @title 时间计算工具
     */
    private static class Utils{


        final static  char SEPARATOR=' ';

        /**
         * 判断缓存的String数据是否到期
         *
         *  @return true ：到期 false：吧、没到期
         */
        private static boolean isDue(String str){

            return isDue(str.getBytes());



        }

        /**
         * 判断缓存的byte数据是否到期
         *@return true ：到期 false 过期
         */
        private static boolean isDue(byte[]data){
            String[] strs=getDateInfoFromDate(data);
            if (strs!=null&&strs.length==2){
                String saveTimeStr=strs[0];
                while (saveTimeStr.startsWith("0")){
                    saveTimeStr=saveTimeStr.substring(1,saveTimeStr.length());
                }
                long saveTime=Long.valueOf(saveTimeStr);
                long deleteAfter=Long.valueOf(strs[1]);
                if(System.currentTimeMillis()>saveTime+deleteAfter*1000){

                    return true;

                }


            }

            return false;

        }

        private static String[]getDateInfoFromDate(byte[]data){
            if(hasDateInfo(data)){
//System.currentTimeMills() 返回的时间是13位
                String saveDate=new String(copyOfRange(data,0,13));
                String deleteAfter=new String(copyOfRange(data,14,indexOf(data,SEPARATOR)));
                return new String[]{saveDate,deleteAfter};

            }
            return  null;


        }

        private static  byte[]copyOfRange(byte[]original,int from,int to){

            int newLength=to-from;
            if(newLength<0) throw  new IllegalArgumentException(from+">"+to);
            byte[]copy=new byte[newLength];
            System.arraycopy(original,from,copy,0,Math.max(original.length-from,newLength));
             return copy;

        }


        //TODO 不太清楚 为什么？
        //System.currentTimeMills()返回的时间位数13
        // "*************-** ";
        private static boolean hasDateInfo(byte[]data){

            return data!=null&&data.length>15&&data[13]=='-'&&indexOf(data,SEPARATOR)>14;


        }


        private static int indexOf(byte[]data,char c){
            for(int i=0;i<data.length;i++){

                if(data[i]==c){
                    return i;
                }


            }
            return -1;

        }

        private static  String newStringWithDateInfo(int second ,String strInfo){


            return createDateInfo(second)+strInfo;


        }

        private static  byte[] newByteArrayWithDateInfo(int second,byte[]data2){

                byte[]data1=createDateInfo(second).getBytes();
                byte[]retdata=new byte[data1.length+data2.length];
                System.arraycopy(data1,0,retdata,0,data1.length);
                System.arraycopy(data2,0,retdata,data1.length,data2.length);
                return retdata;

        }

        private static String createDateInfo(int second){

            String currentTime=System.currentTimeMillis()+"";
            while (currentTime.length()<13){

                currentTime="0"+currentTime;

            }
            return currentTime+"-"+second+SEPARATOR;
        }


        private static String clearDateInfo(String strInfo){
            if(strInfo!=null&&hasDateInfo(strInfo.getBytes())){
                strInfo=strInfo.substring(strInfo.indexOf(SEPARATOR)+1,strInfo.length());
            }
            return  strInfo;

        }


        private static  byte[] clearDateInfo(byte[]data){

            if(hasDateInfo(data)){

                return copyOfRange(data,indexOf(data,SEPARATOR)+1,data.length);
            }
            return  data;

        }



    }


}
