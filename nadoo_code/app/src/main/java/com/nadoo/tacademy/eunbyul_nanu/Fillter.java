package com.nadoo.tacademy.eunbyul_nanu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;

import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;



public class Fillter extends AppCompatActivity implements View.OnClickListener{

    int CHECK_SUM=0;
    String category="";
    TextView minText1,maxText1;
    String min1,max1;
    boolean flag = false;
    String priceKind;

    ImageButton filter_nego_long,filter_nego,filter_day,filter_hour,filter_only,filter_all,
            filter_recent,filter_high,filter_low;

    /*카테고리 부분*/
    ImageButton category_schoolbook,category_book,category_sports,category_electrodevice
            ,category_life, category_animal, category_travel,category_mobile,category_fashion,category_beauty,
            category_instrument, category_etc,category_realestate, category_talent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

         /*가격 - 일, 시간, 협의 버튼*/
        filter_nego = (ImageButton)findViewById(R.id.filter_nego);
        filter_nego.setOnClickListener(this);
        filter_day = (ImageButton)findViewById(R.id.filter_day);
        filter_day.setOnClickListener(this);
        filter_hour = (ImageButton)findViewById(R.id.filter_hour);
        filter_hour.setOnClickListener(this);
        filter_nego_long = (ImageButton)findViewById(R.id.filter_nego_long);

        /*대여가능만, 전체보기*/
        filter_only = (ImageButton)findViewById(R.id.filter_only);
        filter_only.setOnClickListener(this);
        filter_all = (ImageButton)findViewById(R.id.filter_all);
        filter_all.setOnClickListener(this);

        /*정렬부분*/
        filter_recent = (ImageButton)findViewById(R.id.filter_recent);
        filter_recent.setOnClickListener(this);
        filter_high = (ImageButton)findViewById(R.id.filter_high);
        filter_high.setOnClickListener(this);
        filter_low = (ImageButton)findViewById(R.id.filter_low);
        filter_low.setOnClickListener(this);


        minText1 = (TextView)findViewById(R.id.minText1);
        maxText1 = (TextView)findViewById(R.id.maxText1);

        CrystalRangeSeekbar seekbar = (CrystalRangeSeekbar)findViewById(R.id.rangeSeekbar5);
        seekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                min1 = String.valueOf(minValue);
                max1 = String.valueOf(maxValue);

