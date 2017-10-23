package com.nadoo.tacademy.eunbyul_nanu.Join;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.nadoo.tacademy.eunbyul_nanu.MyApplication;
import com.nadoo.tacademy.eunbyul_nanu.NetworkDefineConstant;
import com.nadoo.tacademy.eunbyul_nanu.OkHttpInitSingtonManager;
import com.nadoo.tacademy.eunbyul_nanu.R;

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
 * Created by Kyoonho on 2017-06-05.
 */

public class MembershipActivityStep1 extends Activity implements View.OnClickListener {

    ImageButton nextStep1;
    EditText join_email,join_pw;
    String email,pw;

    Response response;

    public class JoinStep1{
        String join_email;
        String join_pw;
        public JoinStep1(){ }
        public JoinStep1(String join_email,String join_pw){
            this.join_email = join_email;
            this.join_pw = join_pw;
        }
    }

    private JoinStep1 JoinStep1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership_step1);

        if(JoinStep1==null){
            JoinStep1 = new JoinStep1();
        }

        join_email = (EditText)findViewById(R.id.join_email);
        join_pw = (EditText)findViewById(R.id.join_pw);

        nextStep1 = (ImageButton)findViewById(R.id.step_1);
        nextStep1.setOnClickListener(this);

    }
    public void onClick(View v){
        if(v.getId() == R.id.step_1){
            email = join_email.getText().toString();
            pw = join_pw.getText().toString();
            JoinStep1.join_email = email;
            JoinStep1.join_pw = pw;
            if(JoinStep1.join_email == null || JoinStep1.join_email.length() <=0){
                Toast.makeText(MyApplication.getmContext(),"이메일을 입력하세요.",Toast.LENGTH_LONG).show();
            }else if(JoinStep1.join_pw == null || JoinStep1.join_pw.length() <=0) {
                Toast.makeText(MyApplication.getmContext(), "패스워드를 입력하세요.", Toast.LENGTH_LONG).show();
            }else{
                new AsyncJoinStep1().execute(JoinStep1);

            }

        }
    }

    public class AsyncJoinStep1 extends AsyncTask<JoinStep1, Integer,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(JoinStep1... Joinstep1Info) {
            boolean flag;
            String JoinStep1Result = "";
            JoinStep1 reqParams = Joinstep1Info[0];
            response = null;
            final OkHttpClient toServer ;

            try {
                toServer = OkHttpInitSingtonManager.getOkHttpApplyCookieClient();

                RequestBody postBody = new FormBody.Builder()
                        .add("user_id",reqParams.join_email)
                        .build();
                final Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_NADOO_JOINEMAILCHECK)
                        .post(postBody)
                        .build();
                response = toServer.newCall(request).execute();
                flag = response.isSuccessful();
                String returnedJSON;
                if(flag){
                    returnedJSON = response.body().string();
                    Log.e("resultJSON",returnedJSON);
                    try {
                        JSONObject jsonObject = new JSONObject(returnedJSON);
                        JoinStep1Result = jsonObject.optString("msg");

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

            return JoinStep1Result;
        }

        @Override
        protected void onPostExecute(String result) {
            //super.onPostExecute(s);

            if(result != null){

                if (result.equalsIgnoreCase("no match user_id")){
                    Intent intent = new Intent(MyApplication.getmContext(), MembershipActivityStep2.class);
                    intent.putExtra("email",email);
                    intent.putExtra("pw",pw);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else if(result.equalsIgnoreCase("user_id already exist")){
                    Toast.makeText(MyApplication.getmContext(),"중복되는 아이디가 았습니다.",Toast.LENGTH_SHORT).show();
                }


            }else{
                Toast.makeText(MyApplication.getmContext(),"입력 중 문제 발생",Toast.LENGTH_SHORT).show();
            }
            Log.e("result",result);
        }
    }

}
