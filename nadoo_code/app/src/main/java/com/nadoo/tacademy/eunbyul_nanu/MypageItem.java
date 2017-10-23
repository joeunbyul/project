package com.nadoo.tacademy.eunbyul_nanu;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

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

public class MypageItem extends AppCompatActivity {

    BottomSheetBehavior mBottomSheetBehavior;
    NestedScrollView mNestedScrollView;

    RecyclerView recyclerView;
    MypageItemRecyclerAdapter mypageadapter;

    int state;
    ImageButton state1, state2, state3;

    private  MypageItemObject mypageItemObject;

    ImageButton mypage_state;

    /*삭제 다이얼로그*/
    AlertDialog dialog;
    DeleteBoardDialog deleteBoardDialog;

    int item_id;

    int currentPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_item);

        if(mypageItemObject == null){
            mypageItemObject = new MypageItemObject();
        }
        mypage_state = (ImageButton) findViewById(R.id.mypage_item_state);

        mNestedScrollView = (NestedScrollView) findViewById(R.id.activity_coordinate_layout_demo_nsv);
        mBottomSheetBehavior = BottomSheetBehavior.from(mNestedScrollView);

        state1 = (ImageButton) findViewById(R.id.state1);
        state2 = (ImageButton) findViewById(R.id.state2);
        state3 = (ImageButton) findViewById(R.id.state3);

        recyclerView = (RecyclerView) findViewById(R.id.mypage_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MyApplication.getmContext());
        recyclerView.setLayoutManager(layoutManager);
        AsyncMypageItemJSONList asyncMypageItemJSONList = new AsyncMypageItemJSONList();
        asyncMypageItemJSONList.execute();

    }

    public class MypageItemRecyclerAdapter extends RecyclerView.Adapter<MypageItemRecyclerAdapter.ViewHolder> {

        Context context;
        ArrayList<MypageItemObject> mypage_item;
        private int itemlayout;



        public MypageItemRecyclerAdapter(Context context, ArrayList<MypageItemObject> mypage_item, int itemlayout) {
            this.context = context;
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
        public MypageItemRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(itemlayout, viewGroup, false);
            return new MypageItemRecyclerAdapter.ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(final MypageItemRecyclerAdapter.ViewHolder holder, final int position) {
            final MypageItemObject item = mypage_item.get(position);
            Glide.with(MyApplication.getmContext()).load(item.mypage_item_img).into(holder.mypage_item_image);
            holder.mypage_item_title.setText(item.mypage_item_title);
            holder.mypage_item_price.setText(item.mypage_item_price+"원");
            state= Integer.parseInt(item.mypage_item_state);
            switch (item.mypage_item_state){
                case "1":
                    holder.item_state.setBackgroundResource(R.drawable.item_condition_2);
                    state=1;
                    break;
                case "2":
                    holder.item_state.setBackgroundResource(R.drawable.item_condition_1);
                    state=2;
                    break;
                case "3":
                    holder.item_state.setBackgroundResource(R.drawable.item_condition_3);
                    state=3;
                    break;
            }
            if(item.mypage_item_priceKind.equalsIgnoreCase("시간")){
                holder.mypage_item_priceKind.setText(" /시간");
            }else if(item.mypage_item_priceKind.equalsIgnoreCase("일")){
                holder.mypage_item_priceKind.setText(" /일");
            }else if(item.mypage_item_priceKind.equalsIgnoreCase("협의")){
                holder.mypage_item_price.setVisibility(View.GONE);
                holder.mypage_item_priceKind.setText("협의");
            }
            holder.mypage_item_location.setText(item.mypage_item_location);



            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent itemPage = new Intent(MyApplication.getmContext(),MypageItemPage.class);
                    itemPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    item_id = item.mypage_item_need_id;
                    itemPage.putExtra("item_photo", item.mypage_item_img);
                    itemPage.putExtra("item1", item.item1);
                    itemPage.putExtra("item2",item.item2);
                    itemPage.putExtra("item3", item.item3);
                    itemPage.putExtra("item_name", item.mypage_item_title);
                    itemPage.putExtra("item_nickname", item.mypage_item_nickname);
                    itemPage.putExtra("item_price",item.mypage_item_price);
                    itemPage.putExtra("item_time",item.mypage_item_priceKind);
                    itemPage.putExtra("item_category", item.mypage_item_category);
                    itemPage.putExtra("item_article", item.mypage_item_article);
                    itemPage.putExtra("item_check", item.mypage_item_check);
                    itemPage.putExtra("item_location", item.mypage_item_location);
                    itemPage.putExtra("item_userphoto", item.mypage_item_userphoto);
                    itemPage.putExtra("item_id",item.mypage_item_need_id);
                    itemPage.putExtra("item_id",item.mypage_item_need_id);
                    itemPage.putExtra("item_state",state);
                    ActivityCompat.startActivity(MyApplication.getmContext(), itemPage,null);
                }
            });


            holder.mypage_item_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent itemmodify = new Intent(MypageItem.this,ModifyItem.class);
                    item_id = item.mypage_item_need_id;
                    itemmodify.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    itemmodify.putExtra("item_name",item.mypage_item_title);
                    itemmodify.putExtra("item1", item.item1);
                    itemmodify.putExtra("item2", item.item2);
                    itemmodify.putExtra("item3", item.item3);
                    itemmodify.putExtra("item_price",item.mypage_item_price);
                    itemmodify.putExtra("item_time",item.mypage_item_priceKind);
                    itemmodify.putExtra("item_category", item.mypage_item_category);
                    itemmodify.putExtra("item_article", item.mypage_item_article);
                    itemmodify.putExtra("item_location", item.mypage_item_location);
                    itemmodify.putExtra("item_id", item_id);

                    startActivity(itemmodify);

                }
            });

            holder.mypage_item_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(item.mypage_item_state.equalsIgnoreCase("1")){
                        Toast.makeText(MyApplication.getmContext(),"대여 중입니다.",Toast.LENGTH_SHORT).show();
                    }else {
                        currentPosition = position;
                        item_id = item.mypage_item_need_id;
                        deleteBoardDialog = new DeleteBoardDialog(MypageItem.this, leftListener, rightListener);
                        deleteBoardDialog.show();
                    }

                }
            });

            holder.mypage_item_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item_id = item.mypage_item_need_id;
                    if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                    state1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.item_state.setBackgroundResource(R.drawable.item_condition_2);
                            mypageItemObject.mypage_item_state = "1";
                            state=1;
                            new AsyncStateChange().execute(mypageItemObject);
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                    });
                    state2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.item_state.setBackgroundResource(R.drawable.item_condition_1);
                            mypageItemObject.mypage_item_state = "2";
                            state=2;
                            new AsyncStateChange().execute(mypageItemObject);
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                    });
                    state3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.item_state.setBackgroundResource(R.drawable.item_condition_3);
                            mypageItemObject.mypage_item_state = "3";
                            state=3;
                            new AsyncStateChange().execute(mypageItemObject);
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                    });


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

                    AsyncMypageItemDelete asyncMypageItemDelete = new AsyncMypageItemDelete();
                    asyncMypageItemDelete.execute();
                    deleteBoardDialog.dismiss();

            }
        };

        @Override
        public int getItemCount() {
            return mypage_item.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ImageView mypage_item_image;
            public ImageButton mypage_item_state;
            public TextView mypage_item_title;
            public TextView mypage_item_price;
            public TextView mypage_item_priceKind;
            public TextView mypage_item_location;
            public ImageButton mypage_item_edit;
            public ImageButton mypage_item_delete;
            ImageView item_state;
            View view;

            public ViewHolder(View itemView) {
                super(itemView);

                view = itemView;
                mypage_item_image = (ImageView) itemView.findViewById(R.id.mypage_item_image);
                mypage_item_title = (TextView) itemView.findViewById(R.id.mypage_item_title);
                mypage_item_price = (TextView) itemView.findViewById(R.id.mypage_item_price);
                mypage_item_priceKind = (TextView) itemView.findViewById(R.id.mypage_item_priceKind);
                mypage_item_location = (TextView) itemView.findViewById(R.id.mypage_item_location);
                mypage_item_state = (ImageButton) itemView.findViewById(R.id.mypage_item_state);
                mypage_item_edit = (ImageButton) itemView.findViewById(R.id.mypage_item_edit);
                mypage_item_delete = (ImageButton) itemView.findViewById(R.id.mypage_item_delete);
                item_state = (ImageView) itemView.findViewById(R.id.item_state);

            }

        }

    }

    public class AsyncMypageItemJSONList extends AsyncTask<String, Integer, ArrayList<MypageItemObject>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected ArrayList<MypageItemObject> doInBackground(String... params) {
            return MypageItem_HttpAPIHelperHandler.MypageItemAllSelect();
        }

        @Override
        protected void onPostExecute(ArrayList<MypageItemObject> result) {
            super.onPostExecute(result);
            if(result != null && result.size() > 0){
                mypageadapter = new MypageItemRecyclerAdapter(MyApplication.getmContext(), result, R.layout.mypage_recyclerview_item);
                recyclerView.setAdapter(mypageadapter);

            }
        }
    }

    public class AsyncMypageItemDelete extends AsyncTask<MypageItemObject, Integer,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(MypageItemObject... mypageItmeInfo) {
            boolean flag;
            String wantItemInsertResult = "";
            Response response = null;
            final OkHttpClient toServer ;

            try {
                toServer = OkHttpInitSingtonManager.getOkHttpApplyCookieClient();


                final Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_NADO_ITEM_LIST +"/"+item_id)
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

                if(mypageadapter.deletePosition()){
                    mypageadapter.notifyDataSetChanged();
                }
                if (result.equalsIgnoreCase("delete success")){
                    Toast.makeText(MyApplication.getmContext(),"삭제되었습니다",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MyApplication.getmContext(),"실패",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(MyApplication.getmContext(), "입력 중 문제 발생", Toast.LENGTH_SHORT).show();
            }

        }
    }
    //asyncTask
    public class AsyncStateChange extends AsyncTask<MypageItemObject, Integer,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(MypageItemObject... ItmeInfo) {
            boolean flag;
            String ItemInsertResult = "";
            MypageItemObject reqParams = ItmeInfo[0];
            Response response = null;
            final OkHttpClient toServer ;
            try {
                toServer = OkHttpInitSingtonManager.getOkHttpApplyCookieClient();

                RequestBody postBody = new FormBody.Builder()
                        .add("status",reqParams.mypage_item_state)
                        .build();


                final Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_NADOO_STATUS+"/"+item_id)
                        .post(postBody)
                        .build();

                Log.e("state request",request.toString());
                Log.e("item_id",String.valueOf(item_id));
                response = toServer.newCall(request).execute();
                Log.e("state response",response.toString());
                flag = response.isSuccessful();
                Log.e("결과",String.valueOf(flag));
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
                if (result.equalsIgnoreCase("status change success")){
                    switch (state){
                        case 1:
                            Toast.makeText(MyApplication.getmContext(),"대여중으로 변경!",Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Toast.makeText(MyApplication.getmContext(),"대여가능으로 변경!",Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            Toast.makeText(MyApplication.getmContext(),"대여불가로 변경!",Toast.LENGTH_SHORT).show();
                            break;
                    }

                }else{
                   // Toast.makeText(MyApplication.getmContext(),"실패",Toast.LENGTH_SHORT).show();
                }

            }else{
                Toast.makeText(MyApplication.getmContext(),"입력 중 문제 발생",Toast.LENGTH_SHORT).show();
            }
            Log.e("result",result);
        }
    }
}
