package com.superdan.app.coolweather.modules.ui.about;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.superdan.app.coolweather.R;
import com.superdan.app.coolweather.base.BaseActivity;

import com.superdan.app.coolweather.modules.domain.Setting;

/**
 * Created by dsz on 16/3/25.
 */
public class AboutActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_about);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("关于");
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_keyboard_arrow_left_32dpdp));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        setStatusBarColor(R.color.colorPrimary);
        if (mSetting.getInt(Setting.HOUR,0)<6||mSetting.getInt(Setting.HOUR,0)>18){
            toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.colorSunset));
            setStatusBarColor(R.color.colorSunset);
        }

        getFragmentManager().beginTransaction().replace(R.id.fragmentlayout,new AboutFragment()).commit();
    }
}
