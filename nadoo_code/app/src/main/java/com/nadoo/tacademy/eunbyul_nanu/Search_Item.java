package com.nadoo.tacademy.eunbyul_nanu;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Search_Item extends Fragment {

    View view;
    ImageButton item_fillter;
    String search;
    RecyclerView searchitems;

    FrameLayout item_default,item_result;
    public static Search_Item newInstance(int initValue){
        Search_Item search_ItemFrag = new Search_Item();

        return search_ItemFrag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_item,container,false);
        searchitems = (RecyclerView) view.findViewById(R.id.searchItems);
        searchitems.setLayoutManager(new GridLayoutManager(MyApplication.getmContext(),2));

        RecyclerView item_recentlist = (RecyclerView) view.findViewById(R.id.item_recentlist);
        item_recentlist.setLayoutManager(new LinearLayoutManager(MyApplication.getmContext()));
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(MyApplication.getmContext(),new LinearLayoutManager(MyApplication.getmContext()).getOrientation());
        item_recentlist.addItemDecoration(dividerItemDecoration);
        item_recentlist.setAdapter(new RecentItemRecyclerAdpater(MyApplication.getmContext()));


        item_default = (FrameLayout)view.findViewById(R.id.item_default);
        item_result = (FrameLayout)view.findViewById(R.id.item_result);
        final EditText search_edit = (EditText)view.findViewById(R.id.search_edit);

        item_fillter = (ImageButton)view.findViewById(R.id.item_fillter);
        item_fillter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent fillter = new Intent(MyApplication.getmContext(),Fillter.class);
                //startActivity(fillter);
                item_result.setVisibility(View.VISIBLE);
                    item_default.setVisibility(View.GONE);
                    search = search_edit.getText().toString();
                    AsyncSearchItemJSONList asyncSearchItemJSONList= new AsyncSearchItemJSONList();
                    asyncSearchItemJSONList.execute();
            }
        });


        search_edit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        search_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(actionId == EditorInfo.IME_ACTION_DONE)
                {

                    /*item_result.setVisibility(View.VISIBLE);
                    item_default.setVisibility(View.GONE);
                    search = search_edit.getText().toString();
                    AsyncSearchItemJSONList asyncSearchItemJSONList= new AsyncSearchItemJSONList();
                    asyncSearchItemJSONList.execute();
                    return true;*/
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public static class SearchItemRecyclerViewAdapter extends RecyclerView.Adapter<Search_Item.SearchItemRecyclerViewAdapter.ViewHolder>{


        ArrayList<ItemObject> items;
        Context context;
        public SearchItemRecyclerViewAdapter(Context context, ArrayList<ItemObject> objects){
            this.context =context;
            this.items = objects;
        }


        @Override
        public Search_Item.SearchItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview,parent, false);
            return new Search_Item.SearchItemRecyclerViewAdapter.ViewHolder(v);
        }
        public static class ViewHolder extends RecyclerView.ViewHolder {

            public final View mView;
            ImageView item_photo;
            ImageView item_state;
            TextView item_name;
            TextView item_price;
            TextView item_time;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                item_photo = (ImageView)view.findViewById(R.id.item_photo);
                item_state = (ImageView)view.findViewById(R.id.item_state);
                item_name = (TextView)view.findViewById(R.id.item_name);
                item_price = (TextView)view.findViewById(R.id.item_price);
                item_time = (TextView)view.findViewById(R.id.item_time);

            }
        }

        @Override
        public void onBindViewHolder(Search_Item.SearchItemRecyclerViewAdapter.ViewHolder holder, final int position) {

            Glide.with(MyApplication.getmContext()).load(items.get(position).item_photo.toString()).into(holder.item_photo);
            //1:대여가능,2:대여중,3:대여불가
            switch (items.get(position).item_state){
                case 1:
                    holder.item_state.setImageResource(R.drawable.item_condition_1);
                    break;
                case 2:
                    holder.item_state.setImageResource(R.drawable.item_condition_2);
                    break;
            }
            holder.item_name.setText(items.get(position).item_name);
            holder.item_price.setText(items.get(position).item_price+"원");
            if(items.get(position).item_time.equalsIgnoreCase("시간")){
                holder.item_time.setText(" /시간");
            }else if(items.get(position).item_time.equalsIgnoreCase("일")){
                holder.item_time.setText(" /일");
            }else if(items.get(position).item_time.equalsIgnoreCase("협의")){
                holder.item_price.setVisibility(View.GONE);
                holder.item_time.setText("협의");
            }


            //물품상세페이지로 이동!
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent itemPage = new Intent(MyApplication.getmContext(),ItemPage.class);
                    itemPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    itemPage.putExtra("item_id", items.get(position).item_id);
                    itemPage.putExtra("item_photo",items.get(position).item_photo.toString());
                    itemPage.putExtra("item1",items.get(position).item1);
                    itemPage.putExtra("item_name", items.get(position).item_name);
                    itemPage.putExtra("item_price", items.get(position).item_price);
                    itemPage.putExtra("item_time", items.get(position).item_time);
                    itemPage.putExtra("item_userid", items.get(position).item_userid);
                    itemPage.putExtra("item_nickname", items.get(position).item_nickname);
                    itemPage.putExtra("item_category", items.get(position).item_category);
                    itemPage.putExtra("item_article", items.get(position).item_article);
                    itemPage.putExtra("item_check", items.get(position).item_check);
                    itemPage.putExtra("item_location", items.get(position).item_location);
                    itemPage.putExtra("item_userphoto", items.get(position).item_userphoto);

                    ActivityCompat.startActivity(MyApplication.getmContext(), itemPage,null);
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }



    }
    ///////////////////////////////////////////////////////////////////////////////////////

    public class AsyncSearchItemJSONList extends AsyncTask<String, Integer, ArrayList<ItemObject>> {
        SearchItemRecyclerViewAdapter searchitemListAdapter;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<ItemObject> doInBackground(String... params) {
            return SearchItem_HttpAPIHelperHandler.SearchItemAllSelect(search);
        }

        @Override
        protected void onPostExecute(ArrayList<ItemObject> result) {
            super.onPostExecute(result);
            if(result != null && result.size() > 0){
                searchitemListAdapter =
                        new SearchItemRecyclerViewAdapter(MyApplication.getmContext(),result);
                searchitemListAdapter.notifyDataSetChanged();

                searchitems.setAdapter(searchitemListAdapter);

            }
        }
    }



}
