package com.example.praneethguduguntla.medic;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
    PatientViewAdapter adapter;
    FirebaseUser user;
    FirebaseFirestore db;
    TextView nameDoc;
    TextView code;
    Button reload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);
        db = FirebaseFirestore.getInstance();
        initUser();
        adapter = new PatientViewAdapter(getApplicationContext());
        nameDoc = (TextView)findViewById(R.id.nameDoc);
        patients = (ListView)findViewById(R.id.patientList);
        code = (TextView)findViewById(R.id.code);
        patients.setAdapter(adapter);
        reload = (Button) findViewById(R.id.reloadList);
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PatientListActivity.class);
                startActivity(i);
            }
        });

        patients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = ((RelativeLayout)view).findViewById(R.id.message_body);
                String full = textView.getText().toString();
                String uid = full.substring(full.lastIndexOf(" ") + 1);
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
                    String text = task.getResult().get("name").toString();
                    String codeText = task.getResult().get("code").toString();
                    nameDoc.setText(text);
                    code.setText("Code: " + codeText);
                    ArrayList<String> uids = (ArrayList<String>)(task.getResult().get("patients"));
                    for(final String uid : uids) {
                        db.collection("Patients").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful() && task.getResult().exists()) {
                                    Log.e("------>", uid);
                                    String text = task.getResult().get("Name").toString() + " " + uid;
                                    adapter.add(text);
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
