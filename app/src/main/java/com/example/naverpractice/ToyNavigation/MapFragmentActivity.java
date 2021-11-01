package com.example.naverpractice.ToyNavigation;

import android.content.Intent;
import android.graphics.PointF;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.naverpractice.MainService.MainServiceActvity;
import com.example.naverpractice.R;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.ZoomControlView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapFragmentActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final String TAG = "NaverMap";
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    private MapFragment mapFragment;

    private double src_lat, src_lon;
    private double des_lat, des_lon;
    private final double paldal_lat = 37.28476;
    private final double paldal_lon = 127.04425;

    private Marker marker1;

    private PathOverlay path;

    //나중에 클라우드에서 데이터를 읽어와서 이 변수를 update해줘야 함.
    private int num_available = 0;
    private final int num_entire = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_fragment_activity);

        FragmentManager fm = getSupportFragmentManager();
        mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
        if(mapFragment == null){
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)){
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMapReady(NaverMap naverMap) {
        this.naverMap = naverMap;
        //현 위치 추적
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        //네이버 지도에 대한 기본 설정
        naverMap.setMapType(NaverMap.MapType.Navi);
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING,false);
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRAFFIC,true);
        naverMap.setMinZoom(14);
        naverMap.setMaxZoom(16);

        //UISetting -> 현 위치 버튼 + zoom View 위치 조정
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);
        uiSettings.setZoomControlEnabled(false);

        ZoomControlView zoomControlView = findViewById(R.id.zoom);
        zoomControlView.setMap(naverMap);

        //시간에 따른 야간모드 활성화
        long mNow = System.currentTimeMillis();
        Date mDate = new Date(mNow);
        SimpleDateFormat mFormat = new SimpleDateFormat("HH");
        int current_hour= Integer.parseInt(mFormat.format(mDate));

        if(6 < current_hour && current_hour < 18){
         //낮일 때
         naverMap.setNightModeEnabled(false);
        }else{
            //밤일때 야간모드 활성화
            naverMap.setNightModeEnabled(true);
        }

        //현 위치가 변동될때마다 좌표를 추적하여 카메라 이동하는 이벤트
        naverMap.addOnLocationChangeListener(new NaverMap.OnLocationChangeListener() {
            @Override
            public void onLocationChange(Location location) {
                src_lat = location.getLatitude();
                src_lon = location.getLongitude();
                CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(src_lat,src_lon)).animate(CameraAnimation.Easing, 3000);
                naverMap.moveCamera(cameraUpdate);

                //팔달관 주차장 내에 들어왔을 때 마커 발생
                GPSdistance gpsDistance = new GPSdistance();
                double dist = gpsDistance.GetDistanceBetweenPoints(src_lat,src_lon, paldal_lat, paldal_lon);
                Marker marker2 = new Marker();
                if(dist < 200){
                    Log.d(TAG, "해당 현위치는 주차장 반경 200m 내에 속한다");
                    marker2.setPosition(new LatLng(paldal_lat, paldal_lon));
                    marker2.setMap(naverMap);
                    marker2.setMinZoom(14);
                    InfoWindow infoWindow = new InfoWindow();
                    infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(MapFragmentActivity.this){
                        @NonNull
                        @Override
                        public CharSequence getText(@NonNull InfoWindow infoWindow) {
                            return num_available + "/" + num_entire;
                        }
                    });
                    infoWindow.open(marker2);
                }

                // todo: 현재는 팔달관 주차장 좌표에 따라 다음과 같이 구현하였으나 딥러닝 파트에서 차량이 들어오는 것을 탐지했을 때
                //       bool type 변수가 true임을 전달하면, 서비스가 활성화되도록 하는 것이 더 좋을 듯.
                if(dist < 50){
                    Log.d(TAG, "Start Main Service");
                    marker2.setMap(null);

                    Intent intent = new Intent(MapFragmentActivity.this, MainServiceActvity.class);
                    startActivity(intent);
                    //overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_enter); <- 이부분 수정 필요
                }
            }
        });

        //Map Click Event 발생 시 해당 위치에 마커가 생성되고, 마커 위의 info를 Click하면 Navigation이 뜸.
        InfoWindow naviInfo = new InfoWindow();
        naviInfo.setAdapter(new InfoWindow.DefaultTextAdapter(this) {
            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                return "이 위치로\n길찾기 시작";
            }
        });

        naviInfo.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                if(path != null){
                    path.setMap(null);
                }
                //new NaverNaviApi().execute();
                return false;
            }
        });

        naverMap.setOnMapClickListener(new NaverMap.OnMapClickListener(){
            @Override
            public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
                if(marker1 == null){
                    marker1 = new Marker();
                    marker1.setPosition(new LatLng(latLng.latitude, latLng.longitude));
                    marker1.setMap(naverMap);
                    naviInfo.open(marker1);
                    des_lat = latLng.latitude;
                    des_lon = latLng.longitude;
                }
                else{
                    naviInfo.close();
                    marker1.setMap(null);
                    marker1 = null;
                }
            }
        });
    }

    // todo: Project Main Service -> AsyncTask or Thread(메인 스레드가 이 스레드가 다 돌아갈때까지 기다려야하면)
    class MainService extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }


    // todo: 길찾기 service 시작
    //AsyncTask<Params, Progress, Result>
    //execute -> doInBackground -> onProgressUpdate -> onPostExecute
    class NaverNaviApi extends AsyncTask<Void ,Integer, String> {

        @Override
        protected String doInBackground(Void... voids) {
            Retrofit retrofit = new Retrofit.Builder().baseUrl("https://naveropenapi.apigw.ntruss.com/map-direction/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            NaverApi naverApi = retrofit.create(NaverApi.class);

            String start = src_lat +","+src_lon;
            String goal = des_lat + "," + des_lon;
            Call<List<Navigation>> call = naverApi.getPath("kkbsnszctg", "qt7KxFjrZJ6tvHYYScybMB6JpESkBcCUlc12VX5u",start,goal);

            call.enqueue(new Callback<List<Navigation>>() {
                @Override
                public void onResponse(Call<List<Navigation>> call, Response<List<Navigation>> response) {
                    Log.d(TAG, "Call Success");
                }

                @Override
                public void onFailure(Call<List<Navigation>> call, Throwable t) {
                    Log.e(TAG, "Call Failure");
                    Log.e(TAG, t.getMessage());
                }
            });
            return null;
        }
    }
}
