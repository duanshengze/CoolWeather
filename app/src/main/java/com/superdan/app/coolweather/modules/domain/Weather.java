package com.superdan.app.coolweather.modules.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by 段胜泽 on 2016/3/15.
 */
//为什么要实现Serializable
public class Weather implements Serializable {
    /**
     * "basic": {
     "city": "大连",
     "cnty": "中国",
     "id": "CN101070201",
     "lat": "38.944000",
     "lon": "121.576000",
         "update": {
         "loc": "2015-07-15 10:43",
         "utc": "2015-07-15 02:46:14"
         }
     }
     */
    @SerializedName("")



}
