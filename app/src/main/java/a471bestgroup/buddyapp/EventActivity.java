package a471bestgroup.buddyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * Created by angelaranola on 2017-04-06.
 */

public class EventActivity extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference eventDataRef = database.getReference("server/event-data");
    DatabaseReference userDataRef = database.getReference("server/user-data");
    DatabaseReference eventsRef = eventDataRef.child("events");
    DatabaseReference usersRef = userDataRef.child("users");
    DatabaseReference userRef = usersRef.child(mAuth.getCurrentUser().getUid());
    DatabaseReference userRegEventsRef = userRef.child("regEvents");
    private String eventId;
    private TextView name_of_event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        name_of_event = (TextView) findViewById(R.id.name_of_event);

        Bundle b = getIntent().getExtras();
        eventId = Integer.toString(b.getInt("eventId"));

        //Back
        Button profile = (Button) findViewById(R.id.back_button);
        profile.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        showEventDetails();
    }

    public void showEventDetails() {
        final LinearLayout registerUnregister = (LinearLayout)findViewById(R.id.registerLayout);
        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(eventId).exists()) {
                    DataSnapshot eventSnapshot = dataSnapshot.child(eventId);
                    final Event event = eventSnapshot.getValue(Event.class);
                    name_of_event.setText(event.getName());

                    // check if this event created by current user
                    if(!(mAuth.getCurrentUser().getUid()).equals(event.getOwnerId())) {
                        registered();
                    } else {
                        Button button = new Button(getApplicationContext());
                        button.setHeight(150);
                        registerUnregister.addView(button);
                        button.setText("Cancel Event");
                        button.setOnClickListener(new View.OnClickListener(){

                            @Override
                            public void onClick(View v) {
                                cancelEvent();
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {}
        });
    }

    public void cancelEvent() {
        eventsRef.child(eventId).removeValue();
        Toast.makeText(getApplicationContext(), "EVENT CANCELLED", Toast.LENGTH_LONG).show();
        finish();
    }

    public void register() {
        ArrayList<String> regEvents = new ArrayList<>();
        regEvents.add(eventId);
        userRef.child("regEvents").setValue(regEvents);
        Toast.makeText(getApplicationContext(), "REGISTERED", Toast.LENGTH_LONG).show();
        finish();
    }

    public void unregister() {
        userRegEventsRef.child(eventId).removeValue();
        Toast.makeText(getApplicationContext(), "UNREGISTERED", Toast.LENGTH_LONG).show();
        finish();
    }

    public void registered() {
        final LinearLayout registerUnregister = (LinearLayout)findViewById(R.id.registerLayout);
        userRegEventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                // user already registered in event
                if(dataSnapshot.child(eventId).exists()) {
                    Button button = new Button(getApplicationContext());
                    button.setHeight(150);
                    registerUnregister.addView(button);
                    button.setText("Unregister");
                    button.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            unregister();
                        }
                    });
                } else {
                    // user not yet registered in event
                    Button button = new Button(getApplicationContext());
                    button.setHeight(150);
                    registerUnregister.addView(button);
                    button.setText("Register");
                    button.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            register();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {}
        });
    }
}
