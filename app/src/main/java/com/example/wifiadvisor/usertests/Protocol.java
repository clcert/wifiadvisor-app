package com.example.wifiadvisor.usertests;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.widget.TextView;

import com.example.wifiadvisor.BuildConfig;
import com.example.wifiadvisor.R;
import com.example.wifiadvisor.data.SingletonOkHttpCLient;
import com.example.wifiadvisor.moshi.models.TestBase;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.ToJson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Protocol {
    ArrayList<ProtocolTest> test;
    TestBase test_base;
    private final transient TextView logtext;
    private final transient Activity activity;

    public Protocol(TextView logText, Activity activity) {
        this.test = new ArrayList<>();
        this.logtext=logText;
        this.activity=activity;
        this.test_base = new TestBase(activity);
    }

    public int runTest(Context context) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                logtext.setText("Corriendo pruebas de protocolo");
            }
        });
        WifiManager wifiManager = WifiUtils.getWifiManager(context);
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        List<ScanResult> scanResults = wifiManager.getScanResults();
        if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
            String SSID = connectionInfo.getSSID().replaceAll("^\"|\"$", "");
            ScanResult lastScanResult = null;
            long maxTimestamp = Long.MIN_VALUE;
            for (ScanResult scanResult : scanResults) {
                if (scanResult.SSID.equals(SSID) && scanResult.timestamp > maxTimestamp) {
                    lastScanResult = scanResult;
                    maxTimestamp = lastScanResult.timestamp;
                }
            }
            if (lastScanResult != null && !TextUtils.isEmpty(lastScanResult.capabilities)) {
                System.out.println(lastScanResult);
                System.out.println(lastScanResult.capabilities);
                this.addCapabilitiesToTest(lastScanResult.capabilities);
            }
        }
        if (getTest() != null && getTest().size()>0){
            try {
                sendProtocolResults();
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        logtext.setText("Terminando pruebas de protocolo");
                    }
                });
                return 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    void addCapabilitiesToTest(String capabilities) {
        Pattern pattern = Pattern.compile("\\[((\\w+-?){1,3})]");
        Matcher matcher = pattern.matcher(capabilities);
        int start = 0;
        while (matcher.find(start)) {
            start = matcher.start() + 1;
            String[] protocolTest = matcher.group(1).split("-");
            int size = protocolTest.length;
            String protocol = null, key_management = null, cipher = null;
            if (size > 0 && size < 4) {
                protocol = protocolTest[0];
                if (size > 1) key_management = protocolTest[1];
                if (size > 2) cipher = protocolTest[2];
                this.test.add(new ProtocolTest(protocol, key_management, cipher));
            }
        }
    }

    public ArrayList<ProtocolTest> getTest() {
        return test;
    }

    @Override
    public String toString() {
        return "Protocol{" +
                ", tests=" + test +
                '}';
    }

    public void sendProtocolResults() throws Exception {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                logtext.setText("Enviando resultados al servidor");
            }
        });
        Moshi.Builder builder = new Moshi.Builder();
        builder.add(new ListProtocolTestAdapter());
        final Moshi moshi = builder.build();
        final JsonAdapter<Protocol> jsonAdapter = moshi.adapter(Protocol.class);
        final OkHttpClient client = SingletonOkHttpCLient.getInstance().getClient();
        String json = jsonAdapter.toJson(this);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(json, JSON);
        final Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+activity.getString(R.string.url_protocol_test))
                .post(body)
                .build();
        System.out.println(json);
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println(response.code());
                response.body().close();
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e);
                System.out.println(e.getStackTrace());
            }
        });
    }

}

class ProtocolTest {
    String protocol_name;
    String key_management;
    String cipher;

    public ProtocolTest(String protocol_name, String key_management, String cipher) {
        this.protocol_name = protocol_name;
        this.key_management = key_management;
        this.cipher = cipher;
    }

    @Override
    public String toString() {
        return "ProtocolTest{" +
                "protocol_name='" + protocol_name + '\'' +
                ", key_management='" + key_management + '\'' +
                ", cipher='" + cipher + '\'' +
                '}';
    }
}

class ListProtocolTestAdapter{
    @ToJson
    List<ProtocolTest> toJson(ArrayList<ProtocolTest> list){
        return list;
    }

    @FromJson
    ArrayList<ProtocolTest> fromJson(List<ProtocolTest> list){
        return new ArrayList<>(list);
    }
}
