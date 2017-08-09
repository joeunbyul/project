package com.example.byul.eunbyul_app;

/**
 * Created by byul on 2016-11-18.
 */
public class dday_model {

    private String dday_text;
    private String dday_day;
    private String dday_unti;
    private String dday_start;
    private String dday_memo;

    public dday_model(String dday_text, String dday_day,String dday_unti,String dday_start,String dday_memo) {

        this.dday_text = dday_text;
        this.dday_day = dday_day;
        this.dday_unti = dday_unti;
        this.dday_start = dday_start;
        this.dday_memo = dday_memo;
    }


    public String getDday_text() {
        return dday_text;
    }

    public String getDday_day() {
        return dday_day;
    }

    public String getDday_unti() {
        return dday_unti;
    }

    public String getDday_start() {
        return dday_start;
    }

    public String getDday_memo() {
        return dday_memo;
    }
}
