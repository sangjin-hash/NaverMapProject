package com.example.naverpractice.MainService.Astar;

import android.util.Log;

import com.example.naverpractice.network.ApiClient;
import com.example.naverpractice.network.ApiInterface;
import com.example.naverpractice.network.RecommendSeat;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AstarNetwork extends Thread {
    private static final String TAG = "[AstarNetwork]";
    public static int recommend_seat_num = -1;
    private int previous_num = -1;

    @Override
    public void run() {
        while(true){
            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<List<RecommendSeat>> call = apiInterface.getNum();
            call.enqueue(new Callback<List<RecommendSeat>>() {

                @Override
                public void onResponse(Call<List<RecommendSeat>> call, Response<List<RecommendSeat>> response) {
                    previous_num = response.body().get(0).getNum();
                    if(recommend_seat_num != previous_num) recommend_seat_num = previous_num;
                }

                @Override
                public void onFailure(Call<List<RecommendSeat>> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                }
            });
        }
    }
}
