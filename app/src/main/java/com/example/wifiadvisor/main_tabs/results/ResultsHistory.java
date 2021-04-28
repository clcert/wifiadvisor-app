package com.example.wifiadvisor.main_tabs.results;

import android.content.Context;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.wifiadvisor.BuildConfig;
import com.example.wifiadvisor.R;
import com.example.wifiadvisor.data.SingletonOkHttpCLient;
import com.example.wifiadvisor.enums.TestsNamesEnum;
import com.example.wifiadvisor.moshi.adapters.ListDevicesTimestampAdapter;
import com.example.wifiadvisor.moshi.adapters.ListDnsTimestampAdapter;
import com.example.wifiadvisor.moshi.adapters.ListNtdTestOoniTimestampAdapter;
import com.example.wifiadvisor.moshi.adapters.ListProtocolsTimestampAdapter;
import com.example.wifiadvisor.moshi.adapters.ListWebTestOoniTimestampAdapter;
import com.example.wifiadvisor.moshi.models.Results;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ResultsHistory {
    Context context;

    public ResultsHistory(Context context) {
        this.context = context;
    }

    public void loadData(TestsNamesEnum testName){
        Moshi.Builder builder = new Moshi.Builder();
        builder.add(Date.class, new Rfc3339DateJsonAdapter());
        builder.add(new ListDevicesTimestampAdapter());
        builder.add(new ListDnsTimestampAdapter());
        builder.add(new ListDnsTimestampAdapter());
        builder.add(new ListNtdTestOoniTimestampAdapter());
        builder.add(new ListProtocolsTimestampAdapter());
        builder.add(new ListWebTestOoniTimestampAdapter());
        final Moshi moshi = builder.build();
        final JsonAdapter<Results> jsonAdapter = moshi.adapter(Results.class);
        OkHttpClient client = SingletonOkHttpCLient.getInstance().getClient();
        Request request = new Request.Builder()
                .url(String.format("%s%s%s", BuildConfig.SERVER_URL, context.getString(R.string.url_test_results), testName.toString()))
                .build();
        System.out.println(request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                listener.onFailure(request, e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    final Results r = jsonAdapter.fromJson(responseBody.source());
                    assert r != null;
                    System.out.println(r);
                    if (listener != null)
                        listener.onResponse(r);

                }
            }
        });
    }

    private GetResultsListener listener;
    public void setResultsListener(GetResultsListener listener) {
        this.listener = listener;
    }

    public interface GetResultsListener {
        void onFailure(Request request, IOException e);
        void onResponse(Results response);
    }

}