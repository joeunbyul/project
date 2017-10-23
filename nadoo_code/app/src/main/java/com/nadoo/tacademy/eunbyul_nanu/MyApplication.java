package com.nadoo.tacademy.eunbyul_nanu;

import android.app.Application;
import android.content.Context;

/**
 * Created by Tacademy on 2017-05-26.
 */

public class MyApplication extends Application
{
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
    public static Context getmContext(){
        return mContext;
    }
}