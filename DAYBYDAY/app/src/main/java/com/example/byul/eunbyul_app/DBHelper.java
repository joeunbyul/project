package com.example.byul.eunbyul_app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by byul on 2016-05-29.
 *
 *
 */

public class DBHelper extends SQLiteOpenHelper {


    public  DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    public void onCreate(SQLiteDatabase db){
       String CREATE_TABLE = "CREATE TABLE todo_(name TEXT , nowtime TEXT, state TEXT);";
        db.execSQL(CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS todo_");
        onCreate(db);
    }


}
