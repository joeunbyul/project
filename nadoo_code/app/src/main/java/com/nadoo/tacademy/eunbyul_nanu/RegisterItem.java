package com.nadoo.tacademy.eunbyul_nanu;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.util.Log.e;

/**
 * Created by Kyoonho on 2017-05-29.
 */

public class RegisterItem extends Activity implements View.OnClickListener {

    /*카테고리 중복체크 변수*/
    private int CHECK_book=0,CHECK_schoolbook =0,CHECK_sports=0,CHECK_electrodevice=0,CHECK_life=0,
            CHECK_animal=0,CHECK_travel=0,CHECK_mobile=0,CHECK_fashion=0,CHECK_beauty=0,
            CHECK_instrument=0,CHECK_etc=0,CHECK_realestate=0,CHECK_talent=0,CHECK_SUM = 0,temp=0,price_temp=0,CHECK_time=0,CHECK_day=0,CHECK_nego=0;


    Response response;

    String category;
    String priceKind;

      /*프리퍼런스*/

    String item_title, item_info,user,title_data,subtitle_data;
    SharedPreferences iteminfo,getiteminfo,prefs;
    SharedPreferences.Editor iteminfo_editor;


    public static final MediaType IMAGE_MIME_TYPE = MediaType.parse("image/*");
    /*사진*/
    private static final String TAG = "MainActivity";
    private ImageView mPhotoImageView;
    private ImageButton photo_image1,photo_image2,photo_image3;
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private ArrayList<UpLoadValueObject> upLoadfiles = new ArrayList<>();
    int imageCount = 0;
    class UpLoadValueObject {
        File file; //업로드할 파일
        boolean tempFiles; //임시파일 유무

        public UpLoadValueObject(File file, boolean tempFiles) {
            this.file = file;
            this.tempFiles = tempFiles;
        }
    }
    EditText edit_register_item,edit_item_info,register_price;
    ImageButton cancel_regist_item,regist_item,category_schoolbook,category_book,category_sports,category_electrodevice
            ,category_life, category_animal, category_travel,category_mobile,category_fashion,category_beauty,
            category_instrument, category_etc,category_realestate, category_talent, select_map,price_day,price_hour,price_nego,nego_long;

    public ImageView upload_photo;
    TextView item_location_title,item_location_contents;

