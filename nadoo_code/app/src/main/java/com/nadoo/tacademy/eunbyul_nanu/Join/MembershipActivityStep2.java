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

public class MembershipActivityStep2 extends Activity implements View.OnClickListener{

    EditText login_nickname,login_school;
    String nickname, school,email,pw;
    int user_state;

    ImageButton nextStep2,student_checkbox;
    int CHECK_SUM = 0;

    Response response;

    public class JoinStep2{
        String join_nickname;
        String join_school;
        public JoinStep2(){ }
        public JoinStep2(String join_nickname,String join_school){
            this.join_nickname = join_nickname;
            this.join_school = join_school;
        }
    }
    private JoinStep2 JoinStep2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership_step2);

        Intent step1 = getIntent();
        email = step1.getExtras().getString("email");
        pw = step1.getExtras().getString("pw");

        if(JoinStep2==null){
            JoinStep2 = new JoinStep2();
        }


        student_checkbox = (ImageButton) findViewById(R.id.login_step2_check);
        student_checkbox.setOnClickListener(this);

        nextStep2 = (ImageButton)findViewById(R.id.step_2);
        nextStep2.setOnClickListener(this);

        login_nickname = (EditText)findViewById(R.id.login_nickname);
        login_school = (EditText)findViewById(R.id.login_school);

        login_school.setText("동양미래대학교");

    }
    public void onClick(View v){

        if(v.getId() == R.id.login_step2_check){
            if(CHECK_SUM==0){
                student_checkbox.setSelected(true);
                CHECK_SUM=1;
                user_state = 1;

            }else{
                student_checkbox.setSelected(false);
                CHECK_SUM=0;
                user_state = 2;
            }


        }
        else if(v.getId() == R.id.step_2){
            nickname = login_nickname.getText().toString();
            school = login_school.getText().toString();
            JoinStep2.join_nickname = nickname;
            JoinStep2.join_school = school;
            if(JoinStep2.join_nickname == null || JoinStep2.join_nickname.length() <=0){
                Toast.makeText(MyApplication.getmContext(),"닉네임을 입력하세요.",Toast.LENGTH_LONG).show();
            }else if(JoinStep2.join_school == null || JoinStep2.join_school.length() <=0) {
                Toast.makeText(MyApplication.getmContext(), "학교를 입력하세요.", Toast.LENGTH_LONG).show();
            }else{
                 new AsyncJoinStep2().execute(JoinStep2);
            }

        }
    }

    public class AsyncJoinStep2 extends AsyncTask<JoinStep2, Integer,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(JoinStep2... JoinStep2Info) {
            boolean flag;
            String JoinStep2Result = "";
            JoinStep2 reqParams = JoinStep2Info[0];
            response = null;
            final OkHttpClient toServer ;

            try {
                toServer = OkHttpInitSingtonManager.getOkHttpApplyCookieClient();

                RequestBody postBody = new FormBody.Builder()
                        .add("user_id",reqParams.join_nickname)
                        .build();
                final Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_NADOO_JOINNICKNAMECHECK)
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
                        JoinStep2Result = jsonObject.optString("msg");

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

            return JoinStep2Result;
        }

        @Override
        protected void onPostExecute(String result) {
            //super.onPostExecute(s);

            if(result != null){

                if (result.equalsIgnoreCase("no match nick")){
                    Intent intent = new Intent(MyApplication.getmContext(), MembershipActivityStep3.class);
                    intent.putExtra("email",email);
                    intent.putExtra("pw",pw);
                    intent.putExtra("nickname",nickname);
                    intent.putExtra("school",school);
                    intent.putExtra("user_state",user_state);
                    startActivity(intent);
                }else if(result.equalsIgnoreCase("nick already exist")){
                    Toast.makeText(MyApplication.getmContext(),"중복되는 닉네임이 았습니다.",Toast.LENGTH_SHORT).show();
                }


            }else{
                Toast.makeText(MyApplication.getmContext(),"입력 중 문제 발생",Toast.LENGTH_SHORT).show();
            }
            Log.e("result",result);
        }
    }
}
