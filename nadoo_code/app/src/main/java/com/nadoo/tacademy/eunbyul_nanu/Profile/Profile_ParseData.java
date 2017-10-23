package com.nadoo.tacademy.eunbyul_nanu.Profile;

import android.util.Log;

import com.nadoo.tacademy.eunbyul_nanu.ItemObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2017-06-09.
 */

public class Profile_ParseData {
    public static ArrayList<ProfileObject> getJSONBloodRequestAllList(String buf) {
        ArrayList<ProfileObject> jsonAllList = null;
        JSONArray data = null;
        try {
            jsonAllList = new ArrayList<ProfileObject>();
            JSONObject jobject = new JSONObject(buf);//buf로 들어온 jsonobject 저장.
            data = jobject.getJSONArray("data");

            //들어온 jsonobjec 중 "data"값만 추출 후 jsonarray로 저장. "data"에 들어있는 값들은 배열형태이므로.
            int jarray_size = data.length();
            Log.e("size",String.valueOf(jarray_size));

                ProfileObject profile_info = new ProfileObject();
                JSONObject jData = data.getJSONObject(0);
                profile_info.profile_follower = jData.getInt("follower"); //아이템 아이디
                profile_info.profile_school = jData.getString("school"); //사진 이미지
                profile_info.profile_school_check = jData.getInt("school_check"); //아이템 이름
                profile_info.profile_kakaotalk_check = jData.getInt("kakaotalk_check"); //가격
                profile_info.profile_facebook_check = jData.getInt("facebook_check"); //물품 상태
                profile_info.profile_img = jData.getString("profile_thumbURL"); //시간,일,협의
                profile_info.profile_like = jobject.getInt("like");

                jsonAllList.add(profile_info);


        } catch (JSONException je) {
            Log.e("RequestAllList", "JSON파싱 중 에러발생", je);
        }
        return jsonAllList;
    }
}
