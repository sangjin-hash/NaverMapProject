package com.example.naverpractice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class MainServiceActvity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parking_lot_activity);

        imageView = findViewById(R.id.imageView1);
        imageView.setImageResource(R.drawable.parkinglot);
    }
}