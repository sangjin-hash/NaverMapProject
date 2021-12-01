package com.example.naverpractice.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("select.php")
    Call<List<ParkingLot>> getEmpty();

    @GET("recommend.php")
    Call<List<RecommendSeat>> getNum();

    @GET("density.php")
    Call<List<Density>> getDensity();
}
