package com.nadoo.tacademy.eunbyul_nanu;

/**
 * Created by byul on 2017-05-28.
 */
public class GetUserObject {

    String user_id;
    String nickname;
    int school_check;
    int facebook_check;
    int kakaotalk_check;
    String profile_thumbURL;
    String school;


    public GetUserObject(){}

    public GetUserObject(String user_id ,String school,
                         String nickname , int school_check ,
                         int facebook_check, int kakaotalk_check, String profile_thumbURL){
        super();

        this.user_id=user_id;
        this.nickname=nickname;
        this.school=school;
        this.school_check=school_check;
        this.facebook_check=facebook_check;
        this.kakaotalk_check = kakaotalk_check;
        this.profile_thumbURL = profile_thumbURL;
    }
}
