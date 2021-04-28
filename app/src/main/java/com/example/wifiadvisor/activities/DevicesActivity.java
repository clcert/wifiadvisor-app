package com.example.wifiadvisor.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wifiadvisor.R;
import com.example.wifiadvisor.usertests.Devices;

import java.util.concurrent.Executors;

public class DevicesActivity extends AppCompatActivity {
    private TextView logText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_bar);
        logText = (TextView) findViewById(R.id.message);
        Executors.defaultThreadFactory().newThread(() -> {
            Devices devices= new Devices(logText, this);
            devices.runTest();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("results", R.id.navigation_results);
            startActivity(intent);
        }).start();
    }
}
