package waman2.seyeongstar.se0star;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.concurrent.TimeUnit;

public class menu3 extends AppCompatActivity implements View.OnClickListener {

    Button timer_btn1,timer_btn2,timer_btn3,timer_btn4,timer_btn5,timer_btn6,timer_start,timer_stop,set;
    TextView timer;
    int min_,sec_;
    EditText min,sec;
    CountDownTimer timerCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu3);

        timer = (TextView)findViewById(R.id.timer);
        timer.setText("시간 설정");
        setCustomActionbar();

        timer_btn1 = (Button)findViewById(R.id.timer_btn1);
        timer_btn1.setOnClickListener(this);
        timer_btn2 = (Button)findViewById(R.id.timer_btn2);
        timer_btn2.setOnClickListener(this);
        timer_btn3 = (Button)findViewById(R.id.timer_btn3);
        timer_btn3.setOnClickListener(this);
        timer_btn4 = (Button)findViewById(R.id.timer_btn4);
        timer_btn4.setOnClickListener(this);
        timer_btn5 = (Button)findViewById(R.id.timer_btn5);
        timer_btn5.setOnClickListener(this);
        timer_btn6 = (Button)findViewById(R.id.timer_btn6);
        timer_btn6.setOnClickListener(this);

        min = (EditText)findViewById(R.id.min);
        min_ = Integer.parseInt(min.getText().toString());

        sec = (EditText)findViewById(R.id.sec);
        sec_ = Integer.parseInt(sec.getText().toString());

        if(sec_ == 0)
            timer.setText(min_+"분  0"+sec_+"초");
        else
            timer.setText(min_+"분  "+sec_+"초");

        Count((min_ * 60 + sec_) * 1000);

        set = (Button)findViewById(R.id.set);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerCount.cancel();
                min_ = Integer.parseInt(min.getText().toString());
                sec_ = Integer.parseInt(sec.getText().toString());
                timer.setText(min_+"분"+sec_+"초");
                Count((min_ * 60 + sec_) * 1000);
            }
        });

        timer_start = (Button)findViewById(R.id.timer_start);
        timer_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerCount.start();
            }
        });
        timer_stop = (Button)findViewById(R.id.timer_stop);
        timer_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerCount.cancel();
                if(sec_ == 0)
                    timer.setText(min_+"분  0"+sec_+"초");
                else
                    timer.setText(min_+"분  "+sec_+"초");
            }
        });
    }

//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@//

    public void Count(int time){

        timerCount = new CountDownTimer(time, 1000) {
            @Override
            public void onFinish() {
                //TODO : 카운트다운타이머 종료시 처리
                timer.setText("조리 완료");
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                long millisecond = 1000;  // 1초
                vibrator.vibrate(millisecond);

                NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                PendingIntent pendingIntent = PendingIntent.getActivity(menu3.this, 0, new Intent(menu3.this, Main.class), PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder mCompatBuilder = new NotificationCompat.Builder(menu3.this);
                mCompatBuilder.setSmallIcon(R.drawable.chef2);
                mCompatBuilder.setTicker("NotificationCompat.Builder");
                mCompatBuilder.setWhen(System.currentTimeMillis());
                mCompatBuilder.setNumber(10);
                mCompatBuilder.setContentTitle("냉장고를 부탁해");
                mCompatBuilder.setContentText("조리가 완료되었습니다!");
                mCompatBuilder.setDefaults(Notification.DEFAULT_SOUND);
                mCompatBuilder.setContentIntent(pendingIntent);
                mCompatBuilder.setAutoCancel(true);

                nm.notify(222, mCompatBuilder.build());
            }

            @Override
            public void onTick(long millisUntilFinished) {
                //TODO : 카운트다운타이머 onTick구현
                timer.setText(String.format("%d분  %d초",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }
        };

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.timer_btn1:

                timerCount.cancel();
                Count((4 * 60 + 30) * 1000);
                timer.setText("4분  30초");

                return;

            case R.id.timer_btn2:

                timerCount.cancel();
                Count((8*60)*1000);
                timer.setText("8분  00초");

                return;

            case R.id.timer_btn3:

                timerCount.cancel();
                Count((15*60)*1000);
                timer.setText("15분  00초");

                return;

            case R.id.timer_btn4:

                timerCount.cancel();
                Count((3*60)*1000);
                timer.setText("3분  00초");

                return;

            case R.id.timer_btn5:

                timerCount.cancel();
                Count((5*60)*1000);
                timer.setText("5분  00초");

                return;

            case R.id.timer_btn6:

                timerCount.cancel();
                Count((10*60)*1000);
                timer.setText("10분  00초");

                return;
        }
    }

    private void setCustomActionbar() {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        View mCustomView = LayoutInflater.from(this).inflate(R.layout.menu3_actionbar,null);
        actionBar.setCustomView(mCustomView);

        Toolbar parent = (Toolbar) mCustomView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(mCustomView, params);

        Button menu3_back = (Button) findViewById(R.id.menu3_back);

        menu3_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}