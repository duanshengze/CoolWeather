package com.superdan.app.coolweather.modules.ui.about;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.superdan.app.coolweather.R;
import com.superdan.app.coolweather.common.CheckVersion;
import com.superdan.app.coolweather.common.Util;

/**
 * Created by dsz on 16/3/25.
 */
public class AboutFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener{

    private final String INTRODUCTION="introduction";
    private final String CURRENT_VERSION="current_version";
    private final String SHARE="share";
    private final String STAR="Star";
    private final String ENCOURAGE="encourage";
    private final String BLOG="blog";
    private final String GITHUB="github";
    private final String EMAIL="email";
    private final String CHECK="check_version";


    private Preference mIntroduction;
    private Preference mVersion;
    private Preference mCheckVersion;
    private Preference mShare;
    private Preference mStar;
    private Preference mEncourage;
    private Preference mBlog;
    private Preference mGithub;
    private Preference mEmail;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.about);

        mIntroduction=findPreference(INTRODUCTION);
        mVersion=findPreference(CURRENT_VERSION);
        mCheckVersion=findPreference(CHECK);
        mShare=findPreference(SHARE);
        mStar=findPreference(STAR);
        mEncourage=findPreference(ENCOURAGE);
        mBlog=findPreference(BLOG);
        mGithub=findPreference(GITHUB);
        mEmail=findPreference(EMAIL);

        mIntroduction.setOnPreferenceClickListener(this);
        mVersion.setOnPreferenceClickListener(this);
        mCheckVersion.setOnPreferenceClickListener(this);
        mShare.setOnPreferenceClickListener(this);
        mStar.setOnPreferenceClickListener(this);
        mEncourage.setOnPreferenceClickListener(this);
        mBlog.setOnPreferenceClickListener(this);
        mGithub.setOnPreferenceClickListener(this);
        mEmail.setOnPreferenceClickListener(this);

        mVersion.setSummary(getResources().getString(R.string.version_name)+ Util.getVersion(getActivity()));

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key=preference.getKey();
        if(key.equals(CURRENT_VERSION)){
            new AlertDialog.Builder(getActivity()).setTitle("应用完成离不开开源项目支持，向以下致谢：")
                    .setMessage("Google Support Design，Gson,Rxjava，RxAndroid,Retrofit,"+
                            "Glide,systembartint")
                    .setPositiveButton("关闭",null)
                    .show();

        }else if(key.equals(SHARE)){
            Intent shaingIntent=new Intent(Intent.ACTION_SEND);
            shaingIntent.setType("text/plain");
            shaingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
            shaingIntent.putExtra(Intent.EXTRA_TEXT,"欢迎使用“酷儿天气”,希望心情每天是晴天");//TODO
            startActivity(Intent.createChooser(shaingIntent,getString(R.string.app_name)));
        }else if(key.equals(STAR)){


            new AlertDialog.Builder(getActivity()).setTitle("点赞")
                    .setMessage("去github给作者个Star，鼓励下段段")
                    .setNegativeButton("复制", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                copyToClipboard(getView(),getString(R.string.github_address));
                        }
                    }).setPositiveButton("打开", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Uri uri=Uri.parse(getString(R.string.github_address));
                    viewIntoWeb(uri);
                }
            }).show();
        }
        else  if(key.equals(INTRODUCTION)){
            String readme=getString(R.string.readme);
            Uri uri=Uri.parse(readme);
            viewIntoWeb(uri);

        }else if(key.equals(BLOG)){

            String readme=getString(R.string.blog_address);
            Uri uri=Uri.parse(readme);
          viewIntoWeb(uri);

        }else if(key.equals(GITHUB)){
            String github=getString(R.string.github_address);
            Uri uri=Uri.parse(github);
            viewIntoWeb(uri);
        }else if(key.equals(CHECK)){
            CheckVersion.checkVersion(getActivity(),getView());
            Snackbar.make(getView(),"正在检查更新...",Snackbar.LENGTH_SHORT).show();
        }else if(key.equals(EMAIL)){
            copyToClipboard(getView(),preference.getSummary().toString());
        }

        return false;
    }





    /**
      *复制到剪切板
      *@author duanshengze
      *created at 16/3/27 下午8:14
      *@params
      */
    private  void  copyToClipboard(View view,String info){
        ClipboardManager manager=(ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData=ClipData.newPlainText("msg",info);
        manager.setPrimaryClip(clipData);
        Snackbar.make(view,"已经复制到剪切板啦( •̀ .̫ •́ )✧",Snackbar.LENGTH_SHORT).show();
    }


    /**
      *浏览器访问
      *@author duanshengze
      *created at 16/3/27 下午9:21
      *@params
      */
    private  void  viewIntoWeb(Uri uri){

        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(uri);
        getActivity().startActivity(intent);


    }
}
