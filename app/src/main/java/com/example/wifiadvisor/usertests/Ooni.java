package com.example.wifiadvisor.usertests;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wifiadvisor.BuildConfig;
import com.example.wifiadvisor.R;
import com.example.wifiadvisor.data.SingletonOkHttpCLient;
import com.example.wifiadvisor.enums.BlockingEnum;
import com.example.wifiadvisor.enums.DnsConsistencyEnum;
import com.example.wifiadvisor.moshi.adapters.ListTcpConnectWebTestOoni;
import com.example.wifiadvisor.moshi.models.TcpConnectWebTestOoni;
import com.example.wifiadvisor.moshi.models.TestBase;
import com.example.wifiadvisor.moshi.models.WebTestOoni;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Ooni {
    transient Context context;
    String state_dir;
    String temp_dir;
    String assets_dir;
    int version;
    Moshi moshi;
    JsonAdapter<Settings> jsonAdapterSettings;
    TextView logText;
    Activity activity;
    public Ooni(Context context, TextView logText, Activity activity) {
        this.context = context;
        state_dir = getFolder("state_dir");
        temp_dir = getFolder("temp_dir");
        assets_dir = getFolder("asset_dir");
        version = 1;
        Moshi.Builder builder = new Moshi.Builder();
        builder.add(new ListTcpConnectWebTestOoni());
        this.moshi = builder.build();
        this.jsonAdapterSettings = moshi.adapter(Settings.class);
        this.logText = logText;
        this.activity = activity;
    }

    String getFolder(String name) {
        File folder = new File(context.getExternalFilesDir(null), name);
        String folderPath = null;
        folder.mkdirs();
        try {
            folderPath = folder.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return folderPath;
    }

    public void runNtd() {
        String json = jsonAdapterSettings.toJson(new Settings(state_dir, temp_dir, assets_dir, "Ndt", version, new String[0]));
        OoniTest ooniTest = new OoniTest(json, logText, activity);
        Thread thread = Executors.defaultThreadFactory().newThread(ooniTest);
        thread.start();
        try {
            thread.join();
            String result = ooniTest.getResult();
            JsonAdapter<JsonNdt> jsonJsonNdt = moshi.adapter(JsonNdt.class);
            JsonNdt ndtResult = jsonJsonNdt.fromJson(result);
            if(ndtResult!=null){
                ndtResult.test_keys.getSummary().transformToSeconds();
                sendNtdResults(ndtResult);
            }else{
                System.out.println("Error ndt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void runWeb(String[] inputs) {
        String json = jsonAdapterSettings.toJson(new Settings(state_dir, temp_dir, assets_dir, "WebConnectivity", version, inputs));
        OoniTest ooniTest = new OoniTest(json, logText, activity);
        Thread thread = Executors.defaultThreadFactory().newThread(ooniTest);
        thread.start();
        try {
            thread.join();
            String result = ooniTest.getResult().replaceAll("\"blocking\":false", "\"blocking\":\"not_blocking\"");
            JsonAdapter<JsonWebTestOoni> jsonWebTestOoni = moshi.adapter(JsonWebTestOoni.class);
            JsonWebTestOoni webTestOoniResult = jsonWebTestOoni.fromJson(result);
            System.out.println(webTestOoniResult);
            sendWebResults(webTestOoniResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendWebResults(JsonWebTestOoni webTestOoniResult) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                logText.setText("Enviando resultados al servidor");
            }
        });
        JsonAdapter<WebTest> jsonAdapter = moshi.adapter(WebTest.class);
        OkHttpClient client = SingletonOkHttpCLient.getInstance().getClient();
        String json = jsonAdapter.toJson(new WebTest(webTestOoniResult, activity));
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(json, JSON);
        final Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL + context.getString(R.string.url_ooni_web_test))
                .post(body)
                .build();
        System.out.println(json);
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println(response.code());
                response.body().close();
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        logText.setText("Resultados recibidos en el servidor");
                    }
                });
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e);
                System.out.println(e.getStackTrace());
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        logText.setText("Resultados no puedieron ser recibidos por el servidor");
                    }
                });
            }
        });
    }


    public void sendNtdResults(JsonNdt ndtResult) throws Exception {
        JsonAdapter<NdtTest> jsonAdapter = moshi.adapter(NdtTest.class);
        OkHttpClient client = SingletonOkHttpCLient.getInstance().getClient();
        ndtResult.getSummary().setReport_id(ndtResult.report_id);
        String json = jsonAdapter.toJson(new NdtTest(ndtResult.getSummary(), activity));

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(json, JSON);
        final Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL + context.getString(R.string.url_ndt_ooni_test))
                .post(body)
                .build();
        System.out.println(json);
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println(response.code());
                response.body().close();
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        logText.setText("Resultados recibidos en el servidor");
                    }
                });
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e);
                System.out.println(e.getStackTrace());
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        logText.setText("Resultados no puedieron ser recibidos por el servidor");
                    }
                });
            }
        });
    }
}

class Settings {
    String state_dir;
    String temp_dir;
    String assets_dir;
    String name;
    int version;
    Options options;
    String[] inputs;

    public Settings(String state_dir, String temp_dir, String assets_dir, String name, int version, String[] inputs) {
        this.state_dir = state_dir;
        this.temp_dir = temp_dir;
        this.assets_dir = assets_dir;
        this.name = name;
        this.version = version;
        this.options = new Options();
        this.inputs = inputs;
    }
}

class Options {
    String software_name;
    String software_version;

    public Options() {
        this.software_name = "WifiAdvisor";
        this.software_version = "1";
    }
}

