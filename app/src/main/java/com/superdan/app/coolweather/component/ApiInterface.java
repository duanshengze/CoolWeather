package com.superdan.app.coolweather.component;

import com.superdan.app.coolweather.modules.domain.VersionAPI;
import com.superdan.app.coolweather.modules.adapter.WeatherAPI;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by dsz on 16/3/15.
 */
public interface ApiInterface {
    String HOST="https://api.heweather.com/x3/";
    @GET("weather")
    Observable<WeatherAPI>mWeatherAPI(@Query("city")String city,@Query("key")String key);


    //在Retrofit2.0中 我们还可以在@Url里面定义完整的URL：这种情况下Base URl会被忽略。

    @GET("http://api.fir.im/apps/latest/5630e5f1f2fc425c52000006")
    Observable<VersionAPI> mVersionAPI(@Query("api_token")String api_token);


}
