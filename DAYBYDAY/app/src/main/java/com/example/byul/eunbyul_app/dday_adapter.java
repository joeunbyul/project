package com.example.byul.eunbyul_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by byul on 2016-11-18.
 */
public class dday_adapter extends BaseAdapter{

    public ArrayList<dday_model> DdayList;
    Activity activity;
    DBHelper_ dbHelper2;
    SQLiteDatabase db2;
    int dbVersion2 = 1;
    String dbname2 ="ddaylist.db";
    String sql2;
    EditText addddayName;
    EditText addddayMemo;
    Button Start;
    TextView addstarttext;
    String name,memo,start,days,unit,name2;
    private long d;
    private long t;
    private long r;

    private int resultNumber=0;

    private Calendar calendar,dCalendar;
    private int CurYear, CurMonth, CurDay;

    private int dYear=1;        //디데이 연월일 변수
    private int dMonth=1;
    private int dDay=1;

    private DatePickerDialog.OnDateSetListener startDateSetListner;

    public dday_adapter(Activity activity, ArrayList<dday_model> DdayList) {
        super();
        this.activity = activity;
        this.DdayList = DdayList;
    }

    @Override
    public int getCount() {
        return DdayList.size();
    }

    @Override
    public Object getItem(int position) {
        return DdayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {

        TextView ddayname;
        TextView ddaymemo;
        TextView ddaystartday;
        TextView ddaynum;
        TextView ddayunit;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.dday_item, null);
            holder = new ViewHolder();

            holder.ddayname = (TextView) convertView.findViewById(R.id.ddayname);
            holder.ddaymemo = (TextView) convertView.findViewById(R.id.ddaymemo);
            holder.ddaystartday = (TextView) convertView.findViewById(R.id.ddaystartday);
            holder.ddaynum = (TextView) convertView.findViewById(R.id.ddaynum);
            holder.ddayunit = (TextView) convertView.findViewById(R.id.ddayunit);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        dday_model item = DdayList.get(position);


        holder.ddayname.setText(item.getDday_text().toString());
        holder.ddaymemo.setText(item.getDday_memo().toString());
        holder.ddaystartday.setText(item.getDday_start().toString());
        holder.ddaynum.setText(item.getDday_day().toString());
        holder.ddayunit.setText(item.getDday_unti().toString());

        dbHelper2 = new DBHelper_(activity, dbname2, null, dbVersion2);
        db2 = dbHelper2.getWritableDatabase();

        final Button menu = (Button) convertView.findViewById(R.id.menu); //디데이리스트뷰에 있는 목록 버튼을 누르면
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(activity,menu);
                popupMenu.getMenuInflater().inflate(R.menu.menu,popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            /********************************************************************디데이 수정 부분************************************************************************/
                            case R.id.dday_modify:

                                AlertDialog.Builder builder;
                                AlertDialog dialog;
                                LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View layout = inflater.inflate(R.layout.dday_add, (ViewGroup) activity.findViewById(R.id.dday_layout));

                                addddayName = (EditText) layout.findViewById(R.id.addddayName);
                                addddayMemo = (EditText) layout.findViewById(R.id.addddayMemo);
                                Start = (Button) layout.findViewById(R.id.Start);
                                addstarttext = (TextView) layout.findViewById(R.id.addstarttext);

                                addddayName.setText(holder.ddayname.getText().toString());
                                addddayMemo.setText(holder.ddaymemo.getText().toString());
                                addstarttext.setText(holder.ddaystartday.getText().toString());

                                name2 = holder.ddayname.getText().toString();

                                /*************************리스트뷰에 있던 날짜를 가져와서 Date으로 변환*********************************************/
                                SimpleDateFormat CurYearFormat = new SimpleDateFormat("yyyy");
                                SimpleDateFormat CurMonthFormat = new SimpleDateFormat("MM");
                                final SimpleDateFormat CurDayFormat = new SimpleDateFormat("dd");

                                String thisday = holder.ddaystartday.getText().toString();
                                SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    Date thisday2 = transFormat.parse(thisday);
                                    CurYear = Integer.parseInt(CurYearFormat.format(thisday2));
                                    CurMonth = Integer.parseInt(CurMonthFormat.format(thisday2))-1;
                                    CurDay = Integer.parseInt(CurDayFormat.format(thisday2));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                /********************************************************************************************************************/

                                builder = new AlertDialog.Builder(activity);
                                builder.setView(layout);
                                dialog = builder.create();
                                dialog.setTitle("디데이 수정");
                                addddayName.setSelection(addddayName.getText().length());
                                addddayMemo.setSelection(addddayMemo.getText().length()); //editText커서 끝으로 이동

                                /*************************startDateSetListner가 실행이 안됬을 때*************************************************/
                                dCalendar = Calendar.getInstance();
                                dCalendar.set(CurYear, CurMonth, CurDay);

                                calendar =Calendar.getInstance();              //현재 날짜 불러옴
                                t=calendar.getTimeInMillis();

                                d = dCalendar.getTimeInMillis()/86400000;
                                t=calendar.getTimeInMillis()/86400000;
                                r = (t-d);

                                resultNumber = (int) r; //날짜계산
                                /*****************************************************************************************************************/

                                /****************************Start 버튼을 눌러 startDateSetListner가 실행됬을 때*************************************/
                                Start.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        new DatePickerDialog(activity, startDateSetListner, CurYear, CurMonth, CurDay).show();
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
                                        dCalendar = Calendar.getInstance();
                                        dCalendar.set(dYear, dMonth, dDay);

                                        calendar =Calendar.getInstance();              //현재 날짜 불러옴
                                        t=calendar.getTimeInMillis();

                                        d = dCalendar.getTimeInMillis()/86400000;
                                        t=calendar.getTimeInMillis()/86400000;
                                        r = (t-d);

                                        resultNumber = (int) r; //날짜계산
                                        String msg = String.format("%d / %d / %d", year, monthOfYear + 1, dayOfMonth);
                                        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                                        addstarttext.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                    }
                                };
                                dialog.setButton("수정", new DialogInterface.OnClickListener() {
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
                                        try{
                                            sql2 = String.format("UPDATE ddaylist SET name = '%s', days= '%s', unit = '%s', start = '%s', memo = '%s' WHERE name='%s';",name , days,unit,start,memo,name2);
                                            db2.execSQL(sql2);
                                            Toast.makeText(
                                                    activity,
                                                    "수정 완료!" ,
                                                    Toast.LENGTH_SHORT
                                            ).show();

                                            DdayList.clear();
                                            sql2 = "SELECT * FROM ddaylist;";
                                            Cursor cursor2 = db2.rawQuery(sql2, null);
                                            try {
                                                if (cursor2.getCount() > 0) {
                                                    while (cursor2.moveToNext()) {
                                                        dday_model item = new dday_model(cursor2.getString(0),cursor2.getString(1),cursor2.getString(2),cursor2.getString(3),cursor2.getString(4));
                                                        DdayList.add(item);
                                                    }
                                                } else {
                                                }
                                            } finally {
                                                cursor2.close();
                                            }
                                            notifyDataSetChanged();
                                        }catch (Exception e){
                                            Toast.makeText(
                                                    activity,
                                                    e.getMessage(),
                                                    Toast.LENGTH_SHORT
                                            ).show();
                                        }
                                    }
                                });
                                dialog.show();
                                break;

                            /********************************************************************디데이 삭제 부분************************************************************************/
                            case R.id.dday_del:

                                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(activity);
                                alert_confirm.setMessage("삭제하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                String delList = holder.ddayname.getText().toString();
                                                sql2 = String.format("DELETE FROM ddaylist WHERE name='%s';", delList);
                                                db2.execSQL(sql2);

                                                DdayList.remove(position);

                                                Toast.makeText(
                                                        activity,
                                                        "삭제 완료!",
                                                        Toast.LENGTH_SHORT
                                                ).show();
                                                DdayList.clear();
                                                sql2 = "SELECT * FROM ddaylist;";
                                                Cursor cursor2 = db2.rawQuery(sql2, null);
                                                try {
                                                    if (cursor2.getCount() > 0) {
                                                        while (cursor2.moveToNext()) {
                                                            dday_model item = new dday_model(cursor2.getString(0),cursor2.getString(1),cursor2.getString(2),cursor2.getString(3),cursor2.getString(4));
                                                            DdayList.add(item);
                                                        }
                                                    } else {
                                                    }
                                                } finally {
                                                    cursor2.close();
                                                }
                                                notifyDataSetChanged();
                                            }
                                        }).setNegativeButton("취소",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // 'No'
                                                return;
                                            }
                                        });
                                AlertDialog alert = alert_confirm.create();
                                alert.show();
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        return convertView;
    }
}
