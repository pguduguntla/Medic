package com.example.praneethguduguntla.medic;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.icu.util.Calendar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.praneethguduguntla.medic.App.CHANNEL_1_ID;

public class NotifcationActivity extends AppCompatActivity {

    private NotificationManagerCompat notificationManager;
    private EditText editTextTitle;
    private EditText editTextMessage;
    AlarmManager am;
    Intent intent1;
    PendingIntent pendingIntent;
    List<Integer> pendingIntents;
    static int pIntentCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pendingIntents = new ArrayList<Integer>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifcation);
        //App a = new App();
        //a.createNotificationChannels();
        notificationManager = NotificationManagerCompat.from(this);

       /*editTextTitle = findViewById(R.id.edit_text_title);
       editTextMessage = findViewById(R.id.edit_text_message);*/
       /* Calendar calendar = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            calendar = Calendar.getInstance();


            calendar.set(Calendar.HOUR_OF_DAY, 3);
            calendar.set(Calendar.MINUTE, 29);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            AlarmReceiver.changeVariables("Random Title", "Description");
            intent1 = new Intent(NotifcationActivity.this, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(NotifcationActivity.this, pIntentCounter, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            pendingIntents.add(pIntentCounter);
            pIntentCounter++;
            am = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60000, pendingIntent);

        }*/
      /*  Firestore db = FirestoreClient.getFirestore();
        Map<String, Object> docData = new HashMap<>();
        docData.put("name", "NYC");
        docData.put("state", "NY");
        docData.put("country", "USA");
        docData.put("regions", Arrays.asList("east_coast", "cold"));

        ApiFuture<DocumentSnapshot> future = db.collection("cities").document("NYC").get();
        //    String name = future.get("country");

        while (!future.isDone());
        DocumentSnapshot doc = future.get();
      */ createNotification(4, 10, 0, 7, "Title", "Description", 60000);
    }

        public void createNotification(int hour_of_day, int minute, int second, int day_of_week, String title, String description, int repeat){
          Calendar calendar = null;

          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
              calendar = Calendar.getInstance();


              calendar.set(Calendar.HOUR_OF_DAY, hour_of_day);
              calendar.set(Calendar.MINUTE, minute);
              calendar.set(Calendar.SECOND, second);
              calendar.set(Calendar.DAY_OF_WEEK, day_of_week);
              AlarmReceiver.changeVariables(title, description);
              intent1 = new Intent(NotifcationActivity.this, AlarmReceiver.class);
              pendingIntent = PendingIntent.getBroadcast(NotifcationActivity.this, pIntentCounter, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
              pendingIntents.add(pIntentCounter);
              pIntentCounter++;
              am = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
              am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), repeat, pendingIntent);

          }
      }


    public void sendOnChannel1(View v) {
       /*String title = editTextTitle.getText().toString();
       String message = editTextMessage.getText().toString();

      Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
               .setSmallIcon(R.drawable.ic_launcher_foreground)
               .setContentTitle(title)
               .setContentText(message)
               .setCategory(NotificationCompat.CATEGORY_MESSAGE)
               .build();

       notificationManager.notify(1, notification);*/
        int intentNum = 0;
        PendingIntent currentP = PendingIntent.getBroadcast(NotifcationActivity.this, intentNum, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        am.cancel(currentP);
        Log.d("COOL", "CANCELEELED");
    }

}
