package com.nadoo.tacademy.eunbyul_nanu;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.util.Log.e;

/**
 * Created by Kyoonho on 2017-06-14.
 */

public class NewBoardRegister extends Activity {
    ImageButton board_register, board_register_cancel,secret_mode;
    TextView new_board_title, new_board_contents;
    /*int anonymity = 0;//익명성 default = 0 -> 실명*/
    int anonymity;
    private BoardRegisterObject boardRegisterObject;
    int CHECK_SUM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_board_write_activity);


        if(boardRegisterObject==null){
            boardRegisterObject = new BoardRegisterObject();
        }

        secret_mode = (ImageButton)findViewById(R.id.secret_mode);

        new_board_title = (TextView)findViewById(R.id.write_list_title);
        new_board_contents = (TextView)findViewById(R.id.write_list_contents);
        board_register = (ImageButton)findViewById(R.id.board_register);
        board_register_cancel = (ImageButton)findViewById(R.id.board_register_cancel);
    }
    public void check_anonymity(View v){
        if(CHECK_SUM==0){
            secret_mode.setSelected(true);
            CHECK_SUM=1;
            anonymity = 1;
            Toast.makeText(MyApplication.getmContext(),"익명성 : "+anonymity,Toast.LENGTH_SHORT).show();
        }else{
            secret_mode.setSelected(false);
            CHECK_SUM=0;
            anonymity = 0;
            Toast.makeText(MyApplication.getmContext(),"익명성 : "+anonymity,Toast.LENGTH_SHORT).show();
        }
    }

    public void register_board(View v){

        boardRegisterObject.board_userID = "2";
        boardRegisterObject.board_category = "sub1";
        boardRegisterObject.board_title = new_board_title.getText().toString();//제목
        boardRegisterObject.board_article = new_board_contents.getText().toString();//본문
        boardRegisterObject.board_anonymity = anonymity;//익명


        new RegisterBoardAsynk().execute(boardRegisterObject);
        Toast.makeText(MyApplication.getmContext(),"게시판 등록!",Toast.LENGTH_LONG).show();

    }
    public class RegisterBoardAsynk extends AsyncTask<BoardRegisterObject,Integer, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(BoardRegisterObject... BoardInfo) {
            String boardInsertResult = "";
            BoardRegisterObject reqParams = BoardInfo[0];
            boolean flag;
            Response response = null;
            final OkHttpClient toServer ;

            try {
                toServer = OkHttpInitSingtonManager.getOkHttpApplyCookieClient();

                RequestBody postBody = new FormBody.Builder()
                        .add("user_id",reqParams.board_userID)//유저ID
                        .add("title",reqParams.board_title)//제목
                        .add("category",reqParams.board_category)//주제
                        .add("article",reqParams.board_article) //본문
                        .add("anonymity",String.valueOf(reqParams.board_anonymity))//익명 여부
                        .build();

                final Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_NADO_REGISTER_BOARD)
                        .post(postBody)
                        .build();

                response = toServer.newCall(request).execute();
                flag = response.isSuccessful();
                Log.e("결과",String.valueOf(flag));
                String returnedJSON;
                if(flag){
                    returnedJSON = response.body().string();
                    Log.e("resultJSON",returnedJSON);
                    try {
                        JSONObject jsonObject = new JSONObject(returnedJSON);
                        boardInsertResult = jsonObject.optString("data");
                    }catch (JSONException jsone){
                        Log.e("json 에러",jsone.toString());
                    }
                }else{

                }
            }catch (UnknownHostException une) {
                e("aaa", une.toString()); //host찾을 수 없을때
            }catch (UnsupportedEncodingException uee) {
                e("bbb", uee.toString());
            }catch (Exception e) { //이 exception은 반드시 마지막에 넣어줘야 함.
                e("ccc", e.toString());
            }finally{ //finally는 반드시 처리 해줘야함.
                if(response != null) {
                    response.close(); //3.* 이상에서는 반드시 연결을 닫아 준다.
                }
            }
            return boardInsertResult;
        }
        @Override
        protected void onPostExecute(String result) {

            Intent intent = new Intent(MyApplication.getmContext(), BoardFragment.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }
    }
}
