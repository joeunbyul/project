package com.nadoo.tacademy.eunbyul_nanu.Profile;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.nadoo.tacademy.eunbyul_nanu.MyApplication;
import com.nadoo.tacademy.eunbyul_nanu.NetworkDefineConstant;
import com.nadoo.tacademy.eunbyul_nanu.OkHttpInitSingtonManager;
import com.nadoo.tacademy.eunbyul_nanu.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.util.Log.e;

public class ProfileWantItem extends AppCompatActivity {

    int user_id;
    RecyclerView recyclerView;
    ProfileWantObject profileWantObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_want_item);

        if(profileWantObject == null){
            profileWantObject = new ProfileWantObject();
        }
        Intent i = getIntent();
        user_id = i.getExtras().getInt("user_id");
         recyclerView = (RecyclerView)findViewById(R.id.profile_want_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MyApplication.getmContext());
        recyclerView.setLayoutManager(layoutManager);
        new AsyncProfileWant().execute(profileWantObject);
    }

    public class AsyncProfileWant extends AsyncTask<ProfileWantObject, Integer,ArrayList<ProfileWantObject>> {
        ArrayList<ProfileWantObject> profileArrayList = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<ProfileWantObject> doInBackground(ProfileWantObject... Profileinfo) {
            boolean flag;
            Response response = null;
            final OkHttpClient toServer ;

            try {
                toServer = OkHttpInitSingtonManager.getOkHttpApplyCookieClient();

                RequestBody postBody = new FormBody.Builder()
                        .add("search_id",String.valueOf(user_id))
                        .build();

                final Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_NADOO_OTHERUSER_NEED)
                        .post(postBody)
                        .build();

                response = toServer.newCall(request).execute();
                flag = response.isSuccessful();


                String returnedJSON;
                if(flag){
                    returnedJSON = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(returnedJSON);
                        profileArrayList = ProfileWant_ParseData.getJSONBloodRequestAllList(returnedJSON);

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
        protected void onPostExecute(ArrayList<ProfileWantObject> getresult) {
            //super.onPostExecute(s);

            if(getresult != null){
                recyclerView.setAdapter(new ProfileWantRecyclerAdapter(MyApplication.getmContext(), getresult,R.layout.activity_profile_want_recycler_item));
            }
            else{
                Toast.makeText(MyApplication.getmContext(),"입력 중 문제 발생",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
