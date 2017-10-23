package com.nadoo.tacademy.eunbyul_nanu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nadoo.tacademy.eunbyul_nanu.Profile.Profile;

import java.util.ArrayList;

public class WantItemBoard extends Fragment {
    View view;
    RecyclerView wantItems;

    SharedPreferences getuserinfo;
    TextView user_school;
    static String user_name;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    public static WantItemBoard newInstance(int initValue){
        WantItemBoard WantItemBoardFrag = new WantItemBoard();
        return WantItemBoardFrag;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_want_item_board,container,false);

        wantItems = (RecyclerView) view.findViewById(R.id.wantItems);
        wantItems.setLayoutManager(new LinearLayoutManager(MyApplication.getmContext()));

        user_school = (TextView)view.findViewById(R.id.wantItem_school_name);
        getuserinfo = getActivity().getSharedPreferences("user_pref", Context.MODE_PRIVATE);
        String user_school_location = getuserinfo.getString("school", "");
        user_name = getuserinfo.getString("nickname","");
        user_school.setText(user_school_location);

        Log.e("school location",user_school_location);


        return view;
    }
    ////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void onResume() {
        super.onResume();
        AsyncWantItemJSONList asyncWantItemJSONList= new AsyncWantItemJSONList();
        asyncWantItemJSONList.execute();
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    /*
        RecyclerviewAdapter
*/
    public static class WantItemRecyclerViewAdapter extends RecyclerView.Adapter<WantItemRecyclerViewAdapter.ViewHolder>{
        ArrayList<WantItemObject> want_items;
        Context context;
        public WantItemRecyclerViewAdapter(Context context, ArrayList<WantItemObject> objects){
            this.context =context;
            this.want_items = objects;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.wantitem_cardview,parent,false);
            return new ViewHolder(v);
        }
        public void add(WantItemObject wantItemObject){
            want_items.add(wantItemObject);
        }
        public static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView wantItem_user;
            TextView wantItem_username;
            ImageView wantItem_category;
            TextView wantItem_title;
            TextView wantItem_story;
            LinearLayout wantItem_profile;
            ImageButton wantitem_chatting;

            public ViewHolder(View view) {
                super(view);
                wantItem_profile = (LinearLayout)view.findViewById(R.id.wantItem_profile);
                wantItem_user = (ImageView)view.findViewById(R.id.wantItem_user);
                wantItem_username = (TextView)view.findViewById(R.id.wantItem_username);
                wantItem_category = (ImageView)view.findViewById(R.id.wantItem_category);
                wantItem_title = (TextView)view.findViewById(R.id.wantItem_title);
                wantItem_story = (TextView)view.findViewById(R.id.wantItem_story);
                wantitem_chatting = (ImageButton)view.findViewById(R.id.wantitem_chatting);

            }
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            Glide.with(MyApplication.getmContext()).load(want_items.get(position).wantItem_user.toString()).into(holder.wantItem_user);
           holder.wantItem_username.setText(want_items.get(position).wantItem_username);
            String category = want_items.get(position).wantItem_category;
            switch (category){
                case "학교교재":
                    holder.wantItem_category.setImageResource(R.drawable.category_title_1);
                    break;
                case "일반서적":
                    holder.wantItem_category.setImageResource(R.drawable.category_title_2);
                    break;
                case "스포츠":
                    holder.wantItem_category.setImageResource(R.drawable.category_title_3);
                    break;
                case "생활용품":
                    holder.wantItem_category.setImageResource(R.drawable.category_title_4);
                    break;
                case "여행용품":
                    holder.wantItem_category.setImageResource(R.drawable.category_title_5);
                    break;
                case "모바일":
                    holder.wantItem_category.setImageResource(R.drawable.category_title_6);
                    break;
                case "전자기기":
                    holder.wantItem_category.setImageResource(R.drawable.category_title_7);
                    break;
                case "패션잡화":
                    holder.wantItem_category.setImageResource(R.drawable.category_title_8);
                    break;
                case "뷰티":
                    holder.wantItem_category.setImageResource(R.drawable.category_title_9);
                    break;
                case "악기":
                    holder.wantItem_category.setImageResource(R.drawable.category_title_10);
                    break;
                case "부동산":
                    holder.wantItem_category.setImageResource(R.drawable.category_title_11);
                    break;
                case "재능":
                    holder.wantItem_category.setImageResource(R.drawable.category_title_12);
                    break;
                case "애완용품":
                    holder.wantItem_category.setImageResource(R.drawable.category_title_13);
                    break;
                case "기타":
                    holder.wantItem_category.setImageResource(R.drawable.category_title_14);
                    break;
            }
           holder.wantItem_title.setText(want_items.get(position).wantItem_title);
           holder.wantItem_story.setText(want_items.get(position).wantItem_story);
            holder.wantItem_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        int user_id = want_items.get(position).wantItem_userid;
                        Intent profile = new Intent(MyApplication.getmContext(), Profile.class);
                        profile.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        profile.putExtra("user_img", want_items.get(position).wantItem_user);
                        profile.putExtra("user_name", holder.wantItem_username.getText().toString());
                        profile.putExtra("user_id", user_id);
                        ActivityCompat.startActivity(MyApplication.getmContext(), profile, null);

                }
            });

            //로그인한 아이디랑 글의 닉네임이랑 같으면 채팅창 안보이게
            if(user_name.equalsIgnoreCase(want_items.get(position).wantItem_username)){
                holder.wantitem_chatting.setVisibility(View.GONE);
            }

            holder.wantitem_chatting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String chatname="";
                    if (user_name.toString().equals("") || want_items.get(position).wantItem_username.toString().equals(""))
                        return;

                    if(user_name.toString().compareTo(want_items.get(position).wantItem_username.toString())<0){
                        chatname = user_name.toString() + want_items.get(position).wantItem_username.toString();
                    }else if(user_name.toString().compareTo(want_items.get(position).wantItem_username.toString())>0){
                        chatname = want_items.get(position).wantItem_username.toString()+user_name.toString();
                    }
                    Intent chat = new Intent(MyApplication.getmContext(), ChatActivity.class);
                    chat.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    chat.putExtra("chatName", chatname);
                    chat.putExtra("userName", user_name.toString());
                    ActivityCompat.startActivity(MyApplication.getmContext(), chat, null);
                }
            });

            final String story = holder.wantItem_story.getText().toString();
            if( story.length() > 50){
                holder.wantItem_story.setText(story.substring(0,50)+" ...더보기");
                String story2 = holder.wantItem_story.getText().toString();
                holder.wantItem_story.setMovementMethod(LinkMovementMethod.getInstance());
                holder.wantItem_story.setText(story2, TextView.BufferType.SPANNABLE);
                Spannable span = (Spannable)holder.wantItem_story.getText();
                int start = story2.indexOf("더보기");
                int end = start + "더보기".length();
                span.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        holder.wantItem_story.setMaxLines(100);
                        holder.wantItem_story.setText(story);
                    }
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        ds.linkColor = 0xff000000;
                        ds.setUnderlineText(true);
                        super.updateDrawState(ds);
                    }
                },start,end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            }else{
                holder.wantItem_story.setText(story);
            }
        }

        @Override
        public int getItemCount() {
           return want_items.size();
        }
    }

    public class AsyncWantItemJSONList extends AsyncTask<String, Integer, ArrayList<WantItemObject>>{
        WantItemRecyclerViewAdapter wantListAdapter;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected ArrayList<WantItemObject> doInBackground(String... params) {
            return WantItem_HttpAPIHelperHandler.wantItemAllSelect();
        }

        @Override
        protected void onPostExecute(ArrayList<WantItemObject> result) {
            super.onPostExecute(result);
            if(result != null && result.size() > 0){
                wantListAdapter =
                      new WantItemRecyclerViewAdapter(MyApplication.getmContext(),result);
                wantListAdapter.notifyDataSetChanged();
                wantItems.setAdapter(wantListAdapter);

            }
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////

}


