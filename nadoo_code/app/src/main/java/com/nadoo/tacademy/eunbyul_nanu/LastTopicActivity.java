package com.nadoo.tacademy.eunbyul_nanu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Kyoonho on 2017-06-18.
 */

public class LastTopicActivity extends Activity implements View.OnClickListener{

    ImageButton goto_board;
    ImageView goto_topic1, goto_topic2, goto_topic3, goto_topic4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lasttopic);

        goto_board = (ImageButton)findViewById(R.id.last_topic_back_btn);
        goto_topic1 = (ImageView)findViewById(R.id.topic1);
        goto_topic2 = (ImageView)findViewById(R.id.topic2);
        goto_topic3 = (ImageView)findViewById(R.id.topic3);
        goto_topic4 = (ImageView)findViewById(R.id.topic4);


        goto_board.setOnClickListener(this);
        goto_topic1.setOnClickListener(this);
        goto_topic2.setOnClickListener(this);
        goto_topic3.setOnClickListener(this);
        goto_topic4.setOnClickListener(this);

    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.last_topic_back_btn :
                finish();
                break;
            case R.id.topic1:
                Toast.makeText(MyApplication.getmContext(), "아직 준비중입니다.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.topic2 :
                Toast.makeText(MyApplication.getmContext(), "아직 준비중입니다.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.topic3 :
                Toast.makeText(MyApplication.getmContext(), "아직 준비중입니다.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.topic4 :
                Toast.makeText(MyApplication.getmContext(), "아직 준비중입니다.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
