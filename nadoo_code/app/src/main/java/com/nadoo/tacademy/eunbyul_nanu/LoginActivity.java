package com.nadoo.tacademy.eunbyul_nanu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.nadoo.tacademy.eunbyul_nanu.Join.MembershipActivityStep1;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.util.Log.e;
import static android.util.Log.i;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    ImageButton membership_btn, findPassword_btn;
    EditText loginpage_email,loginpage_pw;
    ImageButton loginpage_login, facebook_login, kakao_login;
    String user,nickname,profile_thumbURL;
    int school_check,facebook_check,kakaotalk_check;
    String email,pw;

    //키,값형태로 데이터를 저장하는 구조
    SharedPreferences mPrefs;
    //Preference를 편집할 수 있는 객체
    SharedPreferences.Editor mEditor;

    public static final String PREF_NAME = "prefs"; //주고 싶은 이름으로 주면됨.
    public static final String KEY_ID = "id";
    public static final String KEY_PASSWORD = "password";

    public class Login{
        String loginpage_email;
        String loginpage_pw;
        public Login(){ }
        public Login(String loginpage_email,String loginpage_pw){
            this.loginpage_email = loginpage_email;
            this.loginpage_pw = loginpage_pw;
        }
    }
    private Login Login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(Login==null){
            Login = new Login();
        }

        mPrefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        //프리퍼런스이름(PREF_NAME)을 찾고 우리 앱에서 공유하겠다.
        mEditor = mPrefs.edit();

        loginpage_email = (EditText)findViewById(R.id.loginpage_email);
        loginpage_pw = (EditText)findViewById(R.id.loginpage_pw);
        facebook_login = (ImageButton)findViewById(R.id.facebook_login);
        kakao_login = (ImageButton)findViewById(R.id.kakao_login);
        facebook_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyApplication.getmContext(),"준비 중입니다.",Toast.LENGTH_SHORT).show();
            }
        });
        kakao_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyApplication.getmContext(),"준비 중입니다.",Toast.LENGTH_SHORT).show();
            }
        });


        //startActivity(new Intent(this, SplashActivity.class));
      /*회원가입 페이지 이동*/
        membership_btn = (ImageButton)findViewById(R.id.membership);
        membership_btn.setOnClickListener(this);

      /*비밀번호 찾기 페이지 이동*/
        findPassword_btn = (ImageButton)findViewById(R.id.find_password);
        findPassword_btn.setOnClickListener(this);

        loginpage_login = (ImageButton)findViewById(R.id.loginpage_login);
        loginpage_login.setOnClickListener(this);


    }
    public void onClick(View v){
        //회원가입 페이지
        if(v.getId()==R.id.membership){
            Intent intent = new Intent(this, MembershipActivityStep1.class);
            startActivity(intent);
        }else if(v.getId() == R.id.find_password){
            Intent intent = new Intent(this, FindPasswordActivity.class);
            startActivity(intent);
        }else if(v.getId() == R.id.loginpage_login){
            email = loginpage_email.getText().toString();
            pw = loginpage_pw.getText().toString();

            Login.loginpage_email = email;
            Login.loginpage_pw = pw;
            if(Login.loginpage_email == null || Login.loginpage_email.length() <=0){
                Toast.makeText(MyApplication.getmContext(),"이메일을 입력하세요.",Toast.LENGTH_LONG).show();
            }else if(Login.loginpage_pw == null || Login.loginpage_pw.length() <=0) {
                Toast.makeText(MyApplication.getmContext(), "패스워드를 입력하세요.", Toast.LENGTH_LONG).show();
            }else{
                String id = loginpage_email.getText().toString();
                String password = loginpage_pw.getText().toString();
                MySharedPreferencesManager.getInstance().setId(id);
                MySharedPreferencesManager.getInstance().setPassword(password);
                loginpage_email.setText("");
                loginpage_pw.setText("");

                new AsyncLogin().execute(Login);
            }
        }
    }

    public class AsyncLogin extends AsyncTask<Login, Integer,ArrayList<GetUserObject>> {
        ArrayList<GetUserObject> getUserObjects = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<GetUserObject> doInBackground(Login... LoginInfo) {
            boolean flag;
            String LoginResult = "";
            Login reqParams = LoginInfo[0];
            Response response = null;
            final OkHttpClient toServer ;

            try {
                toServer = OkHttpInitSingtonManager.getOkHttpApplyCookieClient();

                RequestBody postBody = new FormBody.Builder()
                        .add("user_id",reqParams.loginpage_email)
                        .add("password",reqParams.loginpage_pw)
                        .build();

                final Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_NADOO_LOGIN)
                        .post(postBody)
                        .build();

                response = toServer.newCall(request).execute();
                flag = response.isSuccessful();

                String returnedJSON;
                if(flag){
                    returnedJSON = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(returnedJSON);
                        LoginResult = jsonObject.optString("msg");

                        getUserObjects = GetUser_ParseData.getJSONBloodRequestAllList(returnedJSON);

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

            return getUserObjects;
        }

        @Override
        protected void onPostExecute(ArrayList<GetUserObject> getresult) {
            //super.onPostExecute(s);

            if(getresult != null){
                SharedPreferences user_pref = getSharedPreferences("user_pref", MODE_PRIVATE);
                SharedPreferences.Editor editor = user_pref.edit();
                editor.putInt("state",1);
                editor.putString("email",email);
                editor.putString("pw",pw);
                editor.putString("school",getresult.get(0).school);
                editor.putString("user_id",getresult.get(0).user_id); //user_id가 곧 이메일
                editor.putString("nickname",getresult.get(0).nickname);
                editor.putInt("school_check",getresult.get(0).school_check);
                editor.putInt("facebook_check",getresult.get(0).facebook_check);
                editor.putInt("kakaotalk_check",getresult.get(0).kakaotalk_check);
                editor.putString("profile_thumbURL",getresult.get(0).profile_thumbURL);
                editor.apply();

                Intent intent = new Intent(MyApplication.getmContext(), MainActivity.class);
                Toast.makeText(MyApplication.getmContext(), getresult.get(0).nickname+"님 환영합니다.",Toast.LENGTH_SHORT).show();

                loginpage_email.setText(mPrefs.getString(KEY_ID, ""));
                loginpage_pw.setText(mPrefs.getString(KEY_PASSWORD,""));
                loginpage_email.setText(MySharedPreferencesManager.getInstance().getId());
                loginpage_pw.setText(MySharedPreferencesManager.getInstance().getPassword());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);
                finish();
            }/*else if(result.equalsIgnoreCase("no match user_id")){
                    Toast.makeText(MyApplication.getmContext(),"아이디를 다시 입력하세요.",Toast.LENGTH_SHORT).show();
                }else if(result.equalsIgnoreCase("no match password")){
                    Toast.makeText(MyApplication.getmContext(),"비밀번호를 다시 입력하세요.",Toast.LENGTH_SHORT).show();
                }*/

            else{
                Toast.makeText(MyApplication.getmContext(),"입력 중 문제 발생",Toast.LENGTH_SHORT).show();
            }

        }
    }

    public class AsyncUserJSONList extends AsyncTask<String, Integer, ArrayList<GetUserObject>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected ArrayList<GetUserObject> doInBackground(String... params) {
            return GetUser_HttpAPIHelperHandler.userAllSelect();
        }
        @Override
        protected void onPostExecute(ArrayList<GetUserObject> result) {
            super.onPostExecute(result);
            Log.e("result2",String.valueOf(result));
            if(result != null && result.size() > 0){

            }
        }
    }
}
