package a471bestgroup.buddyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class EventActivity extends AppCompatActivity {
    private int day;
    private int month;
    private int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Bundle b = getIntent().getExtras();
        day = b.getInt("day");
        month = b.getInt("month");
        year = b.getInt("year");

        TextView textView = (TextView) findViewById(R.id.date_of_event);
        textView.setText(month + "/" + day + "/" + year);


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
                //Intent intent = new Intent(EventActivity.this, ScheduleActivity.class);
                //startActivity(intent);

                Toast.makeText(getApplicationContext(), "I STILL HAVE TO FIGURE OUT THE DATABASE THINGY", Toast.LENGTH_LONG).show();
            }
        });
    }
}
