package waman2.seyeongstar.se0star;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class menu1 extends AppCompatActivity {

    ArrayList<ExpiryListModel> ExpiryList;
    ExpiryListAdapter ExpirylistAdapter;
    final private Runnable ExpiryUI = new Runnable() {
        public void run() {
            menu1.this.ExpirylistAdapter.notifyDataSetChanged();
        }
    };

    Button menu1_reset;  //초기화 버튼

    private static final String SERVER_ADDRESS = "http://woman3.dothome.co.kr/w3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu1);
        setCustomActionbar();

        ExpiryList = new ArrayList<ExpiryListModel>();
        final ListView expirylist = (ListView) findViewById(R.id.expiry_list);
        ExpirylistAdapter = new ExpiryListAdapter(this, ExpiryList);
        expirylist.setAdapter(ExpirylistAdapter);
        ExpiryDays();

        menu1_reset = (Button)findViewById(R.id.menu1_reset);
        menu1_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Original_ExpiryDays();
                Toast.makeText(menu1.this, "초기화 완료", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@//
    private void ExpiryDays() {
        try {
            URL url = new URL(SERVER_ADDRESS+"/expiry.php?");
            // php에서 $user_id = $_REQUEST['user_id']; 구문!! 리퀘스트방식으로 넣어주는것

            url.openStream();

            ArrayList<String> index = getXmlDataList("expirylist.xml", "index");
            ArrayList<String> category = getXmlDataList("expirylist.xml", "category");
            ArrayList<String> days = getXmlDataList("expirylist.xml", "days");

            if (index.isEmpty()) {

                Toast.makeText(menu1.this, "데이터 없음!!", Toast.LENGTH_SHORT).show();
            }
            else {
                for (int i = 0; i < index.size(); i++) {
                    String strindex = index.get(i).toString();
                    String strkind = category.get(i).toString();
                    String strday = days.get(i).toString();

                    ExpiryListModel item = new ExpiryListModel(strindex, strkind, strday);

                    this.expiryitem(item);

                }
            }
        } catch (Exception e) {
            Toast.makeText(menu1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void Original_ExpiryDays() {

        ExpiryList.clear();

        try {
            URL url = new URL(SERVER_ADDRESS+"/expiry_original.php?");
            // php에서 $user_id = $_REQUEST['user_id']; 구문!! 리퀘스트방식으로 넣어주는것

            url.openStream();

            ArrayList<String> index = getXmlDataList("expiry_originalresult.xml", "index");
            ArrayList<String> category = getXmlDataList("expiry_originalresult.xml", "category");
            ArrayList<String> days = getXmlDataList("expiry_originalresult.xml", "days");

            if (index.isEmpty()) {
                Toast.makeText(menu1.this, "데이터 없음!!", Toast.LENGTH_SHORT).show();
            }
            else {
                for (int i = 0; i < index.size(); i++) {
                    String strindex = index.get(i).toString();
                    String strkind = category.get(i).toString();
                    String strday = days.get(i).toString();

                    ExpiryListModel item = new ExpiryListModel(strindex, strkind, strday);

                    this.expiryitem(item);
                }
            }
        } catch (Exception e) {
            Toast.makeText(menu1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void expiryitem(ExpiryListModel item){
        this.ExpiryList.add(item);
        this.runOnUiThread(ExpiryUI);
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

    private void setCustomActionbar() {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        View mCustomView = LayoutInflater.from(this).inflate(R.layout.menu1_actionbar,null);
        actionBar.setCustomView(mCustomView);

        Toolbar parent = (Toolbar) mCustomView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(mCustomView, params);

        Button menu1_back = (Button) findViewById(R.id.menu1_back);

        menu1_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}