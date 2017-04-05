package a471bestgroup.buddyapp;

/**
 * Created by angelaranola on 2017-04-04.
 */

public class Event {

    private int eventId;
    private String name;
    private int day;
    private int month;
    private int year;
    private String address;

    public Event(String name, int day, int month, int year, String address) {
        this.name = name;
        this.day = day;
        this.month = month;
        this.year = year;
        this.address = address;
    }

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
