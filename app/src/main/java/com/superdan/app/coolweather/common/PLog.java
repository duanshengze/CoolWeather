package com.superdan.app.coolweather.common;


import android.util.Log;

import com.superdan.app.coolweather.base.BaseApplication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by dsz on 16/3/16.
 */
public class PLog {
    public static final String PATH= BaseApplication.cacheDir+"/Log";
    public static  final  String  PLOG_FILE_NAME="log.txt";

    /**
     * 是否写入日志文件
     */
    public static final boolean PLOG_WRITE_TO_FILE=true;

    /**
     * 错误信息
     * @param TAG
     * @param msg
     */
    public final static void e(String TAG,String msg){

        Log.e(TAG, msg);
        if(PLOG_WRITE_TO_FILE)
            writeLogToFile("e",TAG,msg);

    }


    private static void writeLogToFile(String mylongtype,String tag,String msg){
        isExist(PATH);
        String needWriteMessage="\r\n"
                +Time.getNowMDHMSTime()
                +"\r\n"
                +mylongtype
                +"   "
                +tag
                +"\r\n"
                +msg;
        File file=new File(PATH,PLOG_FILE_NAME);
        try{
            FileWriter fileWriter=new FileWriter(file,true);
            BufferedWriter bufWriter=new BufferedWriter((fileWriter));
            bufWriter.write(needWriteMessage);
            bufWriter.newLine();
            bufWriter.close();
            fileWriter.close();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    /**
     *判断文件夹是否存在，如果不存在则创建文件夹
     */


    public static void isExist(String path){
        File file =new File(path);
        if(!file.exists()){
            file.mkdirs();
        }




    }


}
