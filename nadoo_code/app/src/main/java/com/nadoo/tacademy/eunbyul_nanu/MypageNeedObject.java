package com.nadoo.tacademy.eunbyul_nanu;

/**
 * Created by Kyoonho on 2017-05-31.
 */

public class MypageNeedObject {
    String mypage_need_title;
    String mypage_need_time;
    String mypage_need_contents;
    String mypage_need_category;
    String mypage_need_location;
    int mypage_need_id;

    public MypageNeedObject(){}

    public MypageNeedObject(String mypage_need_category,String mypage_need_location,String mypage_need_title, String mypage_need_time,String mypage_need_contents,int mypage_need_id){
        super();
        this.mypage_need_title = mypage_need_title;
        this.mypage_need_time=mypage_need_time;
        this.mypage_need_contents=mypage_need_contents;
        this.mypage_need_category=mypage_need_category;
        this.mypage_need_location=mypage_need_location;
        this.mypage_need_id = mypage_need_id;

    }

}



