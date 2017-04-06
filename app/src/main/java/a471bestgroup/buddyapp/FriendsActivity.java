package a471bestgroup.buddyapp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        //Profile
        Button profile = (Button) findViewById(R.id.profile_button);
        profile.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendsActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        //Schedule
        Button schedule = (Button) findViewById(R.id.schedule_button);
        schedule.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendsActivity.this, ScheduleActivity.class);
                startActivity(intent);
            }
        });

        //Calendar
        Button calendar = (Button) findViewById(R.id.calendar_button);
        calendar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendsActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        //Friends
        Button friends = (Button) findViewById(R.id.friends_button);
        friends.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendsActivity.this, FriendsActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton addFriend = (FloatingActionButton) findViewById(R.id.button_addFriend);
        addFriend.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendsActivity.this, AddFriendActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout friendList = (LinearLayout)findViewById(R.id.friends_layout);
        for(int i=0; i<10; i++){
            TextView tv = new TextView(getApplicationContext());
            tv.setTextSize(100);
            tv.setText(Integer.toString(i));
            friendList.addView(tv);
        }

    }
    public void onStart() {
        super.onStart();
    }
}
