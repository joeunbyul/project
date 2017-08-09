package com.example.byul.eunbyul_app;

/**
 * Created by 315 on 2016-11-17.
 */
public class Todolist_model {

    private String todo_text;
    private String todo_cal;
    private String todo_state;

    public Todolist_model(String todo_text, String todo_cal,String todo_state) {

        this.todo_text = todo_text;
        this.todo_cal = todo_cal;
        this.todo_state =todo_state;
    }


    public String getTodo_cal() {
        return todo_cal;
    }

    public String getTodo_text() {
        return todo_text;
    }

    public String getTodo_state(){
        return todo_state;
    }
}