    /*오브젝트*/
    private ItemObject itemObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_item);
        if(itemObject==null){
            itemObject = new ItemObject();
        }


        /*가격 - 일, 시간, 협의 버튼*/
        price_day = (ImageButton)findViewById(R.id.register_day);
        //price_day.setOnClickListener(this);
        price_hour = (ImageButton)findViewById(R.id.register_time);
      //  price_hour.setOnClickListener(this);
        price_nego = (ImageButton)findViewById(R.id.etc);
       // price_nego.setOnClickListener(this);
        nego_long = (ImageButton)findViewById(R.id.nego_long);

        edit_register_item = (EditText)findViewById(R.id.edit_register_item);
        edit_register_item.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if(keyCode == event.KEYCODE_ENTER)
                {
                    return true;
                }
                return false;
            }
        });

        edit_item_info = (EditText)findViewById(R.id.edit_item_info);
        register_price = (EditText)findViewById(R.id.register_price);

        //사진
        photo_image2 = (ImageButton) findViewById(R.id.photo_image2);
       /* photo_image1 = (ImageButton) findViewById(R.id.photo_image1);
        photo_image3 = (ImageButton) findViewById(R.id.photo_image3);*/
        photo_image2.setOnClickListener(this);
     /*   photo_image1.setOnClickListener(this);
        photo_image3.setOnClickListener(this);*/

       /* Intent intent = getIntent();
        if(!photo.isEmpty()){
            photo = intent.getStringExtra("image");
            File returedImage
                    = new File(intent.getStringExtra("image"));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 16;

            Bitmap tempImage = BitmapFactory.decodeFile(returedImage.getAbsolutePath(), options);
            photo_image1.setImageBitmap(tempImage);
        }*/

        item_location_title = (TextView)findViewById(R.id.item_maptitle);
        item_location_contents=(TextView)findViewById(R.id.item_mapsubtitle);

        select_map = (ImageButton)findViewById(R.id.selectmap);
        select_map.setOnClickListener(this);

        Intent location_intent = getIntent();
        title_data = location_intent.getStringExtra("title");
        subtitle_data = location_intent.getStringExtra("subtitle");
        item_location_title.setText(title_data);
        item_location_contents.setText(subtitle_data);

        /*등록/취소 버튼*/
        cancel_regist_item = (ImageButton)findViewById(R.id.delete_register_item);
        cancel_regist_item.setOnClickListener(this);

           /*프리퍼런스 넣기*/
        iteminfo = getSharedPreferences("item_info",MODE_PRIVATE);
        iteminfo_editor = iteminfo.edit();

        /*프리퍼런스 가져오기*/
        getiteminfo = getSharedPreferences("item_info",MODE_PRIVATE);
        String item_title = getiteminfo.getString("item_title","");
        String item_contents = getiteminfo.getString("item_contents","");
        edit_register_item.setText(item_title);
        edit_item_info.setText(item_contents);

        checkPermission();
    }

    Uri currentSelectedUri; //업로드할 현재 이미지에 대한 Uri
    File myImageDir; //카메라로 찍은 사진을 저장할 디렉토리
    String currentFileName;  //파일이름
    String currentPhotoPath;
    File photoDir;

    //SDCard유무확인
    @Override
    public void onResume() {
        super.onResume();

    }

    //퍼미션 체크
    private final int MY_PERMISSION_REQUEST_STORAGE = 100;
    private final int MY_PERMISSION_REQUEST_CAMERA = 200;

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Explain to the user why we need to write the permission.
                    Toast.makeText(this, "Read/Write external storage", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_REQUEST_STORAGE);

            } else {
                //사용자가 언제나 허락
            }

        }

    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    //사용자가 퍼미션을 OK했을 경우
                    Toast.makeText(this, "Request storage permission success", Toast.LENGTH_LONG).show();


                } else {

                    Toast.makeText(this, "Request storage permission denied", Toast.LENGTH_LONG).show();

                    //사용자가 퍼미션을 거절했을 경우
                }
                break;



        }
    }
    //등록
    public void itemregist_do(View v){

        itemObject.item_photo = myImageDir.toString();
        itemObject.item_article = edit_item_info.getText().toString();
        itemObject.item_name = edit_register_item.getText().toString();
        itemObject.item_category = category;
        itemObject.item_time = priceKind;
        itemObject.item_price = register_price.getText().toString();
        itemObject.item_location = subtitle_data;

        Log.e("photo_dir",myImageDir.getPath());
        if(itemObject.item_name == null || itemObject.item_name.length() <=0){
            Toast.makeText(MyApplication.getmContext(),"물품명을 입력하세요.",Toast.LENGTH_LONG).show();
        }else if(itemObject.item_category == null || itemObject.item_category.length() <=0){
            Toast.makeText(MyApplication.getmContext(),"카테고리를 선택하세요.",Toast.LENGTH_LONG).show();
        }else if(itemObject.item_article == null || itemObject.item_article.length() <=0) {
            Toast.makeText(MyApplication.getmContext(), "설명글을 입력하세요.", Toast.LENGTH_LONG).show();
        }else if(itemObject.item_time  == null || itemObject.item_time .length() <=0) {
            Toast.makeText(MyApplication.getmContext(), "조건을 선택하세요.", Toast.LENGTH_LONG).show();
        }else if(itemObject.item_price == null || itemObject.item_price.length() <=0) {
            Toast.makeText(MyApplication.getmContext(), "가격을 입력하세요.", Toast.LENGTH_LONG).show();
        }else if(itemObject.item_location == null || itemObject.item_location.length() <=0) {
            Toast.makeText(MyApplication.getmContext(), "위치을 선택하세요.", Toast.LENGTH_LONG).show();
        }else{
            new AsyncItemInsert().execute(itemObject);
            Toast.makeText(MyApplication.getmContext(),"잠시만 기다려주세요.",Toast.LENGTH_LONG).show();
        }
        SharedPreferences location_pref = getSharedPreferences("location_pref",MODE_PRIVATE);
        SharedPreferences.Editor editor = location_pref.edit();

        editor.putString("location",title_data);
        editor.apply();

    }

    //카테고리 선택
    public void itemcategory(View v){
        if (v.getId() == R.id.category_shoolbook) {
            category_schoolbook = (ImageButton) findViewById(R.id.category_shoolbook);
            if (temp == 0) {
                if (CHECK_schoolbook == 0) {
                    category_schoolbook.setSelected(true);
                    CHECK_schoolbook = 1;
                    temp = CHECK_schoolbook;
                    category = "학교교재";
                } else {
                    category_schoolbook.setSelected(false);
                    CHECK_schoolbook = 0;
                    temp = CHECK_schoolbook;
                }
                Toast.makeText(MyApplication.getmContext(), "category : " + category, Toast.LENGTH_SHORT).show();
            } else {
                if (CHECK_schoolbook == 0) {
                    Toast.makeText(MyApplication.getmContext(), "카테고리를 1개만 선택하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    category_schoolbook.setSelected(false);
                    CHECK_schoolbook = 0;
                    temp = CHECK_schoolbook;
                }
                Toast.makeText(MyApplication.getmContext(), "category : " + category, Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.category_book) {
            category_book = (ImageButton) findViewById(R.id.category_book);
            if (temp == 0) {
                if (CHECK_book == 0) {
                    category_book.setSelected(true);
                    CHECK_book = 1;
                    temp = CHECK_book;
                    category = "일반서적";
                } else {
                    category_book.setSelected(false);
                    CHECK_book = 0;
                    temp = CHECK_book;
                }
                Toast.makeText(MyApplication.getmContext(), "category : " + category, Toast.LENGTH_SHORT).show();
            } else {
                if (CHECK_book == 0) {
                    Toast.makeText(MyApplication.getmContext(), "카테고리를 1개만 선택하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    category_book.setSelected(false);
                    CHECK_book = 0;
                    temp = CHECK_book;
                }
                Toast.makeText(MyApplication.getmContext(), "category : " + category, Toast.LENGTH_SHORT).show();
            }
        }
        else if (v.getId() == R.id.category_sports) {
            category_sports = (ImageButton) findViewById(R.id.category_sports);
            if (temp == 0) {
                if (CHECK_sports == 0) {
                    category_sports.setSelected(true);
                    CHECK_sports = 1;
                    temp = CHECK_sports;
                    category = "스포츠";
                } else {
                    category_sports.setSelected(false);
                    CHECK_sports = 0;
                    temp = CHECK_sports;
                }
                Toast.makeText(MyApplication.getmContext(), "category : " + category, Toast.LENGTH_SHORT).show();
            } else {
                if (CHECK_sports == 0) {
                    Toast.makeText(MyApplication.getmContext(), "카테고리를 1개만 선택하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    category_sports.setSelected(false);
                    CHECK_sports = 0;
                    temp = CHECK_sports;
                }
                Toast.makeText(MyApplication.getmContext(), "category : " + category, Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.category_electrodevice) {
            category_electrodevice = (ImageButton) findViewById(R.id.category_electrodevice);
            if (temp == 0) {
                if (CHECK_electrodevice == 0) {
                    category_electrodevice.setSelected(true);
                    CHECK_electrodevice = 1;
                    temp = CHECK_electrodevice;
                    category = "전자기기";
                } else {
                    category_electrodevice.setSelected(false);
                    CHECK_electrodevice = 0;
                    temp = CHECK_electrodevice;
                }
                Toast.makeText(MyApplication.getmContext(), "category : " + category, Toast.LENGTH_SHORT).show();
            } else {
                if (CHECK_electrodevice == 0) {
                    Toast.makeText(MyApplication.getmContext(), "카테고리를 1개만 선택하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    category_electrodevice.setSelected(false);
                    CHECK_electrodevice = 0;
                    temp = CHECK_electrodevice;
                }
                Toast.makeText(MyApplication.getmContext(), "category : " + category, Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.category_life) {
            category_life = (ImageButton) findViewById(R.id.category_life);
            if (temp == 0) {
                if (CHECK_life == 0) {
                    category_life.setSelected(true);
                    CHECK_life = 1;
                    temp = CHECK_life;
                    category = "생활용품";
                } else {
                    category_life.setSelected(false);
                    CHECK_life = 0;
                    temp = CHECK_life;
                }
                Toast.makeText(MyApplication.getmContext(), "category : " + category, Toast.LENGTH_SHORT).show();
            } else {
                if (CHECK_life == 0) {
                    Toast.makeText(MyApplication.getmContext(), "카테고리를 1개만 선택하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    category_life.setSelected(false);
                    CHECK_life = 0;
                    temp = CHECK_life;
                }
                Toast.makeText(MyApplication.getmContext(), "category : " + category, Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.category_dog) {
            category_animal = (ImageButton) findViewById(R.id.category_dog);
            if (temp == 0) {
                if (CHECK_animal == 0) {
                    category_animal.setSelected(true);
                    CHECK_animal = 1;
                    temp = CHECK_animal;
                    category = "애완용품";
                } else {
                    category_animal.setSelected(false);
                    CHECK_animal = 0;
                    temp = CHECK_animal;
                }
                Toast.makeText(MyApplication.getmContext(), "category : " + category, Toast.LENGTH_SHORT).show();
            } else {
                if (CHECK_animal == 0) {
                    Toast.makeText(MyApplication.getmContext(), "카테고리를 1개만 선택하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    category_animal.setSelected(false);
                    CHECK_animal = 0;
                    temp = CHECK_animal;
                }
                Toast.makeText(MyApplication.getmContext(), "category : " + category, Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.category_travel) {
            category_travel = (ImageButton) findViewById(R.id.category_travel);
            if (temp == 0) {
                if (CHECK_travel == 0) {
                    category_travel.setSelected(true);
                    CHECK_travel = 1;
                    temp = CHECK_travel;
                    category = "여행용품";
                } else {
                    category_travel.setSelected(false);
                    CHECK_travel = 0;
                    temp = CHECK_travel;
                }
                Toast.makeText(MyApplication.getmContext(), "category : " + category, Toast.LENGTH_SHORT).show();
            } else {
                if (CHECK_travel == 0) {
                    Toast.makeText(MyApplication.getmContext(), "카테고리를 1개만 선택하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    category_travel.setSelected(false);
                    CHECK_travel = 0;
                    temp = CHECK_travel;
                }
                Toast.makeText(MyApplication.getmContext(), "category : " + category, Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.category_mobile) {
            category_mobile = (ImageButton) findViewById(R.id.category_mobile);
            if (temp == 0) {
                if (CHECK_mobile == 0) {
                    category_mobile.setSelected(true);
                    CHECK_mobile = 1;
                    temp = CHECK_mobile;
                    category = "모바일";
                } else {
                    category_mobile.setSelected(false);
                    CHECK_mobile = 0;
                    temp = CHECK_mobile;
                }
                Toast.makeText(MyApplication.getmContext(), "category : " + category, Toast.LENGTH_SHORT).show();
            } else {
                if (CHECK_mobile == 0) {
                    Toast.makeText(MyApplication.getmContext(), "카테고리를 1개만 선택하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    category_mobile.setSelected(false);
                    CHECK_mobile = 0;
                    temp = CHECK_mobile;
                }
                Toast.makeText(MyApplication.getmContext(), "category : " + category, Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.category_fashion) {
            category_fashion = (ImageButton) findViewById(R.id.category_fashion);
            if (temp == 0) {
                if (CHECK_fashion == 0) {
                    category_fashion.setSelected(true);
                    CHECK_fashion = 1;
                    temp = CHECK_fashion;
                    category = "패션잡화";
                } else {
                    category_fashion.setSelected(false);
                    CHECK_fashion = 0;
                    temp = CHECK_fashion;
                }
                Toast.makeText(MyApplication.getmContext(), "category : " + category, Toast.LENGTH_SHORT).show();
            } else {
                if (CHECK_fashion == 0) {
                    Toast.makeText(MyApplication.getmContext(), "카테고리를 1개만 선택하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    category_fashion.setSelected(false);
                    CHECK_fashion = 0;
                    temp = CHECK_fashion;
                }
                Toast.makeText(MyApplication.getmContext(), "category : " + category, Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.category_beauty) {
            category_beauty = (ImageButton) findViewById(R.id.category_beauty);
            if (temp == 0) {
                if (CHECK_beauty == 0) {
                    category_beauty.setSelected(true);
                    CHECK_beauty = 1;
                    temp = CHECK_beauty;
                    category = "뷰티";
                } else {
                    category_beauty.setSelected(false);
                    CHECK_beauty = 0;
                    temp = CHECK_beauty;
                }
                Toast.makeText(MyApplication.getmContext(), "category : " + category, Toast.LENGTH_SHORT).show();
            } else {
                if (CHECK_beauty == 0) {
                    Toast.makeText(MyApplication.getmContext(), "카테고리를 1개만 선택하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    category_beauty.setSelected(false);
                    CHECK_beauty = 0;
                    temp = CHECK_beauty;
                }
                Toast.makeText(MyApplication.getmContext(), "category : " + category, Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.category_instrument) {
            category_instrument = (ImageButton) findViewById(R.id.category_instrument);
            if (temp == 0) {
                if (CHECK_instrument == 0) {
                    category_instrument.setSelected(true);
                    CHECK_instrument = 1;
                    temp = CHECK_instrument;
                    category = "악기";
                } else {
                    category_instrument.setSelected(false);
                    CHECK_instrument = 0;
                    temp = CHECK_instrument;
                }
                Toast.makeText(MyApplication.getmContext(), "category : " + category, Toast.LENGTH_SHORT).show();
            } else {
                if (CHECK_instrument == 0) {
                    Toast.makeText(MyApplication.getmContext(), "카테고리를 1개만 선택하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    category_instrument.setSelected(false);
                    CHECK_instrument = 0;
                    temp = CHECK_instrument;
                }
                Toast.makeText(MyApplication.getmContext(), "category : " + category, Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.category_etc) {
            category_etc = (ImageButton) findViewById(R.id.category_etc);
            if (temp == 0) {
                if (CHECK_etc == 0) {
                    category_etc.setSelected(true);
                    CHECK_etc = 1;
                    temp = CHECK_etc;
                    category = "기타";
                } else {
                    category_etc.setSelected(false);
                    CHECK_etc = 0;
                    temp = CHECK_etc;
                }
                Toast.makeText(MyApplication.getmContext(), "category : " + category, Toast.LENGTH_SHORT).show();
            } else {
                if (CHECK_etc == 0) {
                    Toast.makeText(MyApplication.getmContext(), "카테고리를 1개만 선택하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    category_etc.setSelected(false);
                    CHECK_etc = 0;
                    temp = CHECK_etc;
                }
                Toast.makeText(MyApplication.getmContext(), "category : " + category, Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.category_talent) {
            category_talent = (ImageButton) findViewById(R.id.category_talent);
            if (temp == 0) {
                if (CHECK_talent == 0) {
                    category_talent.setSelected(true);
                    CHECK_talent = 1;
                    temp = CHECK_talent;
                    category = "재능";
                } else {
                    category_talent.setSelected(false);
                    CHECK_talent = 0;
                    temp = CHECK_talent;
                }
                Toast.makeText(MyApplication.getmContext(), "category : " + category, Toast.LENGTH_SHORT).show();
            } else {
                if (CHECK_talent == 0) {
                    Toast.makeText(MyApplication.getmContext(), "카테고리를 1개만 선택하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    category_talent.setSelected(false);
                    CHECK_talent = 0;
                    temp = CHECK_talent;
                }
                Toast.makeText(MyApplication.getmContext(), "category : " + category, Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.category_realeatate) {
            category_realestate = (ImageButton) findViewById(R.id.category_realeatate);
            if (temp == 0) {
                if (CHECK_realestate == 0) {
                    category_realestate.setSelected(true);
                    CHECK_realestate = 1;
                    temp = CHECK_realestate;
                    category = "부동산";

                    price_day.setVisibility(View.GONE);
                    price_hour.setVisibility(View.GONE);
                    price_nego.setVisibility(View.GONE);
                    nego_long.setVisibility(View.VISIBLE);
                } else {
                    category_realestate.setSelected(false);
                    CHECK_realestate = 0;
                    temp = CHECK_realestate;

                    price_day.setVisibility(View.VISIBLE);
                    price_hour.setVisibility(View.VISIBLE);
                    price_nego.setVisibility(View.VISIBLE);
                    nego_long.setVisibility(View.GONE);
                }
                Toast.makeText(MyApplication.getmContext(), "category : " + category, Toast.LENGTH_SHORT).show();
            } else {
                if (CHECK_realestate == 0) {
                    Toast.makeText(MyApplication.getmContext(), "카테고리를 1개만 선택하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    category_realestate.setSelected(false);
                    CHECK_realestate = 0;
                    temp = CHECK_realestate;
                    price_day.setVisibility(View.VISIBLE);
                    price_hour.setVisibility(View.VISIBLE);
                    price_nego.setVisibility(View.VISIBLE);
                    nego_long.setVisibility(View.GONE);
                }
                Toast.makeText(MyApplication.getmContext(), "category : " + category, Toast.LENGTH_SHORT).show();
            }
        }

    }
    //시간,일,협의 선택
    public void priceKind(View v){
         /*가격 - 일, 시간, 협의 버튼 클릭 시 이벤트*/
        if(v.getId() == R.id.register_day){
            if(price_temp==0){
                if(CHECK_day==0){
                    price_day.setSelected(true);
                    CHECK_day=1;
                    price_temp=CHECK_day;
                    priceKind="일";
                    register_price.setText("");
                }else{
                    price_day.setSelected(false);
                    CHECK_day=0;
                    price_temp=CHECK_day;
                }
            }else{
                if (CHECK_day == 0) {
                    Toast.makeText(MyApplication.getmContext(), "가격단위를 1개만 선택하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    price_day.setSelected(false);
                    CHECK_day = 0;
                    price_temp = CHECK_day;
                }
            }
          /*  if(CHECK_SUM==0){
                price_day.setSelected(true);
                CHECK_SUM=1;
                priceKind="일";
            }
            else{
                price_day.setSelected(false);
                CHECK_SUM=0;
            }*/
        }else if(v.getId() == R.id.register_time){
            if(price_temp==0){
                if(CHECK_time==0){
                    price_hour.setSelected(true);
                    CHECK_time=1;
                    price_temp=CHECK_time;
                    priceKind="시간";
                    register_price.setText("");
                }else{
                    price_hour.setSelected(false);
                    CHECK_time=0;
                    price_temp=CHECK_time;
                }
            }else{
                if (CHECK_time == 0) {
                    Toast.makeText(MyApplication.getmContext(), "가격단위를 1개만 선택하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    price_hour.setSelected(false);
                    CHECK_time = 0;
                    price_temp = CHECK_time;
                }
            }
            /*if(CHECK_SUM==0){
                price_hour.setSelected(true);
                CHECK_SUM=1;
                priceKind="시간";
            }
            else{
                price_hour.setSelected(false);
                CHECK_SUM=0;
            }*/
        }else if(v.getId() == R.id.etc){
            if(price_temp==0){
                if(CHECK_nego==0){
                    price_nego.setSelected(true);
                    CHECK_nego=1;
                    price_temp=CHECK_nego;
                    priceKind="협의";
                    register_price.setText("0");
                }else{
                    price_nego.setSelected(false);
                    CHECK_nego=0;
                    price_temp=CHECK_nego;
                }
            }else{
                if (CHECK_nego == 0) {
                    Toast.makeText(MyApplication.getmContext(), "가격단위를 1개만 선택하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    price_nego.setSelected(false);
                    CHECK_nego = 0;
                    price_temp = CHECK_nego;
                }
            }
           /* if(CHECK_SUM==0){
                price_nego.setSelected(true);
                CHECK_SUM=1;
                priceKind="협의";
            }
            else{
                price_nego.setSelected(false);
                CHECK_SUM=0;
            }*/
        }
    }
    //사진 추가(사진촬영,앨범 선택)

    public void onClick(View v){
        if(v.getId() == R.id.selectmap) {
            item_title = edit_register_item.getText().toString();
            item_info = edit_item_info.getText().toString();

            iteminfo_editor.putString("item_title",item_title);
            iteminfo_editor.putString("item_contents",item_info);
            iteminfo_editor.apply();

            Intent intent = new Intent(RegisterItem.this, RegisterItemMapActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);

        }else if(v.getId()== R.id.delete_register_item){
            iteminfo_editor.clear();
            iteminfo_editor.commit();
            Toast.makeText(MyApplication.getmContext(), "등록 취소",Toast.LENGTH_SHORT).show();
            finish();
        }
        else if(v.getId()== R.id.photo_image2) {

           // checkPermission();
            AlertDialog dialog = null;
            mPhotoImageView = (ImageView) v;
            DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    doTakePhotoAction();
                }
            };

         /*   DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    doTakeAlbumAction();
                }
            };*/

            DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };

            /*dialog = new AlertDialog.Builder(this)
                    .setTitle("업로드할 이미지 선택")
                    .setPositiveButton("앨범선택", albumListener)
                    .setNegativeButton("사진촬영", cameraListener)
                    .setNeutralButton("취소", cancelListener).create();*/
            dialog = new AlertDialog.Builder(this)
                    .setTitle("업로드할 이미지 선택")
                    .setPositiveButton("사진촬영", cameraListener)
                    .setNegativeButton("취소", cancelListener).create();

            dialog.show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case CROP_FROM_CAMERA: {
                // 크롭된 이미지를 세팅
                final Bundle extras = data.getExtras();

                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    mPhotoImageView.setImageBitmap(photo);

                }
                break;
            }
            case PICK_FROM_ALBUM: {
                currentSelectedUri = data.getData();
                if (currentSelectedUri != null) {
                    //실제 Image의 full path name을 얻어온다.
                    if (findImageFileNameFromUri(currentSelectedUri)) {
                        //ArrayList에 업로드할  객체를 추가한다.
                        upLoadfiles.add(new UpLoadValueObject(new File(currentFileName), false));
                    }
                } else {
                    Bundle extras = data.getExtras();
                    Bitmap returedBitmap = (Bitmap) extras.get("data");
                    if (tempSavedBitmapFile(returedBitmap)) {
                        Log.e("임시이미지파일저장", "저장됨");
                    } else {
                        Log.e("임시이미지파일저장", "실패");
                    }
                }
                cropIntent(currentSelectedUri);
                break;

            }
            case PICK_FROM_CAMERA: {

                //카메라캡쳐를 이용해 가져온 이미지
                myImageDir = new File(data.getStringExtra("imageDir"));
                Log.e("return photoDir",myImageDir.toString());
                upLoadfiles.add(new UpLoadValueObject(myImageDir,false));


               if(imageCount == 0){
                    imageCount++;
                   photo_image1.setImageURI(Uri.fromFile(myImageDir));
                }else if(imageCount == 1){
                   photo_image2.setImageURI(Uri.fromFile(myImageDir));
                }
                break;
            }
        }
    }
    private  void  cropIntent(Uri cropUri){
        this.grantUriPermission("com.android.camera", currentSelectedUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(cropUri, "image/*");

        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        grantUriPermission("com.company.app.fileprovider", currentSelectedUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivityForResult(intent, CROP_FROM_CAMERA);
    }
    private boolean tempSavedBitmapFile(Bitmap tempBitmap) {
        boolean flag = false;
        try {
            currentFileName = "upload_" + (System.currentTimeMillis() / 1000);
            String fileSuffix = ".jpg";
            //임시파일을 실행한다.
            File tempFile = File.createTempFile(
                    currentFileName,            // prefix
                    fileSuffix,                   // suffix
                    myImageDir                   // directory
            );
            final FileOutputStream bitmapStream = new FileOutputStream(tempFile);
            tempBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bitmapStream);
            upLoadfiles.add(new UpLoadValueObject(tempFile, true));
            if (bitmapStream != null) {
                bitmapStream.close();
            }

            currentSelectedUri = Uri.fromFile(tempFile);
            /*currentSelectedUri = FileProvider.getUriForFile(MyApplication.getmContext(),"com.company.app.fileprovider",
                    tempFile);*/
            flag = true;
        } catch (IOException i) {
            Log.e("저장중 문제발생", i.toString(), i);
        }
        return flag;
    }
    private boolean findImageFileNameFromUri(Uri tempUri) {
        boolean flag = false;

        //실제 Image Uri의 절대이름
        String[] IMAGE_DB_COLUMN = {MediaStore.Images.ImageColumns.DATA};
        Cursor cursor = null;
        try {
            //Primary Key값을 추출
            String imagePK = String.valueOf(ContentUris.parseId(tempUri));
            //Image DB에 쿼리를 날린다.
            cursor = getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    IMAGE_DB_COLUMN,
                    MediaStore.Images.Media._ID + "=?",
                    new String[]{imagePK}, null, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                currentFileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                Log.e("fileName", String.valueOf(currentFileName));
                flag = true;
            }
        } catch (SQLiteException sqle) {
            Log.e("findImage....", sqle.toString(), sqle);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return flag;
    }
    //앨범호출
    private void doTakeAlbumAction() {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }
    //카메라찍기
    File photoFile;
    private void doTakePhotoAction() {
        Intent cameraIntent = new Intent(MyApplication.getmContext(),CameraActivity.class);
        startActivityForResult(cameraIntent, PICK_FROM_CAMERA);
    }

    //asyncTask
    public class AsyncItemInsert extends AsyncTask<ItemObject, Integer,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(ItemObject... ItmeInfo) {
            boolean flag;
            String ItemInsertResult = "";
            ItemObject reqParams = ItmeInfo[0];
            response = null;
            File upLoadfile = new File(reqParams.item_photo);
            final OkHttpClient toServer ;
            try {
                toServer = OkHttpInitSingtonManager.getOkHttpApplyCookieClient();

                Log.e("upLoadfile",reqParams.item_photo);
                RequestBody fileUploadBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("image",upLoadfile.getName(),RequestBody.create(IMAGE_MIME_TYPE,upLoadfile))
                        .addFormDataPart("title",reqParams.item_name)
                        .addFormDataPart("category",reqParams.item_category)
                        .addFormDataPart("article",reqParams.item_article)
                        .addFormDataPart("priceKind",reqParams.item_time)
                        .addFormDataPart("price",reqParams.item_price)
                        .addFormDataPart("location",reqParams.item_location)
                        .build();

                final Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_NADO_ITEM_INSERT)
                        .post(fileUploadBody)
                        .build();
                response = toServer.newCall(request).execute();
                flag = response.isSuccessful();
                Log.e("결과",String.valueOf(flag));
                String returnedJSON;
                if(flag){
                    returnedJSON = response.body().string();
                    Log.e("resultJSON",returnedJSON);
                    try {
                        JSONObject jsonObject = new JSONObject(returnedJSON);
                        ItemInsertResult = jsonObject.optString("msg");
                    }catch (JSONException jsone){
                        Log.e("json 에러",jsone.toString());
                    }
                }else{

                }
            }catch (UnknownHostException une) {
                e("aaa", une.toString()); //host찾을 수 없을때
            }catch (UnsupportedEncodingException uee) {
                e("bbb", uee.toString());
            }catch (Exception e) { //이 exception은 반드시 마지막에 넣어줘야 함.
                e("ccc", e.toString());
            }finally{ //finally는 반드시 처리 해줘야함.
                if(response != null) {
                    response.close(); //3.* 이상에서는 반드시 연결을 닫아 준다.
                }
            }

            return ItemInsertResult;
        }

        @Override
        protected void onPostExecute(String result) {
            //super.onPostExecute(s);
            Log.e("result",result);
            if(result != null){
                Toast.makeText(MyApplication.getmContext(),"등록되었습니다.",Toast.LENGTH_SHORT).show();
                iteminfo_editor.clear();
                iteminfo_editor.commit();

                Intent main = new Intent(MyApplication.getmContext(),MainActivity.class);
                main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(main);
                finish();
            }else{
                Toast.makeText(MyApplication.getmContext(),"입력 중 문제 발생",Toast.LENGTH_SHORT).show();
            }
            Log.e("result",result);
        }
    }

}
