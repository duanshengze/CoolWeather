package com.superdan.app.coolweather.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by dsz on 16/3/17.
 */
public class Util {


    public static  String getVersion(Context context){
        PackageManager mPackageManager=context.getPackageManager();
        try {
            PackageInfo info=mPackageManager.getPackageInfo(context.getPackageName(),0);
            String version=info.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "找不到版本号";

        }
    }


    /**
     *关注 是否联网
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context){
        if(context!=null){
            ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
            if(networkInfo!=null){

                return  networkInfo.isAvailable();
            }

        }


        return  false;

    }


}
