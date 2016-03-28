package com.superdan.app.coolweather.common;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.superdan.app.coolweather.base.BaseApplication;
import com.superdan.app.coolweather.modules.domain.Setting;
import com.superdan.app.coolweather.modules.ui.MainActivity;

/**
 * Created by dsz on 16/3/28.
 */
public class CheckLocation {
    private final static String TAG=CheckLocation.class.getSimpleName();
    private AMapLocationClient mLocationClient=null;
    private AMapLocationClientOption mLocationOption=null;
    private Setting mSetting=Setting.getsInstance();
    private OnLocationListener mOnLocationListener;
    private static CheckLocation sCheckLocation;


    public static CheckLocation getInstance(){
        if (sCheckLocation==null){
            sCheckLocation=new CheckLocation();
        }
        return sCheckLocation;
    }
    private CheckLocation(){
        mLocationClient=new AMapLocationClient(BaseApplication.mAppContext);
        mLocationClient.setLocationListener(new AMapLocationListener() {


            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if(mOnLocationListener!=null){
                    mOnLocationListener.onLocationChanged(aMapLocation);
                }
            }
        });
        mLocationOption=new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔 单位毫秒
        mLocationOption.setInterval(mSetting.getInt(Setting.AUTO_UPDATE,3)*Setting.ONE_HOUR*1000);
        mLocationClient.setLocationOption(mLocationOption);

    }


    public void startLocation(){

        mLocationClient.startLocation();

    }


    public void destoryLocation(){
        mLocationClient.onDestroy();

    }
    public void setOnLocationListener(OnLocationListener onLocationListener){
        mOnLocationListener=onLocationListener;
    }

    public interface  OnLocationListener{
        void onLocationChanged(AMapLocation aMapLocation);
    }
}
