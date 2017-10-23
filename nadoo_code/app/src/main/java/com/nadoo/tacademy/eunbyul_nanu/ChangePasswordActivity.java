package com.nadoo.tacademy.eunbyul_nanu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by Kyoonho on 2017-06-07.
 */

public class ChangePasswordActivity extends Activity implements View.OnClickListener{

    ImageButton change_undo,change_do;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);


        change_undo = (ImageButton)findViewById(R.id.setting_undo);
        change_do = (ImageButton)findViewById(R.id.setting_do);

        change_undo.setOnClickListener(this);
        change_do.setOnClickListener(this);


    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.setting_undo :
                finish();
                break;
            case R.id.setting_do :
                Toast.makeText(MyApplication.getmContext(), "변경완료", Toast.LENGTH_SHORT).show();
                /*intent 추가해야 함.*/
        }
    }
}
