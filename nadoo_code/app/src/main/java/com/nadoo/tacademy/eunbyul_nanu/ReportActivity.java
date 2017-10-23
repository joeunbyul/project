package com.nadoo.tacademy.eunbyul_nanu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created by Kyoonho on 2017-05-30.
 */

public class ReportActivity extends Activity implements View.OnClickListener{

    ImageButton case_1,case_2,case_3,case_4;
    ImageView case_1_check, case_2_check, case_3_check, case_4_check;
    int CHECK_SUM = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        case_1 = (ImageButton)findViewById(R.id.case_1);
        case_1_check = (ImageView) findViewById(R.id.case_1_check);
        case_1.setOnClickListener(this);

        case_2 = (ImageButton)findViewById(R.id.case_2);
        case_2_check = (ImageView)findViewById(R.id.case_2_check);
        case_2.setOnClickListener(this);

        case_3 = (ImageButton)findViewById(R.id.case_3);
        case_3_check = (ImageView)findViewById(R.id.case_3_check);
        case_3.setOnClickListener(this);

        case_4 = (ImageButton)findViewById(R.id.case_4);
        case_4_check = (ImageView)findViewById(R.id.case_4_check);
        case_4.setOnClickListener(this);

    }
    public void onClick(View v){
        if(v.getId()==R.id.case_1){
            if(CHECK_SUM==0){
                case_1.setSelected(true);
                CHECK_SUM=1;
                case_1_check.setVisibility(v.VISIBLE);

            }else{
                CHECK_SUM = 0;
                case_1.setSelected(false);

                case_1_check.setVisibility(v.INVISIBLE);

            }
        }
        else if(v.getId() == R.id.case_2){
            if(CHECK_SUM==0){
                case_2.setSelected(true);
                CHECK_SUM=1;
                case_2_check.setVisibility(v.VISIBLE);
            }else{
                CHECK_SUM = 0;
                case_2.setSelected(false);

                case_2_check.setVisibility(v.INVISIBLE);
            }
        }
        else if(v.getId() == R.id.case_3){
            if(CHECK_SUM==0){
                case_3.setSelected(true);
                CHECK_SUM=1;
                case_3_check.setVisibility(v.VISIBLE);
            }else{
                CHECK_SUM = 0;
                case_3.setSelected(false);

                case_3_check.setVisibility(v.INVISIBLE);
            }
        }
        else if(v.getId() == R.id.case_4){
            if(CHECK_SUM==0){
                case_4.setSelected(true);
                CHECK_SUM=1;
                case_4_check.setVisibility(v.VISIBLE);
            }else{
                CHECK_SUM = 0;
                case_4.setSelected(false);

                case_4_check.setVisibility(v.INVISIBLE);
            }
        }

    }

}
