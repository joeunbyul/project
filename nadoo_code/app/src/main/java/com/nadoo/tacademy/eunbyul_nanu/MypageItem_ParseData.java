package com.nadoo.tacademy.eunbyul_nanu;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2017-06-05.
 */

public class MypageItem_ParseData {
    public static ArrayList<MypageItemObject> getJSONBloodRequestAllList(String buf) {
        ArrayList<MypageItemObject> jsonAllList = null;

         try {
            jsonAllList = new ArrayList<MypageItemObject>();
            JSONObject jobject = new JSONObject(buf);//buf로 들어온 jsonobject 저장.
            JSONArray jarray = jobject.getJSONArray("data");
            //들어온 jsonobjec 중 "data"값만 추출 후 jsonarray로 저장. "data"에 들어있는 값들은 배열형태이므로.
            int jarray_size = jarray.length();
            Log.e("size",String.valueOf(jarray_size));
            for (int i = 0; i < jarray_size; i++) {
                MypageItemObject mypageitem = new MypageItemObject();
                JSONObject jData = jarray.getJSONObject(i);
                mypageitem.item1 = jData.getString("imageURL1");
                mypageitem.item2 = jData.getString("imageURL2");
                mypageitem.item3 = jData.getString("imageURL3");
                mypageitem.mypage_item_need_id = jData.getInt("item_id");
                mypageitem.mypage_item_title = jData.getString("title");
                mypageitem.mypage_item_price = jData.getString("price");
                mypageitem.mypage_item_priceKind = jData.getString("priceKind");
                mypageitem.mypage_item_location = jData.getString("location");
                mypageitem.mypage_item_img=jData.getString("thumbnailURL");
                mypageitem.mypage_item_state = String.valueOf(jData.getInt("status"));
                mypageitem.mypage_item_nickname=jData.getString("nickname");
                mypageitem.mypage_item_category=jData.getString("category");
                mypageitem.mypage_item_article=jData.getString("article");
                mypageitem.mypage_item_check=jData.getInt("check");
                mypageitem.mypage_item_userphoto = jData.getString("profile_thumbURL");
                jsonAllList.add(mypageitem);
            }
        } catch (JSONException je) {
            Log.e("RequestAllList", "JSON파싱 중 에러발생", je);
        }
        return jsonAllList;
    }
}
