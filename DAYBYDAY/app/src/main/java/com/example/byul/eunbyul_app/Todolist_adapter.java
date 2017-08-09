package com.example.byul.eunbyul_app;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 315 on 2016-11-17.
 */
public class Todolist_adapter extends BaseAdapter {
    public ArrayList<Todolist_model> TodoList;
    Activity activity;
    DBHelper dbHelper;
    SQLiteDatabase db;
    int dbVersion = 1;
    String dbname ="todo_.db";
    String sql;
    String star ="";

    public Todolist_adapter(Activity activity, ArrayList<Todolist_model> TodoList) {
        super();
        this.activity = activity;
        this.TodoList = TodoList;
    }

    @Override
    public int getCount() {
        return TodoList.size();
    }

    @Override
    public Object getItem(int position) {
        return TodoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {

        TextView todo_text;
        TextView todo_cal;
        TextView todo_state;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.todolist_item, null);
            holder = new ViewHolder();

            holder.todo_text = (TextView) convertView.findViewById(R.id.todo_text);
            holder.todo_cal = (TextView) convertView.findViewById(R.id.todo_cal);
            holder.todo_state = (TextView) convertView.findViewById(R.id.todo_state);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Todolist_model item = TodoList.get(position);

        holder.todo_text.setText(item.getTodo_text().toString());
        holder.todo_cal.setText(item.getTodo_cal().toString());
        holder.todo_state.setText(item.getTodo_state().toString());

        /*******************************************************************중요표시 여부에 따라서 사진 뿌려주는 코드************************************************************************/
        final Button state = (Button) convertView.findViewById(R.id.state);
        if(holder.todo_state.getText().toString().equals("1")){
            state.setBackgroundResource(R.drawable.after);
        }else{
            state.setBackgroundResource(R.drawable.before);
        }

        /*******************************************************************중요표시하는 부분************************************************************************/
        state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbHelper = new DBHelper(activity, dbname, null, dbVersion);
                db = dbHelper.getWritableDatabase();

                sql = String.format("SELECT * FROM todo_ WHERE name='%s';", holder.todo_text.getText().toString());
                //db.execSQL(sql);
                Cursor cursor = db.rawQuery(sql, null);
                try {
                    if (cursor.getCount() > 0) {
                        while (cursor.moveToNext()) {
                            star =cursor.getString(2).toString();
                            //상태를 받아온다.
                        }
                    } else {
                    }
                } finally {
                    cursor.close();
                }
                if(star.equals("0")) {   //상태가 0이면
                    state.setBackgroundResource(R.drawable.after); //중요표시 해준다.
                    holder.todo_state.setText("1");
                    sql = String.format("UPDATE todo_ SET state = '%s' WHERE name='%s';", "1", holder.todo_text.getText().toString());
                    db.execSQL(sql); //상태바꿔준다.
                    TodoList.clear();
                    sql = "SELECT * FROM todo_ order by state desc;";
                    cursor = db.rawQuery(sql, null);
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
                    notifyDataSetChanged();

                }else if(star.equals("1")){
                    state.setBackgroundResource(R.drawable.before);
                    holder.todo_state.setText("0");
                    sql = String.format("UPDATE todo_ SET state = '%s' WHERE name='%s';", "0", holder.todo_text.getText().toString());
                    db.execSQL(sql);
                    TodoList.clear();
                    sql = "SELECT * FROM todo_ order by state desc;";
                    cursor = db.rawQuery(sql, null);
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
                    notifyDataSetChanged();
                }
            }
        });

        return convertView;
    }
}




