package com.nadoo.tacademy.eunbyul_nanu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.util.Log.e;

/**
 * Created by Kyoonho on 2017-05-31.
 */

public class RegisterWrite extends Activity implements View.OnClickListener {

    /*카테고리 중복체크 변수*/
    private int CHECK_book=0,CHECK_schoolbook =0,CHECK_sports=0,CHECK_electrodevice=0,CHECK_life=0,
            CHECK_animal=0,CHECK_travel=0,CHECK_mobile=0,CHECK_fashion=0,CHECK_beauty=0,
            CHECK_instrument=0,CHECK_etc=0,CHECK_realestate=0,CHECK_talent=0,CHECK_SUM = 0,temp=0;

    Response response;

    /*물품명*/
    EditText edit_register_write;
    String write;

    /*카테고리 부분*/
    ImageButton category_schoolbook,category_book,category_sports,category_electrodevice
            ,category_life, category_animal, category_travel,category_mobile,category_fashion,category_beauty,
            category_instrument, category_etc,category_realestate, category_talent;
    String category;

    /*설명글*/
    EditText edit_write_info;

    /*오브젝트*/
    private WantItemObject wantItemObject;

    /*맵*/
    ImageButton write_select_map;
    TextView maptitle,mapsubtitle;
    String item_name, info;
    /*등록,취소*/

    ImageButton cancel_write;

