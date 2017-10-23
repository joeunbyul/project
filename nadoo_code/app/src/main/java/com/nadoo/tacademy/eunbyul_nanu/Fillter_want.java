package com.nadoo.tacademy.eunbyul_nanu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;


public class Fillter_want extends AppCompatActivity {

    int CHECK_SUM=0;
    String category="";

    /*적용*/
    ImageButton filter_apply;



    /*카테고리 부분*/
    ImageButton category_schoolbook,category_book,category_sports,category_electrodevice
            ,category_life, category_animal, category_travel,category_mobile,category_fashion,category_beauty,
            category_instrument, category_etc,category_realestate, category_talent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_want);

        filter_apply = (ImageButton)findViewById(R.id.filter_apply);
        filter_apply.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Intent i = new Intent(Fillter_want.this,Search.class);
                startActivity(i);
                return true;
            }
        });

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
                category = "부동산";

            }else{
                category_realestate.setSelected(false);
                CHECK_SUM=0;

            }
        }
    }
}
