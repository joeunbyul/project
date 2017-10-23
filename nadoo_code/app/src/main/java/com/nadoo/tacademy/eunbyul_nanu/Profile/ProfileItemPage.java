package com.nadoo.tacademy.eunbyul_nanu.Profile;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nadoo.tacademy.eunbyul_nanu.MyApplication;
import com.nadoo.tacademy.eunbyul_nanu.R;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileItemPage extends AppCompatActivity implements OnMapReadyCallback {

    int item_id; //아이템 id
    String item_photo; //아이템 상세사진
    int item_state; //아이템상태(1:대여가능,2:대여중,3:대여불가)
    String item_name; //이름
    String item_price; //가격
    String item_time; //시간,일,협의
    String item_userid; //유저 id
    String item_nickname; //닉네임
    String item_category; //카테고리
    String item_article; //소개글
    int item_check; //인증여부
    String item_location; //위치
    String item_userphoto; //프로필사진

    ImageView itemPage_photo,itemPage_status,itemPage_category,itemPage_check;
    TextView itemPage_title,itemPage_price,itemPage_days,itemPage_nickname,itemPage_article;
    CircleImageView itemPage_user;
    ImageButton item_page_chatting;

    GoogleMap itemlocationmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_page);
        //intent로 받아온내용
        Intent item = getIntent();
        item_photo = item.getExtras().getString("item_photo");
        item_state = item.getExtras().getInt("item_state");
        item_name = item.getExtras().getString("item_name");
        item_price = item.getExtras().getString("item_price");
        item_time = item.getExtras().getString("item_time");
        item_nickname = item.getExtras().getString("item_nickname");
        item_category = item.getExtras().getString("item_category");
        item_article = item.getExtras().getString("item_article");
        item_check = item.getExtras().getInt("item_check");
        item_location = item.getExtras().getString("item_location");
        item_userphoto = item.getExtras().getString("item_userphoto");

        itemPage_photo=(ImageView)findViewById(R.id.itemPage_photo);
        Glide.with(MyApplication.getmContext()).load(item_photo).into(itemPage_photo);
        itemPage_status=(ImageView)findViewById(R.id.itemPage_status);
        item_page_chatting = (ImageButton)findViewById(R.id.item_page_chatting);
        item_page_chatting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyApplication.getmContext(),"아직 준비중입니다.",Toast.LENGTH_SHORT).show();
            }
        });

        switch (item_state){
            case 1:
                itemPage_status.setImageResource(R.drawable.item_condition_1);
                break;
            case 2:
                itemPage_status.setImageResource(R.drawable.item_condition_2);
                break;
        }
        itemPage_category=(ImageView)findViewById(R.id.itemPage_category);
        switch (item_category){
            case "학교교재":
                itemPage_category.setImageResource(R.drawable.category_title_1); //학교 교재
                break;
            case "일반서적":
                itemPage_category.setImageResource(R.drawable.category_title_2); //일반서적
                break;
            case "스포츠":
                itemPage_category.setImageResource(R.drawable.category_title_3); //스포츠
                break;
            case "생활용품":
                itemPage_category.setImageResource(R.drawable.category_title_4); //생활용품
                break;
            case "여행용품":
                itemPage_category.setImageResource(R.drawable.category_title_5); //여행용품
                break;
            case "모바일":
                itemPage_category.setImageResource(R.drawable.category_title_6); //모바일
                break;
            case "전자기기":
                itemPage_category.setImageResource(R.drawable.category_title_7); //전자기기
                break;
            case "패션잡화":
                itemPage_category.setImageResource(R.drawable.category_title_8); //패션잡화
                break;
            case "뷰티":
                itemPage_category.setImageResource(R.drawable.category_title_9); //뷰티
                break;
            case "악기":
                itemPage_category.setImageResource(R.drawable.category_title_10); //악기
                break;
            case "부동산":
                itemPage_category.setImageResource(R.drawable.category_title_11); //부동산
                break;
            case "재능":
                itemPage_category.setImageResource(R.drawable.category_title_12); //재능
                break;
            case "애완용품":
                itemPage_category.setImageResource(R.drawable.category_title_13); //애완용품
                break;
            case "기타":
                itemPage_category.setImageResource(R.drawable.category_title_14); //기타
                break;
        }

        itemPage_check=(ImageView)findViewById(R.id.itemPage_check);
        if(item_check==1){
            itemPage_check.setImageResource(R.drawable.certificated_user);
        }else{
            itemPage_check.setImageResource(R.drawable.uncertificated_user);
        }

        itemPage_title = (TextView)findViewById(R.id.itemPage_title);
        itemPage_price = (TextView)findViewById(R.id.itemPage_price);
        itemPage_days = (TextView)findViewById(R.id.itemPage_days);
        itemPage_nickname = (TextView)findViewById(R.id.itemPage_nickname);
        itemPage_article = (TextView)findViewById(R.id.itemPage_article);

        itemPage_user = (CircleImageView)findViewById(R.id.itemPage_user);
        Glide.with(MyApplication.getmContext()).load(item_userphoto).into(itemPage_user);

        itemPage_title.setText(item_name);
        itemPage_price.setText(item_price);
        if(item_time.equalsIgnoreCase("시간")){
            itemPage_days.setText(" /시간");
        }else if(item_time.equalsIgnoreCase("일")){
            itemPage_days.setText(" /일");
        }else if(item_time.equalsIgnoreCase("협의")){
            itemPage_price.setVisibility(View.GONE);
            itemPage_days.setText("협의");
        }
        itemPage_nickname.setText(item_nickname);
        itemPage_article.setText(item_article);


        SupportMapFragment supportMapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.item_location);
        supportMapFragment.getMapAsync(this);


    }
    @Override
    public void onMapReady(GoogleMap googleMap){
        itemlocationmap = googleMap;


        Geocoder geocoder = new Geocoder(this);

        List<Address> list = null;
        try{
            list = geocoder.getFromLocationName(item_location,1);
        }catch (IOException e) {
            e.printStackTrace();
            Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
        }
        if (list != null) {
            if (list.size() == 0) {
                Log.e("test","표시되는 주소가 없습니다.");
            } else {

                Address addr = list.get(0);
                double lat = addr.getLatitude();
                double log = addr.getLongitude();


                LatLng latLng = new LatLng(lat,log);
                itemlocationmap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                MarkerOptions options = new MarkerOptions();
                options.position(latLng);
                options.anchor(0.5f, 1);
                options.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin));
                itemlocationmap.animateCamera(CameraUpdateFactory.zoomTo(15));
                itemlocationmap.addMarker(options).showInfoWindow();

            }
        }

    }
}
