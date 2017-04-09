package a471bestgroup.buddyapp;

import android.content.Intent;
import android.graphics.Color;
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
    private DatabaseReference userDataRef = database.getReference("server/user-data");
    private DatabaseReference usersRef = userDataRef.child("users");
    private DatabaseReference userRef = usersRef.child(mAuth.getCurrentUser().getUid());
    private DatabaseReference userRegEventsRef = userRef.child("regEvents");
    private DatabaseReference eventDataRef = database.getReference("server/event-data");
    private DatabaseReference eventsRef = eventDataRef.child("events");
    private DatabaseReference eventRef;
    private DatabaseReference eventRegUsersRef;
    private String eventId;
    private TextView name_of_event;
    private TextView location_of_event;
    private Button host_of_event;
    private TextView month_of_event;
    private TextView day_of_event;
    private TextView year_of_event;
    private Event event;
    private User host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        name_of_event = (TextView) findViewById(R.id.name_of_event);
        location_of_event = (TextView) findViewById(R.id.location_of_event);
        host_of_event = (Button) findViewById(R.id.host_of_event);
        month_of_event = (TextView) findViewById(R.id.month_of_event);
        day_of_event = (TextView) findViewById(R.id.day_of_event);
        year_of_event = (TextView) findViewById(R.id.year_of_event);

        Bundle b = getIntent().getExtras();
        eventId = Integer.toString(b.getInt("eventId"));
        eventRef = eventsRef.child(eventId);
        eventRegUsersRef = eventRef.child("regUsers");

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
                    event = eventSnapshot.getValue(Event.class);
                    name_of_event.setText(event.getName());
                    location_of_event.setText(event.getAddress());
                    getHost();
                    month_of_event.setText(Integer.toString(event.getMonth()) + "/");
                    day_of_event.setText(Integer.toString(event.getDay()) + "/");
                    year_of_event.setText(Integer.toString(event.getYear()));
                    showRegUsers();

                    // check if this event created by current user
                    if(!(mAuth.getCurrentUser().getUid()).equals(event.getOwnerId())) {
                        registered();
                    } else {
                        Button button = new Button(getApplicationContext());
                        button.setHeight(150);
                        button.setWidth(registerUnregister.getWidth());
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
        // remove event from all users registered
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String userId = postSnapshot.getKey();
                    final DatabaseReference userRef = usersRef.child(userId);
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot userSnapshot) {
                            if(userSnapshot.child("regEvents").exists()) {
                                DatabaseReference userEventRef = userRef.child("regEvents");
                                for(DataSnapshot eventSnapshot : userSnapshot.getChildren()) {
                                    if(eventSnapshot.child(eventId).exists())
                                        userEventRef.child(eventId).removeValue();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Toast.makeText(getApplicationContext(), "EVENT CANCELLED", Toast.LENGTH_LONG).show();
        finish();
    }

    public void register() {
        userRegEventsRef.child(eventId).setValue(eventId);
        eventRegUsersRef.child(mAuth.getCurrentUser().getUid()).setValue(mAuth.getCurrentUser().getUid());
        Toast.makeText(getApplicationContext(), "REGISTERED", Toast.LENGTH_LONG).show();
        finish();
    }

    public void unregister() {
        userRegEventsRef.child(eventId).removeValue();
        eventRegUsersRef.child(mAuth.getCurrentUser().getUid()).removeValue();
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
                    button.setWidth(registerUnregister.getWidth());
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
                    button.setWidth(registerUnregister.getWidth());
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

    public void getHost() {
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(event.getOwnerId()).exists()) {
                    DataSnapshot hostSnapshot = dataSnapshot.child(event.getOwnerId());
                    host = hostSnapshot.getValue(User.class);
                    host_of_event.setText(host.getUsername());
                    host_of_event.setTextColor(Color.BLUE);
                    host_of_event.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(EventActivity.this, ViewUserProfileActivity.class);
                            Bundle b = new Bundle();
                            b.putString("userId", event.getOwnerId());
                            intent.putExtras(b);
                            startActivity(intent);
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {}
        });
    }

    public void showRegUsers() {
        final LinearLayout regUsersEvent = (LinearLayout) findViewById(R.id.regUsersLayout);
        eventRegUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    final String userId = postSnapshot.getValue(String.class);
                    usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child(userId).exists()) {
                                DataSnapshot userSnapshot = dataSnapshot.child(userId);
                                User user = userSnapshot.getValue(User.class);
                                Button button = new Button(getApplicationContext());
                                button.setHeight(15000);
                                regUsersEvent.addView(button);
                                button.setText(user.getUsername());
                                button.setOnClickListener(new View.OnClickListener(){

                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(EventActivity.this, ViewUserProfileActivity.class);
                                        Bundle b = new Bundle();
                                        b.putString("userId", userId);
                                        intent.putExtras(b);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {}
        });
    }
}
