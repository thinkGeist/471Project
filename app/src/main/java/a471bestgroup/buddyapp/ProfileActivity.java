package a471bestgroup.buddyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private User currentUser;
    private FirebaseUser user;

    private LinearLayout regEvents = null;
    private LinearLayout upcomingEvents = null;
    private LinearLayout myEvents = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        regEvents = (LinearLayout)findViewById(R.id.regLayout);
        upcomingEvents = (LinearLayout)findViewById(R.id.upcomingLayout);
        myEvents = (LinearLayout)findViewById(R.id.myEventsLayout);
//        ReplaceFont.replaceDefaultFont(this, "sans-serif-medium", "Nunito-ExtraLight.ttf");

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Toast.makeText(ProfileActivity.this, user.getEmail(),
                            Toast.LENGTH_SHORT).show();
                    ValueEventListener userListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            currentUser = dataSnapshot.getValue(User.class);
                            // Toast.makeText(this, currentUser.toString(), Toast.LENGTH_LONG);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Toast.makeText(ProfileActivity.this, "User needs to log in",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                // ...
            }
        };

        //Profile
        Button profile = (Button) findViewById(R.id.profile_button);
        profile.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        //Schedule
        Button schedule = (Button) findViewById(R.id.schedule_button);
        schedule.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ScheduleActivity.class);
                startActivity(intent);
            }
        });

        //Calendar
        Button calendar = (Button) findViewById(R.id.calendar_button);
        calendar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        //Friends
        Button friends = (Button) findViewById(R.id.friends_button);
        friends.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, FriendsActivity.class);
                startActivity(intent);
            }
        });

        //Sign out
        Button signOut = (Button) findViewById(R.id.sign_out_button);
        signOut.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        user = mAuth.getCurrentUser();
        TextView name = (TextView) findViewById(R.id.tvName);
        if (user != null) {
            name.setText(user.getDisplayName());
            displayEvents();
        }
        else {
            Intent intent = new Intent(ProfileActivity.this, CreateEventActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void newButtons(final LinearLayout layout, final Event event) {
        final Button button = new Button(getApplicationContext());
        button.setHeight(15000);
        layout.addView(button);
        button.setText(event.getName());
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EventActivity.class);
                Bundle b = new Bundle();
                b.putInt("eventId", event.getEventId());
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    public void displayEvents() {
        upcomingEvents.removeAllViews(); // delete buttons to avoid doubling buttons
        regEvents.removeAllViews();
        myEvents.removeAllViews();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference eventDataRef = database.getReference("server/event-data");
        final DatabaseReference eventsRef = eventDataRef.child("events");
        DatabaseReference usersRef = database.getReference("server/user-data/users");
        DatabaseReference userRef = usersRef.child(mAuth.getCurrentUser().getUid());
        DatabaseReference userRegEventsRef = userRef.child("regEvents");

        // draw buttons for events the user is registered in
        userRegEventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    final String eventId = postSnapshot.getValue(String.class);
                    eventsRef.addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child(eventId).exists()) {
                                DataSnapshot childSnapshot = dataSnapshot.child(eventId);
                                final Event event = childSnapshot.getValue(Event.class);
                                newButtons(regEvents, event);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError firebaseError) {}
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        // draw buttons for upcoming events
        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    final Event event = postSnapshot.getValue(Event.class);
                    newButtons(upcomingEvents, event);
                }
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {}
        });

        // draw buttons for user's own events
        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    final Event event = postSnapshot.getValue(Event.class);
                    if((event.getOwnerId()).equals(mAuth.getCurrentUser().getUid()))
                        newButtons(myEvents, event);
                }
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {}
        });
    }
}