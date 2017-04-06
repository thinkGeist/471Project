package a471bestgroup.buddyapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddFriendActivity extends AppCompatActivity {

    private ArrayList<String> searchResults;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend);


        //Cancel
        Button cancel = (Button) findViewById(R.id.cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Create Event
        Button createEvent = (Button) findViewById(R.id.create_event_button);
        createEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(EventActivity.this, ScheduleActivity.class);
                //startActivity(intent);

                Toast.makeText(getApplicationContext(), "I STILL HAVE TO FIGURE OUT THE DATABASE THINGY", Toast.LENGTH_LONG).show();
            }
        });

        Button search = (Button) findViewById(R.id.button_search);
        search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Search for friends and update friends layout
                EditText searchParam = (EditText) findViewById(R.id.search_param);
                String toSearch = searchParam.getText().toString();
                final LinearLayout addList = (LinearLayout) findViewById(R.id.friends_layout);
                if(!toSearch.isEmpty()) {
                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                    dbRef = dbRef.child("server").child("user-data").child("users");
                    Query query = dbRef.orderByChild("username").equalTo(toSearch);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                                User user  = postSnapshot.getValue(User.class);
                                Toast.makeText(AddFriendActivity.this, user.getFullName(),
                                        Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }
}
