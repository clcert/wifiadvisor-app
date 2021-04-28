package com.example.wifiadvisor.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wifiadvisor.R;
import com.example.wifiadvisor.usertests.Ooni;

import java.util.concurrent.Executors;

public class BlockingTestActivity extends AppCompatActivity {
    TextView logText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_bar);
        Bundle extras = getIntent().getExtras();
        String url;
        if (extras != null) {
            url = extras.getString("url");
            System.out.println(url);
            url = url.trim();
            if(!(url.startsWith("http://") || url.startsWith("https://"))){
                url = "https://" + url;
            }
            logText = (TextView) findViewById(R.id.message);
            String finalUrl = url;
            Executors.defaultThreadFactory().newThread(() -> {
                Ooni ooni = new Ooni(getApplicationContext(), logText, this);
                String[] link =new String[1];
                link[0]= finalUrl;
                logText.findViewById(R.id.message);
                ooni.runWeb(link);
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("results", R.id.navigation_results);
                startActivity(intent);
            }).start();
        }
    }
}
