package waman2.seyeongstar.se0star;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class menu2 extends AppCompatActivity {

    private static final String SERVER_ADDRESS = "http://woman3.dothome.co.kr/w3";

    EditText menu2_editText;
    Button menu2_button, menu2_reset;
    TextView menu2_none;

    ArrayList<ExpirySearchModel> List;
    ListView list;
    ExpirySearchAdapter listAdapter;
    final private Runnable ListUI = new Runnable() {
        public void run() {
            menu2.this.listAdapter.notifyDataSetChanged();
        }
    };

    int none = 0;
    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu2);
        setCustomActionbar();

        menu2_button = (Button) findViewById(R.id.menu2_button);
        menu2_editText = (EditText) findViewById(R.id.menu2_editText);
        menu2_none = (TextView) findViewById(R.id.menu2_none);
        menu2_reset = (Button) findViewById(R.id.menu2_reset);

        List = new ArrayList<ExpirySearchModel>();
        list = (ListView) findViewById(R.id.list);
        listAdapter = new ExpirySearchAdapter(this, List);
        list.setAdapter(listAdapter);

        ModifyList_all();

        menu2_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu2_none.setVisibility(View.INVISIBLE);
                list.setVisibility(View.VISIBLE);
                int day = Integer.parseInt(menu2_editText.getText().toString());
                List.clear();
                ModifyList(day);
                menu2_reset.setVisibility(View.VISIBLE);
                imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(menu2_editText.getWindowToken(), 0);
            }
        });

        menu2_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List.clear();
                ModifyList_all();
                menu2_reset.setVisibility(View.GONE);
                menu2_none.setVisibility(View.INVISIBLE);
                list.setVisibility(View.VISIBLE);
                menu2_editText.setText("");
            }
        });
    }

    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@//

    private void ModifyList_all() {

        try {
            URL url = new URL(SERVER_ADDRESS + "/foodfind2.php");
            url.openStream(); //서버의 serarch.php파일을 실행함

            ArrayList<String> name = getXmlDataList("searchresult2.xml", "name");
            ArrayList<String> num = getXmlDataList("searchresult2.xml", "num");
            ArrayList<String> unit = getXmlDataList("searchresult2.xml", "unit");
            ArrayList<String> startday = getXmlDataList("searchresult2.xml", "startday");
            ArrayList<String> finalday = getXmlDataList("searchresult2.xml", "finalday");

            if (name.isEmpty()) {
            }
            else {
                for (int i = 0; i < name.size(); i++) {
                    String strname = name.get(i);
                    String strunit  = unit.get(i);
                    String strnum  = num.get(i);
                    String strstartday  = startday.get(i);
                    String strfinalday  = finalday.get(i);

                    int diff = diffOfDate(getDate2(), strfinalday);
                    ExpirySearchModel item = new ExpirySearchModel(strname,strnum,strunit,diff,strfinalday);
                    this.modifyitem(item);
                }
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }

    private void ModifyList(int day) {

        try {
            URL url = new URL(SERVER_ADDRESS + "/foodfind2.php");
            url.openStream(); //서버의 serarch.php파일을 실행함

            ArrayList<String> name = getXmlDataList("searchresult2.xml", "name");
            ArrayList<String> num = getXmlDataList("searchresult2.xml", "num");
            ArrayList<String> unit = getXmlDataList("searchresult2.xml", "unit");
            ArrayList<String> finalday = getXmlDataList("searchresult2.xml", "finalday");

            if (name.isEmpty()) {
            }
            else {
                for (int i = 0; i < name.size(); i++) {
                    String strname = name.get(i);
                    String strunit  = unit.get(i);
                    String strnum  = num.get(i);
                    String strfinalday  = finalday.get(i);

                    int diff = diffOfDate(getDate2(), strfinalday);

                    if (diff == day)
                    {
                        ExpirySearchModel item = new ExpirySearchModel(strname,strnum,strunit,diff,strfinalday);
                        this.modifyitem(item);
                        none = 1;
                    }
                }
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

        if(none == 0){
            list.setVisibility(View.INVISIBLE);
            menu2_none.setVisibility(View.VISIBLE);
        }
        else if (none == 1)
            none = 0;
    }

    private void modifyitem(ExpirySearchModel item){
        this.List.add(item);
        this.runOnUiThread(ListUI);
    }

    public String getDate2() {
        Calendar temp=Calendar.getInstance ( );
        StringBuffer sbDate=new StringBuffer ( );

        int nYear = temp.get ( Calendar.YEAR );
        int nMonth = temp.get ( Calendar.MONTH ) + 1;
        int nDay = temp.get ( Calendar.DAY_OF_MONTH );

        sbDate.append ( nYear );
        sbDate.append("-");
        if ( nMonth < 10 )
            sbDate.append ( "0" );
        sbDate.append ( nMonth );
        sbDate.append("-");
        if ( nDay < 10 )
            sbDate.append ( "0" );
        sbDate.append ( nDay );

        return sbDate.toString ( );
    }

    public static int diffOfDate(String begin, String end) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Date beginDate = formatter.parse(begin);
        Date endDate = formatter.parse(end);

        long diff = endDate.getTime() - beginDate.getTime();
        int diffDays = (int) diff / (24 * 60 * 60 * 1000);

        return diffDays;
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

        View mCustomView = LayoutInflater.from(this).inflate(R.layout.menu2_actionbar,null);
        actionBar.setCustomView(mCustomView);

        Toolbar parent = (Toolbar) mCustomView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(mCustomView, params);

        Button menu2_back = (Button) findViewById(R.id.menu2_back);

        menu2_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}