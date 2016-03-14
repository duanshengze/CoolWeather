package com.superdan.app.coolweather.base;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2016/3/14.
 */
public class BaseApplication extends Application {

    public static Context mAppContext=null;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext=getApplicationContext();
    }
}