    SharedPreferences writeinfo,getwriteinfo;
    SharedPreferences.Editor writeinfo_editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_write);

        if(wantItemObject==null){
            wantItemObject = new WantItemObject();
        }
        //물품명
        edit_register_write = (EditText)findViewById(R.id.edit_register_write);

        //설명글
        edit_write_info = (EditText)findViewById(R.id.edit_write_info);

        edit_register_write.setOnKeyListener(new View.OnKeyListener() {
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

        //지도
        maptitle = (TextView)findViewById(R.id.maptitle);
        mapsubtitle = (TextView)findViewById(R.id.mapsubtitle);

        Intent intent = getIntent();
        String title_data = intent.getStringExtra("title");
        String subtitle_data = intent.getStringExtra("subtitle");

        maptitle.setText(title_data);
        mapsubtitle.setText(subtitle_data);
        write_select_map = (ImageButton)findViewById(R.id.write_selectmap);
        write_select_map.setOnClickListener(this);

        /*등록취소 버튼*/
        cancel_write = (ImageButton)findViewById(R.id.delete_register_write);
        cancel_write.setOnClickListener(this);

        /*프리퍼런스 넣기*/
        writeinfo = getSharedPreferences("write_info",MODE_PRIVATE);
        writeinfo_editor = writeinfo.edit();

        /*프리퍼런스 가져오기*/
        getwriteinfo = getSharedPreferences("write_info",MODE_PRIVATE);
        String item_title = getwriteinfo.getString("write_title","");
        String item_contents = getwriteinfo.getString("write_contents","");
        edit_register_write.setText(item_title);
        edit_write_info.setText(item_contents);

    }

    public void regist_do(View v){
        wantItemObject.wantItem_title = edit_register_write.getText().toString();
        wantItemObject.wantItem_category = category;
        wantItemObject.wantItem_story = edit_write_info.getText().toString();
        wantItemObject.wantItem_location = mapsubtitle.getText().toString();

        if(wantItemObject.wantItem_title == null || wantItemObject.wantItem_title.length() <=0){
            Toast.makeText(MyApplication.getmContext(),"물품명을 입력하세요.",Toast.LENGTH_LONG).show();
        }else if(wantItemObject.wantItem_category == null || wantItemObject.wantItem_category.length() <=0){
            Toast.makeText(MyApplication.getmContext(),"카테고리를 선택하세요.",Toast.LENGTH_LONG).show();
        }else if(wantItemObject.wantItem_story == null || wantItemObject.wantItem_story.length() <=0) {
            Toast.makeText(MyApplication.getmContext(), "설명글을 입력하세요.", Toast.LENGTH_LONG).show();
        }else{
            new AsyncWantItemInsert().execute(wantItemObject);
            Toast.makeText(MyApplication.getmContext(),"등록!",Toast.LENGTH_LONG).show();

        }
    }

    public void onClick(View v){
        if(v.getId() == R.id.write_selectmap) {
            item_name = edit_register_write.getText().toString();
            info = edit_write_info.getText().toString();

            writeinfo_editor.putString("write_title",item_name);
            writeinfo_editor.putString("write_contents",info);
            writeinfo_editor.apply();

            Intent intent = new Intent(RegisterWrite.this, RegisterWriteMapActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if(v.getId()==R.id.delete_register_write){

            writeinfo_editor.clear();
            writeinfo_editor.commit();

            Toast.makeText(MyApplication.getmContext(), "등록 취소",Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    public void category(View v) {
        if (v.getId() == R.id.category_write_shoolbook) {
            category_schoolbook = (ImageButton) findViewById(R.id.category_write_shoolbook);
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
        } else if (v.getId() == R.id.category_write_book) {
            category_book = (ImageButton) findViewById(R.id.category_write_book);
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
        else if (v.getId() == R.id.category_write_sports) {
            category_sports = (ImageButton) findViewById(R.id.category_write_sports);
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
        } else if (v.getId() == R.id.category_write_electrodevice) {
            category_electrodevice = (ImageButton) findViewById(R.id.category_write_electrodevice);
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
        } else if (v.getId() == R.id.category_write_life) {
            category_life = (ImageButton) findViewById(R.id.category_write_life);
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
        } else if (v.getId() == R.id.category_write_dog) {
            category_animal = (ImageButton) findViewById(R.id.category_write_dog);
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
        } else if (v.getId() == R.id.category_write_travel) {
            category_travel = (ImageButton) findViewById(R.id.category_write_travel);
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
        } else if (v.getId() == R.id.category_write_mobile) {
            category_mobile = (ImageButton) findViewById(R.id.category_write_mobile);
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
        } else if (v.getId() == R.id.category_write_fashion) {
            category_fashion = (ImageButton) findViewById(R.id.category_write_fashion);
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
        } else if (v.getId() == R.id.category_write_beauty) {
            category_beauty = (ImageButton) findViewById(R.id.category_write_beauty);
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
        } else if (v.getId() == R.id.category_write_instrument) {
            category_instrument = (ImageButton) findViewById(R.id.category_write_instrument);
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
        } else if (v.getId() == R.id.category_write_etc) {
            category_etc = (ImageButton) findViewById(R.id.category_write_etc);
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
        } else if (v.getId() == R.id.category_write_talent) {
            category_talent = (ImageButton) findViewById(R.id.category_write_talent);
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
        } else if (v.getId() == R.id.category_write_realeatate) {
            category_realestate = (ImageButton) findViewById(R.id.category_write_realeatate);
            if (temp == 0) {
                if (CHECK_realestate == 0) {
                    category_realestate.setSelected(true);
                    CHECK_realestate = 1;
                    temp = CHECK_realestate;
                    category = "부동산";
                } else {
                    category_realestate.setSelected(false);
                    CHECK_realestate = 0;
                    temp = CHECK_realestate;
                }
                Toast.makeText(MyApplication.getmContext(), "category : " + category, Toast.LENGTH_SHORT).show();
            } else {
                if (CHECK_realestate == 0) {
                    Toast.makeText(MyApplication.getmContext(), "카테고리를 1개만 선택하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    category_realestate.setSelected(false);
                    CHECK_realestate = 0;
                    temp = CHECK_realestate;
                }
                Toast.makeText(MyApplication.getmContext(), "category : " + category, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class AsyncWantItemInsert extends AsyncTask<WantItemObject, Integer,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(WantItemObject... wantItmeInfo) {
            boolean flag;
            String wantItemInsertResult = "";
            WantItemObject reqParams = wantItmeInfo[0];
            response = null;
            final OkHttpClient toServer ;

            try {
                toServer = OkHttpInitSingtonManager.getOkHttpApplyCookieClient();

                RequestBody postBody = new FormBody.Builder()
                        .add("title",reqParams.wantItem_title)
                        .add("category",reqParams.wantItem_category)
                        .add("article",reqParams.wantItem_story)
                        .add("location",reqParams.wantItem_location)
                        .build();

                final Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_NADO_WANTITEM_INSERT)
                        .post(postBody)
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
                        wantItemInsertResult = jsonObject.optString("data");

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

            return wantItemInsertResult;
        }

        @Override
        protected void onPostExecute(String result) {
            //super.onPostExecute(s);

            if(result != null){
                Intent main = new Intent(MyApplication.getmContext(),MainActivity.class);
                main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(main);
                SharedPreferences write = getSharedPreferences("write_info",MODE_PRIVATE);
                SharedPreferences.Editor write_editor = write.edit();
                write_editor.clear();
                write_editor.commit();

            }else{
                Toast.makeText(MyApplication.getmContext(),"입력 중 문제 발생",Toast.LENGTH_SHORT).show();
            }
            Log.e("result",result);
        }
    }
}