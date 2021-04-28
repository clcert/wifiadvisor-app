package com.example.wifiadvisor.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wifiadvisor.R;
import com.example.wifiadvisor.usertests.Ooni;

import java.util.concurrent.Executors;

public class SpeedMeasurementActivity extends AppCompatActivity {
    private TextView logText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_bar);
        logText = (TextView) findViewById(R.id.message);
        Executors.defaultThreadFactory().newThread(() -> {
            Ooni ooni = new Ooni(getApplicationContext(), logText, this);
            ooni.runNtd();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("results", R.id.navigation_results);
            startActivity(intent);
        }).start();
    }

}