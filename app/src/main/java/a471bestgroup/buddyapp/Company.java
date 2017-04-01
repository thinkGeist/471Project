package a471bestgroup.buddyapp;


import com.google.firebase.database.*;


public class Company {
    private String companyName;
    private String country;
    private String province;
    private String city;
    private int companyID;

    public Company(String companyName, String country, String province, String city, int companyID){
        this.companyName = companyName;
        this.country = country;
        this.province = province;
        this.city = city;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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
