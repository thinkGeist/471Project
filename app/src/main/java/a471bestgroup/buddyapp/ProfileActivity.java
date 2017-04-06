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
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private User currentUser;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
//        ReplaceFont.replaceDefaultFont(this, "sans-serif-medium", "Nunito-ExtraLight.ttf");

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                System.out.println(user);
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

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        if(user != null) {
            TextView name = (TextView) findViewById(R.id.tvName);
            name.setText(user.getDisplayName());
        }

        LinearLayout regEvents = (LinearLayout)findViewById(R.id.regLayout);
        for(int i=0; i<10; i++){
            Button button = new Button(getApplicationContext());
            button.setHeight(15000);
            regEvents.addView(button);
            button.setText(Integer.toString(i));
        }

        LinearLayout upcomingEvents = (LinearLayout)findViewById(R.id.upcomingLayout);
        for(int i=0; i<10; i++){
            Button button = new Button(getApplicationContext());
            button.setHeight(15000);
            upcomingEvents.addView(button);
            button.setText(Integer.toString(i));
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
