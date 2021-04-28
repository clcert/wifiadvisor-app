package com.example.wifiadvisor.usertests;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

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

public class DnsAkamai implements Runnable {
    Activity activity;
    TextView logtext;
    Context context;
    String ns_akamai;
    String ecs_akamai;
    String ip_akamai;

    public DnsAkamai(Context context, TextView logtext, Activity activity) {
        this.context = context;
        this.logtext = logtext;
        this.activity = activity;
    }

    @Override
    public void run() {
        try {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                   logtext.setText("Ejecutando medición Akamai");
                }
            });
            AndroidUsingLinkProperties.setup(Objects.requireNonNull(context));
            ResolverResult<TXT> akamaiResolver = ResolverApi.INSTANCE.resolve("whoami.ipv4.akahelp.net", TXT.class);
            if(akamaiResolver.getResponseCode()== DnsMessage.RESPONSE_CODE.NO_ERROR){
                List<Record<? extends Data>> answersAkamai = akamaiResolver.getDnsQueryResult().response.answerSection;;
                Pattern nsPattern = Pattern.compile("ns(\\s+/\\s+|\\\"\\s+\")(.*)\"");
                Pattern ecsPattern = Pattern.compile("ecs(\\s+/\\s+|\\\"\\s+\")(.*)\"");
                Pattern ipPattern = Pattern.compile("ip(\\s+/\\s+|\\\"\\s+\")(.*)\"");

                Matcher nsMatcher;
                Matcher ecsMatcher;
                Matcher ipMatcher;

                for(Record<? extends Data> answer : answersAkamai){
                    nsMatcher = nsPattern.matcher(answer.toString());
                    ecsMatcher = ecsPattern.matcher(answer.toString());
                    ipMatcher = ipPattern.matcher(answer.toString());

                    if(nsMatcher.find()){
                        setNsAkamai(nsMatcher.group(2));
                    }
                    if(ecsMatcher.find()){
                        setEcsAkamai(ecsMatcher.group(2));
                    }
                    if(ipMatcher.find()){
                        setIpAkamai(ipMatcher.group(2));
                    }
                }
            }else {
                System.out.println(akamaiResolver.getRawAnswer());
            }
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    logtext.setText("Finalizando medición Akamai");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setEcsAkamai(String ecs_akamai) {
        this.ecs_akamai = ecs_akamai;
    }

    public void setIpAkamai(String ip_akamai) {
        this.ip_akamai = ip_akamai;
    }

    public void setNsAkamai(String ns_akamai) {
        this.ns_akamai = ns_akamai;
    }

    public String getNs_akamai() {
        return ns_akamai;
    }

    public String getEcs_akamai() {
        return ecs_akamai;
    }

    public String getIp_akamai() {
        return ip_akamai;
    }

    @Override
    public String toString() {
        return "DnsAkamai{" +
                "  ns_akamai='" + ns_akamai + '\'' +
                ", ecs_akamai='" + ecs_akamai + '\'' +
                ", ip_akamai='" + ip_akamai + '\'' +
                '}';
    }
}
