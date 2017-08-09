package com.example.byul.eunbyul_app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by byul on 2016-11-24.
 */
public class BroadcastD extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.calendar).setTicker("HETT").setWhen(System.currentTimeMillis())
                .setNumber(1).setContentTitle("D-Day").setContentText("오늘입니다! 디데이를 확인하세요!")
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(pendingIntent).setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX);

        notificationmanager.notify(1, builder.build());

    }
}
