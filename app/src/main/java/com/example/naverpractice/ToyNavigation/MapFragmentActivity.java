package com.example.naverpractice.ToyNavigation;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.naverpractice.MainService.MainServiceActvity;
import com.example.naverpractice.R;
import com.example.naverpractice.network.ApiClient;
import com.example.naverpractice.network.ApiInterface;
import com.example.naverpractice.network.ParkingLot;
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
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.ZoomControlView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapFragmentActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final String TAG = "NaverMap";
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    private MapFragment mapFragment;

    private double src_lat, src_lon;
    private final double paldal_lat = 37.28476;
    private final double paldal_lon = 127.04425;

    private int num_available = 0;
    private final int num_entire = 180;

    private InfoThread infoThread;

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
        infoThread = new InfoThread();
        infoThread.start();
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
                Marker marker = new Marker();
                if(dist < 200){
                    Log.d(TAG, "해당 현위치는 주차장 반경 200m 내에 속한다");
                    marker.setPosition(new LatLng(paldal_lat, paldal_lon));
                    marker.setMap(naverMap);
                    marker.setMinZoom(14);
                    InfoWindow infoWindow = new InfoWindow();
                    infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(MapFragmentActivity.this){
                        @NonNull
                        @Override
                        public CharSequence getText(@NonNull InfoWindow infoWindow) {
                            return num_available + "/" + num_entire;
                        }
                    });
                    infoWindow.open(marker);
                }

                if(dist < 50){
                    Log.d(TAG, "Start Main Service");
                    marker.setMap(null);

                    Intent intent = new Intent(MapFragmentActivity.this, MainServiceActvity.class);
                    startActivity(intent);
                }
            }
        });
    }

    class InfoThread extends Thread{

        private int empty_seat = 0;

        @Override
        public void run() {
            while(true){
                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<List<ParkingLot>> call = apiInterface.getEmpty();
                call.enqueue(new Callback<List<ParkingLot>>() {
                    @Override
                    public void onResponse(Call<List<ParkingLot>> call, Response<List<ParkingLot>> response) {
                        for(int i = 0; i<num_entire;i++){
                            int result = response.body().get(i).getIsEmpty();
                            boolean re = result == 1 ? true : false;
                            if(re){
                                empty_seat++;
                            };
                        }
                        num_available = empty_seat;
                        empty_seat = 0;
                    }

                    @Override
                    public void onFailure(Call<List<ParkingLot>> call, Throwable t) {
                        Log.e(TAG, t.getMessage());
                    }
                });
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
