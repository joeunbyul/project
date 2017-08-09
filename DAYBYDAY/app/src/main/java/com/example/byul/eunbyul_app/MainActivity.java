package com.example.byul.eunbyul_app;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends TabActivity {

    TabHost tabhost;
    TabHost.TabSpec tabSpecdiary, tabSpectodo, tabSpecdday;

    //일기장
    DatePicker datePic;
    EditText edtDiary;
    Button btnDiary;
    String filename;
    TextView dyearText, dmonthText, ddayText;

    //투두리스트
    TextView todoTextView;
    EditText edtTodo;
    Button btnTodo;
    String dbname ="todo_.db";
    int dbVersion = 1;
    DBHelper dbHelper;
    SQLiteDatabase db;
    String sql;
    String data;
    String state = "0";
    ListView listview2;
    ArrayList<Todolist_model> TodoList;
    Todolist_adapter listAdapter;
    String formatDate;

    //디데이
    String dbname2 = "ddaylist.db";
    DBHelper_ dbHelper2;
    SQLiteDatabase db2;
    String sql2;
    Button Start;
    TextView addstarttext,addddayMemo,addddayName;
    TextView today, ddayTextView;
    String name,memo,start,days,unit;
    private int tYear;           //오늘 연월일 변수
    private int tMonth;
    private int tDay;

    private int CurYear, CurMonth, CurDay;

    private int dYear=1;        //디데이 연월일 변수
    private int dMonth=1;
    private int dDay=1;

    private DatePickerDialog.OnDateSetListener startDateSetListner;

    ListView listview3;
    ArrayList<dday_model> DdayList;
    dday_adapter list2Adapter;

    private long d;
    private long t;
    private long r;

    private int resultNumber=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


/********************************************************************탭 설정 부분************************************************************************/
        //tab
        tabhost = getTabHost();

        tabSpecdiary = tabhost.newTabSpec("TAB1").setIndicator("일  기");
        tabSpecdiary.setContent(R.id.diary);
        tabhost.addTab(tabSpecdiary);

        tabSpectodo = tabhost.newTabSpec("TAB2").setIndicator("할  일");
        tabSpectodo.setContent(R.id.todo);
        tabhost.addTab(tabSpectodo);

        tabSpecdday = tabhost.newTabSpec("TAB3").setIndicator("디데이");
        tabSpecdday.setContent(R.id.dday);
        tabhost.addTab(tabSpecdday);

        tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundResource(R.drawable.tabchanged);
        tabhost.getTabWidget().setBackgroundColor(Color.WHITE);
        tabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                for (int i = 0; i < tabhost.getTabWidget().getChildCount(); i++) {
                    tabhost.getTabWidget().getChildAt(i).setBackgroundColor(Color.WHITE);
                }
                tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundResource(R.drawable.tabchanged);

            }
        });
        //tab
/************************************************************************************************************************************************************/

