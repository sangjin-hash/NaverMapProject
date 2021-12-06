package com.example.naverpractice.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("seat.php")
    Call<List<ParkingLot>> getEmpty();

    @GET("distance.php")
    Call<List<RecommendSeat>> getNum();

    @GET("select.php")
    Call<List<Density>> getDensity();
}
