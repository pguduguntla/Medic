package com.example.praneethguduguntla.medic;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.scaledrone.lib.Listener;
import com.scaledrone.lib.Member;
import com.scaledrone.lib.Room;
import com.scaledrone.lib.RoomListener;
import com.scaledrone.lib.Scaledrone;

import java.util.ArrayList;
import java.util.Arrays;

public class MessagingActivity extends AppCompatActivity implements RoomListener {

    private String channelID = "aameFIoSN2i4rpwF";
    private String roomName = "sudoku";
    private EditText editText;
    private Scaledrone scaledrone;
    private Button send;
    private MessageAdapter messageAdapter;
    private ListView messagesView;
    private FirebaseFirestore db;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        initRoomName();

    }

    public void setUpRoom() {
        messageAdapter = new MessageAdapter(this);
        messagesView = (ListView) findViewById(R.id.messages_view);
        messagesView.setAdapter(messageAdapter);
        editText = (EditText) findViewById(R.id.editText);
        send = (Button) findViewById(R.id.send);

        ArrayList<String> data = new ArrayList<>(Arrays.asList("name", "#4286f4"));

        scaledrone = new Scaledrone(channelID, data);
        scaledrone.connect(new Listener() {
            @Override
            public void onOpen() {
                Log.d("---->", "Scaledrone connection open");
                // Since the MainActivity itself already implement RoomListener we can pass it as a target
                scaledrone.subscribe(roomName, MessagingActivity.this);
                Log.d("---->", "Subscribed");
            }

            @Override
            public void onOpenFailure(Exception ex) {
                System.err.println(ex);
            }

            @Override
            public void onFailure(Exception ex) {
                System.err.println(ex);
            }

            @Override
            public void onClosed(String reason) {
                System.err.println(reason);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editText.getText().toString();
                if (message.length() > 0) {
                    scaledrone.publish(roomName, message);
                    editText.getText().clear();
                }
            }
        });
    }

    @Override
    public void onOpen(Room room) {
        Log.d("tag", "lots of success");
    }

    @Override
    public void onOpenFailure(Room room, Exception ex) {
        Log.d("tag", ex.getMessage());
    }

    public void onMessage(Room room, final JsonNode json, final Member member) {
        // To transform the raw JsonNode into a POJO we can use an ObjectMapper
        Log.d("------>", "lots of success am i rite");

        final ObjectMapper mapper = new ObjectMapper();
        try {
            // member.clientData is a MemberData object, let's parse it as such
            final ArrayList<String> data = (ArrayList<String>) (mapper.treeToValue(member.getClientData(), ArrayList.class));
            // if the clientID of the message sender is the same as our's it was sent by us
            boolean belongsToCurrentUser = member.getId().equals(scaledrone.getClientID());
            // since the message body is a simple string in our case we can use json.asText() to parse it as such
            // if it was instead an object we could use a similar pattern to data parsing
            final Message message = new Message(json.asText(), data, belongsToCurrentUser);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    messageAdapter.add(message);
                    // scroll the ListView to the last added element
                    messagesView.setSelection(messagesView.getCount() - 1);
                }
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void initRoomName() {
        db.collection("Patients").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // We are a patient
                    roomName = "observable-" + user.getUid();
                    TextView tv = (TextView) findViewById(R.id.nameOfTexter);
                    tv.setText("Doctor");
                    setUpRoom();
                } else {
                    // We are a doctor
                    String name = getIntent().getStringExtra("patientName");
                    TextView tv = (TextView) findViewById(R.id.nameOfTexter);
                    tv.setText("name");
                    setUpRoom();
                }
            }
        });
    }
}
