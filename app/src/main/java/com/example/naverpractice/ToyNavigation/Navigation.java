package com.example.naverpractice.ToyNavigation;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Navigation {
    @SerializedName("route")
    private Result_trackoption route;

    @SerializedName("message")
    private String message;

    @SerializedName("code")
    private int code;

    public Result_trackoption getRoute() {
        return route;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}

class Result_trackoption{
    @SerializedName("trafast")
    private List<Result_path> traoptimal;

    public List<Result_path> getTraoptimal() {
        return traoptimal;
    }
}

class Result_path{
    @SerializedName("summary")
    private Result_distance summary;

    @SerializedName("path")
    private List<List<Double>> path;

    public Result_distance getSummary() {
        return summary;
    }

    public List<List<Double>> getPath() {
        return path;
    }
}

class Result_distance{
    @SerializedName("distance")
    private int distance;


    public int getDistance() {
        return distance;
    }
}




