package waman2.seyeongstar.se0star;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class menu4 extends AppCompatActivity {

    private static final String SERVER_ADDRESS = "http://woman3.dothome.co.kr/w3";
    EditText menu4_EditText;
    Button menu4_plus,menu4_minus;
    String content="";
    InputMethodManager imm;

    ArrayList<String> BasketList;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu4);
        setCustomActionbar();

        BasketList = new ArrayList<String>();
        final ListView basket_list = (ListView) findViewById(R.id.basket_list);
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_multiple_choice, BasketList);
        basket_list.setAdapter(adapter);
        Basket();

        menu4_EditText = (EditText)findViewById(R.id.menu4_EditText);
        menu4_plus = (Button)findViewById(R.id.menu4_plus);
        menu4_minus = (Button)findViewById(R.id.menu4_minus);

        menu4_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content = menu4_EditText.getText().toString();
                try {
                    URL url = new URL(SERVER_ADDRESS + "/shopping_add.php?"
                            + "content=" + URLEncoder.encode(content, "UTF-8"));
                    //자료를 넘겨줄때 UTF-8로 설정해서 넘겨줌 리퀘스트방식으로!
                    url.openStream(); //php파일 열기.
                    String result = getXmlData("shopping_addresult.xml", "result"); //insert.php에서 DB입력 결과
                    // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                    if (result.equals("1")) { //DB입력이 성공되면!!
                        menu4_EditText.setText("");
                        BasketList.clear();
                        Basket();
                        Toast.makeText(getApplicationContext(), "등록!", Toast.LENGTH_SHORT).show();
                        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(menu4_EditText.getWindowToken(), 0);
                    } else {
                        Toast.makeText(getApplicationContext(), "등록 실패!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("Error : ", e.getMessage());
                }
            }

        });

        menu4_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String content = "";
                SparseBooleanArray checkedItems = basket_list.getCheckedItemPositions();
                int count = adapter.getCount() ;

                for (int i = count-1; i >= 0; i--) {
                    if (checkedItems.get(i)) {
                        content = BasketList.get(i).toString();
                        try {
                            URL url = new URL(SERVER_ADDRESS + "/shopping_del.php?"
                                    + "content=" + URLEncoder.encode(content, "UTF-8"));
                            //자료를 넘겨줄때 UTF-8로 설정해서 넘겨줌 리퀘스트방식으로!
                            url.openStream(); //php파일 열기.
                            String result = getXmlData("shopping_delresult.xml", "result"); //insert.php에서 DB입력 결과
                            // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                            if (result.equals("1")) { //DB입력이 성공되면!!
                                BasketList.remove(i) ;
                                BasketList.clear();
                                Basket();
                                menu4_EditText.setText("");
                                Toast.makeText(getApplicationContext(), "삭제!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "삭제 실패!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            Log.e("Error : ", e.getMessage());
                        }
                    }
                }
                // 모든 선택 상태 초기화.
                basket_list.clearChoices();

                adapter.notifyDataSetChanged();
            }


        });

    }

//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@//

    private void setCustomActionbar() {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        View mCustomView = LayoutInflater.from(this).inflate(R.layout.menu4_actionbar,null);
        actionBar.setCustomView(mCustomView);

        Toolbar parent = (Toolbar) mCustomView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(mCustomView, params);

        Button menu4_back = (Button) findViewById(R.id.menu4_back);

        menu4_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private String getXmlData(String filename, String str) { //태그값 하나를 받아오기위한 String형 함수

        String ret = "";

        try { //XML 파싱을 위한 과정
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            //XmlPullParserFactory의 인스턴스를 얻기위해  XmlPullParserFactory.newInstance()를 호출시킨다.
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            URL server = new URL( SERVER_ADDRESS + "/htdocs/" + filename);
            //parsing하고싶은 url를 셋팅!
            InputStream is = server.openStream();
            xpp.setInput(is, "UTF-8");

            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals(str)) { //태그 이름이 str 인자값과 같은 경우
                        ret = xpp.nextText();
                    }
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            Log.e("Error1", e.getMessage());
        }

        return ret;
    }

    private void Basket() {
        try {
            URL url = new URL(SERVER_ADDRESS+"/basket.php?");
            // php에서 $user_id = $_REQUEST['user_id']; 구문!! 리퀘스트방식으로 넣어주는것

            url.openStream();

            ArrayList<String> content = getXmlDataList("basketlist.xml", "content");


            if (content.isEmpty()) {

                Toast.makeText(menu4.this, "데이터 없음!!", Toast.LENGTH_SHORT).show();
            }
            else {
                for (int i = 0; i < content.size(); i++) {
                    String strcontent = content.get(i).toString();

                    BasketList.add(strcontent.toString());
                    adapter.notifyDataSetChanged();

                }
            }
        } catch (Exception e) {
            Toast.makeText(menu4.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private ArrayList<String> getXmlDataList(String filename, String str) { //태그값 여러개를 받아오기위한 ArrayList<string>형 변수

        ArrayList<String> ret = new ArrayList<String>();
        //최종 결과물을 반환할 변수!!

        try { //XML 파싱을 위한 과정
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            //XmlPullParserFactory의 인스턴스를 얻기위해  XmlPullParserFactory.newInstance()를 호출시킨다.
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            URL server = new URL("http://woman3.dothome.co.kr/w3/htdocs/" + filename);
            //parsing하고싶은 url를 셋팅!
            InputStream is = server.openStream();
            xpp.setInput(is, "UTF-8");
            //server url 내용을 UTF-8 로 셋팅해준다

            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals(str)) { //태그 이름이 str 인자값과 같은 경우
                        ret.add(xpp.nextText());
                        //str은 위에 넘겨준 태그이름!!! php보면 <name>$name</name> <id>$id</id> 부분!!
                        //태그안에 있는 내용을 ret에 셋팅한다!
                    }
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
        return ret; //ret반환해서보내줌
    }

}