package com.example.naverpractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainServiceActvity extends AppCompatActivity {


    private static final String TAG = "[MAIN SERVICE]";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;

    private static final int SECTION1_SIZE = 18;
    private static final int SECTION2_SIZE = 24;

    private HashMap<Integer, Boolean> testCase1 = new HashMap<Integer, Boolean>();

    ImageView parkingLot;
    SurfaceView surfaceView;

    private TextView latitude;
    private TextView longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀바 제거
        setContentView(R.layout.parking_lot_activity);

        parkingLot = findViewById(R.id.parkingLot);
        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.setZOrderOnTop(true);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {  //SurfaceHolder의 holder(Surface 객체를 잡고(Hold) 관리)
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                holder.setFormat(PixelFormat.TRANSPARENT);
                MainService empty = new MainService(holder);
                empty.start();
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                //Create에서 생성한 Thread를 여기서 thread를 null로 취함
            }
        });
    }

    class MainService extends Thread {

        private SurfaceHolder holder;
        private Paint paint;

        public MainService(SurfaceHolder holder) {
            this.holder = holder;
            paint = new Paint();
            paint.setStrokeWidth(5f);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.RED);
        }

        @Override
        public void run() {
            Canvas canvas = holder.lockCanvas();
            init_testCase();
            draw(canvas);
            holder.unlockCanvasAndPost(canvas);

            //SectionSize만 바꿔주면 됌
            for (int i = 0; i < SECTION2_SIZE; i++) {
                try {
                    sleep(500);
                    canvas = holder.lockCanvas();
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    update_testCase(i);
                    draw(canvas);
                    holder.unlockCanvasAndPost(canvas);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void draw(Canvas canvas) {
            for (int i = 0; i < testCase1.size(); i++) {

                /*제일 아래 1구역
                if (testCase1.get(i) == false) {
                    canvas.drawRect(432 + i * 23, 1010, 455 + i * 23, 1100, paint);
                    Log.d(TAG, i + "번째 UI");
                }*/

                //2구역
                if (testCase1.get(i) == false) {
                    canvas.drawRect(432 + i * 23, 840, 455 + i * 23, 930, paint);
                    Log.d(TAG, i + "번째 UI");
                }

                /*3구역
                if (testCase1.get(i) == false) {
                    canvas.drawRect(432 + i * 23, 660, 455 + i * 23, 750, paint);
                    Log.d(TAG, i + "번째 UI");
                }*/

                /*4구역
                if (testCase1.get(i) == false) {
                    canvas.drawRect(432 + i * 23, 495, 455 + i * 23, 585, paint);
                    Log.d(TAG, i + "번째 UI");
                }*/

                /*5구역
                if (testCase1.get(i) == false) {
                    canvas.drawRect(432 + i * 23, 315, 455 + i * 23, 405, paint);
                    Log.d(TAG, i + "번째 UI");
                }*/


                /*6구역
                if (testCase1.get(i) == false) {
                    canvas.drawRect(409 + i * 23, 150, 431 + i * 23, 240, paint);
                    Log.d(TAG, i + "번째 UI");
                }*/


                /*7구역
                if (testCase1.get(i) == false) {
                    canvas.drawRect(17 + i * 23, 150, 40 + i * 23, 240, paint);
                    Log.d(TAG, i + "번째 UI");
                }*/


                /*8구역
                if (testCase1.get(i) == false) {
                    canvas.drawRect(63 + i * 23, 315, 86 + i * 23, 405, paint);
                    Log.d(TAG, i + "번째 UI");
                }*/


                /*9구역
                if (testCase1.get(i) == false) {
                    canvas.drawRect(63 + i * 23, 495, 86 + i * 23, 585, paint);
                    Log.d(TAG, i + "번째 UI");
                }*/

                /*10구역
                if (testCase1.get(i) == false) {
                    canvas.drawRect(40 + i * 23, 660, 63 + i * 23, 750, paint);
                    Log.d(TAG, i + "번째 UI");
                }*/

                /*11구역
                if (testCase1.get(i) == false) {
                    canvas.drawRect(224 + i * 23, 660, 247 + i * 23, 750, paint);
                    Log.d(TAG, i + "번째 UI");
                }*/

                /*12구역
                if (testCase1.get(i) == false) {
                    canvas.drawRect(224 + i * 23, 840, 247 + i * 23, 930, paint);
                    Log.d(TAG, i + "번째 UI");
                }*/

                /*13구역
                if (testCase1.get(i) == false) {
                    canvas.drawRect(942 + i * 23, 495, 965 + i * 23, 585, paint);
                    Log.d(TAG, i + "번째 UI");
                }*/

                /*14구역
                if (testCase1.get(i) == false) {
                    canvas.drawRect(942 + i * 23, 660, 965 + i * 23, 750, paint);
                    Log.d(TAG, i + "번째 UI");
                }*/
            }
        }
    }

    public void init_testCase() {
        //마찬가지로 섹션사이즈만 바꿔주면댐
        for (int i = 0; i < SECTION2_SIZE; i++) {
            testCase1.put(i, true);
        }
    }

    // TODO : Retrofit을 통해 지속적으로 통신하여 GET 하며 좌석(0번~179번)에 대한 빈 자리 유무를 계속 update 해야 한다.
    public void update_testCase(int i) {
        if (i == 0) {
            testCase1.put(i, false);
        } else {
            for (int k = 0; k < SECTION2_SIZE; k++) {
                testCase1.put(k, true);
                if (k == i) testCase1.put(k, false);
            }
        }
    }
}