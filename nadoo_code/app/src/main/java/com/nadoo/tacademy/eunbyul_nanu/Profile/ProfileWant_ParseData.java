package com.nadoo.tacademy.eunbyul_nanu.Profile;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2017-06-09.
 */

public class ProfileWant_ParseData {
    public static ArrayList<ProfileWantObject> getJSONBloodRequestAllList(String buf) {
        ArrayList<ProfileWantObject> jsonAllList = null;
        JSONArray data = null;
        try {
            jsonAllList = new ArrayList<ProfileWantObject>();
            JSONObject jobject = new JSONObject(buf);//buf로 들어온 jsonobject 저장.
            data = jobject.getJSONArray("data");

            //들어온 jsonobjec 중 "data"값만 추출 후 jsonarray로 저장. "data"에 들어있는 값들은 배열형태이므로.
            int jarray_size = data.length();
            Log.e("size",String.valueOf(jarray_size));
           for (int i = 0; i < jarray_size; i++) {
            ProfileWantObject profile_want = new ProfileWantObject();
                JSONObject jData = data.getJSONObject(i);
                profile_want.profile_want_title = jData.getString("title"); //아이템 아이디
                profile_want.profile_want_contents = jData.getString("article"); //사진 이미지
                profile_want.profile_want_date = jData.getString("writedate"); //아이템 이름
                jsonAllList.add(profile_want);
            }

        } catch (JSONException je) {
            Log.e("RequestAllList", "JSON파싱 중 에러발생", je);
        }
        return jsonAllList;
    }
}
