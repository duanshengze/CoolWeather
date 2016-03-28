package com.superdan.app.coolweather.common;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.superdan.app.coolweather.component.RetrofitSingleton;
import com.superdan.app.coolweather.modules.domain.Setting;
import com.superdan.app.coolweather.modules.domain.VersionAPI;
import com.superdan.app.coolweather.modules.ui.MainActivity;

import java.util.Observable;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/3/28.
 */
public class CheckVersion {

    private  static  String TAG=CheckVersion.class.getSimpleName();


    public static void checkVersion(final Context context,final View view){
        RetrofitSingleton.getApiService(context)
                            .mVersionAPI(Setting.API_TOKEN)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VersionAPI>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        RetrofitSingleton.disposeFailureInfo(e,context,view);
                    }

                    @Override
                    public void onNext(VersionAPI versionAPI) {
                        int firVersionCode=Integer.parseInt(versionAPI.version);
                        String firVersionName=versionAPI.versionShort;
                        int currentVersionCode=Util.getVersionCode(context);
                        String currentVersionName=Util.getVersion(context);
                        Log.e(TAG,"当前版本名为: "+currentVersionName+", 当前版本号为: "+currentVersionCode+"," +
                                "fir版本名为: "+firVersionName+", fir版本号为: "+firVersionCode);
                        if(firVersionCode>currentVersionCode){
                            showUpdateDialog(versionAPI,context);
                        }else if(firVersionCode==currentVersionCode){
                            if(!currentVersionName.equals(firVersionCode)){
                                showUpdateDialog(versionAPI,context);
                            }
                        }else {

                            if (context instanceof MainActivity){

                            }else {
                                Snackbar.make(view,"已经是最新版本",Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    }
                });


    }

    private  static  void showUpdateDialog(final  VersionAPI versionAPI,final Context context){
        String title="发现新版"+versionAPI.name+"版本号: "+versionAPI.versionShort;
        new AlertDialog.Builder(context).setTitle(title)
                .setMessage(versionAPI.changelog)
                .setPositiveButton("下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri=Uri.parse(versionAPI.updateUrl);
                        Intent intent=new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(uri);
                        context.startActivity(intent);
                    }
                })
        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show()
        ;




    }


}
