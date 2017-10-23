package com.nadoo.tacademy.eunbyul_nanu;

import com.nadoo.tacademy.eunbyul_nanu.cookie_module.ClearableCookieJar;
import com.nadoo.tacademy.eunbyul_nanu.cookie_module.PersistentCookieJar;
import com.nadoo.tacademy.eunbyul_nanu.cookie_module.SetCookieCache;
import com.nadoo.tacademy.eunbyul_nanu.cookie_module.SharedPrefsCookiePersistor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by pyoinsoo on 2016-11-06.
 */
//이거 하나로 재사용함.
public class OkHttpInitSingtonManager {

    //쿠키를 가진 OkHttpClient(영속성/휘발성 둘다 사용가능)
    private static OkHttpClient okHttpClientApplyCookie;

    //쿠키를 제거하는 클래스
    private static ClearableCookieJar cookieJar;

    /*
      쿠키를 적용한 Okhttp3
     */
    public static OkHttpClient getOkHttpApplyCookieClient() {
        if (okHttpClientApplyCookie != null) {
            return okHttpClientApplyCookie;
        } else {
            if(cookieJar == null){
                cookieJar =  new PersistentCookieJar(new SetCookieCache(),
                        new SharedPrefsCookiePersistor(MyApplication.getmContext().getApplicationContext()));
            }
            okHttpClientApplyCookie = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .cookieJar(cookieJar)
                    .build();
            return okHttpClientApplyCookie;
        }
    }
    public static ClearableCookieJar getCookieJar(){
        return cookieJar;
    }

}
