package com.example.naverpractice.MainService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import com.example.naverpractice.R;

import org.greenrobot.eventbus.EventBus;

public class MainServiceActvity extends AppCompatActivity {
    private static final String TAG = "[MAIN SERVICE]";
    LocationListener gpsLocationListener;
    private double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parking_lot_activity);

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull android.location.Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                LocationEvent event = new LocationEvent();
                event.latitude = latitude;
                event.longitude = longitude;
                EventBus.getDefault().post(event);
            }
        };

        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainServiceActvity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, gpsLocationListener);
        }
    }

    public static class LocationEvent{
        double latitude, longitude;
    }
}