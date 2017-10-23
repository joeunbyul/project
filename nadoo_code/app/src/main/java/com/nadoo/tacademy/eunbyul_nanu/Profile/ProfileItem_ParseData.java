package com.nadoo.tacademy.eunbyul_nanu.Profile;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2017-06-09.
 */

public class ProfileItem_ParseData {
    public static ArrayList<ProfileItemObject> getJSONBloodRequestAllList(String buf) {
        ArrayList<ProfileItemObject> jsonAllList = null;
        JSONArray data = null;
        try {
            jsonAllList = new ArrayList<ProfileItemObject>();
            JSONObject jobject = new JSONObject(buf);//buf로 들어온 jsonobject 저장.
            data = jobject.getJSONArray("data");

            //들어온 jsonobjec 중 "data"값만 추출 후 jsonarray로 저장. "data"에 들어있는 값들은 배열형태이므로.
            int jarray_size = data.length();
            Log.e("size",String.valueOf(jarray_size));
           for (int i = 0; i < jarray_size; i++) {
            ProfileItemObject profile_item = new ProfileItemObject();
                JSONObject jData = data.getJSONObject(i);
                profile_item.profile_item_img = jData.getString("thumbnailURL"); //아이템 아이디
                 profile_item.profile_item_state = jData.getInt("status"); //사진 이미지
               profile_item.profile_item_article = jData.getString("article");
                profile_item.profile_item_category = jData.getString("category"); //아이템 이름
            profile_item.profile_item_title = jData.getString("title");
            profile_item.profile_time_location = jData.getString("location");
            profile_item.profile_time_price = jData.getString("price");
               profile_item.profile_time_priceKind = jData.getString("priceKind");
               profile_item.profile_item_usercheck = jData.getInt("check");
                jsonAllList.add(profile_item);
           }

        } catch (JSONException je) {
            Log.e("RequestAllList", "JSON파싱 중 에러발생", je);
        }
        return jsonAllList;
    }
}
