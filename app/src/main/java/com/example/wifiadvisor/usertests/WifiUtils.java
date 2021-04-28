package com.example.wifiadvisor.usertests;

import android.content.Context;
import android.net.wifi.WifiManager;

import java.math.BigInteger;

import static java.lang.String.format;

public class WifiUtils {
    public static WifiManager getWifiManager(Context context) {
        //TODO CHECK IF WIFI IS CONNECTED NETWORK INFO IS DEPRECATED  https://stackoverflow.com/questions/32547006/connectivitymanager-getnetworkinfoint-deprecated
        //https://stackoverflow.com/questions/53532406/activenetworkinfo-type-is-deprecated-in-api-level-28
        return (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

}
