package com.example.naverpractice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashMap;
import java.util.Random;

public class ParkingLotService extends View {

    private Paint paint;
    private HashMap<Integer, Boolean> testCase1 = new HashMap<>();
    private final int SECTION_SIZE = 180;

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
        for (int i = 0; i < testCase1.size(); i++) {
            if (0 <= i && i <= 14) {
                if (!testCase1.get(i)) {
                    canvas.drawRect(17 + i * 23, 150, 40 + i * 23, 240, paint);
                }
            } else if (15 <= i && i <= 38) {
                if (!testCase1.get(i)) {
                    canvas.drawRect(409 + (i - 15) * 23, 150, 431 + (i - 15) * 23, 240, paint);
                }
            } else if (39 <= i && i <= 50) {
                if (!testCase1.get(i)) {
                    canvas.drawRect(63 + (i - 39) * 23, 315, 86 + (i - 39) * 23, 405, paint);
                }
            } else if (51 <= i && i <= 69) {
                if (!testCase1.get(i)) {
                    canvas.drawRect(432 + (i - 51) * 23, 315, 455 + (i - 51) * 23, 405, paint);
                }
            } else if (70 <= i && i <= 81) {
                if (!testCase1.get(i)) {
                    canvas.drawRect(63 + (i - 70) * 23, 495, 86 + (i - 70) * 23, 585, paint);
                }
            } else if (82 <= i && i <= 100) {
                if (!testCase1.get(i)) {
                    canvas.drawRect(432 + (i - 82) * 23, 495, 455 + (i - 82) * 23, 585, paint);
                }
            } else if (101 <= i && i <= 105) {
                if (!testCase1.get(i)) {
                    canvas.drawRect(942 + (i - 101) * 23, 495, 965 + (i - 101) * 23, 585, paint);
                }
            } else if (106 <= i && i <= 110) {
                if (!testCase1.get(i)) {
                    canvas.drawRect(40 + (i - 106) * 23, 660, 63 + (i - 106) * 23, 750, paint);
                }
            } else if (111 <= i && i <= 115) {
                if (!testCase1.get(i)) {
                    canvas.drawRect(224 + (i - 111) * 23, 660, 247 + (i - 111) * 23, 750, paint);
                }
            } else if (116 <= i && i <= 134) {
                if (!testCase1.get(i)) {
                    canvas.drawRect(432 + (i - 116) * 23, 660, 455 + (i - 116) * 23, 750, paint);
                }
            } else if (135 <= i && i <= 137) {
                if (!testCase1.get(i)) {
                    canvas.drawRect(942 + (i - 135) * 23, 660, 965 + (i - 135) * 23, 750, paint);
                }
            } else if (138 <= i && i <= 142) {
                if (!testCase1.get(i)) {
                    canvas.drawRect(224 + (i - 138) * 23, 840, 247 + (i - 138) * 23, 930, paint);
                }
            } else if (143 <= i && i <= 161) {
                if (!testCase1.get(i)) {
                    canvas.drawRect(432 + (i - 143) * 23, 840, 455 + (i - 143) * 23, 930, paint);
                }
            } else if (162 <= i && i <= 179) {
                if (!testCase1.get(i)) {
                    canvas.drawRect(432 + (i - 162) * 23, 1010, 455 + (i - 162) * 23, 1100, paint);
                }
            }
        }
        super.onDraw(canvas);
    }

    class MainService extends Thread{

        @Override
        public void run() {
            init_testCase();
            while(true){
                update_testCase();
                invalidate();

                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void init_testCase() {
        for (int i = 0; i < SECTION_SIZE; i++) {
            testCase1.put(i, true);
        }
    }

    public void update_testCase() {
        Random random = new Random();
        for (int i = 0; i < SECTION_SIZE; i++) {
            boolean result = random.nextBoolean();
            testCase1.put(i, result);
        }
    }
}
