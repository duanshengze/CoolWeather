package com.superdan.app.coolweather.modules.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 段胜泽 on 2016/3/15.
 */
//为什么要实现Serializable
public class Weather implements Serializable {

    @SerializedName("basic")
    public BasicEntity basic;

    /**
     * "status": "ok",
     */

    @SerializedName("status")
    public String status;
    @SerializedName("aqi")
    public AqiEntity aqi;
    @SerializedName("now")
    public NowEntity now;
    @SerializedName("daily_forecast")
    public List<DailyForecastEntity> dailyForecast;
    @SerializedName("hourly_forecast")
    public List<HourlyForecastEntity> hourlyForecast;
    @SerializedName("suggestion")
    public SuggestionEntity suggestion;


    /**
     * "basic": {
     * "city": "大连",
     * "cnty": "中国",
     * "id": "CN101070201",
     * "lat": "38.944000",
     * "lon": "121.576000",
     * "update": {
     * "loc": "2015-07-15 10:43",
     * "utc": "2015-07-15 02:46:14"
     * }
     * }
     */
    public static class BasicEntity implements Serializable {
        @SerializedName("city")
        public String city;
        @SerializedName("cnty")
        public String cnty;
        @SerializedName("id")
        public String id;
        @SerializedName("lat")
        public String lat;
        @SerializedName("lon")
        public String lon;
        @SerializedName("update")
        public UpdateEntity update;

        public static class UpdateEntity implements Serializable {
            @SerializedName("loc")
            public String loc;
            @SerializedName("utc")
            public String utc;
        }
    }

    /**
     * "aqi": {
     * "city": {
     * "aqi": "71",
     * "co": "1",
     * "no2": "75",
     * "o3": "101",
     * "pm10": "89",
     * "pm25": "44",
     * "qlty": "良",
     * "so2": "27"
     * }
     * }
     */

    public static class AqiEntity implements Serializable {
        @SerializedName("city")public CityEntity city;


        public static class CityEntity implements Serializable {
            @SerializedName("aqi")
            public String api;
            @SerializedName("co")
            public String co;
            @SerializedName("no2")
            public String no2;
            @SerializedName("o3")
            public String o3;
            @SerializedName("pm10")
            public String pm10;
            @SerializedName("pm25")
            public String pm25;
            @SerializedName("qlty")
            public String qlty;
            @SerializedName("so2")
            public String so2;


        }


    }
    /**
     * now": {
     "cond": {
     "code": "100",
     "txt": "晴"
     },
     "fl": "33",
     "hum": "28",
     "pcpn": "0",
     "pres": "1005",
     "tmp": "32",
     "vis": "10",
     "wind": {
     "deg": "350",
     "dir": "东北风",
     "sc": "4-5",
     "spd": "11"
     }
     },
     */
    public static class NowEntity implements Serializable{
        @SerializedName("cond")public CondEntity cond;
        @SerializedName("fl") public String fl;
        @SerializedName("hum")public String hum;
        @SerializedName("pcpn")public  String pcpn;
        @SerializedName("pres")public  String pres;
        @SerializedName("tmp")public String tmp;
        @SerializedName("vis")public String vis;
        @SerializedName("wind")public WindEntity wind;

        public static class  WindEntity implements  Serializable {
            @SerializedName("deg")public String deg;
            @SerializedName("dir")public String dir;
            @SerializedName("sc") public String sc;
            @SerializedName("spd")public  String spd;
        }
        public  static  class  CondEntity implements  Serializable{
            @SerializedName("code")public String code;
            @SerializedName("txt")public  String txt;

        }
    }
    /**
     * {
     "date": "2015-07-15",
     "astro": {
     "sr": "04:40",
     "ss": "19:19"
     },
     "cond": {
     "code_d": "100",
     "code_n": "101",
     "txt_d": "晴",
     "txt_n": "多云"
     },
     "hum": "48",
     "pcpn": "0.0",
     "pop": "0",
     "pres": "1005",
     "tmp": {
     "max": "33",
     "min": "24"
     },
     "vis": "10",
     "wind": {
     "deg": "192",
     "dir": "东南风",
     "sc": "4-5",
     "spd": "11"
     }
     },
     */
    public static class DailyForecastEntity implements Serializable{
        @SerializedName("date") public String date;
        @SerializedName("astro")public AstroEntity astro;
        @SerializedName("cond")public CondEntity cond;
        @SerializedName("hum")public String hum;
        @SerializedName("pcpn")public String pcpn;
        @SerializedName("pop")public String pop;
        @SerializedName("pres")public String pres;
        @SerializedName("tmp")public TmpEntity tmp;
        @SerializedName("vis")public String vis;
        @SerializedName("wind")public WindEntity wind;


