package com.nadoo.tacademy.eunbyul_nanu;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2017-06-05.
 */

public class Board_ParseData {
    public static ArrayList<BoardItem> getJSONBloodRequestAllList(String buf) {
        ArrayList<BoardItem> jsonAllList = null;
        JSONArray jsonArray = null;
         try {
            jsonAllList = new ArrayList<BoardItem>();
            JSONObject jobject = new JSONObject(buf);//buf로 들어온 jsonobject 저장.
            JSONArray jarray = jobject.getJSONArray("data");
            //들어온 jsonobjec 중 "data"값만 추출 후 jsonarray로 저장. "data"에 들어있는 값들은 배열형태이므로.
            int jarray_size = jarray.length();
            //Log.e("size",String.valueOf(jarray_size));
            for (int i = 0; i < jarray_size; i++) {
                BoardItem boardItem = new BoardItem();
                JSONObject jData = jarray.getJSONObject(i);
                boardItem.board_num = jData.getString("board_id");
                boardItem.image = jData.getString("profile_thumbURL");
                boardItem.title = jData.getString("title");
                boardItem.contents = jData.getString("article");
                boardItem.reply_num = jData.getString("commentCount");
                boardItem.recommand_num = jData.getString("likeCount");
                boardItem.nickname = jData.getString("nickname");
                jsonAllList.add(boardItem);
            }
        } catch (JSONException je) {
            Log.e("RequestAllList", "JSON파싱 중 에러발생", je);
        }
        return jsonAllList;
    }
}
