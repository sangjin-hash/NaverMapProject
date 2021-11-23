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
        private WhichNode whichNode;
        private TransformCoordinate transformCoordinate;
        private int node;

        public RenderingThread(SurfaceHolder holder) {
            this.holder = holder;
            holder.setFormat(PixelFormat.TRANSPARENT);
            paint = new Paint();
            paint.setColor(Color.GREEN);

            EventBus.getDefault().register(this);
            whichNode = new WhichNode();
            transformCoordinate = new TransformCoordinate();
        }

        @Override
        public void run() {
            while (true) {
                if (latitude != 0 && longitude != 0) {
                    canvas = holder.lockCanvas();
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    int temp_transformX = GpsToImageX(longitude);
                    int temp_transformY = GpsToImageY(latitude);

                    node = whichNode.setNode(temp_transformX,temp_transformY);
                    if(node != -1){
                        transformCoordinate.transForm(temp_transformX, temp_transformY, node);
                        int transformX = transformCoordinate.getTransformX();
                        int transformY = transformCoordinate.getTransformY();
                        canvas.drawCircle(transformX, transformY, 15.0f, paint);
                    }
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
    }
}