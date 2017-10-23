package com.nadoo.tacademy.eunbyul_nanu.Profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nadoo.tacademy.eunbyul_nanu.GetUserObject;
import com.nadoo.tacademy.eunbyul_nanu.GetUser_ParseData;
import com.nadoo.tacademy.eunbyul_nanu.LoginActivity;
import com.nadoo.tacademy.eunbyul_nanu.MainActivity;
import com.nadoo.tacademy.eunbyul_nanu.MyApplication;
import com.nadoo.tacademy.eunbyul_nanu.MypageItemObject;
import com.nadoo.tacademy.eunbyul_nanu.NetworkDefineConstant;
import com.nadoo.tacademy.eunbyul_nanu.OkHttpInitSingtonManager;
import com.nadoo.tacademy.eunbyul_nanu.R;
import com.nadoo.tacademy.eunbyul_nanu.ReportActivity;
import com.nadoo.tacademy.eunbyul_nanu.WantItemObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.util.Log.e;

public class Profile extends AppCompatActivity {

    TextView profile_id,follow_count;
    ImageButton follow_btn;

    String username,userimg;
    int user_id, like;
    ArrayList<ProfileObject> jsonAllList;
    ProfileObject profileObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if(profileObject == null){
            profileObject = new ProfileObject();
        }
        Intent intent = getIntent();
        username = intent.getExtras().getString("user_name");
        user_id = intent.getExtras().getInt("user_id");
        userimg = intent.getExtras().getString("user_img");

        new AsyncProfile().execute(profileObject);

        profile_id = (TextView)findViewById(R.id.profile_id);
        profile_id.setText(username);

        CircleImageView navi_header_image = (CircleImageView)findViewById(R.id.navi_header_image);
        Glide.with(MyApplication.getmContext()).load(userimg).into(navi_header_image);
        follow_count = (TextView)findViewById(R.id.follow_count);

