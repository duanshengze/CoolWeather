package com.superdan.app.coolweather.modules.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.superdan.app.coolweather.R;
import com.superdan.app.coolweather.common.PLog;
import com.superdan.app.coolweather.modules.domain.Setting;
import com.superdan.app.coolweather.modules.domain.Weather;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by duanshengze on 2016/3/14.
 */
public class WeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String TAG = WeatherAdapter.class.getSimpleName();

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private final int TYPE_ONE = 0;
    private final int TYPE_TWO = 1;
    private final int TYPE_THREE = 2;
    private final int TYPE_FORE = 3;

    private Weather mWeatherData;
    private Setting mSetting;

    private int hourSize;
    private int foreSize;

    public WeatherAdapter(Context context, Weather weatherData) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mWeatherData = weatherData;
        mSetting = Setting.getsInstance();
        hourSize=mWeatherData.hourlyForecast.size();
        foreSize=mWeatherData.dailyForecast.size();

    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case TYPE_ONE:
                return TYPE_ONE;
            case TYPE_TWO:
                return TYPE_TWO;
            case TYPE_THREE:
                return TYPE_THREE;
            case TYPE_FORE:
                return TYPE_FORE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case TYPE_ONE:
                return new NowWeatherViewHolder(mLayoutInflater.inflate(R.layout.item_temperature, parent, false));
            case TYPE_TWO:
                return new HoursWeatherViewHolder(mLayoutInflater.inflate(R.layout.item_hour_info,parent,false));
            case TYPE_THREE:
                return new SuggestionViewHolder(mLayoutInflater.inflate(R.layout.item_suggestion,parent,false));
            case TYPE_FORE:
                return new ForecastViewHolder(mLayoutInflater.inflate(R.layout.item_forecast,parent,false));

            default:return null;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NowWeatherViewHolder){
            try {
                NowWeatherViewHolder nowWeatherViewHolder=(NowWeatherViewHolder)holder;
                Glide.with(mContext)
                        .load(mSetting.getInt(mWeatherData.now.cond.txt,R.mipmap.none))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(nowWeatherViewHolder.weatherIcon);
                nowWeatherViewHolder.tempMax.setText("↑️ " + mWeatherData.dailyForecast.get(0).tmp.max+"°");
                nowWeatherViewHolder.tempMin.setText("↓ "+mWeatherData.dailyForecast.get(0).tmp.min+"°");
                nowWeatherViewHolder.tempFlu.setText(mWeatherData.now.tmp+" ℃");


                nowWeatherViewHolder.tempPm.setText("PM25: "+mWeatherData.aqi.city.pm25);
                nowWeatherViewHolder.tempQuality.setText("空气质量： "+mWeatherData.aqi.city.qlty);
            }catch (Exception e){
                PLog.e(TAG,e.toString());
            }
        }

        if(holder instanceof  HoursWeatherViewHolder){
            try {
                HoursWeatherViewHolder hoursWeatherViewHolder=(HoursWeatherViewHolder)holder;
                for(int i=0;i<hourSize;i++){
                    Weather.HourlyForecastEntity hourlyForecastEntity=mWeatherData.hourlyForecast.get(i);
                    String mDate=hourlyForecastEntity.date;
                    // s.subString(s.length-3,s.length)
                    //第一参数是开始截取的位置，第二个是结束位置（不包括）
                    hoursWeatherViewHolder.mClock[i].setText(mDate.substring(mDate.length()-5,mDate.length()));
                    hoursWeatherViewHolder.mHumidity[i].setText(hourlyForecastEntity.hum+"%");
                    hoursWeatherViewHolder.mTemp[i].setText(hourlyForecastEntity.tmp+"°");
                    hoursWeatherViewHolder.mWind[i].setText(hourlyForecastEntity.wind.spd+"Km");

                }
            }catch (Exception e){
                PLog.e(TAG,e.toString());
            }

        }

        if(holder instanceof SuggestionViewHolder){
            try {
                SuggestionViewHolder suggestionViewHolder=(SuggestionViewHolder)holder;
                Weather.SuggestionEntity suggestionEntity=mWeatherData.suggestion;
                suggestionViewHolder.clothBrief.setText("穿衣指数---"+suggestionEntity.drsg.brf);
                suggestionViewHolder.clothTxt.setText(suggestionEntity.drsg.txt);
                suggestionViewHolder.sportBrief.setText("运动指数---"+suggestionEntity.sport.brf);
                suggestionViewHolder.sportTxt.setText(suggestionEntity.sport.txt);
                suggestionViewHolder.travelBrief.setText("旅行指数---"+suggestionEntity.trav.brf);
                suggestionViewHolder.travelTxt.setText(suggestionEntity.trav.txt);
                suggestionViewHolder.fluBrief.setText("感冒指数---"+suggestionEntity.flu.brf);
                suggestionViewHolder.fluTxt.setText(suggestionEntity.flu.txt);

            }catch (Exception e){

                PLog.e(TAG,e.toString());
            }

        }

        if (holder instanceof  ForecastViewHolder){
            try{

                ForecastViewHolder forecastViewHolder=(ForecastViewHolder)holder;




            }catch (Exception e){


                PLog.e(TAG,e.toString());

            }




        }




    }

    @Override
    public int getItemCount() {
        return 4;
    }

    class NowWeatherViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private ImageView weatherIcon;
        private TextView tempFlu;
        private TextView tempMax;
        private TextView tempMin;
        private TextView tempPm;
        private TextView tempQuality;

        /**
         * 当前天气情况
         */
        public NowWeatherViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            weatherIcon = (ImageView) itemView.findViewById(R.id.weather_icon);
            tempFlu = (TextView) itemView.findViewById(R.id.temp_flu);
            tempMax = (TextView) itemView.findViewById(R.id.temp_max);
            tempMin = (TextView) itemView.findViewById(R.id.temp_min);
            tempPm = (TextView) itemView.findViewById(R.id.temp_pm);
            tempQuality = (TextView) itemView.findViewById(R.id.temp_quality);

        }


    }

    /**
     * 当天小时预告
     */

    class HoursWeatherViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout itemHourInfoLinearlayout;



        private TextView[] mClock = new TextView[hourSize];

        private TextView[] mTemp = new TextView[hourSize];

        private TextView[] mHumidity = new TextView[hourSize];

        private TextView[] mWind = new TextView[hourSize];

        public HoursWeatherViewHolder(View itemView) {
            super(itemView);
            itemHourInfoLinearlayout = (LinearLayout) itemView.findViewById(R.id.item_hour_info_linearlayout);
            for (int i = 0; i < hourSize; i++) {
                View view = mLayoutInflater.inflate(R.layout.item_hour_info_line, null);
                mClock[i] = (TextView) view.findViewById(R.id.one_clock);
                mTemp[i] = (TextView) view.findViewById(R.id.one_temp);
                mHumidity[i] = (TextView) view.findViewById(R.id.one_humidity);
                mWind[i] = (TextView) view.findViewById(R.id.one_wind);
                itemHourInfoLinearlayout.addView(view);
            }

        }



    }




    /**
     * 未来天气
     */

    class ForecastViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout forecastLinear;

        private TextView[] forecastDate = new TextView[foreSize];
        private TextView[] forecastTemp = new TextView[foreSize];
        private TextView[] forecastTxt = new TextView[foreSize];
        private ImageView[] forecastIcon = new ImageView[foreSize];


        public ForecastViewHolder(View itemView) {
            super(itemView);
            forecastLinear = (LinearLayout) itemView.findViewById(R.id.forecast_linear);
            for (int i = 0; i < foreSize; i++) {
                View view = mLayoutInflater.inflate(R.layout.item_forecast_line, null);
                forecastDate[i] = (TextView) view.findViewById(R.id.forecast_date);
                forecastTemp[i] = (TextView) view.findViewById(R.id.forecast_temp);
                forecastTxt[i] = (TextView) view.findViewById(R.id.forecast_txt);
                forecastIcon[i] = (ImageView) view.findViewById(R.id.forecast_icon);
                forecastLinear.addView(view);
            }


        }


    }
    /**
     * 当日建议
     */
    class SuggestionViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView clothBrief;
        private TextView clothTxt;
        private TextView sportBrief;
        private TextView sportTxt;
        private TextView travelBrief;
        private TextView travelTxt;
        private TextView fluBrief;
        private TextView fluTxt;

        public SuggestionViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            clothBrief = (TextView) itemView.findViewById(R.id.cloth_brief);
            clothTxt = (TextView) itemView.findViewById(R.id.cloth_txt);
            sportBrief = (TextView) itemView.findViewById(R.id.sport_brief);
            sportTxt = (TextView) itemView.findViewById(R.id.sport_txt);
            travelBrief = (TextView) itemView.findViewById(R.id.travel_brief);
            travelTxt = (TextView) itemView.findViewById(R.id.travel_txt);
            fluBrief = (TextView) itemView.findViewById(R.id.travel_brief);
            fluTxt = (TextView) itemView.findViewById(R.id.flu_txt);
        }
    }


    /**
     *判断当前日期是星期几
     * @param pTime 需要判断的时间
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */
    public static  String dayForWeek(String pTime)throws Exception{
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Calendar c=Calendar.getInstance();
        c.setTime(format.parse(pTime));
        int dayForWeek=0;
        String week="";
        dayForWeek=c.get(Calendar.DAY_OF_WEEK);
        switch (dayForWeek){
            case 0:
                week="星期日";
                break;
            case 1:
                week="星期一";
                break;
            case 2:
                week="星期二";
                break;
            case 3:
                week="星期三";
                break;
            case 4:
                week="星期四";
                break;
            case 5:
                week="星期五";
                break;
            case 6:
                week="星期六";
                break;




        }
        return week;




    }




}