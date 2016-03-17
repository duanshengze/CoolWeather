package com.superdan.app.coolweather.modules.ui;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.superdan.app.coolweather.R;
import com.superdan.app.coolweather.base.BaseActivity;
import com.superdan.app.coolweather.common.Util;
import com.superdan.app.coolweather.modules.adapter.WeatherAdapter;
import com.superdan.app.coolweather.modules.domain.Setting;
import com.superdan.app.coolweather.modules.domain.Weather;
import com.superdan.app.coolweather.modules.listener.HidingScrollListener;

import java.util.Calendar;

import rx.Observer;

/**
 * Created by dsz on 16/3/16.
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,
        SwipeRefreshLayout.OnRefreshListener {

    private final String TAG = MainActivity.class.getSimpleName();

    private final static int LOADING=1;

    private final static int LOADED=2;


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
        initView();
        initDrawer();
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
        mSetting.putInt(Setting.HOUR, calendar.get(Calendar.HOUR_OF_DAY));
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

            CoordinatorLayout.LayoutParams lp=(CoordinatorLayout.LayoutParams)fab.getLayoutParams();
            final int fabBottomMargin=lp.bottomMargin;
            //recyclerview
            mRecyclerView=(RecyclerView)findViewById(R.id.recyclerview);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.addOnScrollListener(new HidingScrollListener() {
                @Override
                public void onHide() {
                    fab.animate()
                            .translationY(fab.getHeight()+fabBottomMargin);
                }

                @Override
                public void onShow() {
                    fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                }
            });


        }


    }

    /**
     * 初始化抽屉
     */

    private void initDrawer(){

        NavigationView navigationView=(NavigationView)findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout=navigationView.inflateHeaderView(R.layout.nav_header_main);

        headerBackground=(RelativeLayout)headerLayout.findViewById(R.id.header_background);

        if(mSetting.getInt(Setting.HOUR,0)<6||mSetting.getInt(Setting.HOUR,0)>18){


            headerBackground.setBackground(ContextCompat.getDrawable(MainActivity.this,R.mipmap.header_back_night));

        }
        drawer=(DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(
                this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navogation_drawer_close
        );
        drawer.setDrawerListener(toggle);
        toggle.syncState();


    }



    private void showFabDialog(){

        new AlertDialog.Builder(MainActivity.this).setTitle("为ta点赞")
                                                    .setMessage("去gitub给作者个star，鼓励下作者O(∩_∩)O哈哈~")
                                                    .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Uri uri=Uri.parse("https://github.com/duanshengze/CoolWeather");
                                                            Intent intent=new Intent();
                                                            intent.setAction(Intent.ACTION_VIEW);
                                                            intent.setData(uri);
                                                            MainActivity.this.startActivity(intent);
                                                        }
                                                    }).show();

    }


    /**
     * 首先从本地缓存获取数据
     * if
     * 有
     * 更新UI
     * else
     * 直接进行网络请求，更新UI并保持在本地
     */

    private void fetchData(){


        observer=new Observer<Weather>() {
            @Override
            public void onCompleted() {
                new RefreshHandler().sendEmptyMessage(LOADED);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Weather weather) {
                new RefreshHandler().sendEmptyMessage(LOADED);
            }
        };




    }









    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public void onRefresh() {

    }




    class RefreshHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case LOADING:mRefreshLayout.setRefreshing(true);
                break;
                case LOADED:
                    if(mRefreshLayout.isRefreshing()){
                        mRefreshLayout.setRefreshing(false);
                        if(Util.isNetworkConnected(MainActivity.this)){

                            Snackbar.make(fab,"哇o(≧v≦)o~~好棒 加载完毕",Snackbar.LENGTH_SHORT).show();

                        }else {
                            Snackbar.make(fab,"网络出现了问题？/(ㄒoㄒ)/~~",Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    break;


            }

        }
    }
}
