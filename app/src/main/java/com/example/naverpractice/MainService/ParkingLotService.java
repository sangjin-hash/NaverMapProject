package com.example.naverpractice.MainService;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.naverpractice.network.ApiClient;
import com.example.naverpractice.network.ApiInterface;
import com.example.naverpractice.network.ParkingLot;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParkingLotService extends View {

    private Paint paint;
    private HashMap<Integer, Boolean> parking_seat = new HashMap<>();
    private final int SECTION_SIZE = 180;
    private final String TAG = "[ParkingLotService]";

    public ParkingLotService(Context context, AttributeSet attrs) {
        super(context, attrs);
        MainService mService = new MainService();
        mService.start();

        paint = new Paint();
        paint.setStrokeWidth(5f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        for (int i = 0; i < parking_seat.size(); i++) {
            if (0 <= i && i <= 14) {
                if (parking_seat.get(i)) {
                    canvas.drawRect(17 + i * 23, 150, 40 + i * 23, 240, paint);
                }
            } else if (15 <= i && i <= 38) {
                if (parking_seat.get(i)) {
                    canvas.drawRect(409 + (i - 15) * 23, 150, 431 + (i - 15) * 23, 240, paint);
                }
            } else if (39 <= i && i <= 50) {
                if (parking_seat.get(i)) {
                    canvas.drawRect(63 + (i - 39) * 23, 315, 86 + (i - 39) * 23, 405, paint);
                }
            } else if (51 <= i && i <= 69) {
                if (parking_seat.get(i)) {
                    canvas.drawRect(432 + (i - 51) * 23, 315, 455 + (i - 51) * 23, 405, paint);
                }
            } else if (70 <= i && i <= 81) {
                if (parking_seat.get(i)) {
                    canvas.drawRect(63 + (i - 70) * 23, 495, 86 + (i - 70) * 23, 585, paint);
                }
            } else if (82 <= i && i <= 100) {
                if (parking_seat.get(i)) {
                    canvas.drawRect(432 + (i - 82) * 23, 495, 455 + (i - 82) * 23, 585, paint);
                }
            } else if (101 <= i && i <= 105) {
                if (parking_seat.get(i)) {
                    canvas.drawRect(942 + (i - 101) * 23, 495, 965 + (i - 101) * 23, 585, paint);
                }
            } else if (106 <= i && i <= 110) {
                if (parking_seat.get(i)) {
                    canvas.drawRect(40 + (i - 106) * 23, 660, 63 + (i - 106) * 23, 750, paint);
                }
            } else if (111 <= i && i <= 115) {
                if (parking_seat.get(i)) {
                    canvas.drawRect(224 + (i - 111) * 23, 660, 247 + (i - 111) * 23, 750, paint);
                }
            } else if (116 <= i && i <= 134) {
                if (parking_seat.get(i)) {
                    canvas.drawRect(432 + (i - 116) * 23, 660, 455 + (i - 116) * 23, 750, paint);
                }
            } else if (135 <= i && i <= 137) {
                if (parking_seat.get(i)) {
                    canvas.drawRect(942 + (i - 135) * 23, 660, 965 + (i - 135) * 23, 750, paint);
                }
            } else if (138 <= i && i <= 142) {
                if (parking_seat.get(i)) {
                    canvas.drawRect(224 + (i - 138) * 23, 840, 247 + (i - 138) * 23, 930, paint);
                }
            } else if (143 <= i && i <= 161) {
                if (parking_seat.get(i)) {
                    canvas.drawRect(432 + (i - 143) * 23, 840, 455 + (i - 143) * 23, 930, paint);
                }
            } else if (162 <= i && i <= 179) {
                if (parking_seat.get(i)) {
                    canvas.drawRect(432 + (i - 162) * 23, 1010, 455 + (i - 162) * 23, 1100, paint);
                }
            }
        }
        super.onDraw(canvas);
    }

    class MainService extends Thread{

        @Override
        public void run() {
            init_seat();
            while(true){
                update_seat();
                invalidate();

                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void init_seat() {
        for (int i = 0; i < SECTION_SIZE; i++) {
            parking_seat.put(i, false);
        }
    }

    public void update_seat() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<ParkingLot>> call = apiInterface.getEmpty();
        call.enqueue(new Callback<List<ParkingLot>>() {
            @Override
            public void onResponse(Call<List<ParkingLot>> call, Response<List<ParkingLot>> response) {
                for(int i = 0; i<SECTION_SIZE;i++){
                    int index = response.body().get(i).getId();
                    int result = response.body().get(i).getIsEmpty();
                    boolean re = result == 1 ? true : false;
                    parking_seat.put(index, re);
                }
            }

            @Override
            public void onFailure(Call<List<ParkingLot>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }
}