/********************************************************************다이어리 탭 부분************************************************************************/
        //diary
        datePic = (DatePicker) findViewById(R.id.datePic);
        edtDiary = (EditText) findViewById(R.id.edtDiary);
        btnDiary = (Button) findViewById(R.id.btnDiary);
        dyearText = (TextView) findViewById(R.id.dyearText);
        dmonthText = (TextView) findViewById(R.id.dmonthText);
        ddayText = (TextView) findViewById(R.id.ddayText);

        dyearText.setText(Integer.toString(datePic.getYear()));
        dmonthText.setText(Integer.toString(datePic.getMonth() + 1));
        ddayText.setText(Integer.toString(datePic.getDayOfMonth()));

        datePic.init(datePic.getYear(), datePic.getMonth(), datePic.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dyearText.setText(Integer.toString(datePic.getYear()));
                dmonthText.setText(Integer.toString(datePic.getMonth() + 1));
                ddayText.setText(Integer.toString(datePic.getDayOfMonth()));
            }
        });

        Calendar cal = Calendar.getInstance();
        int cYear = cal.get(Calendar.YEAR);
        int cMonth = cal.get(Calendar.MONTH);
        int cDay = cal.get(Calendar.DAY_OF_MONTH);

        filename = Integer.toString(cYear) + "_" + Integer.toString(cMonth+1) + "_" + Integer.toString(cDay) + ".txt";
        String str = readDiary(filename);
        edtDiary.setText(str);
        btnDiary.setEnabled(true);

        datePic.init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                filename = Integer.toString(year) + "_" + Integer.toString(monthOfYear + 1) + "_" + Integer.toString(dayOfMonth) + ".txt";
                String str = readDiary(filename);
                edtDiary.setText(str);
                btnDiary.setEnabled(true);

                dyearText.setText(Integer.toString(datePic.getYear()));
                dmonthText.setText(Integer.toString(datePic.getMonth() + 1));
                ddayText.setText(Integer.toString(datePic.getDayOfMonth()));
            }
        });

        btnDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileOutputStream outFs = openFileOutput(filename, Context.MODE_WORLD_WRITEABLE); //해당 날짜의 파일을 오픈한다. (쓰기모드)

                    if((btnDiary.getText().toString()).equals("새로쓰기")) { //버튼의 text가 "새로쓰기"이면
                        edtDiary.setSelection(edtDiary.getText().length()); //edittext 커서를 끝으로
                        String str = edtDiary.getText().toString(); //editText내용을 str에 저장
                        outFs.write(str.getBytes()); //파일을 오픈해서 str의 내용을 쓴다.
                        outFs.close(); //파일을 닫는다.
                        Toast.makeText(getApplicationContext(), "일기 등록!", Toast.LENGTH_SHORT).show();
                        btnDiary.setText("수정하기");
                    }else if((btnDiary.getText().toString()).equals("수정하기")){ //버튼의 text가 "수정하기"이면
                        edtDiary.setSelection(edtDiary.getText().length()); //edittext 커서를 끝으로
                        String str = edtDiary.getText().toString();
                        if(str.equals("")){ //일기장의 내용이 없으면 버튼의 text를 "새로쓰기"로 바꾼다.
                            outFs.write(str.getBytes());
                            outFs.close();
                            edtDiary.setHint("일기가 없습니다.");
                            btnDiary.setText("새로쓰기");
                        }else{ //일기장의 내용이 있으면 버튼의 text를 "수정하기"로 바꾼다.
                            outFs.write(str.getBytes());
                            outFs.close();
                            btnDiary.setText("수정하기");
                        }
                        Toast.makeText(getApplicationContext(), "수정 완료!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
            }
        });
/**************************************************************************************************************************************************************************/

/*************************************************************스피너를 이용하여 할일 탭 정렬하는 부분 - 리스트를 클리어하고 다시 불러오는 형식*********************************************************************************/
        Spinner spinner = (Spinner) findViewById(R.id.sort);
        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(MainActivity.this,R.array.sort_array,android.R.layout.simple_spinner_item);

        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(sortAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                switch (pos) {
                    case 0:

                        TodoList.clear();
                        sql = "SELECT * FROM todo_ order by state desc;";
                        Cursor cursor1 = db.rawQuery(sql, null);
                        try {
                            if (cursor1.getCount() > 0) {
                                while (cursor1.moveToNext()) {
                                    Todolist_model item = new Todolist_model(cursor1.getString(0),cursor1.getString(1),cursor1.getString(2));
                                    TodoList.add(item);
                                }
                            } else {
                            }
                        } finally {
                            cursor1.close();
                        }
                        listAdapter.notifyDataSetChanged();

                        break;
                    case 1:

                        TodoList.clear();
                        sql = "SELECT * FROM todo_ order by nowtime;";
                        Cursor cursor = db.rawQuery(sql, null);
                        try {
                            if (cursor.getCount() > 0) {
                                while (cursor.moveToNext()) {
                                    Todolist_model item = new Todolist_model(cursor.getString(0),cursor.getString(1),cursor.getString(2));
                                    TodoList.add(item);
                                }
                            } else {
                            }
                        } finally {
                            cursor.close();
                        }
                        listAdapter.notifyDataSetChanged();

                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
/**************************************************************************************************************************************************************************/


/**************************************************************************************************************************************************************************/
        //todolist

        TodoList = new ArrayList<Todolist_model>();
        listview2 = (ListView) findViewById(R.id.listView2);
        listAdapter = new Todolist_adapter(this, TodoList);
        listview2.setAdapter(listAdapter);
        registerForContextMenu(listview2);

        dbHelper = new DBHelper(this, dbname, null, dbVersion);
        db = dbHelper.getWritableDatabase();

        todoTextView = (TextView) findViewById(R.id.todoTextView);

/**************************************************할일 데이터베이스를 불러와서 리스트뷰에 저장하는 부분*************************************************************/

        sql = "SELECT * FROM todo_  order by state desc;";
        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    todoTextView.setVisibility(View.INVISIBLE);
                    Todolist_model item = new Todolist_model(cursor.getString(0),cursor.getString(1),cursor.getString(2));
                    TodoList.add(item);
                }
            } else {

                todoTextView.setVisibility(View.VISIBLE);

            }
        } finally {
            cursor.close();
        }
