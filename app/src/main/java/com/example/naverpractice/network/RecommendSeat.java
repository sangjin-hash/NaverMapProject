package com.example.naverpractice.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecommendSeat {
    @Expose
    @SerializedName("num") private int num;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
