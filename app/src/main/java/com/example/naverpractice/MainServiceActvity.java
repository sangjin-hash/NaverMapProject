package com.example.naverpractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.widget.ImageView;

public class MainServiceActvity extends AppCompatActivity {

    private static final String TAG = "[MAIN SERVICE]";
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

        public EmptyThread(SurfaceHolder holder){
            this.holder = holder;
        }

        @Override
        public void run() {
            Canvas canvas = holder.lockCanvas();
            draw(canvas); // if문으로 묶어서 해야될듯 + 반복문
            holder.unlockCanvasAndPost(canvas); // 최초 한번만 해야됌
        }

        public void draw(Canvas canvas){
            Paint paint = new Paint();
            paint.setStrokeWidth(5f);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.RED);

            //가로 길이 : 20, 세로 길이 : 60
            canvas.drawLine(470,1080,490,1080, paint);
            canvas.drawLine(490,1080,490,1150, paint);
            canvas.drawLine(490,1150,470,1150, paint);
            canvas.drawLine(470,1150,470,1080, paint);
        }
   }
}