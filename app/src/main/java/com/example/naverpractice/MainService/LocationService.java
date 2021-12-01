package com.example.naverpractice.MainService;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.naverpractice.MainService.Astar.AstarNetwork;
import com.example.naverpractice.MainService.Astar.AstarTest;
import com.example.naverpractice.network.ApiClient;
import com.example.naverpractice.network.ApiInterface;
import com.example.naverpractice.network.Density;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    public class RenderingThread extends Thread {
        private SurfaceHolder holder;
        private Paint paint;
        private Paint paint_recommend;
        private Paint paint_density;
        private Canvas canvas;
        private double latitude, longitude;

        private WhichNode whichNode;
        private TransformCoordinate transformCoordinate;
        private RecordNode recordNode;
        private AstarTest astarTest;
        private DensityService density;

        private int node;
        private boolean isDraw;
        private ArrayList<PreviousNode> previous = new ArrayList<PreviousNode>();

        private Path recommend_path;

        private AstarNetwork astarNetwork;

        public RenderingThread(SurfaceHolder holder) {
            this.holder = holder;
            holder.setFormat(PixelFormat.TRANSPARENT);
            paint = new Paint();
            paint.setColor(Color.GREEN);

            paint_recommend = new Paint();
            paint_recommend.setColor(Color.BLUE);
            paint_recommend.setStyle(Paint.Style.STROKE);
            paint_recommend.setStrokeWidth(10f);

            recommend_path = new Path();

            paint_density = new Paint();
            paint_density.setColor(Color.BLUE);
            paint_density.setStyle(Paint.Style.STROKE);
            paint_density.setStrokeWidth(5f);

            EventBus.getDefault().register(this);
            whichNode = new WhichNode();
            transformCoordinate = new TransformCoordinate();
            recordNode = new RecordNode();

            astarNetwork = new AstarNetwork();
            astarNetwork.start();

            density = new DensityService();
            density.start();
        }

        @Override
        public void run() {
            while (true) {
                if (latitude != 0 && longitude != 0) {
                    canvas = holder.lockCanvas();
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);     //가끔 이쪽에서 오류나서 튕김 -> NullPointerException 발생 Canvas.drawcalor -> null object
                    int temp_transformX = GpsToImageX(longitude);
                    int temp_transformY = GpsToImageY(latitude);

                    node = whichNode.setNode(temp_transformX, temp_transformY);

                    if (node != -1) {
                        isDraw = recordNode.record(node);
                        if (isDraw) {
                            transformCoordinate.transForm(temp_transformX, temp_transformY, node);
                            int transformX = transformCoordinate.getTransformX();
                            int transformY = transformCoordinate.getTransformY();
                            if (previous.size() == 0) {
                                PreviousNode pn = new PreviousNode(transformX, transformY, node);
                                previous.add(0, pn);
                            } else {
                                previous.remove(0);
                                PreviousNode pn = new PreviousNode(transformX, transformY, node);
                                previous.add(0, pn);
                            }
                        }
                        canvas.drawCircle(previous.get(0).getX(), previous.get(0).getY(), 15.0f, paint);

                        /*Service 1
                        astarTest = new AstarTest(previous.get(0).getX(), previous.get(0).getY(), previous.get(0).getNode());
                        astarTest.start();

                        try {
                            astarTest.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        ArrayList<Integer[]> recommend = astarTest.path;
                        for (int i = 0; i < recommend.size(); i++) {
                            Integer[] coordinate = recommend.get(i);
                            int x = coordinate[0];
                            int y = coordinate[1];

                            if (i == 0) recommend_path.moveTo(x, y);
                            else recommend_path.lineTo(x, y);
                        }
                        canvas.drawPath(recommend_path, paint_recommend);
                        recommend_path.reset();
                        astarTest.path.clear();*/

                        //Service 2
                        for (int i = 0; i < DensityService.list.size(); i++) {
                            int left = DensityService.list.get(i)[0];
                            int top = DensityService.list.get(i)[1];
                            canvas.drawRect(left, top, left + 23, top + 90, paint_recommend);
                        }
                    }
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }

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