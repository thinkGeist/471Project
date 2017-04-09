package a471bestgroup.buddyapp;


import com.google.firebase.database.*;

import java.util.HashMap;
import java.util.Map;


public class User {
    private String dateOfBirth;
    private String username;
    private String uid;
    private String fullName;
    private String country;
    private String province;
    private String city;

    public User(String dob, String uid, String username, String fullName, String country, String province, String city){
        this.dateOfBirth = dob;
        this.uid = uid;
        this.username = username;
        this.fullName = fullName;
        this.country = country;
        this.province = province;
        this.city = city;
    }

    public User(User other) {
        this.dateOfBirth = other.getDateOfBirth();
        this.username = other.getUsername();
        this.fullName = other.getFullName();
        this.country = other.getCountry();
        this.province = other.getProvince();
        this.city = other.getCity();
    }

    public User(){

    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("username", username);
        result.put("uid", uid);
        result.put("dateOfBirth", dateOfBirth);
        result.put("fullName", fullName);
        result.put("country", country);
        result.put("province", province);
        result.put("city", city);
        return result;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}


