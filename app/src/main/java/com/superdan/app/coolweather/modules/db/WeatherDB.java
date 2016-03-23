package com.superdan.app.coolweather.modules.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.superdan.app.coolweather.modules.domain.City;
import com.superdan.app.coolweather.modules.domain.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/23.
 */
public class WeatherDB {
    private Context context;

    public  WeatherDB(Context context){
        this.context=context;

    }
    public List<Province> loadProvinces(SQLiteDatabase db){
        List<Province>list=new ArrayList<>();
        Cursor cursor=db.query("T_Province",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do {
                Province province=new Province();

                province.proName=cursor.getString(cursor.getColumnIndex("ProName"));
                province.proSort=cursor.getInt(cursor.getColumnIndex("ProSort"));
                list.add(province);

            }while (cursor.moveToNext());



        }
        cursor.close();
        return list;

    }


    public List<City>loadCities(SQLiteDatabase db,int proID){
        List<City>list=new ArrayList<>();
        Cursor cursor=db.query("T_City",null,"ProID=?",new String[]{
                String.valueOf(proID)
        },null,null,null
        );

        if (cursor.moveToFirst()){


            do {
                City city=new City();
                city.cityName=cursor.getString(cursor.getColumnIndex("CityName"));
                list.add(city);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return list;

    }




}

