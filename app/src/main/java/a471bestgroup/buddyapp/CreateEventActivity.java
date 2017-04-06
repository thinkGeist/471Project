package a471bestgroup.buddyapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class CreateEventActivity extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("server/event-data");

    private int day;
    private int month;
    private int year;
    private AutoCompleteTextView mNameView;
    private AutoCompleteTextView mLocationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        Bundle b = getIntent().getExtras();
        day = b.getInt("day");
        month = b.getInt("month");
        year = b.getInt("year");

        TextView textView = (TextView) findViewById(R.id.date_of_event);
        textView.setText(month + "/" + day + "/" + year);

        mNameView = (AutoCompleteTextView) findViewById(R.id.name_event);
        mLocationView = (AutoCompleteTextView) findViewById(R.id.location_event);


        //Cancel
        Button cancel = (Button) findViewById(R.id.cancel_button);
        cancel.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Create Event
        Button createEvent = (Button) findViewById(R.id.create_event_button);
        createEvent.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(CreateEventActivity.this, ScheduleActivity.class);
                //startActivity(intent);

                //Toast.makeText(getApplicationContext(), "I STILL HAVE TO FIGURE OUT THE DATABASE THINGY", Toast.LENGTH_LONG).show();
                checkEventFields();
            }
        });

    }

    public void checkEventFields() {
        // Reset errors.
        mNameView.setError(null);
        mLocationView.setError(null);

        View focusView = null;
        boolean cancel = false;
        final String eventName = mNameView.getText().toString();
        final String eventLocation = mLocationView.getText().toString();

        // check for valid name
        if(TextUtils.isEmpty(eventName)) {
            mNameView.setError("This field is required");
            focusView = mNameView;
            cancel = true;
        }

        // check for valid location
        if(TextUtils.isEmpty(eventLocation)) {
            mNameView.setError("This field is required");
            focusView = mLocationView;
            cancel = true;
        }

        if(cancel) {
            focusView.requestFocus();
        } else {
            final DatabaseReference eventsRef = ref.child("events");
            final Event newEvent = new Event(eventName, month, day, year, eventLocation, mAuth.getCurrentUser().getUid());
            final Map<String, Event> events = new HashMap<String, Event>();
            events.put(eventName, newEvent);
            eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    while(dataSnapshot.child(Integer.toString(newEvent.getEventId())).exists()) {
                        Event.COUNTER++;
                        newEvent.setEventId(Event.COUNTER);
                        events.put(eventName, newEvent);
                    }
                    eventsRef.child(Integer.toString(newEvent.getEventId())).setValue(newEvent);
                }
                @Override
                public void onCancelled(DatabaseError firebaseError) {}
            });

            Toast.makeText(getApplicationContext(), "EVENT CREATED", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
