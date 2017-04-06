package a471bestgroup.buddyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by angelaranola on 2017-04-06.
 */

public class EventActivity extends AppCompatActivity {
    private String eventId;
    private TextView name_of_event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        name_of_event = (TextView) findViewById(R.id.name_of_event);

        Bundle b = getIntent().getExtras();
        eventId = Integer.toString(b.getInt("eventId"));

        //Profile
        Button profile = (Button) findViewById(R.id.profile_button);
        profile.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        //Schedule
        Button schedule = (Button) findViewById(R.id.schedule_button);
        schedule.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventActivity.this, ScheduleActivity.class);
                startActivity(intent);
            }
        });

        //Calendar
        Button calendar = (Button) findViewById(R.id.calendar_button);
        calendar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        //Friends
        Button friends = (Button) findViewById(R.id.friends_button);
        friends.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventActivity.this, EventActivity.class);
                startActivity(intent);
            }
        });

        showEventDetails();
    }

    public void showEventDetails() {
        name_of_event.setText(eventId);
    }
}
