package a471bestgroup.buddyapp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by angelaranola on 2017-04-04.
 */

public class Event {
    private String ownerId;
    private int eventId;
    private String name;
    private int day;
    private int month;
    private int year;
    private String address;
    public static int COUNTER = 0;

    public Event(String name, int month, int day, int year, String address, String ownerId) {
        this.ownerId = ownerId;
        this.name = name;
        this.day = day;
        this.month = month;
        this.year = year;
        this.address = address;
        this.eventId = COUNTER;
    }

    public Event() {}

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
