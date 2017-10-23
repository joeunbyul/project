package com.nadoo.tacademy.eunbyul_nanu.Join;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nadoo.tacademy.eunbyul_nanu.LoginActivity;
import com.nadoo.tacademy.eunbyul_nanu.MainActivity;
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

public class MembershipActivityStep3 extends Activity {

    /*인증메일 보내기*/
    ImageButton mail_sending_btn;
   // CertificationDialog mCertificationDialog;

    String nickname, school,email,pw,schoolemail,schooladdress;
    int user_state;

    /*학교메일*/
    EditText join_schoolemail;
    TextView join_schooladdress;

    ImageButton pass;

    /*오브젝트*/
    public class JoinStep3{
        String join_email;
        String join_pw;
        String join_nickname;
        String join_school;
        int user_state;
        String join_schoolemail;
        String join_schooladdress;
        public JoinStep3(){ }
        public JoinStep3(String join_email,String join_pw, String join_nickname,
                         String join_school,int user_state,String join_schoolemail,
                         String join_schooladdress){
            this.join_email = join_email;
            this.join_pw = join_pw;
            this.join_nickname = join_nickname;
            this.join_school = join_school;
            this.user_state = user_state;
            this.join_schoolemail = join_schoolemail;
            this.join_schooladdress = join_schooladdress;
        }
    }
    private JoinStep3 JoinStep3;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership_step3);

        Intent intent = getIntent();
        nickname = intent.getExtras().getString("nickname");
        email = intent.getExtras().getString("email");
        school = intent.getExtras().getString("school");
        pw = intent.getExtras().getString("pw");
        user_state = intent.getExtras().getInt("user_state");

        if(JoinStep3==null){
            JoinStep3 = new JoinStep3();
        }

        join_schoolemail = (EditText)findViewById(R.id.join_schoolemail);
        schoolemail = join_schoolemail.getText().toString();
        join_schooladdress = (TextView)findViewById(R.id.join_schooladdress);
        if(school.equalsIgnoreCase("동양미래대학교")){
            join_schooladdress.setText("@dongyang.ac.kr");
        }


        pass = (ImageButton)findViewById(R.id.pass);
        pass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent i = new Intent(MembershipActivityStep3.this,MainActivity.class);
                startActivity(i);
                return true;
            }
        });

        mail_sending_btn = (ImageButton)findViewById(R.id.mail_sending_btn);
    }

    public void onClickView(View v){
        switch (v.getId()){
            case R.id.mail_sending_btn :
                schoolemail = join_schoolemail.getText().toString();
                schooladdress = join_schooladdress.getText().toString();
                JoinStep3.join_email = email;
                JoinStep3.join_pw = pw;
                JoinStep3.join_nickname = nickname;
                JoinStep3.join_school = school;
                JoinStep3.join_schoolemail = schoolemail;
                JoinStep3.join_schooladdress = schooladdress;

                 new AsyncJoinStep3().execute(JoinStep3);

                break;
            case R.id.pass:
                JoinStep3.join_email = email;
                JoinStep3.join_pw = pw;
                JoinStep3.join_nickname = nickname;
                JoinStep3.join_school = school;
                JoinStep3.join_schoolemail = "";
                JoinStep3.join_schooladdress = "";
                new AsyncJoinStep3().execute(JoinStep3);
                break;
        }
    }

    public class AsyncJoinStep3 extends AsyncTask<JoinStep3, Integer,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(JoinStep3... JoinStep3Info) {
            boolean flag;
            String JoinStep3Result = "";
            JoinStep3 reqParams = JoinStep3Info[0];
            Response response = null;
            final OkHttpClient toServer ;

            try {
                toServer = OkHttpInitSingtonManager.getOkHttpApplyCookieClient();

                RequestBody postBody = new FormBody.Builder()
                        .add("user_id",reqParams.join_email)
                        .add("password",reqParams.join_pw)
                        .add("nickname",reqParams.join_nickname)
                        .add("school",reqParams.join_school)
                        .add("user_stat",String.valueOf(reqParams.user_state))

                        .build();
                final Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_NADOO_JOIN)
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
                        JoinStep3Result = jsonObject.optString("msg");

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

            return JoinStep3Result;
        }

        @Override
        protected void onPostExecute(String result) {
            //super.onPostExecute(s);

            if(result != null){

                if (result.equalsIgnoreCase("post success")){
                    Toast.makeText(MyApplication.getmContext(),"회원가입이 완료되었습니다.",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MembershipActivityStep3.this,LoginActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(MyApplication.getmContext(),"인증이 되지 않았습니다.",Toast.LENGTH_SHORT).show();
                }


            }else{
                Toast.makeText(MyApplication.getmContext(),"입력 중 문제 발생",Toast.LENGTH_SHORT).show();
            }
            Log.e("result",result);
        }
    }


}
