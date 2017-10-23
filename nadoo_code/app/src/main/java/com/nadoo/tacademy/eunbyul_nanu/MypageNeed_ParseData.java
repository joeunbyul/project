package com.nadoo.tacademy.eunbyul_nanu;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2017-06-05.
 */

public class MypageNeed_ParseData {
    public static ArrayList<MypageNeedObject> getJSONBloodRequestAllList(String buf) {
        ArrayList<MypageNeedObject> jsonAllList = null;
        JSONArray jsonArray = null;
         try {
            jsonAllList = new ArrayList<MypageNeedObject>();
            JSONObject jobject = new JSONObject(buf);//buf로 들어온 jsonobject 저장.
            JSONArray jarray = jobject.getJSONArray("data");
            //들어온 jsonobjec 중 "data"값만 추출 후 jsonarray로 저장. "data"에 들어있는 값들은 배열형태이므로.
            int jarray_size = jarray.length();
            Log.e("size",String.valueOf(jarray_size));
            for (int i = 0; i < jarray_size; i++) {
                MypageNeedObject mypageneed = new MypageNeedObject();
                JSONObject jData = jarray.getJSONObject(i);
                mypageneed.mypage_need_title = jData.getString("title");
                mypageneed.mypage_need_contents = jData.getString("article");
                mypageneed.mypage_need_time = jData.getString("writedate");
                mypageneed.mypage_need_category=jData.getString("category");
                mypageneed.mypage_need_location = jData.getString("location");
                mypageneed.mypage_need_id = jData.getInt("need_id");
                Log.e("!!!need_id!!!",String.valueOf(mypageneed.mypage_need_id));
                jsonAllList.add(mypageneed);
            }
        } catch (JSONException je) {
            Log.e("RequestAllList", "JSON파싱 중 에러발생", je);
        }
        return jsonAllList;
    }
}
