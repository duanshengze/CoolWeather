package com.superdan.app.coolweather.base;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

/**
 * Created by Administrator on 2016/3/14.
 */
public class BaseApplication extends Application {

    public static String cacheDir="";//缓存地址
    public static Context mAppContext=null;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext=getApplicationContext();

        //TODO 初始化retrofit

        if(getApplicationContext().getExternalCacheDir()!=null
                &&existSDCard()){
            //外部缓存地址
            cacheDir=getApplicationContext().getExternalCacheDir().toString();

        }else {
            //内部缓存
            cacheDir=getApplicationContext().getCacheDir().toString();
        }

    }

    private boolean existSDCard(){
        if(android.os.Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return  true;
        }else {
            return  false;
        }
    }
}
