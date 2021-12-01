package com.example.naverpractice.MainService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.example.naverpractice.R;
import com.example.naverpractice.databinding.ActivityMainBinding;

import org.greenrobot.eventbus.EventBus;

public class MainServiceActvity extends AppCompatActivity {
    private static final String TAG = "[MAIN SERVICE]";
    LocationListener gpsLocationListener;
    private EventBusProvider provider;
    private double latitude, longitude;
    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parking_lot_activity);

        provider = new EventBusProvider();

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull android.location.Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                provider.latitude = latitude;
                provider.longitude = longitude;
                EventBus.getDefault().post(provider);
            }
        };

        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainServiceActvity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, gpsLocationListener);
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.info_frame, new InformationFragment());
        ft.commit();
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()){
            case R.id.near_park:
                if(checked) flag = 1;
                provider.flag = flag;
                EventBus.getDefault().post(provider);
                break;
            case R.id.empty_around:
                if(checked) flag = 2;
                provider.flag = flag;
                EventBus.getDefault().post(provider);
                break;
            case R.id.no_service:
                if(checked) flag = 3;
                provider.flag = flag;
                EventBus.getDefault().post(provider);
                break;
        }

    }

    public static class EventBusProvider{
        double latitude, longitude;
        int flag;
    }
}