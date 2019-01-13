package com.example.praneethguduguntla.medic;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PatientSignUpActivity extends AppCompatActivity {

    private Button button;
    private String name;
    private String email;
    private String password;
    private String code;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String docUID;
    private boolean authed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_sign_up);

        authed = false;


        db = FirebaseFirestore.getInstance();
        docUID = code;

        mAuth = FirebaseAuth.getInstance();
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = ((EditText) findViewById(R.id.name)).getText().toString();
                email = ((EditText) findViewById(R.id.emailDocSignup)).getText().toString();
                password = ((EditText) findViewById(R.id.password)).getText().toString();
                code = ((EditText) findViewById(R.id.code)).getText().toString();
                initDocUID();
                check();
            }
        });
    }

    public void createPatient(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("hello", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Map<String, Object> patientData = new HashMap<>();
                            patientData.put("Name", PatientSignUpActivity.this.name);
                            patientData.put("Email", user.getEmail());
                            patientData.put("DocUID", docUID);
                            patientData.put("Total", 0);

                            db.collection("Doctors").document(docUID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    ArrayList<String> patients = (ArrayList<String>) (task.getResult().get("patients"));
                                    patients.add(mAuth.getCurrentUser().getUid());
                                    db.collection("Doctors").document(docUID).update("patients", patients);

//                                    db.collection("Patients").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(patientData);
                                    Intent i = new Intent(PatientSignUpActivity.this, PatientSchedulingActivity.class);
                                    startActivityForResult(i, 0);
                                }
                            });

                            authed = true;
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("hello", "signInWithEmail:failure", task.getException());
                            Toast.makeText(PatientSignUpActivity.this, "Authentication failed. Try again.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });

    }

    private void check(){
        if(!authed){
            Toast.makeText(PatientSignUpActivity.this, "Authentication failed, try again. Double check your doc code.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void initDocUID(){
        db.collection("Doctors").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(document.getData().get("code").equals(code)){
                            docUID = document.getId();
                            ArrayList<String> patients = (ArrayList<String>) (document.getData().get("patients"));
//                            patients.add(mAuth.getCurrentUser().getUid());
//                            db.collection("Doctors").document(docUID).update("patients", patients);
                            createPatient(email, password);
                            break;
                        }
                    }
                }
            }
        });
    }
}
