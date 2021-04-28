package com.example.wifiadvisor.usertests;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.widget.TextView;

import com.example.wifiadvisor.BuildConfig;
import com.example.wifiadvisor.R;
import com.example.wifiadvisor.data.SingletonOkHttpCLient;
import com.example.wifiadvisor.enums.*;
import com.example.wifiadvisor.moshi.models.TestBase;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainDns {
    private transient TextView logtext;
    private transient Activity activity;
    String dns1_android;
    String dns2_android;
    String ns_akamai;
    String ecs_akamai;
    String ip_akamai;
    Boolean do_flag;
    Boolean ad_flag;
    Boolean rrsig;
    String resolver_ip_oarc;
    RatingOarcEnum rating_source_port;
    RatingOarcEnum rating_transaction_id;
    Integer std_source_port;
    Integer std_transaction_id;
    Float bits_of_entropy_source_port;
    Float bits_of_entropy_transaction_id;

    public MainDns(TextView logText, Activity activity) {
        this.logtext=logText;
        this.activity=activity;
    }

    public void runTests(Context context){
        activity.runOnUiThread(new Runnable() {
            public void run() {
                logtext.setText("Iniciando pruebas de DNS");
            }
        });
        try {
            DnsAndroid dnsAndroid = new DnsAndroid();
            dnsAndroid.runAndroidDns(context);
            dns1_android = dnsAndroid.getDns1();
            dns2_android = dnsAndroid.getDns2();
            HandlerThread mHandlerThread = new HandlerThread("dns");
            mHandlerThread.start();
            Handler handler = new Handler(mHandlerThread.getLooper());
            DnsAkamai dnsAkamai = new DnsAkamai(context, logtext, activity);
            DnsOarc dnsOarc = new DnsOarc(context, logtext, activity);
            DnsSec dnsSec = new DnsSec(context, logtext, activity);
            handler.post(dnsAkamai);
            handler.post(dnsOarc);
            handler.post(dnsSec);
            mHandlerThread.quitSafely();
            mHandlerThread.join();
            ns_akamai = dnsAkamai.getNs_akamai();
            ecs_akamai = dnsAkamai.getEcs_akamai();
            ip_akamai = dnsAkamai.getEcs_akamai();
            do_flag = dnsSec.isDoFlag();
            ad_flag = dnsSec.isAdFlag();
            rrsig = dnsSec.isRrsigFlag();
            resolver_ip_oarc = dnsOarc.getResolver_ip_oarc();
            rating_source_port = dnsOarc.getRating_source_port();
            rating_transaction_id = dnsOarc.getRating_transaction_id();
            std_source_port = dnsOarc.getStd_source_port();
            std_transaction_id = dnsOarc.getStd_transaction_id();
            sendDnsResults();
        } catch (Exception e) {
                    e.printStackTrace();
        }
    }

    public void sendDnsResults() throws Exception {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                logtext.setText("Enviando resultados al servidor");
            }
        });
        System.out.println(this.toString());
        Moshi.Builder builder = new Moshi.Builder();
        final Moshi moshi = builder.build();
        final JsonAdapter<DnsTest> jsonAdapter = moshi.adapter(DnsTest.class);
        final OkHttpClient client = SingletonOkHttpCLient.getInstance().getClient();
        String json = jsonAdapter.toJson(new DnsTest(this, activity));

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(json, JSON);
        final Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+ activity.getString(R.string.url_dns_test))
                .post(body)
                .build();
        System.out.println(json);
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                System.out.println(response.code());
                response.body().close();
                activity.runOnUiThread(() -> logtext.setText("Resultados enviados"));
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e);
                System.out.println(e.getStackTrace());
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        logtext.setText("Resultados no pudieron ser enviados");
                    }
                });
            }
        });
    }

    @Override
    public String toString() {
        return "MainDns{" +
                "dns1_android='" + dns1_android + '\'' +
                ", dns2_android='" + dns2_android + '\'' +
                ", ns_akamai='" + ns_akamai + '\'' +
                ", ecs_akamai='" + ecs_akamai + '\'' +
                ", ip_akamai='" + ip_akamai + '\'' +
                ", do_flag=" + do_flag +
                ", ad_flag=" + ad_flag +
                ", rrsig=" + rrsig +
                ", resolver_ip_oarc='" + resolver_ip_oarc + '\'' +
                ", rating_source_port=" + rating_source_port +
                ", rating_transaction_id=" + rating_transaction_id +
                ", std_source_port=" + std_source_port +
                ", std_transaction_id=" + std_transaction_id +
                ", bits_of_entropy_source_port=" + bits_of_entropy_source_port +
                ", bits_of_entropy_transaction_id=" + bits_of_entropy_transaction_id +
                '}';
    }
}

class DnsTest{
    MainDns dns_test;
    TestBase test_base;
    public DnsTest(MainDns mainDns, Activity activity) {

        dns_test = mainDns;
        test_base = new TestBase(activity);

    }
}