class JsonNdt {
    TestKeyNtd test_keys;
    String report_id;

    public TestKeyNtd getTest_keys() {
        return test_keys;
    }

    public Summary getSummary() {
        return getTest_keys().getSummary();
    }

    @Override
    public String toString() {
        return "JsonNdt{" +
                "test_keys=" + test_keys +
                '}';
    }
}

class TestKeyNtd {
    Summary summary;

    public Summary getSummary() {
        return summary;
    }

    @Override
    public String toString() {
        return "TestKeyNtd{" +
                "summary=" + summary +
                '}';
    }

}

class Summary {
    String report_id;
    Float avg_rtt;
    Float download;
    Integer mss;
    Float max_rtt;
    Float min_rtt;
    Float ping;
    Float retransmit_rate;
    Float upload;

    @Override
    public String toString() {
        return "Summary{" +
                "report_id='" + report_id + '\'' +
                ", avg_rtt=" + avg_rtt +
                ", download=" + download +
                ", mss=" + mss +
                ", max_rtt=" + max_rtt +
                ", min_rtt=" + min_rtt +
                ", ping=" + ping +
                ", retransmit_rate=" + retransmit_rate +
                ", upload=" + upload +
                '}';
    }
    public void transformToSeconds(){
        this.download = this.download/1000;
        this.upload = this.upload/1000;
    }

    public void setReport_id(String report_id) {
        this.report_id = report_id;
    }
}

class NdtTest {
    Summary test;
    TestBase test_base;

    NdtTest(Summary summary, Activity activity) {
        this.test = summary;
        this.test_base = new TestBase(activity);
    }
}

class WebTest {
    WebTestOoni web_test_ooni;
    List<TcpConnectWebTestOoni> tcp_connect_web_tests_ooni;
    TestBase test_base;

    public WebTest(JsonWebTestOoni jsonWebTestOoni, Activity activity) {
        TestKeysOoni testKey = jsonWebTestOoni.getTest_keys();
        this.web_test_ooni = new WebTestOoni(jsonWebTestOoni.report_id, jsonWebTestOoni.input, jsonWebTestOoni.resolver_asn, jsonWebTestOoni.resolver_ip,
                jsonWebTestOoni.resolver_network_name, testKey.client_resolver, testKey.dns_experiment_failure, testKey.control_failure,
                testKey.http_experiment_failure, testKey.dns_consistency, testKey.body_length_match, testKey.headers_match, testKey.status_code_match,
                testKey.title_match, testKey.accessible, testKey.blocking);
        this.tcp_connect_web_tests_ooni = testKey.getTcpConnectWebTestOoniList();
        this.test_base = new TestBase(activity);

    }

}

class JsonWebTestOoni {
    String input;
    String report_id;
    String resolver_asn;
    String resolver_ip;
    String resolver_network_name;
    TestKeysOoni test_keys;

    public TestKeysOoni getTest_keys() {
        return test_keys;
    }

    @Override
    public String toString() {
        return "JsonWebTestOoni{" +
                "input='" + input + '\'' +
                ", report_id='" + report_id + '\'' +
                ", resolver_asn='" + resolver_asn + '\'' +
                ", resolver_ip='" + resolver_ip + '\'' +
                ", resolver_network_name='" + resolver_network_name + '\'' +
                ", test_keys=" + test_keys +
                '}';
    }
}

class TestKeysOoni {
    String dns_experiment_failure;
    String control_failure;
    String http_experiment_failure;
    DnsConsistencyEnum dns_consistency;
    Boolean body_length_match;
    Boolean headers_match;
    Boolean status_code_match;
    Boolean title_match;
    Boolean accessible;
    BlockingEnum blocking;
    String client_resolver;
    List<TcpConnectJson> tcp_connect;

    public List<TcpConnectJson> getTcp_connect() {
        return tcp_connect;
    }

    public List<TcpConnectWebTestOoni> getTcpConnectWebTestOoniList() {
        List<TcpConnectWebTestOoni> list = new ArrayList<>();
        if (tcp_connect !=null){
            for (TcpConnectJson tcpJson : tcp_connect) {
                if(tcpJson.getIp().contains("scrubbed")){
                    continue;
                }
                list.add(tcpJson.ToTcpConnectWebTestOoni());
            }
        }else {
            return null;
        }
        return list;
    }

    @Override
    public String toString() {
        return "TestKeysOoni{" +
                "dns_experiment_failure='" + dns_experiment_failure + '\'' +
                ", control_failure='" + control_failure + '\'' +
                ", http_experiment_failure='" + http_experiment_failure + '\'' +
                ", dns_consistency=" + dns_consistency +
                ", body_length_match=" + body_length_match +
                ", headers_match=" + headers_match +
                ", status_code_match=" + status_code_match +
                ", title_match=" + title_match +
                ", accessible=" + accessible +
                ", blocking=" + blocking +
                ", tcp_connect=" + tcp_connect +
                '}';
    }

}

class TcpConnectJson {
    String ip;
    Integer port;
    TcpConnectStatus status;

    TcpConnectWebTestOoni ToTcpConnectWebTestOoni() {
        return new TcpConnectWebTestOoni(ip, port, status.blocked, status.failure, status.success);
    }

    @Override
    public String toString() {
        return "TcpConnectJson{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", status=" + status +
                '}';
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }
}

class TcpConnectStatus {
    Boolean success;
    String failure;
    Boolean blocked;

    @Override
    public String toString() {
        return "TcpConnectStatus{" +
                "success=" + success +
                ", failure='" + failure + '\'' +
                ", blocked=" + blocked +
                '}';
    }
}

