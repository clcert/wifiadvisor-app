package com.example.wifiadvisor.usertests;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;

import java.math.BigInteger;

import static java.lang.String.format;

public class DnsAndroid {
    String dns1;
    String dns2;


    public void  runAndroidDns(Context context){
        WifiManager wifiManager = WifiUtils.getWifiManager(context);
        DhcpInfo dhcp = wifiManager.getDhcpInfo();
        dns1 = numberToIp(BigInteger.valueOf(dhcp.dns1));
        dns2 = numberToIp(BigInteger.valueOf(dhcp.dns2));
    }

    public static String numberToIp(BigInteger value) {
        BigInteger ones = BigInteger.valueOf(0xff);
        return format("%d.%d.%d.%d", value.and(ones), ((value.shiftRight(8)).and(ones)), ((value.shiftRight(16)).and(ones)), (value.shiftRight(24).and(ones)));
    }

    public String getDns1() {
        return dns1;
    }

    public String getDns2() {
        return dns2;
    }

    @Override
    public String toString() {
        return "DnsAndroid{" +
                "dns1='" + dns1 + '\'' +
                ", dns2='" + dns2 + '\'' +
                '}';
    }
}
