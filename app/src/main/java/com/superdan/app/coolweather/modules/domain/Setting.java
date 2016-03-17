package com.superdan.app.coolweather.modules.domain;

import android.content.Context;
import android.content.SharedPreferences;

import com.superdan.app.coolweather.base.BaseApplication;

/**
 * 设置相关
 * Created by duanshengze on 2016/3/14.
 */
public class Setting {
    public static final String CHANGE_ICONS="change_icons";//切换图标
    public static  final String CLEAR_CACHE="clear_cache";//清除缓存
    public static  final String  AUTO_UPDATE="change_update_time";//自动更新时长
    public static  final  String  HOUR="小时";//当前小时
    public static  final String  HOUR_SELECT="hour_select";//设置更新频率的联动-需要改进
    public static final String API_TOKEN="c4a4e9ab87db9edf8d8d0039f2fbbca8";//fir.im api_token
    public static  String KEY="2c1227470d484679ba5cf914afc3929a";//和风天气的KEY

    public static  int ONE_HOUR=60*60;
    private static  Setting sInstance;
    private SharedPreferences mPrefs;

    public static Setting getsInstance(){

        if (sInstance==null){

            sInstance=new Setting(BaseApplication.mAppContext);

        }
        return  sInstance;

    }

    private  Setting(Context context){
        mPrefs=context.getSharedPreferences("setting",Context.MODE_PRIVATE);

    }


    public  Setting putBoolean(String key,boolean value){
        mPrefs.edit().putBoolean(key,value).apply();
        return this;
    }

    public  boolean getBoolean(String key,boolean def){
        return mPrefs.getBoolean(key,def);
    }

    public  Setting putInt(String key,int vaule){
        mPrefs.edit().putInt(key,vaule).apply();
        return  this;
    }

    public  int getInt(String key,int def){
        return mPrefs.getInt(key,def);
    }
    public Setting putString(String key,String value){

        mPrefs.edit().putString(key,value).apply();
        return this;
    }
    public String getString(String key,String def){

        return mPrefs.getString(key,def);

    }


}
