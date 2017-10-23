package com.nadoo.tacademy.eunbyul_nanu;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2017-06-09.
 */

public class Item_ParseData {
    public static ArrayList<ItemObject> getJSONBloodRequestAllList(String buf) {
        ArrayList<ItemObject> jsonAllList = null;
        JSONArray jsonArray = null;
        try {
            jsonAllList = new ArrayList<ItemObject>();
            JSONObject jobject = new JSONObject(buf);//buf로 들어온 jsonobject 저장.
            JSONArray jarray = jobject.getJSONArray("data");
            //들어온 jsonobjec 중 "data"값만 추출 후 jsonarray로 저장. "data"에 들어있는 값들은 배열형태이므로.
            int jarray_size = jarray.length();
           // Log.e("size",String.valueOf(jarray_size));
            for (int i = 0; i < jarray_size; i++) {
                ItemObject item = new ItemObject();
                JSONObject jData = jarray.getJSONObject(i);
                item.item_id = jData.getInt("item_id"); //아이템 아이디
                item.item_photo = jData.getString("thumbnailURL"); //사진 이미지
                item.item1 = jData.getString("imageURL1");
                item.item2 = jData.getString("imageURL2");
                item.item3 = jData.getString("imageURL3");
                item.item_name = jData.getString("title"); //아이템 이름
                item.item_price = jData.getString("price"); //가격
                item.item_state = jData.getInt("status"); //물품 상태
                item.item_time = jData.getString("priceKind"); //시간,일,협의
                item.item_userid = jData.getInt("user_id"); //유저 아이디
                item.item_nickname = jData.getString("nickname"); //유저 닉네임
                item.item_category = jData.getString("category"); //카테고리
                item.item_article = jData.getString("article"); //소개글
                item.item_check = jData.getInt("check"); //인증여부
                item.item_location = jData.getString("location"); //위치
                item.item_userphoto = jData.getString("profile_thumbURL"); //사용자프로필

                jsonAllList.add(item);
            }
        } catch (JSONException je) {
            Log.e("RequestAllList", "JSON파싱 중 에러발생", je);
        }
        return jsonAllList;
    }
}
