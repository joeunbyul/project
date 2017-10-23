package com.nadoo.tacademy.eunbyul_nanu.Profile;

/**
 * Created by Kyoonho on 2017-05-31.
 */

public class ProfileObject {
    int profile_follower;
    String profile_school;
    int profile_school_check;
    int profile_kakaotalk_check;
    int profile_facebook_check;
    String profile_img;
    int profile_like;



    public ProfileObject(){}
    public ProfileObject(int profile_follower,
                         String profile_school,
                         int profile_school_check,
                         int profile_kakaotalk_check,
                         int profile_facebook_check,
                         String profile_img,
                         int profile_like){
        this.profile_follower = profile_follower;
        this.profile_school = profile_school;
        this.profile_school_check = profile_school_check;
        this.profile_kakaotalk_check=profile_kakaotalk_check;
        this.profile_facebook_check = profile_facebook_check;
        this.profile_img = profile_img;
        this.profile_like = profile_like;
    }

}
