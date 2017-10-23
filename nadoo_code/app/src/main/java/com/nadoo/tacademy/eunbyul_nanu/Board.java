package com.nadoo.tacademy.eunbyul_nanu;

import android.content.Intent;
import android.media.Image;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Board  extends Fragment {

    public static Board newInstance(){
        Board BoardFrag = new Board();
        return BoardFrag;
    }

    ImageButton topic_lasst;
    LinearLayout activity_board;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_board,container,false);
        topic_lasst = (ImageButton) view.findViewById(R.id.topic_lasst);
        topic_lasst.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(MyApplication.getmContext(),"지난주제이동!",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), LastTopicActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click","게시판 이동");
                Toast.makeText(MyApplication.getmContext(),"게시판 이동",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), BoardFragment.class);
                startActivity(intent);
            }
        });


        return view;
    }
}
