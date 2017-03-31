package a471bestgroup.buddyapp;


import com.google.firebase.database.*;


public class User {
    private String dateOfBirth;
    private String username;
    private String fullName;
    private String country;
    private String province;
    private String city;

    public User(String dob, String username, String fullName, String country, String province, String city){
        this.dateOfBirth = dob;
        this.username = username;
        this.fullName = fullName;
        this.country = country;
        this.province = province;
        this.city = city;
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


