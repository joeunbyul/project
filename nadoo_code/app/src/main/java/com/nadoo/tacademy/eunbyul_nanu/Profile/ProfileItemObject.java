package com.nadoo.tacademy.eunbyul_nanu.Profile;

/**
 * Created by Kyoonho on 2017-05-31.
 */

public class ProfileItemObject {
     String profile_item_title;
    String profile_item_category;
    String profile_time_location;
    String profile_time_price;
    String profile_time_priceKind;
    String profile_item_img;
    String profile_item_article;
    int profile_item_state;
    int profile_item_usercheck;


    public ProfileItemObject(){}
    public ProfileItemObject( String profile_item_title,
                                String profile_item_category,
                                String profile_time_location,
                                String profile_item_img,
                              String profile_time_price,
                              String profile_time_priceKind,
                                int profile_item_state,String profile_item_article,int profile_item_usercheck){
        this.profile_item_title = profile_item_title;
        this.profile_item_category = profile_item_category;
        this.profile_time_location = profile_time_location;
        this.profile_time_price = profile_time_price;
        this.profile_time_priceKind = profile_time_priceKind;
        this.profile_item_article = profile_item_article;
        this.profile_item_img=profile_item_img;
        this.profile_item_state = profile_item_state;
        this.profile_item_usercheck = profile_item_usercheck;
    }

}
