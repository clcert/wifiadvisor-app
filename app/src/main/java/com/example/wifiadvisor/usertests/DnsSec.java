package com.example.wifiadvisor.usertests;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.provider.ContactsContract;
import android.widget.TextView;

import org.minidns.dnsqueryresult.DnsQueryResult;
import org.minidns.dnsserverlookup.android21.AndroidUsingLinkProperties;
import org.minidns.hla.DnssecResolverApi;
import org.minidns.hla.ResolverResult;
import org.minidns.record.A;
import org.minidns.record.Data;
import org.minidns.record.Record;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Callable;

public class DnsSec implements Runnable{
    private final TextView logtext;
    private final Activity activity;
    boolean adFlag;
    boolean doFlag;
    boolean rrsigFlag;
    Context context;

    public DnsSec(Context context, TextView logtext, Activity activity) {
        this.context = context;
        this.logtext = logtext;
        this.activity = activity;
    }

    public void setAdFlag(boolean adFlag) {
        this.adFlag = adFlag;
    }

    public void setDoFlag(boolean doFlag) {
        this.doFlag = doFlag;
    }

    public void setRrsigFlag(boolean rrsigFlag) {
        this.rrsigFlag = rrsigFlag;
    }

    public boolean isAdFlag() {
        return adFlag;
    }

    public boolean isDoFlag() {
        return doFlag;
    }

    public boolean isRrsigFlag() {
        return rrsigFlag;
    }

    @Override
    public String toString() {
        return "DnsSec{" +
                "adFlag=" + adFlag +
                ", doFlag=" + doFlag +
                ", rrsigFlag=" + rrsigFlag +
                '}';
    }

    @Override
    public void run() {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                logtext.setText("Ejecutando medición DNSSEC");
            }
        });
        ResolverResult<A> result = null;
        try {
            AndroidUsingLinkProperties.setup(Objects.requireNonNull(context));
            result = DnssecResolverApi.INSTANCE.resolve("paypal.com", A.class);
            DnsQueryResult dnsQueryResult = result.getDnsQueryResult();
            setAdFlag(dnsQueryResult.response.authenticData);
            setDoFlag(dnsQueryResult.response.isDnssecOk());
            setRrsigFlag(false);
            for(Record<? extends Data> data:dnsQueryResult.response.answerSection){
                if(data.type.equals(Record.TYPE.RRSIG)){
                    setRrsigFlag(true);
                    break;
                }
            }
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    logtext.setText("Finalizando medición DNSSEC");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
