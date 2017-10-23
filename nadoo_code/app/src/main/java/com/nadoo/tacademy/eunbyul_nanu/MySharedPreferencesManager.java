package com.nadoo.tacademy.eunbyul_nanu;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MySharedPreferencesManager {
    private static MySharedPreferencesManager instance;
    //singleton객체 생성. 딱 한번반 실행.
    public static MySharedPreferencesManager getInstance() {
        if (instance == null) {
            instance = new MySharedPreferencesManager();
        }
        return instance;
    }

    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    private MySharedPreferencesManager() {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getmContext());
        mEditor = mPrefs.edit();
    }

    public static final String KEY_ID = "id";
    public static final String KEY_PASSWORD = "password";

    //setter와 getter를 반드시 줘야함.
    public void setId(String id) {
        mEditor.putString(KEY_ID, id);
        mEditor.commit();
    }

    public String getId() {
        return mPrefs.getString(KEY_ID,"");
    }

    public void setPassword(String password) {
        mEditor.putString(KEY_PASSWORD, password);
        mEditor.commit();//반드시 commit을 해줘야 물리적 구조로 넘어감.
    }

    public String getPassword() {
        return mPrefs.getString(KEY_PASSWORD, "");
    }

    public boolean isBackupSync() {
        return mPrefs.getBoolean("perf_sync", false);
    }
}
