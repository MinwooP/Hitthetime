package com.example.hitthetimetoandrod;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import java.io.Serializable;


@IgnoreExtraProperties
public class FirebasePost implements Parcelable {
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

    @Override
    public String toString(){
        return this.name;
    }

    public double getRecord(){
        return this.bestRecord;
    }
    public String getName(){
        return this.name;
    }


    public FirebasePost(Parcel in) {
        this.name = in.readString();
        this.bestRecord = in.readDouble();
    }

    public static final Creator<FirebasePost> CREATOR = new Creator<FirebasePost>() {
        @Override
        public FirebasePost createFromParcel(Parcel in) {
            return new FirebasePost(in);
        }

        @Override
        public FirebasePost[] newArray(int size) {
            return new FirebasePost[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(bestRecord);
    }
}
