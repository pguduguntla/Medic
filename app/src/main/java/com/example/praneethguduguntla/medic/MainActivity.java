package com.example.praneethguduguntla.medic;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.net.Inet4Address;

public class MainActivity extends AppCompatActivity {

    private Button patient;
    private Button doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Patients").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful() && task.getResult().exists()) {
                        Intent i = new Intent(getApplicationContext(), PatientView.class);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(getApplicationContext(), PatientListActivity.class);
                        startActivity(i);
                    }
                }
            });
        }

        patient = (Button) findViewById(R.id.patient);
        doctor = (Button) findViewById(R.id.doctor);


        patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), PatientSignUpActivity.class);
                startActivity(i);
            }
        });

        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), DoctorSignUpActivity.class);
                startActivityForResult(i, 0);
            }
        });

    }
}
