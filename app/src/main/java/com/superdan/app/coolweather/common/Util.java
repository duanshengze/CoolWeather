package com.superdan.app.coolweather.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by dsz on 16/3/17.
 */
public class Util {


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