/**************************************************************************************************************************************************************************/

        edtTodo = (EditText) findViewById(R.id.edtTodo);
        btnTodo = (Button) findViewById(R.id.btnTodo);

        if (TodoList.isEmpty()) {
            todoTextView.setVisibility(View.VISIBLE);
        }

/****************************************************************할일 탭의 등록부분************************************************************************************/

        long now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        Date date = new Date(now);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // nowDate 변수에 값을 저장한다.
        formatDate = sdfNow.format(date);

        btnTodo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                data = edtTodo.getText().toString();
                if (data.equals("")){
                    Toast.makeText(MainActivity.this, "입력하세요", Toast.LENGTH_SHORT).show();
                }else {
                    if (TodoList.isEmpty()) {
                        sql = "insert into todo_ values('" + data + "','" + formatDate + "','" + state + "');";
                        db.execSQL(sql);

                        Todolist_model item = new Todolist_model(data, formatDate, state);
                        TodoList.add(item);
                        listAdapter.notifyDataSetInvalidated();
                        edtTodo.setText("");
                        todoTextView.setVisibility(View.INVISIBLE);
                        InputMethodManager mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        mInputMethodManager.hideSoftInputFromWindow(edtTodo.getWindowToken(), 0);
                        Toast.makeText(MainActivity.this, "할일 등록!", Toast.LENGTH_SHORT).show();
                    } else {
                        sql = "insert into todo_ values('" + data + "','" + formatDate + "','" + state + "');";
                        db.execSQL(sql);
                        Todolist_model item = new Todolist_model(data, formatDate, state);
                        TodoList.add(item);
                        listAdapter.notifyDataSetChanged();
                        edtTodo.setText("");
                        todoTextView.setVisibility(View.INVISIBLE);
                        InputMethodManager mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        mInputMethodManager.hideSoftInputFromWindow(edtTodo.getWindowToken(), 0);
                        Toast.makeText(MainActivity.this, "할일 등록!", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

        listview2.setOnItemClickListener(itemClickListener); //todolist 수정
/**************************************************************************************************************************************************************************/

 /**************************************************************************************************************************************************************************/
        ddayTextView = (TextView)findViewById(R.id.ddayTextView);
        today=(TextView)findViewById(R.id.today);

        final Calendar calendar =Calendar.getInstance();              //현재 날짜 불러옴
        tYear = calendar.get(Calendar.YEAR);
        tMonth = calendar.get(Calendar.MONTH);
        tDay = calendar.get(Calendar.DAY_OF_MONTH);
        //오늘 날짜를 밀리타임으로 바꿈

        SimpleDateFormat CurYearFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat CurMonthFormat = new SimpleDateFormat("MM");
        final SimpleDateFormat CurDayFormat = new SimpleDateFormat("dd");
        CurYear = Integer.parseInt(CurYearFormat.format(date));
        CurMonth = Integer.parseInt(CurMonthFormat.format(date))-1;
        CurDay = Integer.parseInt(CurDayFormat.format(date));

        today.setText(String.format("%d년 %d월 %d일", tYear, tMonth + 1, tDay));
/**************************************************************************************************************************************************************************/

/********************************************************************디데이 리스트뷰를 설정하고 디비 설정하는 부분**************************************************************************/
        DdayList = new ArrayList<dday_model>();
        listview3 = (ListView) findViewById(R.id.listView3);
        list2Adapter = new dday_adapter(this, DdayList);
        listview3.setAdapter(list2Adapter);

        dbHelper2 = new DBHelper_(this, dbname2, null, dbVersion);
        db2 = dbHelper2.getWritableDatabase();
/**************************************************************************************************************************************************************************/

/**********데이터베이스에서 디데이를 불러와서 현재날짜와 저장된 날짜를 계산해서 수정해준다. 리스트뷰에 넣으니까 자동으로 시간계산을 해줄수가없어서 수동적으로 고쳐줬다.************/
        sql2 = "SELECT * FROM ddaylist;";
        Cursor cursor2 = db2.rawQuery(sql2, null);
        try {
            if (cursor2.getCount() > 0) {
                while (cursor2.moveToNext()) {
                    String dday_text  = cursor2.getString(0);
                    String dday_day = "";
                    String dday_unti = "";
                    String dday_start = cursor2.getString(3);

                    SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date dday_day2 = transFormat.parse(dday_start);
                        CurYear = Integer.parseInt(CurYearFormat.format(dday_day2));
                        CurMonth = Integer.parseInt(CurMonthFormat.format(dday_day2))-1;
                        CurDay = Integer.parseInt(CurDayFormat.format(dday_day2));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    final Calendar dCalendar = Calendar.getInstance();
                    dCalendar.set(CurYear, CurMonth, CurDay);

                    t=calendar.getTimeInMillis();

                    d = dCalendar.getTimeInMillis()/86400000;
                    t=calendar.getTimeInMillis()/86400000;
                    r = (t-d);

                    resultNumber = (int) r; //날짜계산

                    if (resultNumber > 0) {
                        dday_day = Integer.toString(resultNumber);
                        dday_unti = "일";
                    } else if (resultNumber == 0) {
                        dday_day = "";
                        dday_unti = "오늘입니다!";
                    } else {
                        int absR = (Math.abs(resultNumber));
                        dday_day = Integer.toString(absR);
                        dday_unti = "일전";
                    }

                    String sql3 = String.format("UPDATE ddaylist SET days= '%s', unit = '%s'WHERE name='%s';",dday_day,dday_unti,dday_text);
                    db2.execSQL(sql3);
                }
            } else {
            }
        } finally {
            cursor2.close();
        }
/*********************************************************************************************************************************************************************************************/

 /**********************************************데이터베이스에서 디데이를 불러와서 리스트뷰에 넣는 부분************************************************************/
        sql2 = "SELECT * FROM ddaylist;";
        cursor2 = db2.rawQuery(sql2, null);
        try{
            if (cursor2.getCount() > 0) {
                while (cursor2.moveToNext()) {
                    dday_model item = new dday_model(cursor2.getString(0),cursor2.getString(1),cursor2.getString(2),cursor2.getString(3),cursor2.getString(4));
                    DdayList.add(item);
                    list2Adapter.notifyDataSetChanged();
                    ddayTextView.setVisibility(View.INVISIBLE);
                }
            } else {
                list2Adapter.notifyDataSetInvalidated();
                ddayTextView.setVisibility(View.VISIBLE);
            }
        }finally {
            cursor2.close();
        }
/**************************************************************************************************************************************************************************/


/**********************************************************디데이 등록하는 부분************************************************************************/
        //디데이 등록
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder;
                AlertDialog dialog;
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.dday_add, (ViewGroup) findViewById(R.id.dday_layout));

                addddayName = (TextView) layout.findViewById(R.id.addddayName);
                addddayMemo = (TextView) layout.findViewById(R.id.addddayMemo);
                Start = (Button) layout.findViewById(R.id.Start);
                addstarttext = (TextView) layout.findViewById(R.id.addstarttext);

                builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(layout);
                dialog = builder.create();
                dialog.setTitle("디데이 등록");

                Start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new DatePickerDialog(MainActivity.this, startDateSetListner, tYear, tMonth, tDay).show();
                        //현재날짜로 셋팅하고 datepicker을 보여준다.
                    }
                });
                startDateSetListner = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        dYear = year;
                        dMonth = monthOfYear;
                        dDay = dayOfMonth;
                        final Calendar dCalendar = Calendar.getInstance();
                        dCalendar.set(dYear, dMonth, dDay);

                        d = dCalendar.getTimeInMillis()/86400000;
                        t=calendar.getTimeInMillis()/86400000;
                        r = (t-d);

                        resultNumber = (int) r; //날짜계산
                        String msg = String.format("%d / %d / %d", year, monthOfYear + 1, dayOfMonth);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        addstarttext.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                };
                dialog.setButton("등록", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        name = addddayName.getText().toString();
                        memo = addddayMemo.getText().toString();
                        start = addstarttext.getText().toString();

                        if (resultNumber > 0) {
                            days = Integer.toString(resultNumber);
                            unit = "일";
                        } else if (resultNumber == 0) {
                            days = "";
                            unit = "오늘입니다!";
                        } else {
                            int absR = (Math.abs(resultNumber));
                            days = Integer.toString(absR);
                            unit = "일전";
                        }
                        if (DdayList.isEmpty()) {
                            sql2 = "insert into ddaylist values('" + name + "','" + days + "','" + unit + "','" + start + "','" + memo + "');";
                            db2.execSQL(sql2);
                            dday_model item = new dday_model(name, days, unit, start, memo);
                            DdayList.add(item);
                            ddayTextView.setVisibility(View.INVISIBLE);
                            list2Adapter.notifyDataSetInvalidated();

                            Toast.makeText(MainActivity.this, "입력 완료!", Toast.LENGTH_SHORT).show();
                        } else {
                            sql2 = "insert into ddaylist values('" + name + "','" + days + "','" + unit + "','" + start + "','" + memo + "');";
                            db2.execSQL(sql2);
                            dday_model item = new dday_model(name, days, unit, start, memo);
                            ddayTextView.setVisibility(View.INVISIBLE);
                            DdayList.add(item);
                            list2Adapter.notifyDataSetChanged();

                            Toast.makeText(MainActivity.this, "입력 완료!", Toast.LENGTH_SHORT).show();
                        }
                    }

                });

                dialog.show();

            }
        });
