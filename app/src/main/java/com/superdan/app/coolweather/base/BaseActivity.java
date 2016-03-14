package com.superdan.app.coolweather.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.superdan.app.coolweather.common.ACache;
import com.superdan.app.coolweather.modules.domain.Setting;

/**
 * Created by Administrator on 2016/3/14.
 */
public class BaseActivity extends AppCompatActivity {
    public ACache acache;

    public Setting mSetting=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
