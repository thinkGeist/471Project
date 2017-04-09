package a471bestgroup.buddyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

/**
 * Created by angelaranola on 2017-04-08.
 */

public class ViewUserProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference userDataRef = database.getReference("server/user-data");
    private DatabaseReference usersRef = userDataRef.child("users");
    private DatabaseReference userRef;
    private DatabaseReference userRegEventsRef;
    private DatabaseReference eventDataRef = database.getReference("server/event-data");
    private DatabaseReference eventsRef = eventDataRef.child("events");

    private String userId;
    private TextView username;
    private TextView name;
    private TextView date_of_birth;
    private TextView country;
    private TextView province;
    private TextView city;
    private TextView noRegEvents;

    private LinearLayout regEventsLayout;
    private LinearLayout hostEventsLayout;
    private View profileView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        username = (TextView) findViewById(R.id.username);
        name = (TextView) findViewById(R.id.name);
        date_of_birth = (TextView) findViewById(R.id.date_of_birth);
        country = (TextView) findViewById(R.id.country);
        province = (TextView) findViewById(R.id.province);
        city = (TextView) findViewById(R.id.city);

        regEventsLayout = (LinearLayout) findViewById(R.id.regEventsLayout);
        hostEventsLayout = (LinearLayout) findViewById(R.id.hostEventsLayout);
        profileView = findViewById(R.id.profileView);

        Bundle b = getIntent().getExtras();
        userId = b.getString("userId");
        System.out.println("USER: " +userId);

        mAuth = FirebaseAuth.getInstance();

        //Profile
        Button profile = (Button) findViewById(R.id.profile_button);
        profile.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewUserProfileActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        //Schedule
        Button schedule = (Button) findViewById(R.id.schedule_button);
        schedule.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewUserProfileActivity.this, ScheduleActivity.class);
                startActivity(intent);
            }
        });

        //Calendar
        Button calendar = (Button) findViewById(R.id.calendar_button);
        calendar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewUserProfileActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        //Friends
        Button friends = (Button) findViewById(R.id.friends_button);
        friends.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewUserProfileActivity.this, FriendsActivity.class);
                startActivity(intent);
            }
        });

        //Back
        Button back = (Button) findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Set values for user info
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(userId).exists()) {
                    DataSnapshot userSnapshot = dataSnapshot.child(userId);
                    User user = userSnapshot.getValue(User.class);
                    username.setText(user.getUsername());
                    name.setText(user.getFullName());
                    date_of_birth.setText(user.getDateOfBirth());
                    country.setText(user.getCountry());
                    province.setText(user.getProvince());
                    city.setText(user.getCity());
                    showRegEvents(user);
                    showHostEvents();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // user's own events
    public void showHostEvents() {
        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() != 0) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Event event = postSnapshot.getValue(Event.class);
                        if ((event.getOwnerId()).equals(userId))
                            newButtons(hostEventsLayout, event);
                    }
                } else {
                    TextView noHostEvents = new TextView(getApplicationContext());
                    noHostEvents.setHeight(15000);
                    noRegEvents.setGravity(11);
                    hostEventsLayout.addView(noHostEvents);
                    noHostEvents.setText("This user has not hosted any events");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // user's registered events
    public void showRegEvents(User user) {
        userRef = usersRef.child(userId);
        userRegEventsRef = userRef.child("regEvents");
        userRegEventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() != 0) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        final String eventId = postSnapshot.getValue(String.class);
                        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child(eventId).exists()) {
                                    DataSnapshot childSnapshot = dataSnapshot.child(eventId);
                                    final Event event = childSnapshot.getValue(Event.class);
                                    newButtons(regEventsLayout, event);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError firebaseError) {
                            }
                        });
                    }
                }  else {
                    TextView noRegEvents = new TextView(getApplicationContext());
                    noRegEvents.setHeight(15000);
                    noRegEvents.setGravity(11);
                    regEventsLayout.addView(noRegEvents);
                    noRegEvents.setText("This user has not registered for any events");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void newButtons(final LinearLayout layout, final Event event) {
        final Button button = new Button(getApplicationContext());
        button.setHeight(15000);
        layout.addView(button);
        button.setText(event.getName());
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewUserProfileActivity.this, EventActivity.class);
                Bundle b = new Bundle();
                b.putInt("eventId", event.getEventId());
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }
}
