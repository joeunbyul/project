package com.nadoo.tacademy.eunbyul_nanu;

/**
 * Created by Kyoonho on 2017-05-31.
 */

public class MypageItemObject {

    String mypage_item_title;
    String mypage_item_priceKind;
    String mypage_item_price;
    String mypage_item_location;
    String  mypage_item_nickname; //닉네임
    String  mypage_item_category; //카테고리
    String  mypage_item_article; //소개글
    int  mypage_item_check; //인증여부
    String  mypage_item_userphoto; //프로필사진
    int mypage_item_need_id;

    String item1;
    String item2;
    String item3;

    String mypage_item_img;
    String mypage_item_state;

    public MypageItemObject(){}

    public MypageItemObject(int mypage_item_need_id, String item1,
                            String item2,
                            String item3,String mypage_item_title, String mypage_item_priceKind,String mypage_item_location,
                            String mypage_item_img, String mypage_item_price,String mypage_item_state,String  mypage_item_nickname, String  mypage_item_category,String  mypage_item_article,int  mypage_item_check,String  mypage_item_userphoto ){
        super();
        this.mypage_item_title = mypage_item_title;
        this.item1=item1;
        this.item2=item2;
        this.item3=item3;
        this.mypage_item_priceKind=mypage_item_priceKind;
        this.mypage_item_price=mypage_item_price;
        this.mypage_item_location=mypage_item_location;
        this.mypage_item_img=mypage_item_img;
        this.mypage_item_state=mypage_item_state;
        this.mypage_item_nickname=mypage_item_nickname;
        this.mypage_item_category=mypage_item_category;
        this.mypage_item_article=mypage_item_article;
        this.mypage_item_check=mypage_item_check;
        this.mypage_item_userphoto=mypage_item_userphoto;
        this.mypage_item_need_id=mypage_item_need_id;

    }

}



