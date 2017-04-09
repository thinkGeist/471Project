package a471bestgroup.buddyapp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendsActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ArrayList<Friend> friendArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        friendArrayList = new ArrayList<>();

        final View.OnClickListener openFriend = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button pressed = (Button)v;
                String id = (String)pressed.getTag();

                Intent intent = new Intent(FriendsActivity.this, ViewUserProfileActivity.class);
                Bundle b = new Bundle();
                System.out.println("USER: " + id);
                b.putString("userId", id);
                intent.putExtras(b);
                startActivity(intent);
            }
        };
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

        final LinearLayout friendList = (LinearLayout)findViewById(R.id.friends_layout);

        // Get friends list
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef = dbRef.child("server").child("user-data").child("users").child(mAuth.getCurrentUser().getUid()).child("friends");
        Query query = dbRef.orderByChild("fullName");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Friend friend = postSnapshot.getValue(Friend.class);
                    friendArrayList.add(friend);
                    System.out.println("FAS: " + friendArrayList.size());
                    Toast.makeText(getApplicationContext(), friend.getFullName(), Toast.LENGTH_LONG).show();
                }
                for(int i=0; i<friendArrayList.size(); i++){
                    final Button friend = new Button(getApplicationContext());
                    friend.setText(friendArrayList.get(i).getFullName());
                    friend.setTag(friendArrayList.get(i).getUid());
                    friendList.addView(friend);
                    friend.setOnClickListener(openFriend);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void onStart() {
        super.onStart();
    }


}
