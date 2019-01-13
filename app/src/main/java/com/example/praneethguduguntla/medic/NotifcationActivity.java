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
import android.widget.Button;
import android.widget.EditText;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.core.FirestoreClient;

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
    private EditText edit_text_minute;
    private EditText edit_text_hour;
    private EditText edit_text_day;
    private EditText edit_text_name;
    private EditText edit_text_description;
    AlarmManager am;
    Intent intent1;
    PendingIntent pendingIntent;
    List<Integer> pendingIntents;
    static int pIntentCounter = 0;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pendingIntents = new ArrayList<Integer>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifcation);
        //App a = new App();
        //a.createNotificationChannels();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        notificationManager = NotificationManagerCompat.from(this);

        Button b = (Button) findViewById(R.id.Button2);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeNot(view);
            }
        });

       editTextTitle = findViewById(R.id.edit_text_title);
       edit_text_minute = findViewById(R.id.edit_text_minute);
       edit_text_hour = findViewById(R.id.edit_text_hour);
       edit_text_day = findViewById(R.id.edit_text_day);
       edit_text_name = findViewById(R.id.edit_text_name);
        edit_text_description = findViewById(R.id.edit_text_description);
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

      //createNotification(4, 10, 0, 7, "Title", "Description", 60000);
    }

    public void makeNot(View v){
        String Title = editTextTitle.getText().toString();
        String desc = edit_text_description.getText().toString();
        String name = edit_text_name.getText().toString();
        int day = Integer.parseInt(edit_text_day.getText().toString());
        int hour = Integer.parseInt(edit_text_hour.getText().toString());
        int minute = Integer.parseInt(edit_text_minute.getText().toString());
        Map<String, Object> docData = new HashMap<>();
        docData.put("Times", hour + ":" + minute);
        docData.put("Name", name);
        docData.put("Days", day);
        db.collection("Patients").document("testPatient").collection("Meds").add(docData);
        createNotification(hour, minute, 0, 7, Title, desc, 60000);

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
