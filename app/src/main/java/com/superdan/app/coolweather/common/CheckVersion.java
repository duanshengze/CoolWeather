package com.superdan.app.coolweather.common;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.service.restrictions.RestrictionsReceiver;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.superdan.app.coolweather.base.BaseActivity;
import com.superdan.app.coolweather.base.BaseApplication;
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
    private static Setting mSetting=Setting.getsInstance();
    private long enqueueId;
    private DownloadManager mDownloadManager;
    private BroadcastReceiver receiver;
    private Context context;
    private boolean isRegisterReceiver=false;
    public CheckVersion(Context context){
        this.context=context;
        mDownloadManager=(DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        receiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long downloadCompleteId=intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,0);
                if (enqueueId!=downloadCompleteId){
                    return;
                }
                DownloadManager.Query query=new DownloadManager.Query();
                query.setFilterById(enqueueId);
                Cursor c=mDownloadManager.query(query);
                if(c.moveToFirst()){

                    int columnIndex=c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    if(DownloadManager.STATUS_SUCCESSFUL==c.getInt(columnIndex)){
                        String uriString=c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                        //提醒用户安装
                        promptInstall(Uri.parse("file://"+uriString));
                    }

                }


            }
        };

    }
    //注册广播，设置只接受下载完成的广播
    public  void registerReceiver(){
        isRegisterReceiver=true;
        context.registerReceiver(receiver,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

    public void unregisterReceiver(){
        if(isRegisterReceiver){
            context.unregisterReceiver(receiver);
        }

    }

    private void promptInstall(Uri data){
        Intent promptInstall=new Intent(Intent.ACTION_VIEW)
                .setDataAndType(data,"application/vnd.android.package-archive");
        //FLAG_ACTIVITY_NEW_TASK可以保证安装成功时可以正常打开app
        promptInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(promptInstall);
    }

    public  void checkVersion(final View view){
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
                            showUpdateDialog(versionAPI);
                        }else if(firVersionCode==currentVersionCode){
                            if(!currentVersionName.equals(firVersionName)){
                                showUpdateDialog(versionAPI);
                            }else {
                                new AlertDialog.Builder(context).setTitle("版本信息")
                                            .setMessage("当前版本为最新版本！")
                                        .show();

                            }
                        }
                    }
                });
    }

    private   void showUpdateDialog(final  VersionAPI versionAPI){
        String title="发现新版"+versionAPI.name+"版本号: "+versionAPI.versionShort;
        new AlertDialog.Builder(context).setTitle(title)
                .setMessage(versionAPI.changelog)
                .setPositiveButton("下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = Uri.parse(versionAPI.instanllUrl);
                        Log.e(TAG, versionAPI.instanllUrl);
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        // 获取下载队列 id
                        enqueueId = mDownloadManager.enqueue(request);
                        registerReceiver();


                    }
                })
                .setNegativeButton("忽略此版本", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mSetting.putString(Setting.IGNORE_VERSION, Setting.IGNORE);

                        unregisterReceiver();


                    }
        }).show();
    }



}
