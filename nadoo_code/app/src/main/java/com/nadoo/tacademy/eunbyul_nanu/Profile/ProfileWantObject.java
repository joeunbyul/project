package com.nadoo.tacademy.eunbyul_nanu.Profile;

/**
 * Created by Kyoonho on 2017-05-31.
 */

public class ProfileWantObject  {
    String profile_want_title;
    String profile_want_contents;
    String profile_want_date;

    public ProfileWantObject(){}
    public ProfileWantObject( String profile_want_title,
                              String profile_want_contents,
                              String profile_want_date){
        this.profile_want_title = profile_want_title;
        this.profile_want_contents = profile_want_contents;
        this.profile_want_date = profile_want_date;
    }
}
