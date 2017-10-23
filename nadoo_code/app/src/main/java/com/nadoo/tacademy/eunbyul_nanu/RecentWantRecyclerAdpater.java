package com.nadoo.tacademy.eunbyul_nanu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Tacademy on 2017-06-12.
 */

public class RecentWantRecyclerAdpater extends RecyclerView.Adapter<RecentWantRecyclerAdpater.ViewHolder> {

    Context context;

    public RecentWantRecyclerAdpater(Context context){
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_wantrecent_item, viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.wantRecent_name.setText("대영교재");
    }


    @Override
    public int getItemCount() {
        return 3;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView wantRecent_name;
        public ImageButton wantRecent_delete;
        public ViewHolder(View v){
            super(v);

            wantRecent_name = (TextView)v.findViewById(R.id.wantRecent_name);
            wantRecent_delete = (ImageButton)v.findViewById(R.id.wantRecent_delete);
        }
    }
}
