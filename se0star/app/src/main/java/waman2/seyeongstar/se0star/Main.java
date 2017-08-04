package waman2.seyeongstar.se0star;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;  //시스템 디폴트
    LinearLayout homeLayout;  //시스템 디폴트
    InputMethodManager imm; // 키패트 상태 조절
    int delete= 0; // 다른 탭의 상태값
    int chart = 0; // 다른 탭의 상태값
    private int CurYear, CurMonth, CurDay; //현재날짜
    //----------------------------------------------------------------홈
    TextView home_more; //홈탭의 공지사항 밑의 더보기
    TextView homenotification; //홈탭의 공지사항 - 음식이름
    View list4_line; //홈탭의 공지사항 더보기 누르면 나오는 라인(View)
    private PieChart pieChart;
    private float[] yData = new float[7];
    private String[] xData = {"육류","채소류","과일","냉동식품","해산물","반찬","유제품"};
    //----------------------------------------------------------------등록
    int[] arrday = new int[10]; //등록탭의 Dialog에서 Sppiner로 kind고르면 유통기한 자동설정 해줄 때 필요한 유통기한 배열
    String[] arrkind = new String[10];
    Button dataAddButton, addTabAddButton; //Main에 있는 버튼
    EditText addFoodName,addFoodNum,addFoodUnit,addFoodKind, addTabEditText; //Dialog에 있는 EditText
    Button addFoodStart,addFoodFinal, addFoodnumsButton, deleteFoodnumsButton; //Dialog에 있는 Button
    TextView foodstart,foodfinal, foodaddTextView; //Dialog에 있는 TextView
    private DatePickerDialog.OnDateSetListener startDateSetListner, finalDateSetListener; //Dialog에 있는 DatePicker
    //----------------------------------------------------------------추가/삭제
    //----------------------------------------------------------------목록
    ArrayAdapter<String> list_adapter;  //AutoComplete할 ArrayAdapter
    AutoCompleteTextView listTabSearchText;  //AutoComplete 검색 EditText
    EditText listFoodName,listFoodNum,listFoodUnit,listFoodKind; //목록 Dialog
    Button listFoodStart,listFoodFinal; //목록 Dialog
    Button listaddFoodnumsButton, listdeleteFoodnumsButton;
    TextView listfoodstart,listfoodfinal;  //목록 Dialog
    Button listTabSearchButton,listTabBackButton; // 목록 Main에 있는 버튼
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    static final int MENU_LIST = 1;

    //++++++++++++++++++++++++++++++++
    ArrayList<ListModel> List;
    ListAdapter listAdapter;
    final private Runnable ListUI = new Runnable() {
        public void run() {
            Main.this.listAdapter.notifyDataSetChanged();
        }
    };
    //-------------------------------
    ArrayList<AddListModel> AddList;  //추가삭제 탭
    AddListAdapter AddlistAdapter;
    final private Runnable AddUI = new Runnable() {
        public void run() {
            Main.this.AddlistAdapter.notifyDataSetChanged();
        }
    };
    //-------------------------------
    ArrayList<RegistListModel> RegistList;  //등록 탭
    RegistListAdapter RegistlistAdapter;
    //-------------------------------   // 홈탭의 더보기의 유통기한 순위 5개
    ArrayList<RankingListModel> RankingList;
    RankingListAdapter RankinglistAdapter;
    final private Runnable RankUI = new Runnable() {
        public void run() {
            Main.this.RankinglistAdapter.notifyDataSetChanged();
        }
    };
    //++++++++++++++++++++++++++++++++

    private static final String SERVER_ADDRESS = "http://woman3.dothome.co.kr/w3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy); //권한을 우리가 가질수 있도록함.
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        chart(); // 차트에 넣을 음식 종류 불러오는 함수 Ydata[]에 저장
        ExpiryDays(); // 유통기한(일수) 불러오는 함수 arrday[]에 저장

        //---------------------------------------------------------------------------------------------------------탭호스트
        final TabHost host = (TabHost) findViewById(R.id.tabHost);
        host.setup();
        //
        TabHost.TabSpec spec = host.newTabSpec("tab1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("홈");
        host.addTab(spec);
        //
        spec = host.newTabSpec("tab2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("등  록");
        host.addTab(spec);
        //
        spec = host.newTabSpec("tab3");
        spec.setContent(R.id.tab3);
        spec.setIndicator("추가/삭제");
        host.addTab(spec);
        //
        spec = host.newTabSpec("tab4");
        spec.setContent(R.id.tab4);
        spec.setIndicator("목  록");
        //
        host.addTab(spec);
        host.getTabWidget().getChildAt(host.getCurrentTab()).setBackgroundResource(R.drawable.tabchanged);
        host.getTabWidget().setBackgroundColor(Color.WHITE);
        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                for (int i = 0; i < host.getTabWidget().getChildCount(); i++) {
                    host.getTabWidget().getChildAt(i).setBackgroundColor(Color.WHITE);
                }
                switch (host.getCurrentTab()){
                    case 0 :
                        if(chart == 1) {
                            RankingList.clear();
                            ExpiryDays_five();
                            chart = 0;
                        }
                        break;
                    case 1 :
                        break;
                    case 2 :
                        if(delete == 1) {
                            AddList.clear();
                            AddList();
                            delete = 0;
                        }
                        break;
                    case 3 :
                        if(AddlistAdapter.str == 1) {
                            List.clear();
                            ModifyList();
                            AddlistAdapter.str = 0;
                        }
                        break;
                }
                host.getTabWidget().getChildAt(host.getCurrentTab()).setBackgroundResource(R.drawable.tabchanged);
            }
        });
        //-----------------------------------------------------------------------------------------------------------네비게이션드로어
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                homeLayout = (LinearLayout) findViewById(R.id.homeLayout);

                drawer.closeDrawers();

                switch (menuItem.getItemId()) {
                    case R.id.nav_menu1:
                        Intent menu1 = new Intent(Main.this, menu1.class);
                        startActivity(menu1);
                        menuItem.setCheckable(false);
                        return true;
                    case R.id.nav_menu2:
                        Intent menu2 = new Intent(Main.this, menu2.class);
                        startActivity(menu2);
                        menuItem.setCheckable(false);
                        return true;
                    case R.id.nav_menu3:
                        Intent menu3 = new Intent(Main.this, menu3.class);
                        startActivity(menu3);
                        menuItem.setCheckable(false);

                        return true;
                    case R.id.nav_menu4:
                        Intent menu4 = new Intent(Main.this, menu4.class);
                        startActivity(menu4);
                        menuItem.setCheckable(false);
                        return true;
                }
                return true;
            }
        });
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        pieChart = (PieChart) findViewById(R.id.chart);
        pieChart.setUsePercentValues(true);
        pieChart.setDescription(" ");
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(30);
        pieChart.setTransparentCircleRadius(35);
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);
        addData();
        Legend l = pieChart.getLegend();
        l.setEnabled(false);
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 리스트뷰
        List = new ArrayList<ListModel>();
        final ListView list = (ListView) findViewById(R.id.list);
        listAdapter = new ListAdapter(this, List);
        list.setAdapter(listAdapter);
        ModifyList();
        registerForContextMenu(list);

        AddList = new ArrayList<AddListModel>();
        final ListView addlist = (ListView) findViewById(R.id.list2);
        AddlistAdapter = new AddListAdapter(this, AddList);
        addlist.setAdapter(AddlistAdapter);
        AddList();

        RegistList = new ArrayList<RegistListModel>();
        final ListView registlist = (ListView) findViewById(R.id.list3);
        RegistlistAdapter = new RegistListAdapter(this, RegistList);
        registlist.setAdapter(RegistlistAdapter);

        RankingList = new ArrayList<RankingListModel>();
        final ListView rankinglist = (ListView) findViewById(R.id.list4);
        RankinglistAdapter = new RankingListAdapter(this, RankingList);
        rankinglist.setAdapter(RankinglistAdapter);
        ExpiryDays_five();
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        home_more = (TextView) findViewById(R.id.home_more);
        list4_line = (View) findViewById(R.id.list4_line);
        home_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rankinglist.getVisibility() == View.GONE) {
                    rankinglist.setVisibility(View.VISIBLE);
                    list4_line.setVisibility(View.VISIBLE);
                }
                else {
                    rankinglist.setVisibility(View.GONE);
                    list4_line.setVisibility(View.GONE);
                }
            }
        });
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        long now = System.currentTimeMillis();    //현재날짜
        Date date = new Date(now);
        SimpleDateFormat CurYearFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat CurMonthFormat = new SimpleDateFormat("MM");
        final SimpleDateFormat CurDayFormat = new SimpleDateFormat("dd");
        CurYear = Integer.parseInt(CurYearFormat.format(date));
        CurMonth = Integer.parseInt(CurMonthFormat.format(date))-1;
        CurDay = Integer.parseInt(CurDayFormat.format(date));
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 스피너(정렬)

        Spinner spinner = (Spinner) findViewById(R.id.sort);
        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(this,R.array.sort_array,android.R.layout.simple_spinner_item);

        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(sortAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                //String pos1 = String.valueOf(pos);
                //Toast.makeText(getApplicationContext(),pos1,Toast.LENGTH_LONG).show();
                switch (pos) {
                    case 0:
                        List.clear();
                        ModifyList();
                        break;
                    case 1:
                        List.clear();
                        ModifyList_name();
                        break;
                    case 2:
                        List.clear();
                        ModifyList_kind();
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}});
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        addTabAddButton = (Button)findViewById(R.id.addTabAddButton);
        addTabAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context addFood = Main.this;
                AlertDialog.Builder builder;
                AlertDialog dialog;
                LayoutInflater inflater = (LayoutInflater) addFood.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.add_food, (ViewGroup) findViewById(R.id.add_food_layout));

                addFoodName = (EditText) layout.findViewById(R.id.addFoodName);
                addFoodNum = (EditText) layout.findViewById(R.id.addFoodNum);
                addFoodUnit = (EditText) layout.findViewById(R.id.addFoodUnit);
                addFoodKind = (EditText) layout.findViewById(R.id.addFoodKind);
                addFoodStart = (Button) layout.findViewById(R.id.addFoodStart);
                addFoodFinal = (Button) layout.findViewById(R.id.addFoodFinal);
                foodstart = (TextView) layout.findViewById(R.id.foodstart);
                foodfinal = (TextView) layout.findViewById(R.id.foodfinal);
                addTabEditText = (EditText) findViewById(R.id.addTabEditText);
                foodaddTextView = (TextView) findViewById(R.id.foodaddTextView);
                addFoodnumsButton = (Button) layout.findViewById(R.id.addFoodnumsButton);
                deleteFoodnumsButton = (Button) layout.findViewById(R.id.deleteFoodnumsButton);

                Spinner spinner2 = (Spinner) layout.findViewById(R.id.spinner_addFoodKind);
                ArrayAdapter<CharSequence> kindAdapter = ArrayAdapter.createFromResource(Main.this,R.array.kind_spinner,android.R.layout.simple_spinner_item);

                kindAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(kindAdapter);

                try {
                    Field popup = Spinner.class.getDeclaredField("mPopup");
                    popup.setAccessible(true);

                    android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner2);

                    popupWindow.setHeight(800);
                }
                catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) { }

                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        switch (pos)
                        {
                            case 0:
                                addFoodKind.setText("육류");
                                addFoodUnit.setText("g");
                                foodfinal.setText(getDate(arrday[pos]));
                                break;
                            case 1:
                                addFoodKind.setText("채소류");
                                addFoodUnit.setText("개");
                                foodfinal.setText(getDate(arrday[pos]));
                                break;
                            case 2:
                                addFoodKind.setText("과일");
                                addFoodUnit.setText("개");
                                foodfinal.setText(getDate(arrday[pos]));
                                break;
                            case 3:
                                addFoodKind.setText("냉동식품");
                                addFoodUnit.setText("개");
                                foodfinal.setText(getDate(arrday[pos]));
                                break;
                            case 4:
                                addFoodKind.setText("해산물");
                                addFoodUnit.setText("g");
                                foodfinal.setText(getDate(arrday[pos]));
                                break;
                            case 5:
                                addFoodKind.setText("나물반찬");
                                addFoodUnit.setText("그릇");
                                foodfinal.setText(getDate(arrday[pos]));
                                break;
                            case 6:
                                addFoodKind.setText("마른반찬");
                                addFoodUnit.setText("그릇");
                                foodfinal.setText(getDate(arrday[pos]));
                                break;
                            case 7:
                                addFoodKind.setText("기타반찬");
                                addFoodUnit.setText("그릇");
                                foodfinal.setText(getDate(arrday[pos]));
                                break;
                            case 8:
                                addFoodKind.setText("유제품");
                                addFoodUnit.setText("개");
                                foodfinal.setText(getDate(arrday[pos]));
                                break;
                            case 9:
                                addFoodKind.setText("기타");
                                addFoodUnit.setText("개");
                                foodfinal.setText(getDate(arrday[pos]));
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });

                foodstart.setText(CurYear + "-" + (CurMonth + 1) + "-" + CurDay);
                addFoodName.setText(addTabEditText.getText().toString());

                builder = new AlertDialog.Builder(addFood);
                builder.setView(layout);
                dialog = builder.create();
                dialog.setTitle("음식 등록");

                addFoodnumsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int i = Integer.valueOf(addFoodNum.getText().toString());
                        i++;
                        addFoodNum.setText(String.valueOf(i));
                    }
                });

                deleteFoodnumsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int i = Integer.valueOf(addFoodNum.getText().toString());
                        i--;
                        addFoodNum.setText(String.valueOf(i));
                    }
                });

                addFoodStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new DatePickerDialog(Main.this, startDateSetListner, CurYear, CurMonth, CurDay).show();
                    }
                });

                addFoodFinal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new DatePickerDialog(Main.this, finalDateSetListener, CurYear, CurMonth, CurDay).show();
                    }
                });

                startDateSetListner = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        String msg = String.format("%d / %d / %d", year, monthOfYear+1, dayOfMonth);
                        Toast.makeText(Main.this, msg, Toast.LENGTH_SHORT).show();
                        foodstart.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                };

                finalDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        String msg = String.format("%d / %d / %d", year, monthOfYear+1, dayOfMonth);
                        Toast.makeText(Main.this, msg, Toast.LENGTH_SHORT).show();
                        foodfinal.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth );
                    }
                };

                dialog.setButton("등록", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (addFoodKind.getText().toString().equals("")
                                        || addFoodName.getText().toString().equals("")
                                        || addFoodNum.getText().toString().equals("")
                                        || addFoodUnit.getText().toString().equals("")
                                        || foodstart.getText().toString().equals("")
                                        || foodfinal.getText().toString().equals("")) {
                                    Toast.makeText(getApplicationContext(), "입력오류입니다.", Toast.LENGTH_SHORT).show();
                                    return;
                                }   // 하나라도 빈칸이 있으면 입력오류

                                String food_kind = addFoodKind.getText().toString();
                                String food_num = addFoodNum.getText().toString();
                                String food_unit = addFoodUnit.getText().toString();
                                String food_name = addFoodName.getText().toString();
                                String start_day = foodstart.getText().toString();
                                String final_day = foodfinal.getText().toString();   // 나중에 DB에 보내주기 때문에 내용들을 String 형식으로 저장

                                RegistList.add(new RegistListModel(food_kind, food_name, food_num, food_unit, start_day, final_day));  //RegistLostModel에 저장(레지스트 리스트 모델)

                                foodaddTextView.setVisibility(View.INVISIBLE);  //음식을 추가해주세요 글씨 사라짐

                                registlist.setAdapter(RegistlistAdapter);
                                addTabEditText.setText("");  //EditText의 글씨를 지워줌
                            }
                        }

                );
                dialog.show();
            }
        });
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        dataAddButton = (Button) findViewById(R.id.dataAddButton);
        dataAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //이부분 주석처리
                if (RegistlistAdapter.isEmpty()) {
                } else {
                    for (int i = 0; i < RegistlistAdapter.getCount(); i++) {

                        String food_kind = RegistList.get(i).getRegist_list_kind().toString();
                        String food_num = RegistList.get(i).getRegist_list_num().toString();
                        String food_unit = RegistList.get(i).getRegist_unit().toString();
                        String food_name = RegistList.get(i).getRegist_list_name().toString();
                        String start_day = RegistList.get(i).getRegist_list_startday().toString();
                        String final_day = RegistList.get(i).getRegist_list_finalday().toString();

                        try {
                            URL url = new URL(SERVER_ADDRESS + "/foodInsert.php?"
                                    + "food_name=" + URLEncoder.encode(food_name, "UTF-8")
                                    + "&food_num=" + URLEncoder.encode(food_num, "UTF-8")
                                    + "&food_unit=" + URLEncoder.encode(food_unit, "UTF-8")
                                    + "&food_kind=" + URLEncoder.encode(food_kind, "UTF-8")
                                    + "&start_day=" + URLEncoder.encode(start_day, "UTF-8")
                                    + "&final_day=" + URLEncoder.encode(final_day, "UTF-8"));

                            url.openStream();
                            String result = getXmlData("insertresult.xml", "result");
                            if (result.equals("1")) {
                                addFoodName.setText("");
                                addFoodNum.setText("");
                                addFoodUnit.setText("");
                                foodstart.setText("");
                                foodfinal.setText("");
                                addTabEditText.setText("");
                            } else {
                                Toast.makeText(getApplicationContext(), "등록 실패!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e("Error : ", e.getMessage());
                        }
                    }
                    Toast.makeText(getApplicationContext(), "등록완료", Toast.LENGTH_SHORT).show();
                    RegistList.clear();
                    RegistlistAdapter.notifyDataSetInvalidated();
                    AddList.clear();
                    AddList();
                    List.clear();
                    ModifyList();
                    foodaddTextView.setVisibility(View.VISIBLE);
                    chart = 1;
                    chart();
                    addData();
                }
            }

        });
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        listTabSearchButton = (Button)findViewById(R.id.listTabSearchButton);
        listTabSearchText = (AutoCompleteTextView)findViewById(R.id.listTabSearchText);
        listTabSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List_Search();
                imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(listTabSearchText.getWindowToken(), 0);
            }
        });
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ ListTab Search
        listTabBackButton = (Button)findViewById(R.id.listTabBackButton);
        listTabBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List.clear();
                ModifyList();
                listTabBackButton.setVisibility(View.GONE);
                listTabSearchText.setText("");
            }
        });
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    }

    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@//

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return true;
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

    private void ExpiryDays() {
        try {
            URL url = new URL(SERVER_ADDRESS+"/expiry.php?");

            url.openStream();

            ArrayList<String> kind = getXmlDataList("expirylist.xml", "category");
            ArrayList<String> days = getXmlDataList("expirylist.xml", "days");

            if (days.isEmpty()) {
                Toast.makeText(Main.this, "데이터 없음!!", Toast.LENGTH_SHORT).show();
            }
            else {
                for (int i = 0; i < days.size(); i++) {
                    arrkind[i] = kind.get(i);
                    arrday[i] = Integer.parseInt(days.get(i));
                }
            }
        } catch (Exception e) {
            Toast.makeText(Main.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void ExpiryDays_five() {
        try {
            URL url = new URL(SERVER_ADDRESS + "/expiryfive.php");
            url.openStream(); //서버의 serarch.php파일을 실행함

            ArrayList<String> kind = getXmlDataList("expirylistfive.xml", "kind");
            ArrayList<String> name = getXmlDataList("expirylistfive.xml", "name");
            ArrayList<String> num = getXmlDataList("expirylistfive.xml", "num");
            ArrayList<String> unit = getXmlDataList("expirylistfive.xml", "unit");
            ArrayList<String> finalday = getXmlDataList("expirylistfive.xml", "finalday");

            homenotification = (TextView) findViewById(R.id.homenotification);
            homenotification.setText(name.get(0).toString());

            if (name.isEmpty()) {
                Toast.makeText(Main.this, "데이터 없음!!", Toast.LENGTH_SHORT).show();
            } else {
                for (int i = 0; i < name.size(); i++) {
                    String strkind = kind.get(i);
                    String strname = name.get(i);
                    String strnum = num.get(i);
                    String strunit = unit.get(i);
                    String strfinalday = finalday.get(i);
                    String strstartday = getDate2();
                    long dif = diffOfDate(strstartday,strfinalday);

                    String difference = String.valueOf(dif);
                    RankingListModel item = new RankingListModel(strkind, strname, strnum, strunit, strfinalday, difference);

                    this.rankingitem(item);
                }
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }

    private void rankingitem(RankingListModel item){
        this.RankingList.add(item);
        this.runOnUiThread(RankUI);
    }

    private void chart() {
        try {
            URL url = new URL(SERVER_ADDRESS+"/kind_row.php");

            url.openStream();

            ArrayList<String> kind = getXmlDataList("kind_rowresult.xml","kind");

            if (kind.isEmpty()) {
                Toast.makeText(Main.this, "데이터 없음!!", Toast.LENGTH_SHORT).show();
            } else {
                for (int i = 0; i < kind.size(); i++) {
                    yData[i] = Float.parseFloat(kind.get(i));
                }
            }
        } catch (Exception e) {
            Toast.makeText(Main.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void List_Search() {

        List.clear();

        String food_name =listTabSearchText.getText().toString();

        try {
            URL url = new URL(SERVER_ADDRESS+"/search.php?"
                    + "food_name=" + URLEncoder.encode(food_name, "UTF-8"));
            // php에서 $user_id = $_REQUEST['user_id']; 구문!! 리퀘스트방식으로 넣어주는것

            url.openStream();

            ArrayList<String> name = getXmlDataList("searchlist.xml", "name");
            ArrayList<String> num = getXmlDataList("searchlist.xml", "num");
            ArrayList<String> unit = getXmlDataList("searchlist.xml", "unit");
            ArrayList<String> kind = getXmlDataList("searchlist.xml", "kind");
            ArrayList<String> startday = getXmlDataList("searchlist.xml", "startday");
            ArrayList<String> finalday = getXmlDataList("searchlist.xml", "finalday");

            if (name.isEmpty()) {

                Toast.makeText(Main.this, "데이터 없음!!", Toast.LENGTH_SHORT).show();
            }
            else {
                for (int i = 0; i < name.size(); i++) {
                    String strname = name.get(i);
                    String strkind = kind.get(i);
                    String strunit  = unit.get(i);
                    String strnum  = num.get(i);
                    String strstartday  = startday.get(i);
                    String strfinalday  = finalday.get(i);
                    int strindex =i+1;

                    ListModel item = new ListModel(strkind, strname,strnum,strstartday,strfinalday,strunit,strindex);

                    this.modifyitem(item);
                }
            }
        } catch (Exception e) {
            Toast.makeText(Main.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        listTabBackButton.setVisibility(View.VISIBLE);
    }

    private void ModifyList() {

        try {
            URL url = new URL(SERVER_ADDRESS + "/foodfind.php");
            url.openStream(); //서버의 serarch.php파일을 실행함

            ArrayList<String> name = getXmlDataList("searchresult.xml", "name");
            ArrayList<String> num = getXmlDataList("searchresult.xml", "num");
            ArrayList<String> unit = getXmlDataList("searchresult.xml", "unit");
            ArrayList<String> kind = getXmlDataList("searchresult.xml", "kind");
            ArrayList<String> startday = getXmlDataList("searchresult.xml", "startday");
            ArrayList<String> finalday = getXmlDataList("searchresult.xml", "finalday");

             list_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, name);
            listTabSearchText.setAdapter(list_adapter);

            if (name.isEmpty()) {
            }
            else {
                for (int i = 0; i < name.size(); i++) {
                    String strname = name.get(i);
                    String strkind = kind.get(i);
                    String strunit  = unit.get(i);
                    String strnum  = num.get(i);
                    String strstartday  = startday.get(i);
                    String strfinalday  = finalday.get(i);
                    int strindex =i+1;

                    ListModel item = new ListModel(strkind, strname,strnum,strstartday,strfinalday,strunit,strindex);

                    this.modifyitem(item);
                }
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }

    private void ModifyList_name() {

        try {
            URL url = new URL(SERVER_ADDRESS + "/foodfind_name.php");
            url.openStream(); //서버의 serarch.php파일을 실행함

            ArrayList<String> name = getXmlDataList("searchresult.xml", "name");
            ArrayList<String> num = getXmlDataList("searchresult.xml", "num");
            ArrayList<String> unit = getXmlDataList("searchresult.xml", "unit");
            ArrayList<String> kind = getXmlDataList("searchresult.xml", "kind");
            ArrayList<String> startday = getXmlDataList("searchresult.xml", "startday");
            ArrayList<String> finalday = getXmlDataList("searchresult.xml", "finalday");

            if (name.isEmpty()) {}
            else {
                for (int i = 0; i < name.size(); i++) {
                    String strname = name.get(i);
                    String strkind = kind.get(i);
                    String strunit  = unit.get(i);
                    String strnum  = num.get(i);
                    String strstartday  = startday.get(i);
                    String strfinalday  = finalday.get(i);
                    int strindex =i+1;

                    ListModel item = new ListModel(strkind, strname,strnum,strstartday,strfinalday,strunit,strindex);

                    this.modifyitem(item);
                }
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }

    private void ModifyList_kind() {

        try {
            URL url = new URL(SERVER_ADDRESS + "/foodfind_kind.php");
            url.openStream(); //서버의 serarch.php파일을 실행함

            ArrayList<String> name = getXmlDataList("searchresult.xml", "name");
            ArrayList<String> num = getXmlDataList("searchresult.xml", "num");
            ArrayList<String> unit = getXmlDataList("searchresult.xml", "unit");
            ArrayList<String> kind = getXmlDataList("searchresult.xml", "kind");
            ArrayList<String> startday = getXmlDataList("searchresult.xml", "startday");
            ArrayList<String> finalday = getXmlDataList("searchresult.xml", "finalday");

            if (name.isEmpty()) {
            }
            else {
                for (int i = 0; i < name.size(); i++) {
                    String strname = name.get(i);
                    String strkind = kind.get(i);
                    String strunit  = unit.get(i);
                    String strnum  = num.get(i);
                    String strstartday  = startday.get(i);
                    String strfinalday  = finalday.get(i);
                    int strindex =i+1;

                    ListModel item = new ListModel(strkind, strname,strnum,strstartday,strfinalday,strunit,strindex);

                    this.modifyitem(item);
                }

            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }

    private void modifyitem(ListModel item){
        this.List.add(item);
        this.runOnUiThread(ListUI);
    }

    private void AddList() {

        try {
            URL url = new URL(SERVER_ADDRESS + "/foodfind.php");
            url.openStream(); //서버의 serarch.php파일을 실행함

            ArrayList<String> name = getXmlDataList("searchresult.xml", "name");
            ArrayList<String> num = getXmlDataList("searchresult.xml", "num");
            ArrayList<String> unit = getXmlDataList("searchresult.xml", "unit");
            ArrayList<String> kind = getXmlDataList("searchresult.xml", "kind");
            ArrayList<String> startday = getXmlDataList("searchresult.xml", "startday");
            ArrayList<String> finalday = getXmlDataList("searchresult.xml", "finalday");

            if (name.isEmpty()) {
            }
            else {
                for (int i = 0; i < name.size(); i++) {
                    String strname = name.get(i);
                    String strkind = kind.get(i);
                    String strunit  = unit.get(i);
                    String strnum  = num.get(i);
                    String strstartday  = startday.get(i);
                    String strfinalday  = finalday.get(i);

                    AddListModel item = new AddListModel(strkind, strname,strnum,strunit,strstartday,strfinalday);

                    this.Additem(item);
                }
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }

    private void Additem(AddListModel item){
        this.AddList.add(item);
        this.runOnUiThread(AddUI);

    }

    public void onCreateContextMenu(ContextMenu menu, View v,  ContextMenu.ContextMenuInfo menuInfo) {   //수정/삭제 menu부분
        // TODO Auto-generated method stub

        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(MENU_LIST, R.id.modify, Menu.NONE, "수정");
        menu.add(MENU_LIST, R.id.delete, Menu.NONE, "삭제");
    }

    public boolean onContextItemSelected(MenuItem item) {
        if(item.getGroupId()==MENU_LIST) {
            Context listfood = Main.this;
            AlertDialog.Builder builder;
            AlertDialog dialog;
            LayoutInflater inflater = (LayoutInflater) listfood.getSystemService(LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.list_food, (ViewGroup) findViewById(R.id.list_food_layout));
            listFoodName = (EditText) layout.findViewById(R.id.listFoodName);
            listFoodNum = (EditText) layout.findViewById(R.id.listFoodNum);
            listFoodUnit = (EditText) layout.findViewById(R.id.listFoodUnit);
            listFoodKind = (EditText) layout.findViewById(R.id.listFoodKind);
            listfoodstart = (TextView) layout.findViewById(R.id.listfoodstart);
            listfoodfinal = (TextView) layout.findViewById(R.id.listfoodfinal);
            listFoodStart = (Button) layout.findViewById(R.id.listFoodStart);
            listFoodFinal = (Button) layout.findViewById(R.id.listFoodFinal);
            listaddFoodnumsButton = (Button) layout.findViewById(R.id.listaddFoodnumsButton);
            listdeleteFoodnumsButton = (Button) layout.findViewById(R.id.listdeleteFoodnumsButton);

            AdapterView.AdapterContextMenuInfo pos= (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
            int index  = pos.position;
            switch (item.getItemId()) {

                case R.id.modify:
                    builder = new AlertDialog.Builder(listfood);
                    builder.setView(layout);
                    dialog = builder.create();
                    dialog.setTitle("수정하기");

                    listFoodName.setText(List.get(index).getModify_list_name().toString());
                    listFoodNum.setText(List.get(index).getModify_list_num().toString());
                    listFoodKind.setText(List.get(index).getModify_list_kind().toString());
                    listFoodUnit.setText(List.get(index).getunit().toString());
                    listfoodstart.setText(List.get(index).getModify_list_startday().toString());
                    listfoodfinal.setText(List.get(index).getModify_list_finalday().toString());

                    final String spinnerstr = listFoodKind.getText().toString();

                    Spinner spinner3 = (Spinner) layout.findViewById(R.id.spinner_listFoodKind);
                    ArrayAdapter<CharSequence> kindAdapter = ArrayAdapter.createFromResource(Main.this,R.array.kind_spinner,android.R.layout.simple_spinner_item);

                    kindAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(kindAdapter);

                    try {
                        Field popup = Spinner.class.getDeclaredField("mPopup");
                        popup.setAccessible(true);

                        android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner3);

                        popupWindow.setHeight(800);
                    }
                    catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) { }

                    spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                            switch (pos)
                            {
                                case 0:
                                    listFoodKind.setText("육류");
                                    listFoodUnit.setText("g");
                                    listfoodfinal.setText(getDate(arrday[pos]));
                                    break;
                                case 1:
                                    listFoodKind.setText("채소류");
                                    listFoodUnit.setText("개");
                                    listfoodfinal.setText(getDate(arrday[pos]));
                                    break;
                                case 2:
                                    listFoodKind.setText("과일");
                                    listFoodUnit.setText("개");
                                    listfoodfinal.setText(getDate(arrday[pos]));
                                    break;
                                case 3:
                                    listFoodKind.setText("냉동식품");
                                    listFoodUnit.setText("개");
                                    listfoodfinal.setText(getDate(arrday[pos]));
                                    break;
                                case 4:
                                    listFoodKind.setText("해산물");
                                    listFoodUnit.setText("g");
                                    listfoodfinal.setText(getDate(arrday[pos]));
                                    break;
                                case 5:
                                    listFoodKind.setText("나물반찬");
                                    listFoodUnit.setText("그릇");
                                    listfoodfinal.setText(getDate(arrday[pos]));
                                    break;
                                case 6:
                                    listFoodKind.setText("마른반찬");
                                    listFoodUnit.setText("그릇");
                                    listfoodfinal.setText(getDate(arrday[pos]));
                                    break;
                                case 7:
                                    listFoodKind.setText("기타반찬");
                                    listFoodUnit.setText("그릇");
                                    listfoodfinal.setText(getDate(arrday[pos]));
                                    break;
                                case 8:
                                    listFoodKind.setText("유제품");
                                    listFoodUnit.setText("개");
                                    listfoodfinal.setText(getDate(arrday[pos]));
                                    break;
                                case 9:
                                    listFoodKind.setText("기타");
                                    listFoodUnit.setText("개");
                                    listfoodfinal.setText(getDate(arrday[pos]));
                                    break;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });

                    for(int z=0; z<10 ; z++)
                    {
                        if(spinnerstr.equals(arrkind[z])) {
                            spinner3.setSelection(z);
                        }
                    }

                    listFoodStart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new DatePickerDialog(Main.this, startDateSetListner, CurYear, CurMonth, CurDay).show();
                        }
                    });

                    listFoodFinal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new DatePickerDialog(Main.this, finalDateSetListener, CurYear, CurMonth, CurDay).show();
                        }
                    });

                    listaddFoodnumsButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int i = Integer.valueOf(listFoodNum.getText().toString());
                            i++;
                            listFoodNum.setText(String.valueOf(i));
                        }
                    });

                    listdeleteFoodnumsButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int i = Integer.valueOf(listFoodNum.getText().toString());
                            i--;
                            listFoodNum.setText(String.valueOf(i));
                        }
                    });

                    startDateSetListner = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                            // TODO Auto-generated method stub
                            String msg = String.format("%d / %d / %d", year, monthOfYear+1, dayOfMonth);
                            Toast.makeText(Main.this, msg, Toast.LENGTH_SHORT).show();
                            listfoodstart.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth );
                        }
                    };

                    finalDateSetListener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                            // TODO Auto-generated method stub
                            String msg = String.format("%d / %d / %d", year, monthOfYear+1, dayOfMonth);
                            Toast.makeText(Main.this, msg, Toast.LENGTH_SHORT).show();
                            listfoodfinal.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth );
                        }
                    };

                    dialog.setButton("Edit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if ( listFoodKind.getText().toString().equals("")
                                    ||  listFoodName.getText().toString().equals("")
                                    ||  listFoodNum.getText().toString().equals("")
                                    ||  listFoodUnit.getText().toString().equals("")
                                    ||  listfoodstart.getText().toString().equals("")
                                    ||  listfoodfinal.getText().toString().equals("")) {

                                Toast.makeText(getApplicationContext(), "입력오류입니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String food_kind = listFoodKind.getText().toString();
                                    String food_num = listFoodNum.getText().toString();
                                    String food_unit = listFoodUnit.getText().toString();
                                    String food_name = listFoodName.getText().toString();
                                    String start_day = listfoodstart.getText().toString();
                                    String final_day = listfoodfinal.getText().toString();

                                    try {
                                        URL url = new URL(SERVER_ADDRESS + "/foodupdate.php?"
                                                + "food_name=" + URLEncoder.encode(food_name, "UTF-8")
                                                + "&food_num=" + URLEncoder.encode(food_num, "UTF-8")
                                                + "&food_unit=" + URLEncoder.encode(food_unit, "UTF-8")
                                                + "&food_kind=" + URLEncoder.encode(food_kind, "UTF-8")
                                                + "&start_day=" + URLEncoder.encode(start_day, "UTF-8")
                                                + "&final_day=" + URLEncoder.encode(final_day, "UTF-8"));

                                        //자료를 넘겨줄때 UTF-8로 설정해서 넘겨줌 리퀘스트방식으로!

                                        url.openStream(); //php파일 열기.
                                        String result = getXmlData("updateresult.xml", "result"); //insert.php에서 DB입력 결과
                                        //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        if (result.equals("1")) { //DB입력이 성공되면!!
                                            List.clear();
                                            ModifyList();
                                            delete = 1;
                                            chart = 1;
                                            Toast.makeText(getApplicationContext(), "수정 완료!", Toast.LENGTH_SHORT).show();

                                        } else {
                                            Toast.makeText(getApplicationContext(), "수정 실패!", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        Log.e("Error : ", e.getMessage());
                                    }
                                }
                            });

                        }
                    });
                    dialog.show();
                    return true;

                case R.id.delete:
                    String food_name = List.get(index).getModify_list_name().toString();
                    try {
                        URL url = new URL("http://woman3.dothome.co.kr/w3/fooddelete.php?"
                                + "food_name=" + URLEncoder.encode(food_name, "UTF-8"));
                        // php에서 $user_id = $_REQUEST['user_id']; 구문!! 리퀘스트방식으로 넣어주는것

                        url.openStream(); //실행

                        String result = getXmlData("deleteresult.xml", "result"); //디비 삭제결과
                        if (result.equals("1")) { //DB삭제되면
                            List.clear();
                            ModifyList();
                            delete = 1;
                            chart = 1;
                            Toast.makeText(getApplicationContext(), "삭제 완료!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "삭제 실패!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e("Error : ", e.getMessage());
                    }
                    return true;
            }
        }
        return true;
    };

    public String getDate ( int iDay ) {  //int형을 날짜 받으면 YYYY-MM-SS String 형식으로 반환
        Calendar temp=Calendar.getInstance ( );
        StringBuffer sbDate=new StringBuffer ( );

        temp.add ( Calendar.DAY_OF_MONTH, iDay );

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

    public String getDate2( ) {   //현재날짜 반환
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

    private void addData(){     // chart
        ArrayList<Entry> Y = new ArrayList<Entry>();

        for (int i=0; i<yData.length; i++)
            Y.add(new Entry(yData[i],i));

        ArrayList<String> X = new ArrayList<String>();

        for(int i=0;i<xData.length;i++)
            X.add(xData[i]);

        PieDataSet dataset = new PieDataSet(Y,"Market share");
        dataset.setSliceSpace(3);
        dataset.setSelectionShift(5);

        ArrayList<Integer> colors = new ArrayList<Integer>();
        for(int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataset.setColors(colors);

        PieData data = new PieData(X,dataset);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.GRAY);

        pieChart.setData(data);

        pieChart.highlightValues(null);

        pieChart.invalidate();
    }

    public static long diffOfDate(String begin, String end) throws Exception {   //두 날짜 간의 차이
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Date beginDate = formatter.parse(begin);
        Date endDate = formatter.parse(end);

        long diff = endDate.getTime() - beginDate.getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000);

        return diffDays;
    }

}
