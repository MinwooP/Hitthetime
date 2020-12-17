package com.example.hitthetimetoandrod;


import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;


@IgnoreExtraProperties
public class FirebasePost {
    public String name;
    public double bestRecord;

    public FirebasePost(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public FirebasePost(String name, double bestRecord){
        this.name = name;
        this.bestRecord = bestRecord;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("name", name);
        result.put("bestRecord", bestRecord);
        return result;
    }
}
