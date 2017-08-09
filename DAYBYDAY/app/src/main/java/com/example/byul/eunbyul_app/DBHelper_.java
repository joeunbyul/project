package com.example.byul.eunbyul_app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 216 on 2016-11-21.
 */
public class DBHelper_ extends SQLiteOpenHelper {


    public  DBHelper_(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    public void onCreate(SQLiteDatabase db){
        String CREATE_TABLE = "CREATE TABLE ddaylist(name TEXT , days TEXT, unit TEXT, start TEXT, memo TEXT );";
        db.execSQL(CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS ddaylist");
        onCreate(db);
    }
}
