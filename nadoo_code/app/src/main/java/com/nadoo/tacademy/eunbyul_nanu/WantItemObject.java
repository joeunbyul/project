package com.nadoo.tacademy.eunbyul_nanu;

/**
 * Created by byul on 2017-05-28.
 */
public class WantItemObject {
    int count;
    int wantItem_id;
    String wantItem_user;
    String wantItem_username;
    String wantItem_category;
    String wantItem_title;
    String wantItem_story;
    String wantItem_time;
    String wantItem_location;
    int wantItem_userid;


    public WantItemObject(){}

    public WantItemObject(int wantItem_userid,int count,int wantItem_id,String wantItem_user, String wantItem_username ,
                          String wantItem_category ,String wantItem_title ,
                          String wantItem_story, String wantItem_time,String wantItem_location){
        super();
        this.wantItem_userid = wantItem_userid;
        this.count = count;
        this.wantItem_id = wantItem_id;
        this.wantItem_user=wantItem_user;
        this.wantItem_username=wantItem_username;
        this.wantItem_category=wantItem_category;
        this.wantItem_title=wantItem_title;
        this.wantItem_story=wantItem_story;
        this.wantItem_time = wantItem_time;
        this.wantItem_location = wantItem_location;
    }
}
