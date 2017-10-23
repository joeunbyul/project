package com.nadoo.tacademy.eunbyul_nanu;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2017-06-05.
 */

public class GetUser_ParseData {
    public static ArrayList<GetUserObject> getJSONBloodRequestAllList(String buf) {
        ArrayList<GetUserObject> jsonGetUserdata = null;
        JSONArray jsonArray = null;
         try {
             jsonGetUserdata = new ArrayList<GetUserObject>();
            JSONObject jobject = new JSONObject(buf);//buf로 들어온 jsonobject 저장.
            JSONArray jarray = jobject.getJSONArray("data");
            //들어온 jsonobjec 중 "data"값만 추출 후 jsonarray로 저장. "data"에 들어있는 값들은 배열형태이므로.
            int jarray_size = jarray.length();
            Log.e("size",String.valueOf(jarray_size));
            for (int i = 0; i < jarray_size; i++) {
                GetUserObject user = new GetUserObject();
                JSONObject jData = jarray.getJSONObject(i);
                user.user_id = jData.getString("user_id");
                user.school = jData.getString("school");
                user.nickname = jData.getString("nickname");
                user.school_check = jData.getInt("school_check");
                user.facebook_check = jData.getInt("facebook_check");
                user.kakaotalk_check = jData.getInt("kakaotalk_check");
                user.profile_thumbURL = jData.getString("profile_thumbURL");
                jsonGetUserdata.add(user);
            }
        } catch (JSONException je) {
            Log.e("RequestAllList", "JSON파싱 중 에러발생", je);
        }
        return jsonGetUserdata;
    }
}
