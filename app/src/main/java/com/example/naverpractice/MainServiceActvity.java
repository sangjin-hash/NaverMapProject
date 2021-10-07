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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainServiceActvity extends AppCompatActivity {

    private static final String TAG = "[MAIN SERVICE]";
    private static final int SECTION1_SIZE = 18;

    private HashMap<Integer, Boolean> testCase1 = new HashMap<Integer, Boolean>();

    ImageView parkingLot;
    SurfaceView surfaceView;

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
                EmptyThread empty = new EmptyThread(holder);
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

    class EmptyThread extends Thread{

        private SurfaceHolder holder;
        private Paint paint;

        public EmptyThread(SurfaceHolder holder){
            this.holder = holder;
            paint = new Paint();
            paint.setStrokeWidth(5f);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.RED);
        }

        @Override
        public void run() {
            /* 칸마다 거리 와꾸재기
            Canvas canvas = holder.lockCanvas();
            canvas.drawLine(435,980,845,980,paint);
            holder.unlockCanvasAndPost(canvas);*/

            Canvas canvas = holder.lockCanvas();
            init_testCase();
            draw(canvas);
            holder.unlockCanvasAndPost(canvas);

            for(int i = 1; i < SECTION1_SIZE; i++){
                try {
                    sleep(1500);
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

        public void draw(Canvas canvas){
            for(int i = 0; i<testCase1.size();i++){
                if(testCase1.get(i) == false){
                    canvas.drawRect(435+i*23, 1010, 458+i*23, 1100, paint);
                    Log.d(TAG, i+"번째 UI");
                }
            }
        }
    }

    public void init_testCase(){
        //차가 있다 -> true, 차가 없다 -> false, 제일 초기 화면에 대한 UI 구성
        testCase1.put(0,false);

        for(int i = 1 ; i < SECTION1_SIZE; i++){
            testCase1.put(i,true);
        }
    }

    public void update_testCase(int i){
        for(int k = 0; k < SECTION1_SIZE; k++){
            testCase1.put(k, true);
            if(k == i) testCase1.put(k,false);
        }
    }
}