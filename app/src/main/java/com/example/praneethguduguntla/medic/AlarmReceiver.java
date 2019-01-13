package com.example.praneethguduguntla.medic;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import static com.example.praneethguduguntla.medic.App.CHANNEL_1_ID;

public class AlarmReceiver extends BroadcastReceiver {
    static String contentTitle;
    static String description;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        createNotificationChannels(context);
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(contentTitle)
                .setContentText(description).setSound(alarmSound)
                .setAutoCancel(true).setWhen(when)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        notificationManager.notify(1, mNotifyBuilder.build());
        Log.d("NOTIFY", "ITS GOOD!");
    }
    public static void changeVariables(String one, String two){
        contentTitle = one;
        description = two;
    }
    public void createNotificationChannels(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is a Channel");
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }
}

