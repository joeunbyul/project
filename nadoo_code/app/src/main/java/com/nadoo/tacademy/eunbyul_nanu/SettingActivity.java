package com.nadoo.tacademy.eunbyul_nanu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nadoo.tacademy.eunbyul_nanu.cookie_module.ClearableCookieJar;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton alarm_nosound,setting_logout, alarm_nosound2, alarm_sound, alarm_all,change_password;
    ImageView alarm_nosound_check, alarm_nosound2_check, alarm_sound_check, alarm_all_check,state_kakao, state_facebook;
    TextView state_email;
    int CHECK_SUM = 0,kakaotalk_check,facebook_check,state,temp,CHECK_alarm_nosound,CHECK_alarm_nosound2,CHECK_alarm_sound,CHECK_alarm_all;
    SharedPreferences prefs;
    String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        /*로그인 정보 가져오기*/

        prefs = getSharedPreferences("user_pref", MODE_PRIVATE);
        state_email = (TextView)findViewById(R.id.show_setting_email);
        state_kakao = (ImageView) findViewById(R.id.show_setting_kakao);
        state_facebook = (ImageView)findViewById(R.id.show_setting_facebook);
        state = prefs.getInt("state",0);
        kakaotalk_check = prefs.getInt("kakaotalk_check",0);
        facebook_check = prefs.getInt("facebook_check",0);
        user = prefs.getString("user_id","");


        state_email.setText(user);//user 이메일(user_id)
        if(state==1) {
            if (kakaotalk_check == 1) {//카카오 인증됨
                state_kakao.setImageResource(R.drawable.certification_y);
            } else {
                state_kakao.setImageResource(R.drawable.certification_n);
            }
            if (facebook_check == 1) {//facebook 인증됨
                state_facebook.setImageResource(R.drawable.certification_y);
            } else {
                state_facebook.setImageResource(R.drawable.certification_n);
            }
        }else{
            state_kakao.setVisibility(View.GONE);
            state_facebook.setVisibility(View.GONE);
        }


        alarm_nosound = (ImageButton) findViewById(R.id.alarm_nosound);
        alarm_nosound_check = (ImageView) findViewById(R.id.alarm_nosound_check);
        alarm_nosound.setOnClickListener(this);

        alarm_nosound2 = (ImageButton)findViewById(R.id.alarm_nosound_2);
        alarm_nosound2_check = (ImageView)findViewById(R.id.alarm_nosound_2_check);
        alarm_nosound2.setOnClickListener(this);

        alarm_sound = (ImageButton)findViewById(R.id.alarm_sound);
        alarm_sound_check = (ImageView)findViewById(R.id.alarm_sound_check);
        alarm_sound.setOnClickListener(this);

        alarm_all = (ImageButton)findViewById(R.id.alarm_all);
        alarm_all_check = (ImageView)findViewById(R.id.alarm_all_check);
        alarm_all.setOnClickListener(this);


        setting_logout = (ImageButton)findViewById(R.id.setting_logout);
        setting_logout.setOnClickListener(this);

        change_password = (ImageButton) findViewById(R.id.setting_change_password);
        change_password.setOnClickListener(this);


    }
    public void onClick(View v){
        if(v.getId() == R.id.alarm_nosound){
            if(temp==0){
                if(CHECK_alarm_nosound==0){
                    alarm_nosound.setSelected(true);
                    CHECK_alarm_sound=1;
                    temp = CHECK_alarm_sound;
                }else{
                    alarm_nosound.setSelected(false);
                    CHECK_alarm_nosound=0;
                    temp=CHECK_alarm_nosound;
                }
            }else{
                if(CHECK_alarm_nosound==0){

                }else{
                    alarm_nosound.setSelected(false);
                    CHECK_alarm_nosound=0;
                    temp=CHECK_alarm_nosound;
                }
            }
           /* if(CHECK_SUM==0){
                alarm_nosound.setSelected(true);
                CHECK_SUM=1;
                alarm_nosound_check.setVisibility(v.VISIBLE);

            }else{
                CHECK_SUM = 0;
                alarm_nosound.setSelected(false);
                alarm_nosound_check.setVisibility(v.INVISIBLE);

            }*/
        }else if(v.getId() == R.id.alarm_nosound_2){
            if(temp==0){
                if(CHECK_alarm_nosound2==0){
                    alarm_nosound2.setSelected(true);
                    CHECK_alarm_nosound2=1;
                    temp = CHECK_alarm_nosound2;
                }else{
                    alarm_nosound2.setSelected(false);
                    CHECK_alarm_nosound2=0;
                    temp=CHECK_alarm_nosound2;
                }
            }else{
                if(CHECK_alarm_nosound==0){

                }else{
                    alarm_nosound2.setSelected(false);
                    CHECK_alarm_nosound2=0;
                    temp=CHECK_alarm_nosound2;
                }
            }
           /* if(CHECK_SUM==0){
                alarm_nosound2.setSelected(true);
                CHECK_SUM=1;
                alarm_nosound2_check.setVisibility(v.VISIBLE);

            }else{
                CHECK_SUM = 0;
                alarm_nosound2.setSelected(false);
                alarm_nosound2_check.setVisibility(v.INVISIBLE);
            }*/
        }else if(v.getId() == R.id.alarm_sound)  {
            if(temp==0){
                if(CHECK_alarm_sound==0){
                    alarm_sound.setSelected(true);
                    CHECK_alarm_sound=1;
                    temp = CHECK_alarm_sound;
                }else{
                    alarm_sound.setSelected(false);
                    CHECK_alarm_sound=0;
                    temp=CHECK_alarm_sound;
                }
            }else{
                if(CHECK_alarm_sound==0){

                }else{
                    alarm_sound.setSelected(false);
                    CHECK_alarm_sound=0;
                    temp=CHECK_alarm_sound;
                }
            }
            /*if(CHECK_SUM==0){
                alarm_sound.setSelected(true);
                CHECK_SUM=1;
                alarm_sound_check.setVisibility(v.VISIBLE);
            }else{
                CHECK_SUM = 0;
                alarm_sound.setSelected(false);
                alarm_sound_check.setVisibility(v.INVISIBLE);
            }*/
        }else if(v.getId() == R.id.alarm_all){
            if(temp==0){
                if(CHECK_alarm_all==0){
                    alarm_all.setSelected(true);
                    CHECK_alarm_all=1;
                    temp = CHECK_alarm_all;
                }else{
                    alarm_all.setSelected(false);
                    CHECK_alarm_all=0;
                    temp=CHECK_alarm_all;
                }
            }else{
                if(CHECK_alarm_all==0){

                }else{
                    alarm_all.setSelected(false);
                    CHECK_alarm_all=0;
                    temp=CHECK_alarm_all;
                }
            }
           /* if(CHECK_SUM==0){
                alarm_all.setSelected(true);
                CHECK_SUM=1;
                alarm_all_check.setVisibility(v.VISIBLE);

            }else{
                CHECK_SUM = 0;
                alarm_all.setSelected(false);
                alarm_all_check.setVisibility(v.INVISIBLE);
            }*/
        }else if(v.getId() == R.id.setting_change_password){
            Intent intent = new Intent(this, ChangePasswordActivity.class);
            startActivity(intent);
        }else if(v.getId() == R.id.setting_logout){
            SharedPreferences prefs = getSharedPreferences("user_pref", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.commit();

            ClearableCookieJar clearableCookieJar = OkHttpInitSingtonManager.getCookieJar();
            if( clearableCookieJar != null)    clearableCookieJar.clear();

            Intent i = new Intent(SettingActivity.this,MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }

    }
}
