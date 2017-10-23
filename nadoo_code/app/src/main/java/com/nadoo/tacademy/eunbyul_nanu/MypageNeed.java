package com.nadoo.tacademy.eunbyul_nanu;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.util.Log.e;

public class MypageNeed extends AppCompatActivity {

    List<MypageNeedObject> list = new ArrayList<MypageNeedObject>();
    MypageNeedItemRecyclerAdapter mypageNeedAdapter;
    String editLabel;
    RecyclerView recyclerView;
    String location,category;
    int need_id;

    int currentPosition = -1;

    DeleteBoardDialog deleteBoardDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_need);

        recyclerView = (RecyclerView)findViewById(R.id.mypage_need_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MyApplication.getmContext());
        recyclerView.setLayoutManager(layoutManager);
        AsyncMypageNeddJSONList asyncMypageNeddJSONList= new AsyncMypageNeddJSONList();
        asyncMypageNeddJSONList.execute();

    }

    public class MypageNeedItemRecyclerAdapter extends RecyclerView.Adapter<MypageNeedItemRecyclerAdapter.ViewHolder> {

        Context context;
        private int itemlayout;
        ArrayList<MypageNeedObject> mypage_item;
        MypageNeedObject item;

       public MypageNeedItemRecyclerAdapter(Context context, ArrayList<MypageNeedObject> mypage_item, int itemlayout){
            this.context =context;
            this.mypage_item = mypage_item;
            this.itemlayout = itemlayout;
        }

        public boolean deletePosition(){
            if(mypage_item.remove(currentPosition) != null){
                return true;
            }else{
                return false;
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(itemlayout, viewGroup, false);
            return new ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final MypageNeedObject item = mypage_item.get(position);
            String datetime="";

            holder.mypage_need_item_title.setText(item.mypage_need_title);
            //시간 추출
            int index = item.mypage_need_time.indexOf("T");
            String time = item.mypage_need_time.substring(0,index);
            //
            holder.mypage_need_item_time.setText(time);
            holder.mypage_need_item_contents.setText(item.mypage_need_contents);

           category = item.mypage_need_category;
            location = item.mypage_need_location;

            final String story = holder.mypage_need_item_contents.getText().toString();
            if( story.length() > 50){
                holder.mypage_need_item_contents.setText(story.substring(0,50)+" ...더보기");
                String story2 = holder.mypage_need_item_contents.getText().toString();
                holder.mypage_need_item_contents.setMovementMethod(LinkMovementMethod.getInstance());
                holder.mypage_need_item_contents.setText(story2, TextView.BufferType.SPANNABLE);
                Spannable span = (Spannable)holder.mypage_need_item_contents.getText();
                int start = story2.indexOf("더보기");
                int end = start + "더보기".length();
                span.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        holder.mypage_need_item_contents.setMaxLines(100);
                        holder.mypage_need_item_contents.setText(story);
                    }
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        ds.linkColor = 0xff000000;
                        ds.setUnderlineText(true);
                        super.updateDrawState(ds);
                    }
                }, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            } else {
                holder.mypage_need_item_contents.setText(story);
            }


            holder.mypage_need_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    need_id = item.mypage_need_id;
                    Intent i = new Intent(MypageNeed.this, ModifyWrite.class);
                    i.putExtra("itemname", holder.mypage_need_item_title.getText().toString());
                    i.putExtra("category", category);
                    i.putExtra("content", holder.mypage_need_item_contents.getText().toString());
                    i.putExtra("location",location);
                    i.putExtra("need_id",need_id);
                    startActivity(i);
                }
            });
            holder.mypage_need_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPosition = position;
                    need_id = item.mypage_need_id;
                    deleteBoardDialog = new DeleteBoardDialog(MypageNeed.this, leftListener,rightListener);
                    deleteBoardDialog.show();

                }
            });
        }

        private View.OnClickListener leftListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyApplication.getmContext(),"취소하셨습니다.",Toast.LENGTH_SHORT).show();
                deleteBoardDialog.dismiss();
            }
        };
        private View.OnClickListener rightListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncMypageNeedDelete asyncMypageNeedDelete = new AsyncMypageNeedDelete();
                asyncMypageNeedDelete.execute();
                deleteBoardDialog.dismiss();
            }
        };

        @Override
        public int getItemCount() {
           return mypage_item.size();

        }

        public  class ViewHolder extends RecyclerView.ViewHolder{


            public View mview;
            public TextView mypage_need_item_title;
            public TextView mypage_need_item_time;
            public TextView mypage_need_item_contents;
            public ImageButton mypage_need_edit;
            public ImageButton mypage_need_delete;

            public ViewHolder(View itemView){
                super(itemView);

                mview = itemView;
                mypage_need_item_title = (TextView) itemView.findViewById(R.id.mypage_need_title);
                mypage_need_item_time = (TextView) itemView.findViewById(R.id.mypage_need_date);
                mypage_need_item_contents = (TextView) itemView.findViewById(R.id.mypage_need_contents);
                mypage_need_edit = (ImageButton)itemView.findViewById(R.id.mypage_need_edit);
                mypage_need_delete = (ImageButton)itemView.findViewById(R.id.mypage_need_delete);
            }

        }
    }

    //mypage 요청글 불러오는 asyncTask
    public class AsyncMypageNeddJSONList extends AsyncTask<String, Integer, ArrayList<MypageNeedObject>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected ArrayList<MypageNeedObject> doInBackground(String... params) {
            return MypageNeed_HttpAPIHelperHandler.MypageNeedAllSelect();
        }
        @Override
        protected void onPostExecute(ArrayList<MypageNeedObject> result) {
            super.onPostExecute(result);
            if(result != null && result.size() > 0){
                mypageNeedAdapter =
                        new MypageNeedItemRecyclerAdapter(MyApplication.getmContext(), result, R.layout.mypage_need_recyclerview_item);
                mypageNeedAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(mypageNeedAdapter);

            }
        }
    }

    //요청글 삭제하는 asyncTask
    public class AsyncMypageNeedDelete extends AsyncTask<MypageNeedObject, Integer,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(MypageNeedObject... mypageItmeInfo) {
            boolean flag;
            String wantItemInsertResult = "";
            Response response = null;
            final OkHttpClient toServer ;

            try {
                toServer = OkHttpInitSingtonManager.getOkHttpApplyCookieClient();


                final Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_NADO_WANTITEM_LIST +"/"+need_id)
                        .delete()
                        .build();

                response = toServer.newCall(request).execute();
                flag = response.isSuccessful();
                Log.e("결과", String.valueOf(flag));
                String returnedJSON;
                if(flag){
                    returnedJSON = response.body().string();
                    Log.e("resultJSON",returnedJSON);
                    try {
                        JSONObject jsonObject = new JSONObject(returnedJSON);
                        wantItemInsertResult = jsonObject.optString("msg");

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

            return wantItemInsertResult;
        }

        @Override
        protected void onPostExecute(String result) {
            //super.onPostExecute(s);

            if(result != null){
                if(mypageNeedAdapter.deletePosition()){
                    mypageNeedAdapter.notifyDataSetChanged();
                }
                if (result.equalsIgnoreCase("delete success")){
                    Toast.makeText(MyApplication.getmContext(),"삭제되었습니다",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MyApplication.getmContext(),"실패",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(MyApplication.getmContext(), "입력 중 문제 발생", Toast.LENGTH_SHORT).show();
            }
            Log.e("result",result);
        }
    }
}

