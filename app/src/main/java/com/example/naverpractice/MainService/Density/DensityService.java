package com.example.naverpractice.MainService.Density;

import android.util.Log;

import com.example.naverpractice.network.ApiClient;
import com.example.naverpractice.network.ApiInterface;
import com.example.naverpractice.network.Density;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DensityService extends Thread{
    private static final String TAG = "[DENSITYSERVICE]";
    public static ArrayList<Integer[]> list;
    private ArrayList<Integer[]> prev_list;

    public DensityService(){
        list = new ArrayList<Integer[]>();
        prev_list = new ArrayList<Integer[]>();
    }

    @Override
    public void run() {
        density_service();
    }

    public void density_service() {
        while(true){
            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<List<Density>> call = apiInterface.getDensity();
            call.enqueue(new Callback<List<Density>>() {
                @Override
                public void onResponse(Call<List<Density>> call, Response<List<Density>> response) {
                        for (int i = 0; i < response.body().size(); i++) {
                            Integer[] coordinate = new Integer[]{response.body().get(i).getLeft(), response.body().get(i).getTop()};
                            prev_list.add(coordinate);
                        }
                    if(observer(prev_list)){
                        list = prev_list;
                        prev_list.clear();
                    }
                }

                @Override
                public void onFailure(Call<List<Density>> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                }
            });
        }
    }

    public boolean observer(ArrayList<Integer[]> compare){
        boolean result = false;
        if(list.size() == 0) return true;

        for(int i = 0; i < compare.size(); i++){
            int prev_left = compare.get(i)[0];
            int prev_top = compare.get(i)[1];

            int list_left = list.get(i)[0];
            int list_top = list.get(i)[1];

            if(prev_left != list_left || prev_top != list_top){
                result = true;
                break;
            }
        }
        return result;
    }
}
