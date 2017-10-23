package com.nadoo.tacademy.eunbyul_nanu;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2017-06-09.
 */

public class MypageFriend_ParseData {
    public static ArrayList<MyfriendObject> getJSONBloodRequestAllList(String buf) {
        ArrayList<MyfriendObject> jsonAllList = null;
        JSONArray jsonArray = null;
        try {
            jsonAllList = new ArrayList<MyfriendObject>();
            JSONObject jobject = new JSONObject(buf);//buf로 들어온 jsonobject 저장.
            JSONArray jarray = jobject.getJSONArray("data");
            //들어온 jsonobjec 중 "data"값만 추출 후 jsonarray로 저장. "data"에 들어있는 값들은 배열형태이므로.
            int jarray_size = jarray.length();
            Log.e("size",String.valueOf(jarray_size));
            for (int i = 0; i < jarray_size; i++) {
                MyfriendObject friend = new MyfriendObject();
                JSONObject jData = jarray.getJSONObject(i);
                friend.mypage_friend_user_key = jData.getInt("user_key"); //아이템 아이디
                friend.mypage_friend_nickname = jData.getString("nickname"); //사진 이미지
                friend.mypage_friend_img = jData.getString("profile_thumbURL");

                jsonAllList.add(friend);
            }
        } catch (JSONException je) {
            Log.e("RequestAllList", "JSON파싱 중 에러발생", je);
        }
        return jsonAllList;
    }
}
