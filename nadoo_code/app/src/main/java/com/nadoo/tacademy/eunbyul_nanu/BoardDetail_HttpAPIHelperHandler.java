package com.nadoo.tacademy.eunbyul_nanu;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.util.Log.e;

/**
 * Created by Kyoonho on 2017-06-13.
 */

public class BoardDetail_HttpAPIHelperHandler {
    private static final String DEBUG_TAG = "BoardDetail_HttpAPIHelperHandler";
    public static ArrayList<BoardItem> BoardDetailSelect(String boardid){
        ArrayList<BoardItem> boardDetailItems = null;
        boolean flag;
        Response response = null;
        OkHttpClient toServer;

        try {

            toServer = OkHttpInitSingtonManager.getOkHttpApplyCookieClient();
            Request request = new Request.Builder().url(NetworkDefineConstant.SERVER_URL_NADO_BOARD_DETAIL+boardid).build();
            response = toServer.newCall(request).execute();

            flag = response.isSuccessful();
            String returedJSON;

            if (flag) { //성공했다면
                returedJSON = response.body().string(); //string형태로 json저장.
                boardDetailItems = BoardDetail_ParseData.getJSONBoardDetail(returedJSON);

            } else {
                //요청에러 발생시(http 에러)
            }
        } catch (UnknownHostException une) {
            e("aaa", une.toString()); //host찾을 수 없을때
        } catch (UnsupportedEncodingException uee) {
            e("bbb", uee.toString());
        } catch (Exception e) { //이 exception은 반드시 마지막에 넣어줘야 함.
            e("ccc", e.toString());
        } finally { //finally는 반드시 처리 해줘야함.
            if (response != null) {
                response.close(); //3.* 이상에서는 반드시 연결을 닫아 준다.
            }
        }
        return boardDetailItems;
    }



}