        follow_btn = (ImageButton)findViewById(R.id.follow_btn);
        follow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(follow_btn.getTag() == "unfollow" && like == 0){
                    new AsyncProfileFollow().execute(profileObject);
                }else if(follow_btn.getTag() == "follow" && like == 1){
                    new AsyncProfileUnFollow().execute(profileObject);
                }
            }
        });
    }

    public void profile_btns(View v){
        switch (v.getId()){
            case R.id.profile_item:
                Intent profile_item = new Intent(MyApplication.getmContext(),ProfileItem.class);
                profile_item.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                profile_item.putExtra("username", username);
                profile_item.putExtra("user_id", user_id);
                profile_item.putExtra("userimg", userimg);
                ActivityCompat.startActivity(MyApplication.getmContext(), profile_item, null);
                break;
            case R.id.profile_want:
                Intent profile_want = new Intent(MyApplication.getmContext(),ProfileWantItem.class);
                profile_want.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                profile_want.putExtra("user_id",user_id);
                ActivityCompat.startActivity(MyApplication.getmContext(), profile_want,null);
                break;
            case R.id.profile_report:
                Intent profile_report = new Intent(MyApplication.getmContext(),ReportActivity.class);
                profile_report.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ActivityCompat.startActivity(MyApplication.getmContext(), profile_report,null);
                break;
        }
    }

    public class AsyncProfile extends AsyncTask<ProfileObject, Integer,ArrayList<ProfileObject>> {
        ArrayList<ProfileObject> profileArrayList = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<ProfileObject> doInBackground(ProfileObject... Profileinfo) {
            boolean flag;
            Response response = null;
            final OkHttpClient toServer ;

            try {
                toServer = OkHttpInitSingtonManager.getOkHttpApplyCookieClient();

                RequestBody postBody = new FormBody.Builder()
                        .add("search_id",String.valueOf(user_id))
                        .build();

                final Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_NADOO_OTHERUSER)
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
                        profileArrayList = Profile_ParseData.getJSONBloodRequestAllList(returnedJSON);

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
            return profileArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<ProfileObject> getresult) {
            //super.onPostExecute(s);

            if(getresult != null){
                follow_count.setText(String.valueOf(getresult.get(0).profile_follower));
                like = getresult.get(0).profile_like;
                if(like == 1){
                    follow_btn.setBackgroundResource(R.drawable.follow_active);
                    follow_btn.setTag("follow");
                }else if(like == 0){
                    follow_btn.setBackgroundResource(R.drawable.follow_unactive);
                    follow_btn.setTag("unfollow");
                }
            }
            else{
                Toast.makeText(MyApplication.getmContext(),"입력 중 문제 발생",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class AsyncProfileFollow extends AsyncTask<ProfileObject, Integer,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(ProfileObject... profileInfo) {
            boolean flag;
            String ItemInsertResult = "";
            ProfileObject reqParams = profileInfo[0];
            Response response = null;
            final OkHttpClient toServer ;
            try {
                toServer = OkHttpInitSingtonManager.getOkHttpApplyCookieClient();

                RequestBody postBody = new FormBody.Builder()
                        .add("following_id", String.valueOf(user_id))
                        .build();

                final Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_NADOO_OTHERUSER_FOLLOW)
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
                        ItemInsertResult = jsonObject.optString("msg");

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

            return ItemInsertResult;
        }

        @Override
        protected void onPostExecute(String result) {
            //super.onPostExecute(s);
            Log.e("result",result);
            if(result != null){
                if (result.equalsIgnoreCase("follow post success")){
                    follow_btn.setBackgroundResource(R.drawable.follow_active);
                    follow_btn.setTag("follow");
                    Toast.makeText(MyApplication.getmContext(),username+"님을 팔로우하셨습니다.",Toast.LENGTH_SHORT).show();
                    new AsyncProfile().execute(profileObject);
                }else{
                    Toast.makeText(MyApplication.getmContext(),"실패",Toast.LENGTH_SHORT).show();
                }

            }else{
                Toast.makeText(MyApplication.getmContext(),"입력 중 문제 발생",Toast.LENGTH_SHORT).show();
            }
            Log.e("result",result);
        }
    }

    public class AsyncProfileUnFollow extends AsyncTask<ProfileObject, Integer,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(ProfileObject... profileInfo) {
            boolean flag;
            String ItemInsertResult = "";
            ProfileObject reqParams = profileInfo[0];
            Response response = null;
            final OkHttpClient toServer ;
            try {
                toServer = OkHttpInitSingtonManager.getOkHttpApplyCookieClient();

                RequestBody postBody = new FormBody.Builder()
                        .add("following_id", String.valueOf(user_id))
                        .build();

                final Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_NADOO_OTHERUSER_FOLLOW)
                        .delete(postBody)
                        .build();

                response = toServer.newCall(request).execute();
                flag = response.isSuccessful();

                String returnedJSON;
                if(flag){
                    returnedJSON = response.body().string();
                    Log.e("resultJSON",returnedJSON);
                    try {
                        JSONObject jsonObject = new JSONObject(returnedJSON);
                        ItemInsertResult = jsonObject.optString("msg");

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

            return ItemInsertResult;
        }

        @Override
        protected void onPostExecute(String result) {
            //super.onPostExecute(s);
            Log.e("result",result);
            if(result != null){
                if (result.equalsIgnoreCase("follow delete success")){
                    follow_btn.setBackgroundResource(R.drawable.follow_unactive);
                    follow_btn.setTag("unfollow");
                    Toast.makeText(MyApplication.getmContext(),username+"님을 팔로우취소하셨습니다.",Toast.LENGTH_SHORT).show();
                    new AsyncProfile().execute(profileObject);
                }else{
                    Toast.makeText(MyApplication.getmContext(),"실패",Toast.LENGTH_SHORT).show();
                }

            }else{
                Toast.makeText(MyApplication.getmContext(),"입력 중 문제 발생",Toast.LENGTH_SHORT).show();
            }
            Log.e("result",result);
        }
    }
}
