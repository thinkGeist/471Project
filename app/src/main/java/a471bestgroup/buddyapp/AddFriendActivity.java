package a471bestgroup.buddyapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class AddFriendActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ArrayList<User> searchResults;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend);
        mAuth = FirebaseAuth.getInstance();
        searchResults = new ArrayList<User>();
        final LinearLayout addFriendLayout = (LinearLayout) findViewById(R.id.friends_layout) ;

        // Finished
        Button createEvent = (Button) findViewById(R.id.return_button);
        createEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
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
                                searchResults.add(user);
                                Toast.makeText(AddFriendActivity.this, user.getFullName(),
                                        Toast.LENGTH_SHORT).show();
                                final ToggleButton addFriend = new ToggleButton(getApplicationContext());
                                addFriend.setChecked(false);
                                addFriend.setText("Add " + searchResults.get(0).getFullName());
                                addFriendLayout.addView(addFriend);
                                addFriend.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        //Intent intent = new Intent(EventActivity.this, ScheduleActivity.class);
                                        //startActivity(intent);

                                            addFriend.setChecked(true);
                                            addFriend.setText(searchResults.get(0).getFullName() + " added");
                                            addFriend.setBackgroundColor(Color.parseColor("#1CD5ED"));
                                            addFriend.setClickable(false);
                                            dbAddFriend(searchResults.get(0));

                                    }
                                });

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

    private void dbAddFriend(User toAdd){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef = dbRef.child("server").child("user-data").child("users").child(mAuth.getCurrentUser().getUid()).child("friends");
        Friend friend = new Friend(toAdd.getUsername(), toAdd.getFullName(), toAdd.getUid());
        Map<String, Object> users = friend.toMap();
        dbRef.child(toAdd.getUsername()).setValue(users);

    }
}
