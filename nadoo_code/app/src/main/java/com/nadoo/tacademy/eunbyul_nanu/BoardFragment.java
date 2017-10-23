package com.nadoo.tacademy.eunbyul_nanu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

/**
 * Created by Kyoonho on 2017-05-29.
 */

public class BoardFragment extends Activity{

    RecyclerView rv;
    ImageView gotowritepage;
    static int CHECK_SUM = 0;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_activity_board);

        /*게시판 글 작성*/
        gotowritepage = (ImageView)findViewById(R.id.goto_writepage);
        gotowritepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyApplication.getmContext(), NewBoardRegister.class);
                startActivity(intent);
            }
        });

        rv = (RecyclerView)findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(MyApplication.getmContext()));
        AsyncBoardJSONList asyncBoardJSONList = new AsyncBoardJSONList();
        asyncBoardJSONList.execute();

        final ImageButton checkbox_my_feed = (ImageButton)findViewById(R.id.checkbox_my_feed);
        checkbox_my_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CHECK_SUM == 0){
                    checkbox_my_feed.setSelected(true);
                    CHECK_SUM = 1;
                }else{
                    checkbox_my_feed.setSelected(false);
                    CHECK_SUM = 0;
                }
            }
        });
    }

    public static class BoardRecyclerViewAdapter extends RecyclerView.Adapter<BoardRecyclerViewAdapter.ViewHolder>{

        ArrayList<BoardItem> arrayList;
        Context mContext;
        public BoardRecyclerViewAdapter(Context context, ArrayList<BoardItem> objects){
            this.mContext = context;
            this.arrayList = objects;

        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView image;
            TextView title,reply_num,recommand_num;
            TextView contents,user_id;
            ImageButton replybtn,likebtn;
            public ViewHolder(View itemView) {
                super(itemView);
                image = (ImageView) itemView.findViewById(R.id.board_profile_img);
                title = (TextView) itemView.findViewById(R.id.title);
                contents = (TextView) itemView.findViewById(R.id.contents);
                reply_num = (TextView)itemView.findViewById(R.id.reply_num);
                recommand_num = (TextView)itemView.findViewById(R.id.recommand_num);
                user_id = (TextView)itemView.findViewById(R.id.board_nickname);
                replybtn = (ImageButton) itemView.findViewById(R.id.board_input_reply);
                likebtn = (ImageButton) itemView.findViewById(R.id.board_like);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_item,
                    parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            String image_URL = arrayList.get(position).image;
            final String board_title = arrayList.get(position).title;
            final String board_contents = arrayList.get(position).contents;
            final String boardID = arrayList.get(position).board_num;
            final String reply_number = arrayList.get(position).reply_num;
            final String like_number = arrayList.get(position).recommand_num;
            holder.title.setText(arrayList.get(position).title);

            Glide.with(MyApplication.getmContext()).load(image_URL).diskCacheStrategy(DiskCacheStrategy.ALL).animate(android.R.anim.slide_in_left).into(holder.image);

            holder.contents.setText(arrayList.get(position).contents);
            holder.reply_num.setText(reply_number);
            holder.recommand_num.setText(like_number);
            holder.user_id.setText(arrayList.get(position).nickname);
            holder.replybtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   /* Toast.makeText(mContext,"게시물 상세보기 및 댓글작성",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MyApplication.getmContext(), BoardReply.class);
                    intent.putExtra("board_id",boardID);
                    intent.putExtra("board_title",board_title);
                    intent.putExtra("board_contents",board_contents);
                    intent.putExtra("reply_num",reply_number);
                    intent.putExtra("recommand_num",like_number);

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);*/
                    Toast.makeText(mContext,"준비 중입니다.",Toast.LENGTH_SHORT).show();

                }
            });
            holder.likebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(CHECK_SUM == 0){
                        holder.likebtn.setSelected(true);
                        CHECK_SUM = 1;
                    }else{
                        holder.likebtn.setSelected(false);
                        CHECK_SUM = 0;
                    }
                  Toast.makeText(mContext,"추천 수 + 1",Toast.LENGTH_SHORT).show();
                }
            });
        }
        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }
    public class AsyncBoardJSONList extends AsyncTask<String, Integer, ArrayList<BoardItem>>{
        BoardRecyclerViewAdapter boardRecyclerViewAdapter;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }
        @Override
        protected ArrayList<BoardItem> doInBackground(String... params){
            return Board_HttpAPIHelperHandler.BoardAllSelect();
        }
        @Override
        protected void onPostExecute(ArrayList<BoardItem> result){
            super.onPostExecute(result);
            boardRecyclerViewAdapter = new BoardRecyclerViewAdapter(MyApplication.getmContext(),result);
            boardRecyclerViewAdapter.notifyDataSetChanged();
            rv.setAdapter(boardRecyclerViewAdapter);
        }
    }
}
