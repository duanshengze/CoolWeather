package com.superdan.app.coolweather.modules.ui;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.amap.api.location.AMapLocation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.superdan.app.coolweather.R;
import com.superdan.app.coolweather.base.BaseActivity;
import com.superdan.app.coolweather.common.CheckLocation;
import com.superdan.app.coolweather.common.CheckVersion;
import com.superdan.app.coolweather.common.PLog;
import com.superdan.app.coolweather.common.Util;
import com.superdan.app.coolweather.component.RetrofitSingleton;
import com.superdan.app.coolweather.modules.adapter.WeatherAdapter;
import com.superdan.app.coolweather.modules.domain.Setting;
import com.superdan.app.coolweather.modules.domain.Weather;
import com.superdan.app.coolweather.modules.domain.WeatherAPI;
import com.superdan.app.coolweather.modules.listener.HidingScrollListener;
import com.superdan.app.coolweather.modules.ui.about.AboutActivity;
import com.superdan.app.coolweather.modules.ui.setting.SettingActivity;

import java.util.Calendar;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by dsz on 16/3/16.
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,
        SwipeRefreshLayout.OnRefreshListener{

    private final String TAG = MainActivity.class.getSimpleName();

    public static final int REQUST_CITY=111;

    public static final int RESULT_OK=222;

    private final static int LOADING = 1;

    private final static int LOADED = 2;

    private final static String WEATHER_DATA = "WeatherData";

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

    private long exitTime=0;//记录第一次点击的时间

//    private boolean isLoaction=false;
//
//
//    private AMapLocationClient mLocationClient=null;
//    private AMapLocationClientOption mLocationOption=null;
    private CheckLocation mCheckLocation;

    private CheckVersion mCheckVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCheckVersion=new CheckVersion(this);

        initView();
        initDrawer();
        initIcon();
        fetchData();

        if(Util.isNetworkConnected(this)){
            if(!Setting.IGNORE.equals(mSetting.getString(Setting.IGNORE_VERSION,Setting.NO_IGNORE))){
                mCheckVersion.checkVersion(fab);
            }

            initLocation();
        }


    }

    /**
     * 初始化基础View
     */

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bannner = (ImageView) findViewById(R.id.bannner);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiprefresh);
        mRefreshLayout.setOnRefreshListener(this);

