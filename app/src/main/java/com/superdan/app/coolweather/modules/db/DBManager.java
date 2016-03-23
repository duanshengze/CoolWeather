package com.superdan.app.coolweather.modules.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.superdan.app.coolweather.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/3/23.
 */
public class DBManager {
    private static String TAG = DBManager.class.getSimpleName();
    public static final String DB_NAME = "china_city.db";//数据库名
    public static final String PACKAGE_NAME = "com.superdan.app.coolweather";
    public static final String DB_PATH = "/data" + Environment.getDataDirectory()
            .getAbsolutePath() + "/" + PACKAGE_NAME;
    private SQLiteDatabase database;
    private Context context;

    private final int BUFFER_SIZE = 400000;

    public DBManager(Context context) {

        this.context = context;
    }

    public SQLiteDatabase getDatabase() {

        return database;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }

    private SQLiteDatabase openDatabase(String dbfile) {
        if (new File(dbfile).exists()) {
            try {
                InputStream is = context.getResources().openRawResource(R.raw.china_city);
                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = is.read(buffer)) != 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
        return db;
    }

    public void closeDatabase(){

        this.database.close();

    }
}