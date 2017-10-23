package com.nadoo.tacademy.eunbyul_nanu;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nadoo.tacademy.eunbyul_nanu.Profile.Profile;

import java.util.ArrayList;

public class Search_WantItem extends Fragment {

    private static final int CATEGORY = 0;

    View view;
    FrameLayout wantitem_default,wantitem_result;
    ImageButton fillter_want;
    RecyclerView wantItem;
    String search,category;

    public static Search_WantItem newInstance(int initValue){
        Search_WantItem search_WantItemFrag = new Search_WantItem();
        return search_WantItemFrag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_wantitem,container,false);
        wantItem = (RecyclerView) view.findViewById(R.id.searchWantItems);
        wantItem.setLayoutManager(new LinearLayoutManager(MyApplication.getmContext()));;

        RecyclerView want_recentlist = (RecyclerView) view.findViewById(R.id.want_recentlist);
        want_recentlist.setLayoutManager(new LinearLayoutManager(MyApplication.getmContext()));
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(MyApplication.getmContext(),new LinearLayoutManager(MyApplication.getmContext()).getOrientation());
        want_recentlist.addItemDecoration(dividerItemDecoration);
        want_recentlist.setAdapter(new RecentWantRecyclerAdpater(MyApplication.getmContext()));

        wantitem_default = (FrameLayout)view.findViewById(R.id.wantitem_default);
        wantitem_result = (FrameLayout)view.findViewById(R.id.wantitem_result);
        final EditText wantsearch_edit = (EditText)view.findViewById(R.id.wantsearch_edit);

        fillter_want = (ImageButton)view.findViewById(R.id.fillter_want);
        fillter_want.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent fillter = new Intent(MyApplication.getmContext(),Fillter_want.class);
                //startActivity(fillter);
               /* Toast.makeText(MyApplication.getmContext(),"필터검색 준비 중입니다.",Toast.LENGTH_SHORT).show();*/
                wantitem_result.setVisibility(View.VISIBLE);
                wantitem_default.setVisibility(View.GONE);
                search = wantsearch_edit.getText().toString();
                AsyncSearchWantItemJSONList asyncSearchWantItemJSONList= new  AsyncSearchWantItemJSONList();
                asyncSearchWantItemJSONList.execute();
            }
        });


        wantsearch_edit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        wantsearch_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(actionId == EditorInfo.IME_ACTION_DONE)
                {
                  /*  wantitem_result.setVisibility(View.VISIBLE);
                    wantitem_default.setVisibility(View.GONE);
                    search = wantsearch_edit.getText().toString();
                    AsyncSearchWantItemJSONList asyncSearchWantItemJSONList= new  AsyncSearchWantItemJSONList();
                    asyncSearchWantItemJSONList.execute();*/
                    return true;
                }
                return false;
            }
        });

        return view;
    }


    public static class SearchWantItemRecyclerViewAdapter extends RecyclerView.Adapter<Search_WantItem.SearchWantItemRecyclerViewAdapter.ViewHolder>{

        ArrayList<WantItemObject> want_items;
        Context context;
        public SearchWantItemRecyclerViewAdapter(Context context, ArrayList<WantItemObject> objects){
            this.context =context;
            this.want_items = objects;
        }

        @Override
        public Search_WantItem.SearchWantItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.wantitem_cardview,parent,false);
            return new Search_WantItem.SearchWantItemRecyclerViewAdapter.ViewHolder(v);
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView wantItem_user;
            TextView wantItem_username;
            ImageView wantItem_category;
            TextView wantItem_title;
            TextView wantItem_story;
            LinearLayout wantItem_profile;

            //ImageButton textbtn;
            public ViewHolder(View view) {
                super(view);

                wantItem_profile = (LinearLayout)view.findViewById(R.id.wantItem_profile);
                wantItem_user = (ImageView)view.findViewById(R.id.wantItem_user);
                wantItem_username = (TextView)view.findViewById(R.id.wantItem_username);
                wantItem_category = (ImageView)view.findViewById(R.id.wantItem_category);
                wantItem_title = (TextView)view.findViewById(R.id.wantItem_title);
                wantItem_story = (TextView)view.findViewById(R.id.wantItem_story);
               // textbtn = (ImageButton)view.findViewById(R.id.textbtn);
            }
        }

        @Override
        public void onBindViewHolder(final Search_WantItem.SearchWantItemRecyclerViewAdapter.ViewHolder holder, int position) {

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

                    Intent profile = new Intent(MyApplication.getmContext(),Profile.class);

                    Toast.makeText(MyApplication.getmContext(),String.valueOf(holder.wantItem_story.getLineCount()),Toast.LENGTH_LONG).show();
                    profile.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    profile.putExtra("user_name",holder.wantItem_username.getText().toString());
                    ActivityCompat.startActivity(MyApplication.getmContext(), profile,null);
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

    public class AsyncSearchWantItemJSONList extends AsyncTask<String, Integer, ArrayList<WantItemObject>> {
        SearchWantItemRecyclerViewAdapter searchwantListAdapter;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected ArrayList<WantItemObject> doInBackground(String... params) {
            return SearchWantItem_HttpAPIHelperHandler.searchWantItemAllSelect(search);
        }

        @Override
        protected void onPostExecute(ArrayList<WantItemObject> result) {
            super.onPostExecute(result);
            if(result != null && result.size() > 0){
                searchwantListAdapter =
                        new SearchWantItemRecyclerViewAdapter(MyApplication.getmContext(),result);
                searchwantListAdapter.notifyDataSetChanged();
                wantItem.setAdapter(searchwantListAdapter);

            }
        }
    }


}