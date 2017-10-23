package com.nadoo.tacademy.eunbyul_nanu;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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
import com.nadoo.tacademy.eunbyul_nanu.Profile.Profile;

import java.util.ArrayList;
import java.util.List;

public class MypageFriend extends AppCompatActivity implements View.OnClickListener{

    List<MyfriendObject> list = new ArrayList<>();
    ImageButton plus;
    RecyclerView recyclerView;

    MyfriendObject myfriendObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_friend);

        if(myfriendObject == null){
            myfriendObject = new MyfriendObject();
        }

        recyclerView = (RecyclerView)findViewById(R.id.mypage_friend_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MyApplication.getmContext());
        recyclerView.setLayoutManager(layoutManager);

       AsyncFriendJSONList asyncFriendJSONList = new AsyncFriendJSONList();
        asyncFriendJSONList.execute();


        plus = (ImageButton) findViewById(R.id.mypage_friend_plus);
        plus.setOnClickListener(this);


    }
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, AddFriends.class);
        startActivity(intent);
    }

    public class MypageFriendRecyclerAdapter extends RecyclerView.Adapter<MypageFriendRecyclerAdapter.ViewHolder> {

        Context context;
        ArrayList<MyfriendObject> friend_list;
        private int itemlayout;
        public MypageFriendRecyclerAdapter(Context context, ArrayList<MyfriendObject> friend_list, int itemlayout){
            this.context = context;
            this.friend_list=friend_list;
            this.itemlayout = itemlayout;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(itemlayout, viewGroup,false);
            return new MypageFriendRecyclerAdapter.ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(MypageFriendRecyclerAdapter.ViewHolder holder, final int position) {
            final MyfriendObject friend = friend_list.get(position);
            Glide.with(MyApplication.getmContext()).load(friend.mypage_friend_img).into(holder.mypage_friend_image);
            holder.mypage_friend_nickname.setText(friend.mypage_friend_nickname);
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int user_id = friend.mypage_friend_user_key;
                    Intent profile = new Intent(MyApplication.getmContext(), Profile.class);
                    profile.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    profile.putExtra("user_img", friend.mypage_friend_img);
                    profile.putExtra("user_name", friend.mypage_friend_nickname);
                    profile.putExtra("user_id", user_id);
                    ActivityCompat.startActivity(MyApplication.getmContext(), profile, null);
                }
            });

        }

        @Override
        public int getItemCount() {
            return friend_list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            public ImageView mypage_friend_image;
            public TextView mypage_friend_nickname;
            public View view;

            public ViewHolder(View itemView){
                super(itemView);
                view = itemView;
                mypage_friend_image= (ImageView) itemView.findViewById(R.id.friend_image);
                mypage_friend_nickname = (TextView) itemView.findViewById(R.id.friend_name);

            }

        }
    }

    public class AsyncFriendJSONList extends AsyncTask<String, Integer, ArrayList<MyfriendObject>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected ArrayList<MyfriendObject> doInBackground(String... params) {
            Log.e("doInBackground","doInBackground");
            return MypageFriend_HttpAPIHelperHandler.FriendAllSelect();
        }

        @Override
        protected void onPostExecute(ArrayList<MyfriendObject> result) {
            super.onPostExecute(result);
            if(result != null && result.size() > 0){
                recyclerView.setAdapter(new MypageFriendRecyclerAdapter(MyApplication.getmContext(), result,R.layout.mypage_friend_recyclerview_item));
            }
        }
    }


}
