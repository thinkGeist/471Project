package a471bestgroup.buddyapp;

/**
 * Created by ASlipperySeal on 3/22/2017.
 */

public class User {
    private String dateOfBirth;
    private String fullName;
    private String country;
    private String province;
    private String city;

    public User(String dob, String fullName, String country, String province, String city){
        this.dateOfBirth = dob;
        this.fullName = fullName;
        this.country = country;
        this.province = province;
        this.city = city;
    }


}
