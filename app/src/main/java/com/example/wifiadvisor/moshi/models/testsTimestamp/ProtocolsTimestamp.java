package com.example.wifiadvisor.moshi.models.testsTimestamp;

import android.util.Pair;

import com.example.wifiadvisor.R;
import com.example.wifiadvisor.moshi.models.ProtocolTest;

import java.util.Date;
import java.util.List;

public class ProtocolsTimestamp implements TestTimestampI{
    List<ProtocolTest> test;
    Date timestamp;

    @Override
    public String toString() {
        return "ProtocolsTimestamp{" +
                "protocols=" + test +
                ", timestamp=" + timestamp +
                '}';
    }
    //I/System.out: {"test":[{"cipher":"CCMP","key_management":"PSK","protocol_name":"WPA2"},{"cipher":"CCMP","key_management":"PSK","protocol_name":"RSN"},{"protocol_name":"ESS"},{"key_management":"HT","protocol_name":"WFA"},{"key_management":"VHT","protocol_name":"WFA"}]}
    public Pair<Boolean, Integer> check(){
        boolean correct = false;
        int correctMessage = R.string.protocol_result_wpa2_not_support;
        for(ProtocolTest protocolTest: test){
            if(protocolTest.getProtocolName().equals("WPA2"))
                if(protocolTest.getCipher() != null && !protocolTest.getCipher().equals("")){
                    correctMessage = R.string.protocol_result_wpa2_support;
                    correct = true;
                    break;
                }else {
                    correctMessage = R.string.protocol_result_wpa_but_not_cipher;
                    break;
                }
        }
        return new Pair<>(correct, correctMessage);
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public List<ProtocolTest> getTest() {
        return test;
    }
}
