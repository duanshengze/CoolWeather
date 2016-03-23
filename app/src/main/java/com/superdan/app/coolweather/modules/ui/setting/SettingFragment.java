package com.superdan.app.coolweather.modules.ui.setting;

import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;

import com.superdan.app.coolweather.R;
import com.superdan.app.coolweather.base.BaseApplication;
import com.superdan.app.coolweather.common.ACache;
import com.superdan.app.coolweather.common.FileSizeUtil;
import com.superdan.app.coolweather.modules.domain.Setting;

/**
 * Created by dsz on 16/3/21.
 */
public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener{

    private static String TAG=SettingFragment.class.getSimpleName();

    private Setting mSetting;
    private Preference mChangeIcons;
    private Preference mChangeUpdate;
    private Preference mClearCache;
    private ACache mACache;
    private static final String DIR=BaseApplication.cacheDir+"/Data";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        mSetting=Setting.getsInstance();
        mACache=ACache.get(getActivity());
        mChangeIcons=findPreference(Setting.CHANGE_ICONS);
        mChangeUpdate=findPreference(Setting.AUTO_UPDATE);
        mClearCache=findPreference(Setting.CLEAR_CACHE);

        mChangeIcons.setSummary(getResources().getStringArray(R.array.icons)[mSetting.getInt(Setting.CHANGE_ICONS,0)]);
        mChangeUpdate.setSummary(getResources().getStringArray(R.array.cache_time)[mSetting.getInt(Setting.HOUR_SELECT,0)]);
        mClearCache.setSummary(FileSizeUtil.getAutoFileOrFileSize(BaseApplication.cacheDir+"/Data"));
        mClearCache.setOnPreferenceClickListener(this);
        mChangeUpdate.setOnPreferenceClickListener(this);
        mChangeIcons.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {

        if(preference.getKey().equals(Setting.CHANGE_ICONS)){

            showIconDialog();

        }else if(preference.getKey().equals(Setting.CLEAR_CACHE)){
            mACache.clear();
            mClearCache.setSummary(FileSizeUtil.getAutoFileOrFileSize(DIR));
            Snackbar.make(getView(),"缓存已清除",Snackbar.LENGTH_SHORT).show();


        }else if(preference.getKey().equals(Setting.AUTO_UPDATE)){
            showUpdateDialog();
        }



        return false;
    }


    private void showIconDialog(){

        new AlertDialog.Builder(getActivity()).setTitle("更换图标")
                                                .setSingleChoiceItems(getResources().getStringArray(R.array.icons),
                                                        mSetting.getInt(Setting.CHANGE_ICONS, 0), new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                if (which != mSetting.getInt(Setting.CHANGE_ICONS, 0)) {
                                                                    mSetting.putInt(Setting.CHANGE_ICONS,which);

                                                                }
                                                                dialog.dismiss();
                                                                mChangeIcons.setSummary(getResources().getStringArray(R.array.icons)[mSetting.getInt(
                                                                        Setting.CHANGE_ICONS, 0
                                                                )]);
                                                                Snackbar.make(getView(),"切换成功，重启应用生效",Snackbar.LENGTH_SHORT).show();
                                                            }
                                                        }).show();


    }


    private void showUpdateDialog(){

        new AlertDialog.Builder(getActivity()).setTitle("更换频率")
                                                .setSingleChoiceItems(getResources().getStringArray(R.array.cache_time),
                                                        mSetting.getInt(Setting.HOUR_SELECT, 0), new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                mSetting.putInt(Setting.HOUR_SELECT,which);
                                                                switch (which) {
                                                                    case 0:
                                                                        mSetting.putInt(Setting.AUTO_UPDATE,0);
                                                                        break;
                                                                    case 1:
                                                                        mSetting.putInt(Setting.AUTO_UPDATE,1);
                                                                        break;
                                                                    case 2:
                                                                        mSetting.putInt(Setting.AUTO_UPDATE,3);
                                                                        break;
                                                                    case 3:
                                                                        mSetting.putInt(Setting.AUTO_UPDATE,6);
                                                                        break;
                                                                }
                                                               mChangeUpdate.setSummary(getResources().getStringArray(R.array.cache_time)[mSetting.getInt(
                                                                       Setting.HOUR_SELECT,0
                                                               )]);
                                                                dialog.dismiss();
                                                                Snackbar.make(getView(),"设置成功",Snackbar.LENGTH_SHORT).show();
                                                            }
                                                        }).show();


    }

}
