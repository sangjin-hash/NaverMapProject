package com.example.naverpractice;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Align;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.ZoomControlView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MapFragmentActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final String TAG = "NaverMap";
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    private MapFragment mapFragment;
    private double lat, lon;

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
                lat = location.getLatitude();
                lon = location.getLongitude();
                CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(lat,lon)).animate(CameraAnimation.Easing, 3000);
                naverMap.moveCamera(cameraUpdate);

                GPSdistance gpsDistance = new GPSdistance();
                boolean result = gpsDistance.isRange(lat, lon);
                if(result){
                    Log.d(TAG, "해당 현위치는 주차장 반경 200m 내에 속한다");
                    Marker marker = new Marker();
                    marker.setPosition(new LatLng(37.28476, 127.04425)); //팔달관 주차장 좌표
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
            }
        });
    }
}
