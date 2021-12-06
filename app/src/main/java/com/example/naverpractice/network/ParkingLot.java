package com.example.naverpractice.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParkingLot {
    @Expose
    @SerializedName("seatNo") private int id;

    @Expose
    @SerializedName("isEmpty") private int isEmpty;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsEmpty() {
        return isEmpty;
    }

    public void setIsEmpty(int isEmpty) {
        this.isEmpty = isEmpty;
    }
}
