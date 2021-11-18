package com.example.naverpractice.MainService;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.naverpractice.databinding.ActivityMainBinding;
import com.example.naverpractice.databinding.FragInfoBinding;
import com.example.naverpractice.network.ApiClient;
import com.example.naverpractice.network.ApiInterface;
import com.example.naverpractice.network.ParkingLot;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InformationFragment extends Fragment {

    private FragInfoBinding mBinding;
    private final int SECTION_SIZE= 18;
    private final String TAG = "[INFO]";
    private Handler mHandler = new Handler();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragInfoBinding.inflate(inflater, container, false);
        InfoThread info = new InfoThread();
        info.start();
        return mBinding.getRoot();
    }

    class InfoThread extends Thread{

        private int empty_seat = 0;

        @Override
        public void run() {
            while(true){
                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<List<ParkingLot>> call = apiInterface.getEmpty();
                call.enqueue(new Callback<List<ParkingLot>>() {
                    @Override
                    public void onResponse(Call<List<ParkingLot>> call, Response<List<ParkingLot>> response) {
                        for(int i = 0; i<SECTION_SIZE;i++){
                            String result = response.body().get(i).getIsEmpty();
                            if(Boolean.parseBoolean(result)){
                                empty_seat++;
                            };
                        }
                        mBinding.information.setText("주차장 정보 = "+empty_seat+"/180");
                        empty_seat = 0;
                    }

                    @Override
                    public void onFailure(Call<List<ParkingLot>> call, Throwable t) {
                        Log.e(TAG, t.getMessage());
                    }
                });
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
