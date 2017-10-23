package com.nadoo.tacademy.eunbyul_nanu.Profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nadoo.tacademy.eunbyul_nanu.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyoonho on 2017-05-31.
 */

public class ProfileWantRecyclerAdapter extends RecyclerView.Adapter<ProfileWantRecyclerAdapter.ViewHolder>{

    Context context;
    ArrayList<ProfileWantObject> profile_want_item;
    private int itemlayout;
    public ProfileWantRecyclerAdapter(Context context, ArrayList<ProfileWantObject> profile_want_item, int itemlayout){
        this.context = context;
        this.profile_want_item = profile_want_item;
        this.itemlayout = itemlayout;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(itemlayout, viewGroup,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.profile_want_title.setText(profile_want_item.get(position).profile_want_title);
        holder.profile_want_date.setText(profile_want_item.get(position).profile_want_date);
        holder.profile_want_contents.setText(profile_want_item.get(position).profile_want_contents);

        final String story = holder.profile_want_contents.getText().toString();
        if( story.length() > 50){
            holder.profile_want_contents.setText(story.substring(0,50)+" ...더보기");
            String story2 = holder.profile_want_contents.getText().toString();
            holder.profile_want_contents.setMovementMethod(LinkMovementMethod.getInstance());
            holder.profile_want_contents.setText(story2, TextView.BufferType.SPANNABLE);
            Spannable span = (Spannable)holder.profile_want_contents.getText();
            int start = story2.indexOf("더보기");
            int end = start + "더보기".length();
            span.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    holder.profile_want_contents.setMaxLines(100);
                    holder.profile_want_contents.setText(story);
                }
                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.linkColor = 0xff000000;
                    ds.setUnderlineText(true);
                    super.updateDrawState(ds);
                }
            },start,end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }else{
            holder.profile_want_contents.setText(story);
        }
    }

    @Override
    public int getItemCount() {
        return profile_want_item.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{


        public TextView profile_want_title;
        public TextView profile_want_contents;
        public TextView profile_want_date;

        public ViewHolder(View itemView){
            super(itemView);

            profile_want_title = (TextView)itemView.findViewById(R.id.profile_want_title);
            profile_want_contents = (TextView)itemView.findViewById(R.id.profile_want_contents);
            profile_want_date = (TextView)itemView.findViewById(R.id.profile_want_date);
        }

    }
}
