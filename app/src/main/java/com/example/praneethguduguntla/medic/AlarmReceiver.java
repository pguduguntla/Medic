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
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.praneethguduguntla.medic.App.CHANNEL_1_ID;

public class AlarmReceiver extends BroadcastReceiver {
    static String contentTitle;
    static String description;

    int index;
    public Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        index = -1;
        mContext = context;
        createNotificationChannels(context);
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);



        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String uid = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d("HERENOT", "WHAt is " + index);
        db.collection("Patients").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                index = Math.toIntExact((long)(task.getResult().get("Total"))) - 1;
                Intent notificationIntent = new Intent(mContext, YesOrNo.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                notificationIntent.putExtra("index", index);

                PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
                        notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                NotificationManager notificationManager = (NotificationManager) mContext
                        .getSystemService(Context.NOTIFICATION_SERVICE);

                NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(mContext, CHANNEL_1_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(contentTitle)
                        .setContentText(description).setSound(alarmSound)
                        .setAutoCancel(true).setWhen(System.currentTimeMillis())
                        .setContentIntent(pendingIntent)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
                notificationManager.notify(1, mNotifyBuilder.build());
                Log.d("NOTIFY", "ITS GOOD!");
            }
        });



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

