package com.nadoo.tacademy.eunbyul_nanu;

/**
 * Created by Tacademy on 2017-05-29.
 */

public class ItemObject {
    int item_id; //아이템 id
    String item_photo; //아이템 상세사진
    int item_state; //아이템상태(1:대여가능,2:대여중,3:대여불가)
    String item_name; //이름
    String item_price; //가격
    String item_time; //시간,일,협의
    int item_userid; //유저 id
    String item_nickname; //닉네임
    String item_category; //카테고리
    String item_article; //소개글
    int item_check; //인증여부
    String item_location; //위치
    String item_userphoto; //프로필사진
    String item1;
    String item2;
    String item3;


    public ItemObject(){

    }
    ItemObject(int item_id,
               String item1,
               String item2,
               String item3,
            String item_photo,
            int item_state,
            String item_name,
            String item_price,
            String item_time,
            int item_userid,
            String item_nickname,
            String item_category,
            String item_article,
            int item_check,
            String item_location,
            String item_userphoto){

        this.item_id=item_id;
        this.item1=item1;
        this.item2=item2;
        this.item3=item3;
        this.item_photo=item_photo;
        this.item_state=item_state;
        this.item_name=item_name;
        this.item_price=item_price;
        this.item_time=item_time;
        this.item_userid=item_userid;
        this.item_nickname=item_nickname;
        this.item_category=item_category;
        this.item_article=item_article;
        this.item_check=item_check;
        this.item_location=item_location;
        this.item_userphoto=item_userphoto;
    }
}
