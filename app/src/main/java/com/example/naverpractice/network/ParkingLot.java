package com.example.naverpractice.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParkingLot {
    @Expose
    @SerializedName("id") private int id;

    @Expose
    @SerializedName("isEmpty") private String isEmpty;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsEmpty() {
        return isEmpty;
    }

    public void setIsEmpty(String isEmpty) {
        this.isEmpty = isEmpty;
    }
}
