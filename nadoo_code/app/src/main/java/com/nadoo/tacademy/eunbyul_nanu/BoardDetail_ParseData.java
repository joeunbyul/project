package com.nadoo.tacademy.eunbyul_nanu;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Kyoonho on 2017-06-13.
 */

public class BoardDetail_ParseData {
    public static ArrayList<BoardItem> getJSONBoardDetail(String buf){
        ArrayList<BoardItem> jsondetail = null;
        try {
            jsondetail = new ArrayList<BoardItem>();
            JSONObject jobject = new JSONObject(buf);//buf로 들어온 jsonobject 저장.
            JSONArray jarray = jobject.getJSONArray("data_comment");
            //들어온 jsonobjec 중 "data"값만 추출 후 jsonarray로 저장. "data"에 들어있는 값들은 배열형태이므로.
            int jarray_size = jarray.length();
            for (int i = 0; i < jarray_size; i++) {
                BoardItem boardItem = new BoardItem();
                JSONObject jData = jarray.getJSONObject(i);

                boardItem.reply_nickname = jData.getString("nickname");
                boardItem.reply_contents = jData.getString("article");
                boardItem.reply_img = jData.getString("profile_thumbURL");

                jsondetail.add(boardItem);
            }
        } catch (JSONException je) {
            Log.e("RequestAllList", "JSON파싱 중 에러발생", je);
        }
        return jsondetail;
    }
}
