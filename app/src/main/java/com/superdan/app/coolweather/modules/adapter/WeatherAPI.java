package com.superdan.app.coolweather.modules.adapter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.superdan.app.coolweather.modules.domain.Weather;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsz on 16/3/16.
 */
public class WeatherAPI implements Serializable {
    @SerializedName("HeWeather data service 3.0")@Expose
   public List<Weather> mHeWeatherDataService30s=new ArrayList<>();

}
