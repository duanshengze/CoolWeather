package com.superdan.app.coolweather.modules.ui;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import com.superdan.app.coolweather.R;
import com.superdan.app.coolweather.base.BaseActivity;
import com.superdan.app.coolweather.modules.adapter.CityAdapter;
import com.superdan.app.coolweather.modules.domain.City;
import com.superdan.app.coolweather.modules.domain.Province;

import java.util.ArrayList;
import java.util.List;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_city);
    }
}
