package com.superdan.app.coolweather.modules.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duanshengze on 2016/3/17.
 */
public class WeatherAPI {
    @SerializedName("HeWeather data service 3.0")@Expose
    public List<Weather>mHeWeatherDataService30s=new ArrayList<>();
}