/**************************************************************************************************************************************************************************/

    }

/********************************************************************다이어리 읽어오는 함수*****************************************************************************/
    String readDiary(String fname){
        String diaryStr = null;
        FileInputStream inFs;
        try{
            inFs = openFileInput(fname);
            byte[] txt = new byte[500];
            inFs.read(txt);
            inFs.close();
            diaryStr = (new String(txt)).trim();
            if(diaryStr.equals("")){
                edtDiary.setHint("일기가 없습니다.");
                btnDiary.setText("새로쓰기");
            }else {
                btnDiary.setText("수정하기");
            }
        }catch (IOException e){
            edtDiary.setHint("일기가 없습니다.");
            btnDiary.setText("새로쓰기");
        }
        return diaryStr;
    }

/********************************************************************할일 탭부분에서 수정부분***************************************************************************/

    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        getMenuInflater().inflate(R.menu.menu_listview, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    //listview 를 롱클릭했을 때 나오는 컨텍스트메뉴
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo pos= (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        final int index  = pos.position;
        switch( item.getItemId() ){
            case R.id.modify:
                final EditText edtText = new EditText(this);
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("수정하기");
                dialog.setView(edtText);

                edtText.setText(TodoList.get(index).getTodo_text().toString());
                edtText.setSelection(edtText.getText().length());
                dialog.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String updateList = TodoList.get(index).getTodo_text();
                        String inputValue = edtText.getText().toString();
                        InputMethodManager mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        mInputMethodManager.hideSoftInputFromWindow(edtText.getWindowToken(), 0);
                        sql = String.format("UPDATE todo_ SET name = '%s' WHERE name='%s';", inputValue, updateList);
                        db.execSQL(sql);
                        TodoList.clear();
                        sql = "SELECT * FROM todo_ order by state desc;";
                        Cursor cursor = db.rawQuery(sql, null);
                        try {
                            if (cursor.getCount() > 0) {
                                while (cursor.moveToNext()) {
                                    Todolist_model item = new Todolist_model(cursor.getString(0),cursor.getString(1),cursor.getString(2));
                                    TodoList.add(item);
                                }
                            } else {

                            }
                        } finally {
                            cursor.close();
                        }
                        listAdapter.notifyDataSetChanged();
                        Toast.makeText(
                                getApplicationContext(),
                                "수정 완료!",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
                dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();
                break;
        }

        return true;

    };

/***************************************************할일 탭의 삭제부분*********************************************************************************/
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick(AdapterView<?> adapterView, View clickedView, final int pos, long id)
        {
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MainActivity.this);
            alert_confirm.setMessage("완료하셨습니까?").setCancelable(true).setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String delList = TodoList.get(pos).getTodo_text();
                            sql = String.format("DELETE FROM todo_ WHERE name='%s';", delList);
                            db.execSQL(sql);

                            TodoList.remove(pos);
                            listview2.clearChoices();
                            listAdapter.notifyDataSetChanged();
                            Toast.makeText(
                                    getApplicationContext(),
                                    "할일 완료!",
                                    Toast.LENGTH_SHORT
                            ).show();

                            if (TodoList.isEmpty()) {
                                listAdapter.notifyDataSetInvalidated();
                                todoTextView.setVisibility(View.VISIBLE);
                            }
                        }
                    });
            AlertDialog alert = alert_confirm.create();
            alert.show();

        }
    };

 /**************************************************************************************************************************************************************************/
    @Override
    public void onBackPressed() {
        //Alert로 종료시키기
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog  .setTitle("종료 알림")
                .setMessage("정말 종료하시겠습니까?")
                .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create().show();
    }


}