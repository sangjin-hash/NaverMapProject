package com.example.naverpractice.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Density {
    @Expose
    @SerializedName("num") private int seatNo;

    @Expose
    @SerializedName("left") private int left;

    @Expose
    @SerializedName("top") private int top;

    public int getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(int seatNo) {
        this.seatNo = seatNo;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }
}
