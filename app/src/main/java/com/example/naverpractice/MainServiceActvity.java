package com.example.naverpractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainServiceActvity extends AppCompatActivity {
    private static final String TAG = "[MAIN SERVICE]";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final int SECTION_SIZE = 180;
    private static int count;

    private HashMap<Integer, Boolean> testCase1 = new HashMap<Integer, Boolean>();

    private TextView information;

    ImageView parkingLot;
    SurfaceView surfaceView;

    LocationListener gpsLocationListener;
    private Handler locationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀바 제거
        setContentView(R.layout.parking_lot_activity);

        information = findViewById(R.id.textView);

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        gpsLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                double coord[] = {latitude, longitude};
                Message msg = Message.obtain();
                msg.obj = coord;
                locationHandler.sendMessage(msg);
                Log.d(TAG, "locationHandler에게 msg 보내기");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }
        };

        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainServiceActvity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, gpsLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 1, gpsLocationListener);
        }

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
        private Paint location_paint;
        private double latitude;

        private Handler mHandler = new Handler();

        private double coordinate[];
        private double lat_in, lon_in;
        private double lat_out, lon_out;
        private Canvas canvas;

        public MainService(SurfaceHolder holder) {
            this.holder = holder;
            paint = new Paint();
            paint.setStrokeWidth(5f);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.RED);

            location_paint = new Paint();
            location_paint.setColor(Color.GREEN);
        }

        // Todo: while문으로 canvas 관련 statement를 모두 묶어주고, handler를 통해 TextView setText하는 작업이 필요하다.
        @Override
        public void run() {
            init_testCase();

            locationHandler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    coordinate = (double[]) msg.obj;
                    lat_in = coordinate[0];
                    lon_in = coordinate[1];
                    Log.d(TAG, "Handler 내부 위도 : " + lat_in);
                    Log.d(TAG, "Handler 내부 경도 : " + lon_in);
                }
            };

            while (true) {

                canvas = holder.lockCanvas();
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                update_testCase();
                draw(canvas);

                lat_out = lat_in;
                lon_out = lon_in;
                Log.d(TAG, "Handler 외부 위도 : " + lat_out);
                Log.d(TAG, "Handler 외부 경도 : " + lon_out);

                if (lat_out != 0 && lon_out != 0) {
                    int transformX = GpsToImageX(lon_out);
                    int transformY = GpsToImageY(lat_out);
                    Log.d(TAG, "X : " + transformX + " Y : " + transformY);
                    canvas.drawCircle(transformX, transformY, 15.0f, location_paint);
                }

                String str = "주차장 정보 = " + count + "/180";
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        information.setText(str);
                    }
                });
                count = 0;
                holder.unlockCanvasAndPost(canvas);

            }
        }


        // Todo : draw에 사용자 현위치 추가하기
        public void draw(Canvas canvas) {
            for (int i = 0; i < testCase1.size(); i++) {
                if (0 <= i && i <= 14) {
                    if (!testCase1.get(i)) {
                        canvas.drawRect(17 + i * 23, 150, 40 + i * 23, 240, paint);
                        count++;
                    }
                } else if (15 <= i && i <= 38) {
                    if (!testCase1.get(i)) {
                        canvas.drawRect(409 + (i - 15) * 23, 150, 431 + (i - 15) * 23, 240, paint);
                        count++;
                    }
                } else if (39 <= i && i <= 50) {
                    if (!testCase1.get(i)) {
                        canvas.drawRect(63 + (i - 39) * 23, 315, 86 + (i - 39) * 23, 405, paint);
                        count++;
                    }
                } else if (51 <= i && i <= 69) {
                    if (!testCase1.get(i)) {
                        canvas.drawRect(432 + (i - 51) * 23, 315, 455 + (i - 51) * 23, 405, paint);
                        count++;
                    }
                } else if (70 <= i && i <= 81) {
                    if (!testCase1.get(i)) {
                        canvas.drawRect(63 + (i - 70) * 23, 495, 86 + (i - 70) * 23, 585, paint);
                        count++;
                    }
                } else if (82 <= i && i <= 100) {
                    if (!testCase1.get(i)) {
                        canvas.drawRect(432 + (i - 82) * 23, 495, 455 + (i - 82) * 23, 585, paint);
                        count++;
                    }
                } else if (101 <= i && i <= 105) {
                    if (!testCase1.get(i)) {
                        canvas.drawRect(942 + (i - 101) * 23, 495, 965 + (i - 101) * 23, 585, paint);
                        count++;
                    }
                } else if (106 <= i && i <= 110) {
                    if (!testCase1.get(i)) {
                        canvas.drawRect(40 + (i - 106) * 23, 660, 63 + (i - 106) * 23, 750, paint);
                        count++;
                    }
                } else if (111 <= i && i <= 115) {
                    if (!testCase1.get(i)) {
                        canvas.drawRect(224 + (i - 111) * 23, 660, 247 + (i - 111) * 23, 750, paint);
                        count++;
                    }
                } else if (116 <= i && i <= 134) {
                    if (!testCase1.get(i)) {
                        canvas.drawRect(432 + (i - 116) * 23, 660, 455 + (i - 116) * 23, 750, paint);
                        count++;
                    }
                } else if (135 <= i && i <= 137) {
                    if (!testCase1.get(i)) {
                        canvas.drawRect(942 + (i - 135) * 23, 660, 965 + (i - 135) * 23, 750, paint);
                        count++;
                    }
                } else if (138 <= i && i <= 142) {
                    if (!testCase1.get(i)) {
                        canvas.drawRect(224 + (i - 138) * 23, 840, 247 + (i - 138) * 23, 930, paint);
                        count++;
                    }
                } else if (143 <= i && i <= 161) {
                    if (!testCase1.get(i)) {
                        canvas.drawRect(432 + (i - 143) * 23, 840, 455 + (i - 143) * 23, 930, paint);
                        count++;
                    }
                } else if (162 <= i && i <= 179) {
                    if (!testCase1.get(i)) {
                        canvas.drawRect(432 + (i - 162) * 23, 1010, 455 + (i - 162) * 23, 1100, paint);
                        count++;
                    }
                }
            }
        }
    }

    public int GpsToImageX(double longitude) {
        final double lon1 = 127.04489156;    //Image 최상단 경도
        final double lon2 = 127.04349;       //Image 최하단 경도
        final double width1 = 17;         //Image 최상단 너비 = ImageView의 x 좌표
        final double width2 = 1057;        //Image 최하단 너비 = ImageView의 x 좌표

        // y = a*x + b => Linear transform 2차원 (위도, 경도) => 2차원 (x,y)
        double a2 = (width1 - width2) / (lon1 - lon2);
        double b2 = width1 - (a2 * lon1);

        int width = (int) ((a2 * longitude) + b2);
        return width;
    }

    public int GpsToImageY(double latitude) {
        final double lat1 = 37.28461536;    //Image 최상단 위도
        final double lat2 = 37.28508;       //Image 최하단 위도
        final double height1 = 150;         //Image 최상단 높이 = ImageView의 y 좌표
        final double height2 = 1100;        //Image 최하단 높이 = ImageView의 y 좌표

        // y = a*x + b => Linear transform 2차원 (위도, 경도) => 2차원 (x,y)
        double a1 = (height1 - height2) / (lat1 - lat2);
        double b1 = height1 - (a1 * lat1);

        int height = (int) ((a1 * latitude) + b1);
        return height;
    }


    public void init_testCase() {
        for (int i = 0; i < SECTION_SIZE; i++) {
            testCase1.put(i, true);
        }
    }

    // Todo : update 하나만 쓰고, true false random으로 코드 간결화하기.
    // TODO : Retrofit을 통해 지속적으로 통신하여 GET 하며 좌석(0번~179번)에 대한 빈 자리 유무를 계속 update 해야 한다.
    public void update_testCase() {
        Random random = new Random();
        for (int i = 0; i < SECTION_SIZE; i++) {
            boolean result = random.nextBoolean();
            testCase1.put(i, result);
        }
    }
}