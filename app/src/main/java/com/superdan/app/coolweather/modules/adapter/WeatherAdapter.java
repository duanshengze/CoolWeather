package com.superdan.app.coolweather.modules.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.superdan.app.coolweather.R;
import com.superdan.app.coolweather.modules.domain.Setting;
import com.superdan.app.coolweather.modules.domain.Weather;

/**
 * Created by duanshengze on 2016/3/14.
 */
public class WeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static String TAG=WeatherAdapter.class.getSimpleName();

    private Context mContext;
    private  final  int TYPE_ONE=0;
    private  final  int TYPE_TWO=1;
    private  final  int TYPE_THREE=2;
    private  final int TYPE_FORE=3;

    private Weather mWeatherData;
    private Setting mSetting;

    public  WeatherAdapter(Context context,Weather weatherData){
        mContext=context;
        mWeatherData=weatherData;
        mSetting=Setting.getsInstance();

    }

    @Override
    public int getItemViewType(int position) {
        switch (position){
            case TYPE_ONE:
                 return TYPE_ONE;
            case  TYPE_TWO:
                return  TYPE_TWO;
            case  TYPE_THREE:
                return  TYPE_THREE;
            case  TYPE_FORE:
                return  TYPE_FORE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    class NowWeatherViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        private ImageView weatherIcon;
        private TextView tempFlu;
        private TextView tempMax;
        private  TextView tempMin;
        private TextView tempPm;
        private  TextView tempQuality;

        /**
         * 当前天气情况
         */
        public NowWeatherViewHolder(View itemView) {
            super(itemView);
            cardView=(CardView)itemView.findViewById(R.id.cardView);
            weatherIcon=(ImageView)itemView.findViewById(R.id.weather_icon);
            tempFlu=(TextView)itemView.findViewById(R.id.temp_flu);
            tempMax=(TextView)itemView.findViewById(R.id.temp_max);
            tempMin=(TextView)itemView.findViewById(R.id.temp_min);
            tempPm=(TextView)itemView.findViewById(R.id.temp_pm);
            tempQuality=(TextView)itemView.findViewById(R.id.temp_quality);

        }
        /**
         * 当天小时预告
         */

        class HoursWeatherViewHolder extends RecyclerView.ViewHolder{
            private LinearLayout itemHourInfoLinearlayout;


            public HoursWeatherViewHolder(View itemView) {
                super(itemView);
            }
        }

        /**
         *当日建议
         */
        class SuggestionViewHolder extends RecyclerView.ViewHolder{
            private CardView cardView;
            private  TextView clothBrief;
            private  TextView clothTxt;
            private  TextView sportBrief;
            private  TextView sportTxt;
            private  TextView travelBrief;
            private  TextView travelTxt;
            private  TextView fluBrief;
            private  TextView fluTxt;

            public SuggestionViewHolder(View itemView) {
                super(itemView);
                cardView=(CardView)itemView.findViewById(R.id.cardView);
                clothBrief=(TextView)itemView.findViewById(R.id.cloth_brief);
                clothTxt=(TextView)itemView.findViewById(R.id.cloth_txt);
                sportBrief=(TextView)itemView.findViewById(R.id.sport_brief);
                sportTxt=(TextView)itemView.findViewById(R.id.sport_txt);
                travelBrief=(TextView)itemView.findViewById(R.id.travel_brief);
                travelTxt=(TextView)itemView.findViewById(R.id.travel_txt);
                fluBrief=(TextView)itemView.findViewById(R.id.travel_brief);
                fluTxt=(TextView)itemView.findViewById(R.id.flu_txt);
            }
        }
        /**
         * 未来天气
         */

        class   ForecastViewHolder extends  RecyclerView.ViewHolder{
            private LinearLayout forecastLinear;
            private TextView[] forecastDate=new TextView[];
            private TextView[] forecastDate=

            public  ForecastViewHolder(View itemView){
                super(itemView);


            }


        }

    }
}