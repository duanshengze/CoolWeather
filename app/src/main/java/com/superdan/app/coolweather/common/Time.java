package com.superdan.app.coolweather.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dsz on 16/3/16.
 */
public class Time {


    public static String getNowMDHMSTime(){

        SimpleDateFormat mDateFormat=new SimpleDateFormat("MM-dd HH:mm:ss");

        String date=mDateFormat.format(new Date());
        return date;


    }

}
