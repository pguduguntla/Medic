package com.example.praneethguduguntla.medic;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class PatientListActivity extends AppCompatActivity {

    ListView patients;
    MedViewAdapter adapter;
    FirebaseUser user;
    FirebaseFirestore db;
    TextView nameDoc;
    TextView code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);
        db = FirebaseFirestore.getInstance();
        initUser();
        nameDoc = (TextView)findViewById(R.id.nameDoc);
        patients = (ListView)findViewById(R.id.patientList);
        code = (TextView)findViewById(R.id.code);
        patients.setAdapter(adapter);
        patients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String full = (((TextView)view).getText()).toString();
                String uid = full.substring(full.indexOf(":") + 1);
                Intent intent = new Intent(PatientListActivity.this, PatientView.class);
                intent.putExtra("uid", uid);
                intent.putExtra("isDoctor", true);
                startActivity(intent);
            }
        });
        db.collection("Doctors").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    String text = task.getResult().get("Name").toString();
                    String codeText = task.getResult().get("Code").toString();
                    nameDoc.setText(text);
                    code.setText(codeText);
                    ArrayList<String> uids = (ArrayList<String>)(task.getResult().get("Patients"));
                    for(String uid : uids) {
                        db.collection("Patients").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()) {
                                    String text = task.getResult().get("Name").toString() + ", ID: " + task.getResult().getId();
                                    adapter.add(task.getResult().get("Name").toString());
                                }
                            }
                        });
                    }

                }
            }
        });







    }

    public void initUser() {
        user = FirebaseAuth.getInstance().getCurrentUser();
    }
}
