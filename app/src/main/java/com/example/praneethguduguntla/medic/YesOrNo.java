package com.example.praneethguduguntla.medic;

import android.app.AlarmManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YesOrNo extends AppCompatActivity {
    private Button yes;
    private Button no;
    private FirebaseFirestore db;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yes_or_no);
        yes = (Button) findViewById(R.id.ThisButton);
        no = (Button) findViewById(R.id.ThisButton2);
        db = FirebaseFirestore.getInstance();
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saidYes(view);
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saidNo(view);
            }
        });



    }

    public void saidYes(final View view) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("Patients").document(user.getUid()).collection("Meds").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int ind = getIntent().getIntExtra("index", -1);
                    List<DocumentSnapshot> docs = task.getResult().getDocuments();
                    for (DocumentSnapshot doc : docs) {
                        Log.d("Hoping", "Well: " + doc.get("Index"));
                        //Math.toIntExact((long)(task.getResult().get("Total")))
                        if (Math.toIntExact((long) doc.get("Index")) == ind) {
                            ArrayList<String> list = (ArrayList<String>) (doc.get("History"));
                            Calendar c = Calendar.getInstance();
                            list.add(c.get(Calendar.YEAR) + " " + (c.get(Calendar.MONTH)+1) + " " + c.get(Calendar.DAY_OF_MONTH) + " 1");
                            db.collection("Patients").document(user.getUid()).collection("Meds").document(doc.getId()).update("History", list);
                        }
                    }
                }
                Intent i = new Intent(view.getContext(), PatientView.class);
                startActivityForResult(i, 0);

            }
        });

    }

    public void saidNo(final View view) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("Patients").document(user.getUid()).collection("Meds").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int ind = getIntent().getIntExtra("index", -1);
                    List<DocumentSnapshot> docs = task.getResult().getDocuments();
                    for (DocumentSnapshot doc : docs) {
                        Log.d("Hoping", "Well: " + doc.get("Index"));
                        //Math.toIntExact((long)(task.getResult().get("Total")))
                        if (Math.toIntExact((long) doc.get("Index")) == ind) {
                            ArrayList<String> list = (ArrayList<String>) (doc.get("History"));
                            Calendar c = Calendar.getInstance();
                            list.add(c.get(Calendar.YEAR) + " " + (c.get(Calendar.MONTH)+1) + " " + c.get(Calendar.DAY_OF_MONTH) + " 0");
                            db.collection("Patients").document(user.getUid()).collection("Meds").document(doc.getId()).update("History", list);
                        }
                    }
                }
                Intent i = new Intent(view.getContext(), PatientView.class);
                startActivityForResult(i, 0);

            }
        });
    }
}