//        标题
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(" ");
//      夜间模式
        Calendar calendar = Calendar.getInstance();
        mSetting.putInt(Setting.HOUR, calendar.get(Calendar.HOUR_OF_DAY));
        setStatusBarColorForKitKat(R.color.colorSunrise);
        if (mSetting.getInt(Setting.HOUR, 0) < 6 || mSetting.getInt(Setting.HOUR, 0) > 18) {
            Glide.with(this).load(R.mipmap.sunset)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(bannner);

            //TODO ??
            collapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(this, R.color.colorSunset));
            setStatusBarColorForKitKat(R.color.colorSunset);

        }
        //fab
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //showFabDialog
                showFabDialog();
            }
        });

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        final int fabBottomMargin = lp.bottomMargin;
        //recyclerview
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                fab.animate()
                        .translationY(fab.getHeight() + fabBottomMargin)
                        .setInterpolator(new AccelerateInterpolator(2))
                        .start();
            }

            @Override
            public void onShow() {
                fab.animate().
                        translationY(0).
                        setInterpolator(new DecelerateInterpolator(2))
                        .start();
            }
        });

    }

    /**
     * 初始化抽屉
     */

    private void initDrawer() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);

        headerBackground = (RelativeLayout) headerLayout.findViewById(R.id.header_background);

        if (mSetting.getInt(Setting.HOUR, 0) < 6 || mSetting.getInt(Setting.HOUR, 0) > 18) {


            headerBackground.setBackground(ContextCompat.getDrawable(MainActivity.this, R.mipmap.header_back_night));

        }
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navogation_drawer_close
        );
        drawer.setDrawerListener(toggle);
        toggle.syncState();


    }


    /**
     * 初始化Icon
     */
    private void initIcon(){
        if(mSetting.getInt(Setting.CHANGE_ICONS,0)==0){
            mSetting.putInt("未知",R.mipmap.none);
            mSetting.putInt("晴",R.mipmap.type_one_sunny);
            mSetting.putInt("阴",R.mipmap.type_one_cloudy);
            mSetting.putInt("多云",R.mipmap.type_one_cloudy);
            mSetting.putInt("少云",R.mipmap.type_one_cloudy);
            mSetting.putInt("晴转多云",R.mipmap.type_one_cloudytosunny);
            mSetting.putInt("小雨",R.mipmap.type_one_light_rain);
            mSetting.putInt("中雨",R.mipmap.type_one_middle_rain);
            mSetting.putInt("大雨",R.mipmap.type_one_heavy_rain);
            mSetting.putInt("阵雨",R.mipmap.type_one_thunderstorm);
            mSetting.putInt("雷阵雨", R.mipmap.type_one_thunderstorm);
            mSetting.putInt("霾", R.mipmap.type_one_fog);
            mSetting.putInt("雾", R.mipmap.type_one_fog);
        }

    }



    private void initLocation(){
        mCheckLocation=CheckLocation.getInstance();
        mCheckLocation.setOnLocationListener(new CheckLocation.OnLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        aMapLocation.getLocationType();
                        showLocationDiag(aMapLocation);
                        Log.e(TAG, "定位地址: " + aMapLocation.getCity());
                    }else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());

                    }
                }
            }
        });
        mCheckLocation.startLocation();
    }
    private void showFabDialog() {

        new AlertDialog.Builder(MainActivity.this).setTitle("为我点赞")
                .setMessage("去github给段段个star，鼓励下作者O(∩_∩)O哈哈~")
                .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = Uri.parse("https://github.com/duanshengze/CoolWeather");
                        Intent intent = new Intent();
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

    private void fetchData() {
        observer = new Observer<Weather>() {
            @Override
            public void onCompleted() {
                new RefreshHandler().sendEmptyMessage(LOADED);
            }

            @Override
            public void onError(Throwable e) {
                RetrofitSingleton.disposeFailureInfo(e, MainActivity.this, fab);
                new RefreshHandler().sendEmptyMessage(LOADED);
            }

            @Override
            public void onNext(Weather weather) {
                mProgressBar.setVisibility(View.INVISIBLE);
                new RefreshHandler().sendEmptyMessage(LOADED);
                collapsingToolbarLayout.setTitle(weather.basic.city);
                mAdapter=new WeatherAdapter(MainActivity.this,weather);
                mRecyclerView.setAdapter(mAdapter);
            }
        };
        fetchDataByCache(observer);
    }

    /**
     * 从本地获取
     */
    private void fetchDataByCache(Observer<Weather> observer) {

        Weather weather = null;

        try {
            weather = (Weather) mACache.getAsObject(WEATHER_DATA);
        }catch (Exception e){

            PLog.e(TAG,e.toString());
        }
         if (weather!=null){

             Observable.just(weather).distinct().subscribe(observer);
         }else {

             fetchDataByNetWork(observer);
         }


    }


    /**
      *从网络获取
      *@author duanshengze
      *created at 16/3/21 上午12:27
      *@params
      */
    private void fetchDataByNetWork(Observer<Weather>observer){

        String cityName=mSetting.getString(Setting.CITY_NAME,"北京");
        if (cityName!=null){

            cityName=cityName.replace("市","")
                                .replace("省","")
                                .replace("自治区","")
                                .replace("特别行政区","")
                                .replace("地区","")
                                .replace("盟","");
        }
        RetrofitSingleton.getApiService(this)
                .mWeatherAPI(cityName,Setting.KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<WeatherAPI, Boolean>() {
                    @Override
                    public Boolean call(WeatherAPI weatherAPI) {
                        return weatherAPI.mHeWeatherDataService30s.get(0).status.equals("ok");
                    }
                })
                .map(new Func1<WeatherAPI, Weather>() {


                    @Override
                    public Weather call(WeatherAPI weatherAPI) {
                        return weatherAPI.mHeWeatherDataService30s.get(0);
                    }
                })
                .doOnNext(new Action1<Weather>() {
                    @Override
                    public void call(Weather weather) {
                        mACache.put(WEATHER_DATA,weather,mSetting.getInt(Setting.AUTO_UPDATE,1)*Setting.ONE_HOUR);//默认一小时
                    }
                })
                .subscribe(observer);

    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case  R.id.nav_set:
            Intent intentSetting=new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intentSetting);
            break;
            case R.id.nav_city:
                startActivityForResult(new Intent(MainActivity.this,ChoiceCityActivity.class),REQUST_CITY);
                break;
            case  R.id.nav_about:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                break;

        }

        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode== RESULT_OK){
            if (requestCode==REQUST_CITY){
                String cityName=data.getStringExtra(Setting.CITY_NAME);
                mSetting.putString(Setting.CITY_NAME,cityName);
                fetchDataByNetWork(observer);
            }


        }

    }

    @Override
    public void onRefresh() {
        fetchDataByNetWork(observer);
    }

    @Override
    public void onBackPressed() {
       if(drawer.isDrawerOpen(GravityCompat.START)){

           drawer.closeDrawer(GravityCompat.START);
       }else {
           if((System.currentTimeMillis()-exitTime)>2000){
               Snackbar.make(fab,"再按一次退出程序",Snackbar.LENGTH_SHORT).show();
               exitTime=System.currentTimeMillis();
           }else {
               finish();
           }

       }
    }


    private void showLocationDiag( AMapLocation aMapLocation){
        final String cityName=aMapLocation.getCity();
        new AlertDialog.Builder(this).setTitle("定位信息")
                .setMessage("定位到当前位置为："+cityName+"， 是否切换该地区?")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSetting.putString(Setting.CITY_NAME, cityName);
                        MainActivity.this.onRefresh();
                    }
                }).show();

    }


    class RefreshHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case LOADING:
                    mRefreshLayout.setRefreshing(true);
                    break;
                case LOADED:
                    if (mRefreshLayout.isRefreshing()) {
                        mRefreshLayout.setRefreshing(false);
                        if (Util.isNetworkConnected(MainActivity.this)) {

                            Snackbar.make(fab, "哇o(≧v≦)o~~好棒 加载完毕", Snackbar.LENGTH_SHORT).show();

                        } else {
                            Snackbar.make(fab, "网络出现了问题？/(ㄒoㄒ)/~~", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    break;

            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCheckLocation!=null){
            mCheckLocation.destoryLocation();
        }

        if (mCheckVersion!=null){
            //注销广播
            mCheckVersion.unregisterReceiver();
        }
    }
}
