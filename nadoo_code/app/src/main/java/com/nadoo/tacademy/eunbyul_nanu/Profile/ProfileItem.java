package com.nadoo.tacademy.eunbyul_nanu.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nadoo.tacademy.eunbyul_nanu.ItemPage;
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

/**
 * Created by Kyoonho on 2017-05-31.
 */

public class ProfileItem extends AppCompatActivity {

    String username,userimg,article;
    int user_id;


    ProfileItemObject profileItemObject;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_item);

        if(profileItemObject == null){
            profileItemObject = new ProfileItemObject();
        }

        Intent intent = getIntent();
        username = intent.getExtras().getString("username");
        user_id = intent.getExtras().getInt("user_id");
        userimg = intent.getExtras().getString("userimg");


        recyclerView = (RecyclerView)findViewById(R.id.profile_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MyApplication.getmContext());
        recyclerView.setLayoutManager(layoutManager);

        new AsyncProfileItem().execute(profileItemObject);
    }
        public class ProfileItemRecyclerAdapter extends RecyclerView.Adapter<ProfileItemRecyclerAdapter.ViewHolder> {

        Context context;
        List<ProfileItemObject> profile_item;
        private int itemlayout;
        public ProfileItemRecyclerAdapter(Context context, List<ProfileItemObject> profile_item, int itemlayout){
            this.context = context;
            this.profile_item = profile_item;
            this.itemlayout = itemlayout;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(itemlayout, viewGroup,false);
            return new ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            Glide.with(MyApplication.getmContext()).load(profile_item.get(position).profile_item_img.toString()).into(holder.profile_item_image);
            // holder.item_state.setImageResource(R.drawable.item_condition_1);
            //1:대여가능,2:대여중,3:대여불가
            switch (profile_item.get(position).profile_item_state){
                case 1:
                    holder.profile_item_state.setImageResource(R.drawable.item_condition_2);
                    break;
                case 2:
                    holder.profile_item_state.setImageResource(R.drawable.item_condition_1);
                    break;
            }
            holder.profile_item_title.setText(profile_item.get(position).profile_item_title);
            article = profile_item.get(position).profile_item_article;
            holder.profile_item_price.setText(profile_item.get(position).profile_time_price);
            if(profile_item.get(position).profile_time_priceKind.equalsIgnoreCase("시간")){
                holder.profile_item_priceKind.setText(" /시간");
            }else if(profile_item.get(position).profile_time_priceKind.equalsIgnoreCase("일")){
                holder.profile_item_priceKind.setText(" /일");
            }else if(profile_item.get(position).profile_time_priceKind.equalsIgnoreCase("협의")){
                holder.profile_item_price.setVisibility(View.GONE);
                holder.profile_item_priceKind.setText("협의");
            }
            holder.profile_item_location.setText(profile_item.get(position).profile_time_location);

            //물품상세페이지로 이동!
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent itemPage = new Intent(MyApplication.getmContext(),ProfileItemPage.class);
                    itemPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);


                    itemPage.putExtra("item_check", profile_item.get(position).profile_item_usercheck);
                    itemPage.putExtra("item_photo", profile_item.get(position).profile_item_img.toString());
                    itemPage.putExtra("item_name", profile_item.get(position).profile_item_title);
                    itemPage.putExtra("item_price", profile_item.get(position).profile_time_price);
                    itemPage.putExtra("item_time", profile_item.get(position).profile_time_priceKind);
                    itemPage.putExtra("item_nickname", username);
                    itemPage.putExtra("item_category", profile_item.get(position).profile_item_category);
                    itemPage.putExtra("item_article", article);
                    itemPage.putExtra("item_state", profile_item.get(position).profile_item_state);
                    itemPage.putExtra("item_location", profile_item.get(position).profile_time_location);
                    itemPage.putExtra("item_userphoto", userimg);

                    ActivityCompat.startActivity(MyApplication.getmContext(), itemPage, null);
                }
            });
        }

        @Override
        public int getItemCount() {
            return profile_item.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            public ImageView profile_item_image;
            public ImageView profile_item_state;
            public TextView profile_item_title;
            public TextView profile_item_location;
            public TextView profile_item_price;
            public TextView profile_item_priceKind;
            public View mView;

            public ViewHolder(View itemView){
                super(itemView);
                mView = itemView;
                profile_item_image= (ImageView) itemView.findViewById(R.id.profile_item_image);
                profile_item_title = (TextView) itemView.findViewById(R.id.profile_item_title);
                profile_item_price = (TextView) itemView.findViewById(R.id.profile_item_price);
                profile_item_priceKind = (TextView) itemView.findViewById(R.id.profile_item_priceKind);
                profile_item_location = (TextView) itemView.findViewById(R.id.profile_item_location);
                profile_item_state = (ImageView)itemView.findViewById(R.id.profile_item_state);
            }

        }
    }

    public class AsyncProfileItem extends AsyncTask<ProfileItemObject, Integer,ArrayList<ProfileItemObject>> {
        ArrayList<ProfileItemObject> profileArrayList = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<ProfileItemObject> doInBackground(ProfileItemObject... Profileinfo) {
            boolean flag;
            Response response = null;
            final OkHttpClient toServer ;

            try {
                toServer = OkHttpInitSingtonManager.getOkHttpApplyCookieClient();

                RequestBody postBody = new FormBody.Builder()
                        .add("search_id",String.valueOf(user_id))
                        .build();

                final Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_NADOO_OTHERUSER_ITEM)
                        .post(postBody)
                        .build();

                response = toServer.newCall(request).execute();
                flag = response.isSuccessful();

                String returnedJSON;
                if(flag){
                    returnedJSON = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(returnedJSON);
                        profileArrayList = ProfileItem_ParseData.getJSONBloodRequestAllList(returnedJSON);

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
        protected void onPostExecute(ArrayList<ProfileItemObject> getresult) {
            //super.onPostExecute(s);

            if(getresult != null){
                recyclerView.setAdapter(new ProfileItemRecyclerAdapter(MyApplication.getmContext(), getresult,R.layout.profile_recyclerview_item));
            }
            else{
                Toast.makeText(MyApplication.getmContext(), "입력 중 문제 발생", Toast.LENGTH_SHORT).show();
            }
        }
    }


}

