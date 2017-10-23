package com.nadoo.tacademy.eunbyul_nanu;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2017-06-05.
 */

public class WantItem_ParseData {
    public static ArrayList<WantItemObject> getJSONBloodRequestAllList(String buf) {
        ArrayList<WantItemObject> jsonAllList = null;
        JSONArray jsonArray = null;
         try {
            jsonAllList = new ArrayList<WantItemObject>();
            JSONObject jobject = new JSONObject(buf);//buf로 들어온 jsonobject 저장.
            JSONArray jarray = jobject.getJSONArray("data");
            //들어온 jsonobjec 중 "data"값만 추출 후 jsonarray로 저장. "data"에 들어있는 값들은 배열형태이므로.
            int jarray_size = jarray.length();
            //Log.e("size",String.valueOf(jarray_size));
            for (int i = 0; i < jarray_size; i++) {
                WantItemObject want = new WantItemObject();
                JSONObject jData = jarray.getJSONObject(i);
                want.wantItem_userid = jData.getInt("user_id");
                want.wantItem_id = jData.getInt("need_id"); //글의 아이디
                want.wantItem_user = jData.getString("profile_thumbURL"); //사진
                want.wantItem_username = jData.getString("nickname"); //유저 아이디
                want.wantItem_category = jData.getString("category"); //카테고리
                want.wantItem_title = jData.getString("title"); //제목
                want.wantItem_story = jData.getString("article"); //본문
                want.wantItem_time = jData.getString("writedate"); //본문
                want.wantItem_location = jData.getString("location"); //본문
                jsonAllList.add(want);
            }
        } catch (JSONException je) {
            Log.e("RequestAllList", "JSON파싱 중 에러발생", je);
        }
        return jsonAllList;
    }
}
