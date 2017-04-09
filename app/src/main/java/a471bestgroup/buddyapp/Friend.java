package a471bestgroup.buddyapp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ASlipperySeal on 4/7/2017.
 */

public class Friend {
    private String username;
    private String fullName;
    private String uid;

    public Friend(){}

    public Friend(String username, String fullName, String uid){
        this.username = username;
        this.fullName = fullName;
        this.uid = uid;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("username", username);
        result.put("fullName", fullName);
        result.put("uid", uid);
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
