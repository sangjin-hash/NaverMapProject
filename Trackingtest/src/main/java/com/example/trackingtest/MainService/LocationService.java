package com.example.trackingtest.MainService;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.trackingtest.Data.PreviousNode;
import com.example.trackingtest.Data.RecordNode;
import com.example.trackingtest.Data.TransformCoordinate;
import com.example.trackingtest.Data.WhichNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LocationService extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "[Test]";
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

    public LocationService(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        mRThread = new RenderingThread(mHolder);
        mRThread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    public class RenderingThread extends Thread {
        private SurfaceHolder holder;
        private Paint paint;
        private Canvas canvas;

        private int node;
        private boolean isDraw;
        private ArrayList<PreviousNode> previous = new ArrayList<>();

        private WhichNode whichNode;
        private TransformCoordinate transformCoordinate;
        private RecordNode recordNode;

        private ArrayList<String[]> coordinate_list = new ArrayList<>();

        private int k,m;

        public RenderingThread(SurfaceHolder holder) {
            this.holder = holder;
            holder.setFormat(PixelFormat.TRANSPARENT);
            paint = new Paint();
            paint.setColor(Color.GREEN);

            whichNode = new WhichNode();
            transformCoordinate = new TransformCoordinate();
            recordNode = new RecordNode();

            AssetManager am = getResources().getAssets();
            InputStream is = null;

            try {
                is = am.open("mock_gps_data.csv");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line = null;
                while ((line = reader.readLine()) != null) {
                    String[] mock_coordinate = line.split(",");
                    coordinate_list.add(mock_coordinate);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            for (int i = 0; i < coordinate_list.size(); i++) {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                }

                double mock_lat = Double.parseDouble(coordinate_list.get(i)[0]);
                double mock_lon = Double.parseDouble(coordinate_list.get(i)[1]);

                //1st transform
                int temp_mock_image_X = GpsToImageX(mock_lon);
                int temp_mock_image_Y = GpsToImageY(mock_lat);

                node = whichNode.setNode(temp_mock_image_X, temp_mock_image_Y);

                if (node != -1) {
                    isDraw = recordNode.record(node);
                    if (isDraw) {

                        //2nd transform
                        transformCoordinate.transForm(temp_mock_image_X, temp_mock_image_Y, node);
                        int mock_image_X = transformCoordinate.getTransformX();
                        int mock_image_Y = transformCoordinate.getTransformY();
                        if (previous.size() == 0) {
                            PreviousNode pn = new PreviousNode(mock_image_X, mock_image_Y, node);
                            previous.add(0, pn);
                        } else {
                            previous.remove(0);
                            PreviousNode pn = new PreviousNode(mock_image_X, mock_image_Y, node);
                            previous.add(0, pn);
                        }
                    }
                    else{
                        k++;
                        Log.e(TAG, k + "개 이상치 발견");
                    }
                }else{
                    m++;
                    Log.d(TAG, m+"개가 지도 밖으로 좌표가 잡힘");
                }
                canvas.drawCircle(previous.get(0).getX(), previous.get(0).getY(), 15.0f, paint);
                holder.unlockCanvasAndPost(canvas);
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int GpsToImageX(double longitude) {
        final double lon1 = 127.04485156;    //Image 최상단 경도
        final double lon2 = 127.04353077;       //Image 최하단 경도
        final double width1 = 30;         //Image 최상단 너비 = ImageView의 x 좌표
        final double width2 = 1057;        //Image 최하단 너비 = ImageView의 x 좌표

        // y = a*x + b => Linear transform 2차원 (위도, 경도) => 2차원 (x,y)
        double a2 = (width1 - width2) / (lon1 - lon2);
        double b2 = width1 - (a2 * lon1);

        int width = (int) ((a2 * longitude) + b2);
        return width;
    }

    public int GpsToImageY(double latitude) {
        final double lat1 = 37.28462536;    //Image 최상단 위도
        final double lat2 = 37.28508;       //Image 최하단 위도
        final double height1 = 120;         //Image 최상단 높이 = ImageView의 y 좌표
        final double height2 = 1120;        //Image 최하단 높이 = ImageView의 y 좌표

        // y = a*x + b => Linear transform 2차원 (위도, 경도) => 2차원 (x,y)
        double a1 = (height1 - height2) / (lat1 - lat2);
        double b1 = height1 - (a1 * lat1);

        int height = (int) ((a1 * latitude) + b1);
        return height;
    }
}
