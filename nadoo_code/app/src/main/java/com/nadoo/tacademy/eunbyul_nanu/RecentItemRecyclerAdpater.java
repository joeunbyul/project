package com.nadoo.tacademy.eunbyul_nanu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Tacademy on 2017-06-12.
 */

public class RecentItemRecyclerAdpater extends RecyclerView.Adapter<RecentItemRecyclerAdpater.ViewHolder> {

    Context context;

    public RecentItemRecyclerAdpater(Context context){
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_itemrecent_item, viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.itemRecent_name.setText("노트북");
        holder.itemRecent_delete.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(MyApplication.getmContext(),"삭제",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }


    @Override
    public int getItemCount() {
        return 3;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView itemRecent_name;
        public ImageButton itemRecent_delete;
        public ViewHolder(View v){
            super(v);

            itemRecent_name = (TextView)v.findViewById(R.id.itemRecent_name);
            itemRecent_delete = (ImageButton)v.findViewById(R.id.itemRecent_delete);
        }
    }
}
