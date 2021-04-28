/*
 * Part of the code was obtained from https://rorist.github.io/android-network-discovery/
 * */

package com.example.wifiadvisor.usertests;

import android.app.Activity;
import android.widget.TextView;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.example.wifiadvisor.usertests.Devices.numberToIp;

public class DeviceFinderPrivateIp {
    final ExecutorService mPool;
    private final static int TIMEOUT_SCAN = 3600; // seconds
    private final static int TIMEOUT_SHUTDOWN = 10; // seconds
    int currentIps;
    ArrayList<String> hosts;
    String network;
    int maxHosts;
    BigInteger firstIp;
    TextView textView;
    Activity activity;

    public DeviceFinderPrivateIp(String network, int maxHosts, BigInteger firstIp, TextView textView, Activity activity) {
        this.hosts = new ArrayList<String>();
        this.mPool = Executors.newFixedThreadPool(8);
        this.network = network;
        this.maxHosts = maxHosts;
        this.firstIp =  firstIp;
        this.textView = textView;
        this.activity = activity;
        this.currentIps = 0;
    }

    public ArrayList<String> getHosts() {
        return hosts;
    }

    public int getMaxHosts() {
        return maxHosts;
    }

    public BigInteger getFirstIp() {
        return firstIp;
    }

    public void doInBackground(Void... params) {
        for (int i = 0; i < getMaxHosts(); i++) {
            BigInteger value = getFirstIp().add(BigInteger.valueOf(i));
            launch(numberToIp(value));
        }
        mPool.shutdown();
        try {
            if (!mPool.awaitTermination(TIMEOUT_SCAN, TimeUnit.SECONDS)) {
                mPool.shutdownNow();
                mPool.awaitTermination(TIMEOUT_SHUTDOWN, TimeUnit.SECONDS);
            }
        } catch (InterruptedException e) {
            mPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }


    private void launch(String ip) {
        if (!mPool.isShutdown()) {
            mPool.execute(new CheckRunnable(ip));
        }
    }

    public void onCancelled() {
        if (mPool != null) {
            synchronized (mPool) {
                mPool.shutdownNow();
            }
        }
    }

    private class CheckRunnable implements Runnable {
        private final String address;

        CheckRunnable(String address) {
            this.address = address;
        }

        public void run() {
            try {
                InetAddress h = InetAddress.getByName(address);
                if (h.isReachable(1000)) {
                    publish(address);
                    return;
                }
                publish(null);

            } catch (IOException e) {
                publish(null);
            }
        }
    }

    private void publish(String host) {
        System.out.println(host);
        currentIps = currentIps +1;
        if (host != null) {
            getHosts().add(host);
        }
        activity.runOnUiThread(new Runnable() {
            public void run() {
                textView.setText(String.format("%d ips escaneadas de %d", currentIps, maxHosts) );
            }
        });
    }
}
