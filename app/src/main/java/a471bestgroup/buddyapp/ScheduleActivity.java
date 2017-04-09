package a471bestgroup.buddyapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.List;

import static android.content.ContentValues.TAG;

/* List views are based off https://www.youtube.com/watch?v=WRANgDgM2Zg */

public class ScheduleActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private User currentUser;
    private FirebaseUser user;

    private ListView regEvents = null;
    private List<Event> eventList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        regEvents = (ListView) findViewById(R.id.schedule_list);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    //Toast.makeText(ScheduleActivity.this, user.getEmail(),
                            //Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ScheduleActivity.this, "User needs to log in",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ScheduleActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                // ...
            }
        };

        populateList();

        //Profile
        Button profile = (Button) findViewById(R.id.profile_button);
        profile.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScheduleActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        //Schedule
        Button schedule = (Button) findViewById(R.id.schedule_button);
        schedule.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScheduleActivity.this, ScheduleActivity.class);
                startActivity(intent);
            }
        });

        //Calendar
        Button calendar = (Button) findViewById(R.id.calendar_button);
        calendar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScheduleActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        //Friends
        Button friends = (Button) findViewById(R.id.friends_button);
        friends.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScheduleActivity.this, FriendsActivity.class);
                startActivity(intent);
            }
        });

    }

    private void populateList() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference eventDataRef = database.getReference("server/event-data");
        final DatabaseReference eventsRef = eventDataRef.child("events");
        DatabaseReference usersRef = database.getReference("server/user-data/users");
        DatabaseReference userRef = usersRef.child(mAuth.getCurrentUser().getUid());
        DatabaseReference userRegEventsRef = userRef.child("regEvents");

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
                                eventList.add(event);
                                populateListView();
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
        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    final Event event = postSnapshot.getValue(Event.class);
                    if((event.getOwnerId()).equals(mAuth.getCurrentUser().getUid()))
                        eventList.add(event);
                        populateListView();                }
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {}
        });
    }

    private void populateListView() {
        ArrayAdapter<Event> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.schedule_list);
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<Event> {
        public MyListAdapter() {
            super(ScheduleActivity.this, R.layout.single_event_element, eventList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View itemView = convertView;

            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.single_event_element, parent, false);
            }

            // Find the car to work with.
            final Event currentEvent = eventList.get(position);

            // Name:
            TextView nameText = (TextView) itemView.findViewById(R.id.eventName);
            nameText.setText(currentEvent.getName());

            // Date:
            TextView dateText = (TextView) itemView.findViewById(R.id.eventDate);
            dateText.setText(currentEvent.getDay() + "/" + currentEvent.getMonth() + "/" + currentEvent.getYear());

            // Location:
            TextView locText = (TextView) itemView.findViewById(R.id.eventLocation);
            locText.setText(currentEvent.getAddress());

            return itemView;
        }
    }

}
