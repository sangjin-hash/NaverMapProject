package com.example.naverpractice.MainService;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class LocationService extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "[LocationService]";
    SurfaceHolder mHolder;
    RenderingThread mRThread;

    public LocationService(Context context) {
        super(context);
    }

    public LocationService(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHolder = getHolder();
        mHolder.setFormat(PixelFormat.TRANSPARENT);
        mHolder.addCallback(this);
        setZOrderOnTop(true);
    }

    public LocationService(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        mRThread = new RenderingThread(mHolder);
        mRThread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    class RenderingThread extends Thread {
        private SurfaceHolder holder;
        private Paint paint;
        private Canvas canvas;
        private double latitude, longitude;

        public RenderingThread(SurfaceHolder holder) {
            this.holder = holder;
            holder.setFormat(PixelFormat.TRANSPARENT);
            paint = new Paint();
            paint.setColor(Color.GREEN);

            EventBus.getDefault().register(this);
        }

        @Override
        public void run() {
            while (true) {
                if (latitude != 0 && longitude != 0) {
                    canvas = holder.lockCanvas();
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    int transformX = GpsToImageX(longitude);
                    int transformY = GpsToImageY(latitude);
                    canvas.drawCircle(transformX, transformY, 15.0f, paint);
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }

        //EventBus에서 보낸 이벤트를 수신하는 콜백 메서드
        @Subscribe
        public void onLocationEvent(MainServiceActvity.LocationEvent event) {
            latitude = event.latitude;
            longitude = event.longitude;
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
            width = tuning_X(width);
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
            height = tuning_Y(height);
            return height;
        }

        public int tuning_X(int width) {
            int tuning_X = 0;

            if (-100 <= width && width <= 63) {
                tuning_X = 40;
                return tuning_X;
            } else if (63 < width && width <= 362) {
                return width;
            } else if (362 < width && width <= 409) {
                tuning_X = 386;
                return tuning_X;
            } else if (409 < width && width <= 873) {
                return width;
            } else if (873 < width && width <= 942) {
                tuning_X = 907;
                return tuning_X;
            } else if (942 < width && width < 1100) {
                return width;
            }
            return tuning_X;
        }

        public int tuning_Y(int height) {
            int tuning_Y = 0;

            if (150 <= height && height <= 315) {
                tuning_Y = 278;
                return tuning_Y;
            } else if (315 < height && height <= 585) {
                return height;
            } else if (585 < height && height <= 660) {
                tuning_Y = 623;
                return tuning_Y;
            } else if (660 < height && height <= 930) {
                return height;
            } else if (930 < height && height <= 1010) {
                tuning_Y = 970;
                return tuning_Y;
            } else if (1010 < height && height < 1100) {
                return height;
            }
            return tuning_Y;
        }
    }
}
