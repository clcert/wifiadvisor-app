package com.example.wifiadvisor.moshi.models;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.wifiadvisor.R;

public class TestBase {
    String place;
    String username;

    public TestBase(Activity activity){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        System.out.println(sharedPref.getString(activity.getString(R.string.username), ""));
        System.out.println(sharedPref.getString(activity.getString(R.string.place_measurement), ""));
        this.username=sharedPref.getString(activity.getString(R.string.username), "");
        this.place=sharedPref.getString(activity.getString(R.string.place_measurement), "");
    }
}
