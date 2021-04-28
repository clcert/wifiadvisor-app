package com.example.wifiadvisor.moshi.models.testsTimestamp;

import android.content.Context;
import android.util.Pair;

import com.example.wifiadvisor.R;
import com.example.wifiadvisor.enums.RatingOarcEnum;
import com.example.wifiadvisor.moshi.models.DnsTest;

import java.util.Date;

public class DnsTimestamp implements TestTimestampI{
    DnsTest test;
    Date timestamp;

    @Override
    public String toString() {
        return "DnsTimestamp{" +
                "test=" + test +
                ", Timestamp=" + timestamp +
                '}';
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public DnsTest getTest() {
        return test;
    }

    public Pair<Boolean, String> checkAndroidDns(Context context){
        boolean androidDnsCorrect = !test.getDns1Android().equals(test.getDns2Android());
        int androidDnsCorrectMessage = androidDnsCorrect? R.string.correct_dns_configuration: R.string.incorrect_dns_configuration;
        return new Pair<>(androidDnsCorrect, context.getString(androidDnsCorrectMessage));
    }

    public Pair<Boolean, String> checkDnssec(Context context){
        boolean dnssecCorrect = test.getDoFlag() && test.getAdFlag() && test.getRrsig();
        int dnssecCorrectMessage = dnssecCorrect? R.string.dnssec_support:R.string.dnssce_not_support;
        return new Pair<>(dnssecCorrect, context.getString(dnssecCorrectMessage));
    }
    public Pair<Boolean, String> checkSourcePort(Context context){
        if(test.getRatingSourcePort()==null){
            return new Pair<>(null, context.getString(R.string.measurement_not_performed));
        }
        boolean sourcePortCorrect = RatingOarcEnum.GREAT.equals(test.getRatingSourcePort());
        int sourcePortMessage = sourcePortCorrect? R.string.source_port_randomness_correct:R.string.source_port_randomness_incorrect;
        return new Pair<>(sourcePortCorrect, context.getString(sourcePortMessage));
    }
    public Pair<Boolean, String> checkTransactionId(Context context){
        if(test.getRatingTransactionId()==null){
            return new Pair<>(null, context.getString(R.string.measurement_not_performed));
        }
        boolean transactionIdCorrect = RatingOarcEnum.GREAT.equals(test.getRatingTransactionId());
        int transactionIdMessage = transactionIdCorrect? R.string.transaction_id_randomness_correct:R.string.transaction_id_randomness_incorrect;
        return new Pair<>(transactionIdCorrect, context.getString(transactionIdMessage));
    }
}
