package com.example.praneethguduguntla.medic;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.praneethguduguntla.medic.NotifcationActivity.pIntentCounter;

public class PatientSchedulingActivity extends AppCompatActivity {

    private Button create;
    private CheckBox Monday;
    private CheckBox Tuesday;
    private CheckBox Wednesday;
    private CheckBox Thursday;
    private CheckBox Friday;
    private CheckBox Saturday;
    private CheckBox Sunday;
    private TimePicker time;
    private EditText name;
    PendingIntent pendingIntent;
    //    List<Integer> pendingIntents;
    AlarmManager am;
    Intent intent1;

    private FirebaseFirestore db;
    private FirebaseUser user;
    private int index;
    private Map<String, Object> userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_scheduling);

        create = (Button) findViewById(R.id.createRegimen);
        Monday = (CheckBox) findViewById(R.id.daySelectMonday);
        Tuesday = (CheckBox) findViewById(R.id.daySelectTuesday);
        Wednesday = (CheckBox) findViewById(R.id.daySelectWednesday);
        Thursday = (CheckBox) findViewById(R.id.daySelectThursday);
        Friday = (CheckBox) findViewById(R.id.daySelectFriday);
        Saturday = (CheckBox) findViewById(R.id.daySelectSaturday);
        Sunday = (CheckBox) findViewById(R.id.daySelectSunday);
        time = (TimePicker) findViewById(R.id.timePicker1);
        name = (EditText) findViewById(R.id.name);

        db = FirebaseFirestore.getInstance();
        initUser();

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                db.collection("Patients").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isComplete()) {
                            String Title = name.getText().toString();
                            String desc = "Remember to take " + Title;
                            String name = PatientSchedulingActivity.this.name.getText().toString();

                            // boolean[] days = new boolean[7];
                            ArrayList<Boolean> days = new ArrayList<>();
                /*days[0] = PatientSchedulingActivity.this.Sunday.isChecked();
                days[1] = PatientSchedulingActivity.this.Monday.isChecked();
                days[2] = PatientSchedulingActivity.this.Tuesday.isChecked();
                days[3] = PatientSchedulingActivity.this.Wednesday.isChecked();
                days[4] = PatientSchedulingActivity.this.Thursday.isChecked();
                days[5] = PatientSchedulingActivity.this.Friday.isChecked();
                days[6] = PatientSchedulingActivity.this.Saturday.isChecked();*/
                            days.add(PatientSchedulingActivity.this.Sunday.isChecked());
                            days.add(PatientSchedulingActivity.this.Monday.isChecked());
                            days.add(PatientSchedulingActivity.this.Tuesday.isChecked());
                            days.add(PatientSchedulingActivity.this.Wednesday.isChecked());
                            days.add(PatientSchedulingActivity.this.Thursday.isChecked());
                            days.add(PatientSchedulingActivity.this.Friday.isChecked());
                            days.add(PatientSchedulingActivity.this.Saturday.isChecked());

                            int hour = time.getHour();
                            int minute = time.getMinute();

                            index = Math.toIntExact((long)(task.getResult().get("Total")));

                            db.collection("Patients").document(user.getUid()).update("Total", index + 1);

                            Map<String, Object> docData = new HashMap<>();
                            docData.put("Times", Arrays.asList(hour, minute));
                            docData.put("Name", name);
                            docData.put("Days", days);
                            docData.put("Index", index);
                            docData.put("History", new ArrayList<>());

                            Log.d("Testing", user.getUid() + "");
                            db.collection("Patients").document(user.getUid()).collection("Meds").add(docData);
                            createNotification(hour, minute, 0, days, Title, desc, (int) (AlarmManager.INTERVAL_DAY*7));
                        }
                    }
                });


            }
        });
    }

    public void createNotification(int hour_of_day, int minute, int second, ArrayList<Boolean> days, String title, String description, int repeat) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            //calendar.set(Calendar.DAY_OF_WEEK, day_of_week);
            for (int i = 0; i < 7; i++) {
                if (days.get(i) == false) continue;
                Calendar calendar = null;
                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour_of_day);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, second);
                calendar.set(Calendar.DAY_OF_WEEK, i + 1);
                Log.d("TEstNofits", "Here is : " + hour_of_day + " and " + minute);

                AlarmReceiver.changeVariables(title, description);
                intent1 = new Intent(PatientSchedulingActivity.this, AlarmReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(PatientSchedulingActivity.this, index, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                am = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
                am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), repeat, pendingIntent);

            }

        }
    }

    public void initUser() {
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

}