        public static class AstroEntity implements Serializable{
            @SerializedName("sr")public  String sr;
            @SerializedName("ss")public String ss;
        }
        public static class  CondEntity implements  Serializable{
            @SerializedName("code_d")public  String codeD;
            @SerializedName("code_n")public String codeN;
            @SerializedName("txt_d")public String txtD;
            @SerializedName("txt_n")public String txtN;
        }
        public static class TmpEntity implements Serializable{
            @SerializedName("max")public String max;
            @SerializedName("min")public String min;

        }
        public static class  WindEntity implements Serializable{
            @SerializedName("deg")public String deg;
            @SerializedName("dir")public String dir;
            @SerializedName("sc")public String sc;
            @SerializedName("spd")public String spd;

        }

    }
    /**
     *   {
     "date": "2015-07-15 16:00",
     "hum": "54",
     "pop": "0",
     "pres": "1005",
     "tmp": "31",
     "wind": {
     "deg": "216",
     "dir": "西南风",
     "sc": "微风",
     "spd": "6"
     }
     },
     */

    public static class HourlyForecastEntity implements Serializable{
        @SerializedName("date")public  String date;
        @SerializedName("hum")public String hum;
        @SerializedName("pop")public String pop;
        @SerializedName("pres")public String pres;
        @SerializedName("tmp")public String tmp;
        @SerializedName("wind")public WindEntity wind;
        public static class  WindEntity implements  Serializable{
            @SerializedName("deg")public String deg;
            @SerializedName("dir")public String dir;
            @SerializedName("sc")public String sc;
            @SerializedName("spd")public String spd;

        }



    }
/**
 * "suggestion": {
 "comf": {
 "brf": "较舒适",
 "txt": "白天天气晴好，您在这种天气条件下，会感觉早晚凉爽、舒适，午后偏热。"
 },
 "cw": {
 "brf": "较不宜",
 "txt": "较不宜洗车，未来一天无雨，风力较大，如果执意擦洗汽车，要做好蒙上污垢的心理准备。"
 },
 "drsg": {
 "brf": "炎热",
 "txt": "天气炎热，建议着短衫、短裙、短裤、薄型T恤衫等清凉夏季服装。"
 },
 "flu": {
 "brf": "少发",
 "txt": "各项气象条件适宜，发生感冒机率较低。但请避免长期处于空调房间中，以防感冒。"
 },
 "sport": {
 "brf": "较适宜",
 "txt": "天气较好，但风力较大，推荐您进行室内运动，若在户外运动请注意防风。"
 },
 "trav": {
 "brf": "适宜",
 "txt": "天气较好，是个好天气哦。稍热但是风大，能缓解炎热的感觉，适宜旅游，可不要错过机会呦！"
 },
 "uv": {
 "brf": "强",
 "txt": "紫外线辐射强，建议涂擦SPF20左右、PA++的防晒护肤品。避免在10点至14点暴露于日光下。"
 }
 }
 */

    public static  class  SuggestionEntity implements Serializable{

        @SerializedName("comf")public ComfEntity comf;
        @SerializedName("cw")public CwEntity cw;
        @SerializedName("drsg")public DrsgEntity drsg;
        @SerializedName("flu")public FluEntity flu;
        @SerializedName("sport")public SportEntity sport;
        @SerializedName("trav")public TravEntity trav;
        @SerializedName("uv")public UvEntity uv;

        public static class ComfEntity implements  Serializable{
            @SerializedName("brf")public String brf;
            @SerializedName("txt")public String txt;
        }
        public static class CwEntity implements  Serializable{
            @SerializedName("brf")public String brf;
            @SerializedName("txt")public String txt;
        }
        public static class DrsgEntity implements  Serializable{
            @SerializedName("brf")public String brf;
            @SerializedName("txt")public String txt;
        }
        public static class FluEntity implements  Serializable{
            @SerializedName("brf")public String brf;
            @SerializedName("txt")public String txt;
        }
        public static class SportEntity implements  Serializable{
            @SerializedName("brf")public String brf;
            @SerializedName("txt")public String txt;
        }
        public static class TravEntity implements  Serializable{
            @SerializedName("brf")public String brf;
            @SerializedName("txt")public String txt;
        }public static class UvEntity implements  Serializable{
            @SerializedName("brf")public String brf;
            @SerializedName("txt")public String txt;
        }


    }

}
