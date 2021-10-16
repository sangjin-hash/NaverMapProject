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

    private static final int SECTION_SIZE = 180;

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

            update_testCase1();
            draw(canvas);
            holder.unlockCanvasAndPost(canvas);

            try {
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            canvas = holder.lockCanvas();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            update_testCase2();
            draw(canvas);
            holder.unlockCanvasAndPost(canvas);
        }

        public void draw(Canvas canvas) {
            for (int i = 0; i < testCase1.size(); i++) {
                if(0<= i && i <= 14){
                    if(!testCase1.get(i)) canvas.drawRect(17 + i * 23, 150, 40 + i * 23, 240, paint);
                }
                else if(15<=i && i<=38){
                    if(!testCase1.get(i)) canvas.drawRect(409 + (i-15) * 23, 150, 431 + (i-15) * 23, 240, paint);
                }
                else if(39<=i && i<=50){
                    if(!testCase1.get(i)) canvas.drawRect(63 + (i-39) * 23, 315, 86 + (i-39) * 23, 405, paint);
                }
                else if(51<=i && i<=69){
                    if(!testCase1.get(i)) canvas.drawRect(432 + (i-51) * 23, 315, 455 + (i-51) * 23, 405, paint);
                }
                else if(70<=i && i<=81){
                    if(!testCase1.get(i)) canvas.drawRect(63 + (i-70) * 23, 495, 86 + (i-70) * 23, 585, paint);
                }
                else if(82<=i && i<=100){
                    if(!testCase1.get(i)) canvas.drawRect(432 + (i-82) * 23, 495, 455 + (i-82) * 23, 585, paint);
                }
                else if(101<=i && i<=105){
                    if(!testCase1.get(i)) canvas.drawRect(942 + (i-101) * 23, 495, 965 + (i-101) * 23, 585, paint);
                }
                else if(106<=i && i<=110){
                    if(!testCase1.get(i)) canvas.drawRect(40 + (i-106) * 23, 660, 63 + (i-106) * 23, 750, paint);
                }
                else if(111<=i && i<=115){
                    if(!testCase1.get(i)) canvas.drawRect(224 + (i-111) * 23, 660, 247 + (i-111) * 23, 750, paint);
                }
                else if(116<=i && i<=134){
                    if(!testCase1.get(i))  canvas.drawRect(432 + (i-116) * 23, 660, 455 + (i-116) * 23, 750, paint);
                }
                else if(135<=i && i<=137){
                    if(!testCase1.get(i))  canvas.drawRect(942 + (i-135) * 23, 660, 965 + (i-135) * 23, 750, paint);
                }
                else if(138<=i && i<= 142){
                    if(!testCase1.get(i)) canvas.drawRect(224 + (i-138) * 23, 840, 247 + (i-138) * 23, 930, paint);
                }
                else if(143<=i && i<=161){
                    if(!testCase1.get(i)) canvas.drawRect(432 + (i-143) * 23, 840, 455 + (i-143) * 23, 930, paint);
                }
                else if(162<=i && i<=179){
                    if(!testCase1.get(i))  canvas.drawRect(432 + (i-162) * 23, 1010, 455 + (i-162) * 23, 1100, paint);
                }
            }
        }
    }

    public void init_testCase() {
        //마찬가지로 섹션사이즈만 바꿔주면댐
        for (int i = 0; i < SECTION_SIZE; i++) {
            testCase1.put(i, true);
        }
    }

    // TODO : Retrofit을 통해 지속적으로 통신하여 GET 하며 좌석(0번~179번)에 대한 빈 자리 유무를 계속 update 해야 한다.

    public void update_testCase1() {
        testCase1.put(0, true);
        testCase1.put(1, false);
        testCase1.put(2, false);
        testCase1.put(3, true);
        testCase1.put(4, false);
        testCase1.put(5, true);
        testCase1.put(6, true);
        testCase1.put(7, true);
        testCase1.put(8, true);
        testCase1.put(9, false);
        testCase1.put(10, false);
        testCase1.put(11, true);
        testCase1.put(12, true);
        testCase1.put(13, true);
        testCase1.put(14, true);

        testCase1.put(15, true);
        testCase1.put(16, false);
        testCase1.put(17, false);
        testCase1.put(18, true);
        testCase1.put(19, false);
        testCase1.put(20, true);
        testCase1.put(21, true);
        testCase1.put(22, true);
        testCase1.put(23, true);
        testCase1.put(24, false);
        testCase1.put(25, false);
        testCase1.put(26, true);
        testCase1.put(27, true);
        testCase1.put(28, true);
        testCase1.put(29, true);
        testCase1.put(30, true);
        testCase1.put(31, true);
        testCase1.put(32, true);
        testCase1.put(33, true);
        testCase1.put(35, true);
        testCase1.put(36, true);
        testCase1.put(37, true);
        testCase1.put(38, true);
    }

    public void update_testCase2() {
        testCase1.put(0, true);
        testCase1.put(1, true);
        testCase1.put(2, false);
        testCase1.put(3, false);
        testCase1.put(4, true);
        testCase1.put(5, true);
        testCase1.put(6, true);
        testCase1.put(7, false);
        testCase1.put(8, true);
        testCase1.put(9, true);
        testCase1.put(10, true);
        testCase1.put(11, true);
        testCase1.put(12, true);
        testCase1.put(13, true);
        testCase1.put(14, false);

        testCase1.put(15, true);
        testCase1.put(16, true);
        testCase1.put(17, true);
        testCase1.put(18, false);
        testCase1.put(19, false);
        testCase1.put(20, true);
        testCase1.put(21, true);
        testCase1.put(22, true);
        testCase1.put(23, true);
        testCase1.put(24, true);
        testCase1.put(25, false);
        testCase1.put(26, true);
        testCase1.put(27, true);
        testCase1.put(28, true);
        testCase1.put(29, true);
        testCase1.put(30, false);
        testCase1.put(31, true);
        testCase1.put(32, true);
        testCase1.put(33, false);
        testCase1.put(35, true);
        testCase1.put(36, true);
        testCase1.put(37, true);
        testCase1.put(38, true);
    }

}