package com.superdan.app.coolweather.modules.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.superdan.app.coolweather.R;
import com.superdan.app.coolweather.base.BaseActivity;
import com.superdan.app.coolweather.modules.adapter.CityAdapter;
import com.superdan.app.coolweather.modules.db.DBManager;
import com.superdan.app.coolweather.modules.db.WeatherDB;
import com.superdan.app.coolweather.modules.domain.City;
import com.superdan.app.coolweather.modules.domain.Province;
import com.superdan.app.coolweather.modules.domain.Setting;
import com.superdan.app.coolweather.modules.domain.Weather;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * Created by dsz on 16/3/22.
 */
public class ChoiceCityActivity extends BaseActivity {
    private static String TAG=ChoiceCityActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;

    private ProgressBar mProgressBar;

    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    private ArrayList<String>dataList=new ArrayList<>();

    private Province selectProvince;

    private City selectCity;

    private List<Province>provinceList;

    private List<City> cityList;

    private CityAdapter mAdapter;

    private DBManager mDBManager;

    private WeatherDB mWeatherDB;



    private static final int LEVEL_PROVINCE=1;

    private static final  int LEVEL_CITY=2;

    private int currentLevel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_city);
        mDBManager=new DBManager(this);
        mDBManager.openDatabase();
        mWeatherDB=new WeatherDB(this);
        initView();
        initRecyclerView();
        queryProvinces();
    }


    private  void initView(){

        ImageView bannner=(ImageView)findViewById(R.id.bannner);
        mCollapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        setStatusBarColorForKitKat(R.color.colorSunrise);
        if(mSetting.getInt(Setting.HOUR,0)<6||mSetting.getInt(Setting.HOUR,0)>18){
            mCollapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(this,R.color.colorSunset));
            Glide.with(this).load(R.mipmap.city_night).into(bannner);
            setStatusBarColorForKitKat(R.color.colorSunset);
        }
        mProgressBar=(ProgressBar)findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);
    }


    private void initRecyclerView(){
        mRecyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mAdapter=new CityAdapter(this,dataList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOntemClickListener(new CityAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectProvince = provinceList.get(pos);
                    mRecyclerView.scrollTo(0, 0);
                    queryCities();

                } else if (currentLevel == LEVEL_CITY) {
                    selectCity = cityList.get(pos);
                    Intent intent = new Intent();
                    String cityName = selectCity.cityName;
                    intent.putExtra(Setting.CITY_NAME, cityName);
                    setResult(MainActivity.RESULT_OK, intent);
                    finish();

                }
            }
        });

    }



    /**
      *查询选中省份的所有城市，从数据库查询
      *@author duanshengze
      *created at 16/3/23 下午8:46
      *@params
      */
    private  void queryCities(){

        dataList.clear();
        mCollapsingToolbarLayout.setTitle(selectProvince.proName);
        Observer<City> obaserver=new Observer<City>() {
            @Override
            public void onCompleted() {
                currentLevel=LEVEL_CITY;
                mAdapter.notifyDataSetChanged();
                mRecyclerView.scrollToPosition(0);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(City city) {
                    dataList.add(city.cityName);
            }
        };

        Observable.defer(new Func0<Observable<City>>() {
            @Override
            public Observable<City> call() {

                cityList=mWeatherDB.loadCities(mDBManager.getDatabase(),selectProvince.proSort);

                return Observable.from(cityList);
            }
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(obaserver);
    }


    private void queryProvinces(){
        mCollapsingToolbarLayout.setTitle("选择省份");
        Observer<Province>oberver=new Observer<Province>() {
            @Override
            public void onCompleted() {
                currentLevel=LEVEL_PROVINCE;
                mAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Province province) {
                dataList.add(province.proName);
            }
        };
        Observable.defer(new Func0<Observable<Province>>() {
                             @Override
                             public Observable<Province> call() {

                                 provinceList=mWeatherDB.loadProvinces(mDBManager.getDatabase());
                                 dataList.clear();
                                 return Observable.from(provinceList);
                             }
                         }
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(oberver);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if (currentLevel==LEVEL_PROVINCE){
                finish();
            }else {

                queryProvinces();
                mRecyclerView.scrollToPosition(0);
            }


        }
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDBManager.closeDatabase();
    }
}
