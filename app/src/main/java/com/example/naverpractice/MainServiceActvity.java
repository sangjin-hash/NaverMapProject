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
    private static int count;

    private HashMap<Integer, Boolean> testCase1 = new HashMap<Integer, Boolean>();

    private TextView information;

    ImageView parkingLot;
    SurfaceView surfaceView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀바 제거
        setContentView(R.layout.parking_lot_activity);

        information = findViewById(R.id.textView);

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
        private Handler mHandler = new Handler();

        public MainService(SurfaceHolder holder) {
            this.holder = holder;
            paint = new Paint();
            paint.setStrokeWidth(5f);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.RED);
        }

        // Todo: while문으로 canvas 관련 statement를 모두 묶어주고, handler를 통해 TextView setText하는 작업이 필요하다.
        @Override
        public void run() {
            init_testCase();
            try {
                Canvas canvas = holder.lockCanvas();
                update_testCase1();
                draw(canvas);
                String str = "주차장 정보 = " + count + "/180";
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        information.setText(str);
                    }
                });
                count = 0;
                holder.unlockCanvasAndPost(canvas);

                sleep(3000);

                canvas = holder.lockCanvas();
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                update_testCase2();
                draw(canvas);
                String str2 = "주차장 정보 = " + count + "/180";
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        information.setText(str2);
                    }
                });
                count = 0;
                holder.unlockCanvasAndPost(canvas);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void draw(Canvas canvas) {
            for (int i = 0; i < testCase1.size(); i++) {
                if (0 <= i && i <= 14) {
                    if (!testCase1.get(i)){
                        canvas.drawRect(17 + i * 23, 150, 40 + i * 23, 240, paint);
                        count++;
                    }
                } else if (15 <= i && i <= 38) {
                    if (!testCase1.get(i)){
                        canvas.drawRect(409 + (i - 15) * 23, 150, 431 + (i - 15) * 23, 240, paint);
                        count++;
                    }
                } else if (39 <= i && i <= 50) {
                    if (!testCase1.get(i)){
                        canvas.drawRect(63 + (i - 39) * 23, 315, 86 + (i - 39) * 23, 405, paint);
                        count++;
                    }
                } else if (51 <= i && i <= 69) {
                    if (!testCase1.get(i)){
                        canvas.drawRect(432 + (i - 51) * 23, 315, 455 + (i - 51) * 23, 405, paint);
                        count++;
                    }
                } else if (70 <= i && i <= 81) {
                    if (!testCase1.get(i)){
                        canvas.drawRect(63 + (i - 70) * 23, 495, 86 + (i - 70) * 23, 585, paint);
                        count++;
                    }
                } else if (82 <= i && i <= 100) {
                    if (!testCase1.get(i)){
                        canvas.drawRect(432 + (i - 82) * 23, 495, 455 + (i - 82) * 23, 585, paint);
                        count++;
                    }
                } else if (101 <= i && i <= 105) {
                    if (!testCase1.get(i)){
                        canvas.drawRect(942 + (i - 101) * 23, 495, 965 + (i - 101) * 23, 585, paint);
                        count++;
                    }
                } else if (106 <= i && i <= 110) {
                    if (!testCase1.get(i)){
                        canvas.drawRect(40 + (i - 106) * 23, 660, 63 + (i - 106) * 23, 750, paint);
                        count++;
                    }
                } else if (111 <= i && i <= 115) {
                    if (!testCase1.get(i)){
                        canvas.drawRect(224 + (i - 111) * 23, 660, 247 + (i - 111) * 23, 750, paint);
                        count++;
                    }
                } else if (116 <= i && i <= 134) {
                    if (!testCase1.get(i)){
                        canvas.drawRect(432 + (i - 116) * 23, 660, 455 + (i - 116) * 23, 750, paint);
                        count++;
                    }
                } else if (135 <= i && i <= 137) {
                    if (!testCase1.get(i)){
                        canvas.drawRect(942 + (i - 135) * 23, 660, 965 + (i - 135) * 23, 750, paint);
                        count++;
                    }
                } else if (138 <= i && i <= 142) {
                    if (!testCase1.get(i)){
                        canvas.drawRect(224 + (i - 138) * 23, 840, 247 + (i - 138) * 23, 930, paint);
                        count++;
                    }
                } else if (143 <= i && i <= 161) {
                    if (!testCase1.get(i)){
                        canvas.drawRect(432 + (i - 143) * 23, 840, 455 + (i - 143) * 23, 930, paint);
                        count++;
                    }
                } else if (162 <= i && i <= 179) {
                    if (!testCase1.get(i)){
                        canvas.drawRect(432 + (i - 162) * 23, 1010, 455 + (i - 162) * 23, 1100, paint);
                        count++;
                    }
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

    /* TODO : Retrofit을 통해 지속적으로 통신하여 GET 하며 좌석(0번~179번)에 대한 빈 자리 유무를 계속 update 해야 한다.
    public void update_testCase(int i) {
        if(i == 0){
            testCase1.put(i, false);
        } else{
            for (int k = 0; k < SECTION2_SIZE; k++) {
                testCase1.put(k, true);
                if (k == i) testCase1.put(k, false);
            }
        }
    }
    */


    public void update_testCase1() {
        //section 1
        testCase1.put(0, true);
        testCase1.put(1, true);
        testCase1.put(2, true);
        testCase1.put(3, true);
        testCase1.put(4, false);
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

        //section2
        testCase1.put(15, true);
        testCase1.put(16, true);
        testCase1.put(17, true);
        testCase1.put(18, true);
        testCase1.put(19, true);
        testCase1.put(20, true);
        testCase1.put(21, true);
        testCase1.put(22, false);
        testCase1.put(23, true);
        testCase1.put(24, true);
        testCase1.put(25, true);
        testCase1.put(26, true);
        testCase1.put(27, true);
        testCase1.put(28, true);
        testCase1.put(29, true);
        testCase1.put(30, false);
        testCase1.put(31, false);
        testCase1.put(32, true);
        testCase1.put(33, true);
        testCase1.put(34, true);
        testCase1.put(35, true);
        testCase1.put(36, true);
        testCase1.put(37, false);
        testCase1.put(38, false);

        //section 3
        testCase1.put(39, true);
        testCase1.put(40, true);
        testCase1.put(41, true);
        testCase1.put(42, true);
        testCase1.put(43, false);
        testCase1.put(44, false);
        testCase1.put(45, false);
        testCase1.put(46, true);
        testCase1.put(47, true);
        testCase1.put(48, true);
        testCase1.put(49, true);
        testCase1.put(50, false);

        //section 4
        testCase1.put(51, true);
        testCase1.put(52, true);
        testCase1.put(53, true);
        testCase1.put(54, true);
        testCase1.put(55, false);
        testCase1.put(56, false);
        testCase1.put(57, true);
        testCase1.put(58, true);
        testCase1.put(59, true);
        testCase1.put(60, true);
        testCase1.put(61, false);
        testCase1.put(62, true);
        testCase1.put(63, true);
        testCase1.put(64, true);
        testCase1.put(65, true);
        testCase1.put(66, false);
        testCase1.put(67, true);
        testCase1.put(68, true);
        testCase1.put(69, false);

        //section 5
        testCase1.put(70, true);
        testCase1.put(71, true);
        testCase1.put(72, true);
        testCase1.put(73, true);
        testCase1.put(74, true);
        testCase1.put(75, false);
        testCase1.put(76, false);
        testCase1.put(77, true);
        testCase1.put(78, true);
        testCase1.put(79, true);
        testCase1.put(80, true);
        testCase1.put(81, false);

        //section 6
        testCase1.put(82, true);
        testCase1.put(83, true);
        testCase1.put(84, true);
        testCase1.put(85, true);
        testCase1.put(86, true);
        testCase1.put(87, true);
        testCase1.put(88, false);
        testCase1.put(89, false);
        testCase1.put(90, true);
        testCase1.put(91, true);
        testCase1.put(92, true);
        testCase1.put(93, true);
        testCase1.put(94, true);
        testCase1.put(95, true);
        testCase1.put(96, false);
        testCase1.put(97, true);
        testCase1.put(98, true);
        testCase1.put(99, true);
        testCase1.put(100, false);

        //section 7
        testCase1.put(101, true);
        testCase1.put(102, true);
        testCase1.put(103, true);
        testCase1.put(104, false);
        testCase1.put(105, false);

        //section 8
        testCase1.put(106, false);
        testCase1.put(107, false);
        testCase1.put(108, true);
        testCase1.put(109, true);
        testCase1.put(110, false);

        //section 9
        testCase1.put(111, true);
        testCase1.put(112, true);
        testCase1.put(113, false);
        testCase1.put(114, true);
        testCase1.put(115, false);

        //section 10
        testCase1.put(116, true);
        testCase1.put(117, true);
        testCase1.put(118, true);
        testCase1.put(119, true);
        testCase1.put(120, false);
        testCase1.put(121, true);
        testCase1.put(122, true);
        testCase1.put(123, true);
        testCase1.put(124, true);
        testCase1.put(125, true);
        testCase1.put(126, false);
        testCase1.put(127, false);
        testCase1.put(128, true);
        testCase1.put(129, true);
        testCase1.put(130, true);
        testCase1.put(131, true);
        testCase1.put(132, true);
        testCase1.put(133, true);
        testCase1.put(134, false);

        //section 11
        testCase1.put(135, true);
        testCase1.put(136, true);
        testCase1.put(137, false);

        //section 12
        testCase1.put(138, true);
        testCase1.put(139, true);
        testCase1.put(140, true);
        testCase1.put(141, false);
        testCase1.put(142, false);

        //section 13
        testCase1.put(143, true);
        testCase1.put(144, true);
        testCase1.put(145, true);
        testCase1.put(146, true);
        testCase1.put(147, true);
        testCase1.put(148, true);
        testCase1.put(149, true);
        testCase1.put(150, false);
        testCase1.put(151, false);
        testCase1.put(152, true);
        testCase1.put(153, true);
        testCase1.put(154, true);
        testCase1.put(155, true);
        testCase1.put(156, false);
        testCase1.put(157, false);
        testCase1.put(158, false);
        testCase1.put(159, false);
        testCase1.put(160, true);
        testCase1.put(161, false);

        //section 14
        testCase1.put(162, true);
        testCase1.put(163, true);
        testCase1.put(164, true);
        testCase1.put(165, true);
        testCase1.put(166, true);
        testCase1.put(167, true);
        testCase1.put(168, false);
        testCase1.put(169, true);
        testCase1.put(170, false);
        testCase1.put(171, false);
        testCase1.put(172, true);
        testCase1.put(173, true);
        testCase1.put(174, true);
        testCase1.put(175, true);
        testCase1.put(176, true);
        testCase1.put(177, true);
        testCase1.put(178, true);
        testCase1.put(179, false);

    }


    public void update_testCase2() {
        //section 1
        testCase1.put(0, false);
        testCase1.put(1, true);
        testCase1.put(2, true);
        testCase1.put(3, true);
        testCase1.put(4, true);
        testCase1.put(5, true);
        testCase1.put(6, true);
        testCase1.put(7, true);
        testCase1.put(8, false);
        testCase1.put(9, true);
        testCase1.put(10, true);
        testCase1.put(11, false);
        testCase1.put(12, true);
        testCase1.put(13, false);
        testCase1.put(14, true);

        //section2
        testCase1.put(15, false);
        testCase1.put(16, true);
        testCase1.put(17, true);
        testCase1.put(18, true);
        testCase1.put(19, true);
        testCase1.put(20, false);
        testCase1.put(21, false);
        testCase1.put(22, true);
        testCase1.put(23, true);
        testCase1.put(24, true);
        testCase1.put(25, true);
        testCase1.put(26, true);
        testCase1.put(27, true);
        testCase1.put(28, true);
        testCase1.put(29, true);
        testCase1.put(30, true);
        testCase1.put(31, true);
        testCase1.put(32, true);
        testCase1.put(33, false);
        testCase1.put(34, true);
        testCase1.put(35, true);
        testCase1.put(36, false);
        testCase1.put(37, true);
        testCase1.put(38, true);

        //section 3
        testCase1.put(39, false);
        testCase1.put(40, true);
        testCase1.put(41, true);
        testCase1.put(42, true);
        testCase1.put(43, false);
        testCase1.put(44, false);
        testCase1.put(45, true);
        testCase1.put(46, true);
        testCase1.put(47, false);
        testCase1.put(48, true);
        testCase1.put(49, true);
        testCase1.put(50, false);

        //section 4
        testCase1.put(51, false);
        testCase1.put(52, true);
        testCase1.put(53, true);
        testCase1.put(54, true);
        testCase1.put(55, false);
        testCase1.put(56, false);
        testCase1.put(57, true);
        testCase1.put(58, true);
        testCase1.put(59, true);
        testCase1.put(60, false);
        testCase1.put(61, true);
        testCase1.put(62, true);
        testCase1.put(63, true);
        testCase1.put(64, false);
        testCase1.put(65, true);
        testCase1.put(66, true);
        testCase1.put(67, false);
        testCase1.put(68, true);
        testCase1.put(69, true);

        //section 5
        testCase1.put(70, false);
        testCase1.put(71, true);
        testCase1.put(72, true);
        testCase1.put(73, true);
        testCase1.put(74, true);
        testCase1.put(75, true);
        testCase1.put(76, false);
        testCase1.put(77, true);
        testCase1.put(78, true);
        testCase1.put(79, false);
        testCase1.put(80, true);
        testCase1.put(81, false);

        //section 6
        testCase1.put(82, false);
        testCase1.put(83, true);
        testCase1.put(84, true);
        testCase1.put(85, false);
        testCase1.put(86, false);
        testCase1.put(87, true);
        testCase1.put(88, true);
        testCase1.put(89, true);
        testCase1.put(90, true);
        testCase1.put(91, true);
        testCase1.put(92, true);
        testCase1.put(93, true);
        testCase1.put(94, true);
        testCase1.put(95, false);
        testCase1.put(96, true);
        testCase1.put(97, true);
        testCase1.put(98, false);
        testCase1.put(99, true);
        testCase1.put(100, false);

        //section 7
        testCase1.put(101, false);
        testCase1.put(102, true);
        testCase1.put(103, true);
        testCase1.put(104, false);
        testCase1.put(105, true);

        //section 8
        testCase1.put(106, false);
        testCase1.put(107, false);
        testCase1.put(108, true);
        testCase1.put(109, true);
        testCase1.put(110, true);

        //section 9
        testCase1.put(111, false);
        testCase1.put(112, true);
        testCase1.put(113, true);
        testCase1.put(114, false);
        testCase1.put(115, true);

        //section 10
        testCase1.put(116, false);
        testCase1.put(117, true);
        testCase1.put(118, true);
        testCase1.put(119, true);
        testCase1.put(120, true);
        testCase1.put(121, true);
        testCase1.put(122, true);
        testCase1.put(123, true);
        testCase1.put(124, false);
        testCase1.put(125, true);
        testCase1.put(126, false);
        testCase1.put(127, true);
        testCase1.put(128, true);
        testCase1.put(129, true);
        testCase1.put(130, true);
        testCase1.put(131, true);
        testCase1.put(132, false);
        testCase1.put(133, true);
        testCase1.put(134, true);

        //section 11
        testCase1.put(135, false);
        testCase1.put(136, true);
        testCase1.put(137, true);

        //section 12
        testCase1.put(138, false);
        testCase1.put(139, true);
        testCase1.put(140, false);
        testCase1.put(141, false);
        testCase1.put(142, true);

        //section 13
        testCase1.put(143, false);
        testCase1.put(144, false);
        testCase1.put(145, false);
        testCase1.put(146, false);
        testCase1.put(147, true);
        testCase1.put(148, true);
        testCase1.put(149, true);
        testCase1.put(150, true);
        testCase1.put(151, true);
        testCase1.put(152, true);
        testCase1.put(153, true);
        testCase1.put(154, true);
        testCase1.put(155, false);
        testCase1.put(156, true);
        testCase1.put(157, true);
        testCase1.put(158, false);
        testCase1.put(159, true);
        testCase1.put(160, true);
        testCase1.put(161, true);

        //section 14
        testCase1.put(162, false);
        testCase1.put(163, true);
        testCase1.put(164, false);
        testCase1.put(165, true);
        testCase1.put(166, true);
        testCase1.put(167, true);
        testCase1.put(168, true);
        testCase1.put(169, true);
        testCase1.put(170, true);
        testCase1.put(171, true);
        testCase1.put(172, true);
        testCase1.put(173, true);
        testCase1.put(174, false);
        testCase1.put(175, false);
        testCase1.put(176, true);
        testCase1.put(177, true);
        testCase1.put(178, false);
        testCase1.put(179, true);
    }

}