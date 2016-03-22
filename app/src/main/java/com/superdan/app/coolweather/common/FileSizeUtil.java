package com.superdan.app.coolweather.common;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by dsz on 16/3/22.
 */
public class FileSizeUtil {
    public static final int SIZETYPE_B=1;//获取文件大小单位为B的double值
    public static final int SIZETYPE_KB=2;//获取文件大小单位为KB的double值
    public static  final  int SIZETYPE_MB=3;//获取文件大小单位为MB的double值
    public static final  int SIZETYPE_GB=4;//获取文件大小单位为GB的double值


    private static final long ONE_KB=1024;
    private static  final long ONE_MB=1024*1024;
    private static final long ONE_GB=1024*1024*1024;

    public static double getFileOrFilesSize(String filePath,int sizeType){
        File file=new File(filePath);

        long blockSize=0;
        try {
            blockSize = getFilesSize(file);

            if (file.isDirectory()) {


            } else {
                blockSize = getFileSize(file);


            }
        }catch (IOException e) {
            e.printStackTrace();
        }

        return formatFileSize(blockSize,sizeType);


    }
/**
  *调用此方法自动计算指定文件或文件夹得大小
  *@author duanshengze
  *created at 16/3/22 下午5:02
  *@params
  */
public  static String getAutoFileOrFileSize(String filePath){
    File file=new File(filePath);
    long blockSize=0;
    try{
        if(file.isDirectory()){
            blockSize=getFilesSize(file);
        }else {
            blockSize=getFileSize(file);

        }
    }catch (Exception e){

        e.printStackTrace();
        Log.e("获取文件大小","获取失败！");
    }
    return formatFileSize(blockSize);



}





    /**
      *
      *@author duanshengze
      *created at 16/3/22 下午4:24
      *@params
      */
    private static long getFilesSize(File file) throws IOException {

        long size=0;
        File[]flist=file.listFiles();

        for(int i=0;i<flist.length;i++){

            if (flist[i].isDirectory()){
                size+=getFilesSize(flist[i]);

            }else {
                size+=getFileSize(flist[i]);
            }

        }



        return size;

    }


    /**
      *获取指定文件的大小
      *@author duanshengze
      *created at 16/3/22 下午4:28
      *@params
      */
    private static long getFileSize(File file) throws IOException {
        long size=0;
        if(file.exists()){
            FileInputStream fis=new FileInputStream(file);
            size=fis.available();
        }else {
            file.createNewFile();
            Log.e("获取文件大小","文件不存在");


        }
        return size;

    }




    /**
      *
      *@author duanshengze
      *created at 16/3/22 下午3:57
      *@params
      */

    private static String formatFileSize(long fileS){

        DecimalFormat df=new DecimalFormat("#.00");
        String fileSizeString="";
        if (fileS==0){
            return "0B";
        }
        if (fileS<ONE_KB){
            fileSizeString=df.format((double)fileS)+"B";

        }else if (fileS<ONE_MB){
            fileSizeString=df.format((double)fileS/ONE_KB)+"KB";


        }else if (fileS<ONE_GB){
            fileSizeString=df.format((double)fileS/ONE_MB)+"MB";


        }else {

            fileSizeString=df.format((double)fileS/ONE_GB)+"GB";

        }
        return fileSizeString;

    }



/**
  *转换文件大小 指定转换的类型
  *@author duanshengze
  *created at 16/3/22 下午3:56
  *@params
  */
    private static  double formatFileSize(long fileS,int sizeType){
        DecimalFormat df=new DecimalFormat("#.00");
        double fileSizeLong=0;
        switch (sizeType){

            case SIZETYPE_B:

                fileSizeLong=Double.valueOf(df.format((double)fileS));
                break;
            case  SIZETYPE_KB:
                fileSizeLong=Double.valueOf(df.format((double)fileS/ONE_KB));
                break;
            case SIZETYPE_MB:
                fileSizeLong=Double.valueOf(df.format((double)fileS/(ONE_MB)));
                break;
            case  SIZETYPE_GB:
                fileSizeLong=Double.valueOf(df.format((double)fileS/(ONE_GB)));
                break;
            default:
                break;

        }
        return  fileSizeLong;


    }


}
