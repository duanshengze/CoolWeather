package com.superdan.app.coolweather.modules.ui;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.superdan.app.coolweather.R;
import com.superdan.app.coolweather.base.BaseActivity;
import com.superdan.app.coolweather.modules.adapter.WeatherAdapter;
import com.superdan.app.coolweather.modules.domain.Setting;
import com.superdan.app.coolweather.modules.domain.Weather;

import java.util.Calendar;

import rx.Observer;

/**
 * Created by dsz on 16/3/16.
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,
        SwipeRefreshLayout.OnRefreshListener {

    private final String TAG = MainActivity.class.getSimpleName();

    private CollapsingToolbarLayout collapsingToolbarLayout;

    private Toolbar toolbar;

    private DrawerLayout drawer;

    private FloatingActionButton fab;

    private SwipeRefreshLayout mRefreshLayout;

    private ImageView bannner;

    private ProgressBar mProgressBar;

    private RelativeLayout headerBackground;

    private RecyclerView mRecyclerView;

    private WeatherAdapter mAdapter;

    private Observer<Weather> observer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     *初始化基础View
     */

    private  void initView(){
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bannner=(ImageView)findViewById(R.id.bannner);
        mProgressBar=(ProgressBar)findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        mRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swiprefresh);
        mRefreshLayout.setOnRefreshListener(this);

//        标题
        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(" ");
//      夜间模式
        Calendar calendar=Calendar.getInstance();
        mSetting.putInt(Setting.HOUR, calendar.get(Calendar.HOUR_OF_DAY))
        setStatusBarColorForKitKat(R.color.colorSunrise);
        if(mSetting.getInt(Setting.HOUR,0)<6||mSetting.getInt(Setting.HOUR,0)>18){
            Glide.with(this).load(R.mipmap.sunset)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(bannner);

            //TODO ??
            collapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(this, R.color.colorSunset));
            setStatusBarColorForKitKat(R.color.colorSunset);

            //fab
            fab=(FloatingActionButton)findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //showFabDialog
                    showFabDialog();
                }
            });

            CoordinatorLayout.LayoutParams

        }


    }



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public void onRefresh() {

    }
}
