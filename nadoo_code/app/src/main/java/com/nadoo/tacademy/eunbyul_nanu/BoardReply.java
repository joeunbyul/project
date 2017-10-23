package com.nadoo.tacademy.eunbyul_nanu;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

/**
 * Created by Kyoonho on 2017-05-29.
 */

public class BoardReply extends Activity {
    int CHECK_SUM = 0;
    int board_id;
    private TextView board_title,board_contents,board_reply_num,board_like_num,count_reply;
    private ImageButton secret_mode,deleteboard;
    private RecyclerView boarddetail_rv;
    AlertDialog dialog;
    DeleteBoardDialog deleteBoardDialog;
    @Override
    public void  onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reply_activity);

        /*게시글 상세보기 중, '내 게시글만 보기' 버튼 클릭*/
        secret_mode = (ImageButton)findViewById(R.id.detail_checkbox_my_feed);
        secret_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CHECK_SUM == 0){
                    secret_mode.setSelected(true);
                    CHECK_SUM = 1;
                }else{
                    secret_mode.setSelected(false);
                    CHECK_SUM = 0;
                }
            }
        });


        Log.e("요청하는 board_id",String.valueOf(board_id));
        /*게시판 상세보기 중 메인 글*/
        board_title = (TextView)findViewById(R.id.detail_title);
        board_contents = (TextView)findViewById(R.id.detail_contents);
        board_reply_num = (TextView)findViewById(R.id.board_detail_reply_num);
        board_like_num = (TextView)findViewById(R.id.board_detail_recommand_num);
        count_reply = (TextView)findViewById(R.id.board_reply_num); //댓글위에 표시되는 댓글 수

        deleteboard = (ImageButton)findViewById(R.id.board_delete);


        Intent intent = getIntent();
        String board_id = intent.getStringExtra("board_id");
        String title = intent.getStringExtra("board_title");
        String contents = intent.getStringExtra("board_contents");
        String reply_num = intent.getStringExtra("reply_num");
        String recommand_num = intent.getStringExtra("recommand_num");
        board_title.setText(title);
        board_contents.setText(contents);
        board_reply_num.setText(reply_num);
        board_like_num.setText(recommand_num);
        count_reply.setText(reply_num);

        /*댓글 보기*/
        boarddetail_rv = (RecyclerView)findViewById(R.id.reply_recyclerview);
        boarddetail_rv.setLayoutManager(new LinearLayoutManager(MyApplication.getmContext()));
        AsyncBoardDetail asyncBoardDetail = new AsyncBoardDetail();
        asyncBoardDetail.execute(board_id);

    }
    public void onClickDialogView(View v){
        switch (v.getId()){
            case R.id.board_delete :
                deleteBoardDialog = new DeleteBoardDialog(BoardReply.this, leftListener,rightListener);
                deleteBoardDialog.show();
                break;
        }
    }
    private View.OnClickListener leftListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(MyApplication.getmContext(),"취소버튼 클릭",Toast.LENGTH_SHORT).show();
            deleteBoardDialog.dismiss();
        }
    };
    private View.OnClickListener rightListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(MyApplication.getmContext(),"삭제버튼 클릭",Toast.LENGTH_SHORT).show();
            deleteBoardDialog.dismiss();
        }
    };
    public static class BoardDetailRecyclerViewAdapter extends RecyclerView.Adapter<BoardDetailRecyclerViewAdapter.ViewHolder>{

        ArrayList<BoardItem> reply_arrayList;
        Context mContext;
        public BoardDetailRecyclerViewAdapter(Context context, ArrayList<BoardItem> objects){
            this.mContext = context;
            this.reply_arrayList = objects;
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView reply_profile;
            TextView reply_contents,reply_nickname;
            public ViewHolder(View itemView) {
                super(itemView);
                reply_profile = (ImageView) itemView.findViewById(R.id.reply_image);
                reply_contents = (TextView) itemView.findViewById(R.id.reply_contents);
                reply_nickname = (TextView)itemView.findViewById(R.id.reply_nickname);
            }
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_detail_item,
                    parent, false);
            return new BoardDetailRecyclerViewAdapter.ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(final BoardDetailRecyclerViewAdapter.ViewHolder holder, final int position) {
            String reply_profile_img = reply_arrayList.get(position).reply_img;
            Glide.with(MyApplication.getmContext()).load(reply_profile_img).diskCacheStrategy(DiskCacheStrategy.ALL).animate(android.R.anim.slide_in_left).into(holder.reply_profile);
            holder.reply_contents.setText(reply_arrayList.get(position).reply_contents);
            holder.reply_nickname.setText(reply_arrayList.get(position).reply_nickname);
        }
        @Override
        public int getItemCount() {
            return reply_arrayList.size();
        }
    }

    public class AsyncBoardDetail extends AsyncTask<String, Integer, ArrayList<BoardItem>>{
        BoardDetailRecyclerViewAdapter boardDetailRecyclerViewAdapter;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }
        @Override
        protected ArrayList<BoardItem> doInBackground(String... params){
            return BoardDetail_HttpAPIHelperHandler.BoardDetailSelect(params[0]);
        }
        @Override
        protected void onPostExecute(ArrayList<BoardItem> result){
            super.onPostExecute(result);

            boardDetailRecyclerViewAdapter = new BoardDetailRecyclerViewAdapter(MyApplication.getmContext(),result);
            boardDetailRecyclerViewAdapter.notifyDataSetChanged();
            boarddetail_rv.setAdapter(boardDetailRecyclerViewAdapter);

        }
    }

    Handler mHandler = new Handler(Looper.getMainLooper());
    public void onAlertDialog(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setMessage("게시글을 삭제하시겠어요?");
        builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(BoardReply.this, "No click", Toast.LENGTH_SHORT).show();

            }
        });

        builder.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Toast.makeText(BoardReply.this, "No click", Toast.LENGTH_SHORT).show();
            }

        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {

            }
        });

        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        }, 2000);
    }
}
