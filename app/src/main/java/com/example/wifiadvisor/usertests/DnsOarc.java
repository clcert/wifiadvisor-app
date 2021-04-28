package com.example.wifiadvisor.usertests;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import com.example.wifiadvisor.R;
import com.example.wifiadvisor.enums.RatingOarcEnum;

import org.minidns.dnsmessage.DnsMessage;
import org.minidns.dnsserverlookup.android21.AndroidUsingLinkProperties;
import org.minidns.hla.ResolverApi;
import org.minidns.hla.ResolverResult;
import org.minidns.record.Data;
import org.minidns.record.Record;
import org.minidns.record.TXT;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DnsOarc implements Runnable {
    private final TextView logtext;
    private final Activity activity;
    String resolver_ip_oarc;
    RatingOarcEnum rating_source_port;
    RatingOarcEnum rating_transaction_id;
    Integer std_source_port;
    Integer std_transaction_id;
    Context context;

    public DnsOarc(Context context, TextView logtext, Activity activity) {
        this.context = context;
        this.logtext = logtext;
        this.activity = activity;
    }

    @Override
    public void run() {
        try {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    logtext.setText("Ejecutando medición Oarc");
                }
            });
            AndroidUsingLinkProperties.setup(Objects.requireNonNull(context));
            ResolverResult<TXT> resultPortTest = ResolverApi.INSTANCE.resolve("porttest.dns-oarc.net", TXT.class);
            ResolverResult<TXT> resultIdTest = ResolverApi.INSTANCE.resolve("txidtest.dns-oarc.net", TXT.class);
            Pattern oarcResultPattern = Pattern.compile("(TXT)\\s\"(.*)\\s+is\\s+(\\w+):.*std\\s+dev\\s+(\\d+)\"", Pattern.CASE_INSENSITIVE);
            Matcher oarcResultmatcher;
            if(resultPortTest.getResponseCode()== DnsMessage.RESPONSE_CODE.NO_ERROR){
                for (Record<? extends Data> answer : resultPortTest.getDnsQueryResult().response.answerSection) {
                    oarcResultmatcher = oarcResultPattern.matcher(answer.toString());
                    if (oarcResultmatcher.find()) {
                        resolver_ip_oarc = oarcResultmatcher.group(2);
                        rating_source_port = RatingOarcEnum.valueOf(oarcResultmatcher.group(3));
                        std_source_port = Integer.parseInt(Objects.requireNonNull(oarcResultmatcher.group(4)));
                    }
                }
            }else {
                System.out.println(resultPortTest.getRawAnswer());
            }
            if(resultIdTest.getResponseCode()== DnsMessage.RESPONSE_CODE.NO_ERROR){
                for (Record<? extends Data> answer : resultIdTest.getDnsQueryResult().response.answerSection) {
                    oarcResultmatcher = oarcResultPattern.matcher(answer.toString());
                    if (oarcResultmatcher.find()) {
                        if (resolver_ip_oarc == null) resolver_ip_oarc = oarcResultmatcher.group(2);
                        rating_transaction_id = RatingOarcEnum.valueOf(oarcResultmatcher.group(3));
                        std_transaction_id = Integer.parseInt(Objects.requireNonNull(oarcResultmatcher.group(4)));
                    }
                }
            }else {
                System.out.println(resultIdTest.getRawAnswer());
            }
            activity.runOnUiThread(() -> logtext.setText("Finalizando test Oarc"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RatingOarcEnum getRating_source_port() {
        return rating_source_port;
    }

    public RatingOarcEnum getRating_transaction_id() {
        return rating_transaction_id;
    }

    public String getResolver_ip_oarc() {
        return resolver_ip_oarc;
    }

    public Integer getStd_source_port() {
        return std_source_port;
    }

    public Integer getStd_transaction_id() {
        return std_transaction_id;
    }

    @Override
    public String toString() {
        return "DnsOarc{" +
                "resolver_ip_oarc='" + resolver_ip_oarc + '\'' +
                ", rating_source_port=" + rating_source_port +
                ", rating_transaction_id=" + rating_transaction_id +
                ", std_source_port='" + std_source_port + '\'' +
                ", std_transaction_id='" + std_transaction_id + '\'' +
                '}';
    }
}

