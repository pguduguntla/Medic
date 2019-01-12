package com.example.praneethguduguntla.medic;

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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DoctorSignUpActivity extends AppCompatActivity {

    private Button button;
    private String name;
    private String email;
    private String password;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_sign_up);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        button = (Button) findViewById(R.id.signUpDoc);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = ((EditText) findViewById(R.id.nameDocSignup)).getText().toString();
                email = ((EditText) findViewById(R.id.emailDocSignup)).getText().toString();
                password = ((EditText) findViewById(R.id.passwordDocSignup)).getText().toString();
                createDoctor(email, password, name);
            }
        });
    }

    public void createDoctor(String email, String password, String name){
        boolean added = false;
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("hello", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Map<String, Object> userData = new HashMap<>();
                            userData.put("name", "guy");
                            userData.put("email", user.getEmail());
                            userData.put("code", "123");

                            db.collection("Doctors").document(user.getUid()).set(userData);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("hello", "signInWithEmail:failure", task.getException());
                            Toast.makeText(DoctorSignUpActivity.this, "Authentication failed. Try again.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    public String generateCode(){
        return "123";
    }
}
