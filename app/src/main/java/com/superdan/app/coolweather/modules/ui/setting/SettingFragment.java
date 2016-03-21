package com.superdan.app.coolweather.modules.ui.setting;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.superdan.app.coolweather.R;
import com.superdan.app.coolweather.common.ACache;
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        mSetting=Setting.getsInstance();
        mACache=ACache.get(getActivity());




    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        return false;
    }
}