                minText1.setText(min1+"원");
                maxText1.setText(max1+"원");
            }
        });
        filter_nego = (ImageButton)findViewById(R.id.filter_nego);
        filter_day = (ImageButton)findViewById(R.id.filter_day);
        filter_hour = (ImageButton)findViewById(R.id.filter_hour);
        filter_nego_long = (ImageButton)findViewById(R.id.filter_nego_long);

    }

    public void filter_category(View v){
        if(v.getId() == R.id.category_filter_shoolbook) {
            category = "학교교재";
            category_schoolbook = (ImageButton) findViewById(R.id.category_filter_shoolbook);
            if (CHECK_SUM == 0) {
                category_schoolbook.setSelected(true);
                CHECK_SUM = 1;
            } else {
                category_schoolbook.setSelected(false);
                CHECK_SUM = 0;
            }
        }
        else if(v.getId() == R.id.category_filter_book){
            category = "일반서적";
            category_book = (ImageButton)findViewById(R.id.category_filter_book);
            if(CHECK_SUM==0){
                category_book.setSelected(true);
                CHECK_SUM=1;
            }else{
                category_book.setSelected(false);
                CHECK_SUM=0;
            }
        }
        else if(v.getId() == R.id.category_filter_sports){
            category_sports = (ImageButton)findViewById(R.id.category_filter_sports);
            if(CHECK_SUM==0){
                category_sports.setSelected(true);
                CHECK_SUM=1;
                category = "스포츠";
            }else{
                category_sports.setSelected(false);
                CHECK_SUM=0;
            }
        }
        else if(v.getId() == R.id.category_filter_electrodevice){
            category_electrodevice = (ImageButton)findViewById(R.id.category_filter_electrodevice);
            if(CHECK_SUM==0){
                category_electrodevice.setSelected(true);
                CHECK_SUM=1;
                category = "전자기기";

            }else{
                category_electrodevice.setSelected(false);
                CHECK_SUM=0;
            }
        }
        else if(v.getId() == R.id.category_filter_life){
            category_life = (ImageButton)findViewById(R.id.category_filter_life);
            if(CHECK_SUM==0){
                category_life.setSelected(true);
                category = "생활용품";
                CHECK_SUM=1;

            }else{
                category_life.setSelected(false);
                CHECK_SUM=0;
            }
        }
        else if(v.getId() == R.id.category_filter_dog){
            category_animal = (ImageButton)findViewById(R.id.category_filter_dog);
            if(CHECK_SUM==0){
                category_animal.setSelected(true);
                CHECK_SUM=1;
                category = "애완용품";

            }else{
                category_animal.setSelected(false);
                CHECK_SUM=0;
            }
        }
        else if(v.getId() == R.id.category_filter_travel){
            category_travel = (ImageButton)findViewById(R.id.category_filter_travel);
            if(CHECK_SUM==0){
                category_travel.setSelected(true);
                CHECK_SUM=1;
                category = "여행용품";

            }else{
                category_travel.setSelected(false);
                CHECK_SUM=0;
            }
        }
        else if(v.getId() == R.id.category_filter_mobile){
            category_mobile = (ImageButton)findViewById(R.id.category_filter_mobile);
            if(CHECK_SUM==0){
                category_mobile.setSelected(true);
                CHECK_SUM=1;
                category = "모바일";

            }else{
                category_mobile.setSelected(false);
                CHECK_SUM=0;
            }
        }
        else if(v.getId() == R.id.category_filter_fashion){
            category_fashion = (ImageButton)findViewById(R.id.category_filter_fashion);
            if(CHECK_SUM==0){
                category_fashion.setSelected(true);
                category = "패션잡화";
                CHECK_SUM=1;

            }else{
                category_fashion.setSelected(false);
                CHECK_SUM=0;
            }
        }
        else if(v.getId() == R.id.category_filter_beauty){
            category_beauty = (ImageButton)findViewById(R.id.category_filter_beauty);
            category = "뷰티";
            if(CHECK_SUM==0){
                category_beauty.setSelected(true);
                CHECK_SUM=1;

            }else{
                category_beauty.setSelected(false);
                CHECK_SUM=0;
            }
        }
        else if(v.getId() == R.id.category_filter_instrument){
            category_instrument = (ImageButton)findViewById(R.id.category_filter_instrument);
            if(CHECK_SUM==0){
                category_instrument.setSelected(true);
                CHECK_SUM=1;
                category = "악기";

            }else{
                category_instrument.setSelected(false);
                CHECK_SUM=0;
            }
        }
        else if(v.getId() == R.id.category_filter_etc){
            category_etc = (ImageButton)findViewById(R.id.category_filter_etc);
            if(CHECK_SUM==0){
                category_etc.setSelected(true);
                CHECK_SUM=1;
                category = "기타";
            }else{
                category_etc.setSelected(false);
                CHECK_SUM=0;
            }
        }
        else if(v.getId() == R.id.category_filter_talent){
            category_talent = (ImageButton)findViewById(R.id.category_filter_talent);
            if(CHECK_SUM==0){
                category_talent.setSelected(true);
                CHECK_SUM=1;
                category = "재능";

            }else{
                category_talent.setSelected(false);
                CHECK_SUM=0;
            }
        }

        else if(v.getId() == R.id.category_filter_realeatate){
            category_realestate = (ImageButton)findViewById(R.id.category_filter_realeatate);
            if(CHECK_SUM==0){
                category_realestate.setSelected(true);
                CHECK_SUM=1;



                filter_day.setVisibility(View.GONE);
                filter_hour.setVisibility(View.GONE);
                filter_nego.setVisibility(View.GONE);
                filter_nego_long.setVisibility(View.VISIBLE);
                category = "부동산";

            }else{
                category_realestate.setSelected(false);
                CHECK_SUM=0;
                filter_day.setVisibility(View.VISIBLE);
                filter_hour.setVisibility(View.VISIBLE);
                filter_nego.setVisibility(View.VISIBLE);
                filter_nego_long.setVisibility(View.GONE);

            }
        }
    }
    public void onClick(View v){

            /*가격 - 일, 시간, 협의 버튼 클릭 시 이벤트*/
        if(v.getId() == R.id.filter_day){
            if(CHECK_SUM==0){
                filter_day.setSelected(true);
                CHECK_SUM=1;
                priceKind="일";
            }
            else{
                filter_day.setSelected(false);
                CHECK_SUM=0;
            }
        }else if(v.getId() == R.id.filter_hour){
            if(CHECK_SUM==0){
                filter_hour.setSelected(true);
                CHECK_SUM=1;
                priceKind="시간";
            }
            else{
                filter_hour.setSelected(false);
                CHECK_SUM=0;
            }
        }else if(v.getId() == R.id.filter_nego){
            if(CHECK_SUM==0){
                filter_nego.setSelected(true);
                CHECK_SUM=1;
                priceKind="협의";
            }
            else{
                filter_nego.setSelected(false);
                CHECK_SUM=0;
            }
        }else if(v.getId()==R.id.filter_all){
            if(CHECK_SUM==0){
                filter_all.setSelected(true);
                CHECK_SUM=1;
            }
            else{
                filter_all.setSelected(false);
                CHECK_SUM=0;
            }
        }else if(v.getId()==R.id.filter_only){
            if(CHECK_SUM==0){
                filter_only.setSelected(true);
                CHECK_SUM=1;
            }
            else{
                filter_only.setSelected(false);
                CHECK_SUM=0;
            }
        }else if(v.getId()==R.id.filter_high){
            if(CHECK_SUM==0){
                filter_high.setSelected(true);
                CHECK_SUM=1;
            }
            else{
                filter_high.setSelected(false);
                CHECK_SUM=0;
            }
        }
        else if(v.getId()==R.id.filter_low){
            if(CHECK_SUM==0){
                filter_low.setSelected(true);
                CHECK_SUM=1;
            }
            else{
                filter_low.setSelected(false);
                CHECK_SUM=0;
            }
        }
        else if(v.getId()==R.id.filter_recent){
            if(CHECK_SUM==0){
                filter_recent.setSelected(true);
                CHECK_SUM=1;
            }
            else{
                filter_recent.setSelected(false);
                CHECK_SUM=0;
            }
        }
    }
}
