package com.example.hitthetimetoandrod;


import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;


@IgnoreExtraProperties
public class FirebasePost {
    public String id;
    public String name;
    public int score;
    public String birthday;
    public String loginType;

    public FirebasePost(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public FirebasePost(String id, String name, int score, String birthday, String loginType) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.birthday = birthday;
        this.loginType = loginType;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("score", score);
        result.put("birthday", birthday);
        result.put("loginType", loginType);
        return result;
    }
}
