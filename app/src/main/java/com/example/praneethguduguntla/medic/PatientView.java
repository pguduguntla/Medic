package com.example.praneethguduguntla.medic;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Scanner;

public class PatientView extends AppCompatActivity {

    private ListView lv;

    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    ArrayList<Object> allMedsList;
    ArrayList<Object> todayList;
    ArrayList<String> todayListNames;
    ArrayList<Boolean> todayListBool;
    Calendar c;
    private MedViewAdapter messageAdapter;
    private ListView messagesView;
    private TextView tv;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        tv = (TextView) findViewById(R.id.textView4);


        user = FirebaseAuth.getInstance().getCurrentUser();
        Toast.makeText(this, "" + user.getUid(), Toast.LENGTH_SHORT).show();

        // lv = (Med) findViewById(R.id.lv);


        allMedsList = new ArrayList<>();
        todayList = new ArrayList<>();

        todayListNames = new ArrayList<>();
        todayListBool = new ArrayList<>();

        db.collection("Patients").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    String name = snapshot.get("Name").toString();
                    tv.setText(name);

                }
            }
        });


        db.collection("Patients").document(user.getUid()).collection("Meds").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "get succeeded ", task.getException());
                    ArrayList<DocumentSnapshot> myList = new ArrayList<>(task.getResult().getDocuments());

                    Log.d("myList", myList.toString(), task.getException());
                    for (DocumentSnapshot doc : myList) {
                        Log.d("TAG", "getting doc: " + doc, task.getException());
                        ArrayList<Integer> days = new ArrayList<>();
                        ArrayList<Boolean> booleans = (ArrayList<Boolean>) doc.get("Days");
                        for (int i = 0; i < booleans.size(); i++) {
                            if (booleans.get(i) == true) {
                                days.add(i);
                            }
                        }

                        Log.d("TAG", "Days : " + days.toString(), task.getException());

                        ArrayList<Long> times = (ArrayList<Long>) doc.get("Times");
                        String hour = times.get(0).toString();
                        String minute = times.get(1).toString();
                        String name = doc.get("Name").toString();

                        ArrayList<String> history = (ArrayList<String>) doc.get("History");


                        ArrayList<Object> medInfo = new ArrayList<>();
                        medInfo.add(name);
                        medInfo.add(days);
                        medInfo.add(hour);
                        medInfo.add(minute);
                        medInfo.add(history);

                        allMedsList.add(medInfo);


                    }


                    ArrayList<String> names = new ArrayList<String>();
                    ArrayList<Integer> days = new ArrayList<Integer>();

                    // [[name, [0, 2, 5, 7], hour, minute], [name, day, hour, minute]]


                    c = Calendar.getInstance();
                    int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;

                    ArrayList<Integer> todayMedIndexes = new ArrayList<>();

                    for (int i = 0; i < allMedsList.size(); i++) {
                        ArrayList<Object> medInfo = (ArrayList<Object>) allMedsList.get(i);
                        ArrayList<Integer> daysOfMed = (ArrayList<Integer>) medInfo.get(1);
                        for (int j = 0; j < daysOfMed.size(); j++) {
                            if (dayOfWeek == daysOfMed.get(j)) {
                                todayMedIndexes.add(i);

                            }

                        }


                    }


                    for (int todayMedIndex : todayMedIndexes) {
                        todayList.add((ArrayList<Object>) allMedsList.get(todayMedIndex));
                    }

                    //[[name, [false, true..., true], hour, minute], [name, day, hour, minute]]
                    todayList.sort(new Comparator<Object>() {
                        @Override
                        public int compare(Object o1, Object o2) {

                            int hour = Integer.parseInt(((ArrayList<String>) o1).get(2).toString());
                            int min = Integer.parseInt(((ArrayList<String>) o1).get(3).toString());
                            int hour2 = Integer.parseInt(((ArrayList<String>) o2).get(2).toString());
                            int min2 = Integer.parseInt(((ArrayList<String>) o2).get(3).toString());
                            int currHour = c.get(Calendar.HOUR_OF_DAY);
                            int currMin = c.get(Calendar.MINUTE);
                            if (Math.abs(currHour - hour) == Math.abs(currHour - hour2)) {
                                if (Math.abs(currMin - min) < Math.abs(currMin - min2)) {
                                    return 1;
                                }
                                return 0;
                            }
                            if (Math.abs(currHour - hour) < Math.abs(currHour - hour2)) {
                                return 1;
                            }
                            return 0;
                        }
                    });

                    Log.d("TAG", "ALL Meds : " + allMedsList);
                    Log.d("TAG", "today List : " + todayList);


                    for (int i = 0; i < todayList.size(); i++) {
                        ArrayList<Object> arr = (ArrayList<Object>) todayList.get(i);
                        todayListNames.add(arr.get(0).toString());

                        ArrayList<String> history = (ArrayList<String>) arr.get(4);
                        if (history.size() == 0) {
                            todayListBool.add(false);
                            continue;
                        }
                        Log.d("TAG", "History : " + history);
                        Log.d("ANOTHER", "Size: " + history.size());
                        String hist = history.get(history.size() - 1);
                        Log.d("TAG", "History : " + hist + " i " + i);

                        Scanner scanner = new Scanner(hist);
                        String year = scanner.next();
                        String month = scanner.next();
                        String day = scanner.next();
                        String hasTaken = scanner.next();

                        c = Calendar.getInstance();
                        int currMonth = c.get(Calendar.MONTH);
                        int currDay = c.get(Calendar.DAY_OF_MONTH);

                        if (currMonth != Integer.parseInt(month)) {
                            todayListBool.add(false);
                            continue;
                        }

                        if (currDay != Integer.parseInt(day)) {
                            todayListBool.add(false);
                            continue;
                        }

                        //Toast.makeText(getApplicationContext(), history.toString(), Toast.LENGTH_LONG).show();





                        if (hasTaken.equals("1")) {
                            todayListBool.add(true);
                        } else {
                            todayListBool.add(false);
                        }


                    }
                    Log.d("TAG", "ListBool : " + todayListBool);

                    Log.d("TAG", "today List Names : " + todayListNames);

                      /*  ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                                PatientView.this,
                                android.R.layout.simple_list_item_1,
                                todayListNames );



                        /*for(int i = 0; i<todayListNames.size(); i++){
                            arrayAdapter.add(todayListNames.get(i));
                        }

                        lv.setAdapter(arrayAdapter);*/

                    messageAdapter = new MedViewAdapter(PatientView.this);
                    messageAdapter.setMessages(todayListNames);

                    messageAdapter.setHasTakenMedication(todayListBool);
                    messagesView = (ListView) findViewById(R.id.lv);

                    messagesView.setAdapter((ListAdapter) messageAdapter);

                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }

            }

        });


    }


}
