package com.example.wifiadvisor.moshi.adapters;

import com.example.wifiadvisor.moshi.models.testsTimestamp.DnsTimestamp;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.util.ArrayList;
import java.util.List;


public class ListDnsTimestampAdapter{
    @ToJson
    List<DnsTimestamp> fromJson(ArrayList<DnsTimestamp> list){
        return list;
    }

    @FromJson
    ArrayList<DnsTimestamp> fromJson(List<DnsTimestamp> list){
        return new ArrayList<>(list);
    }
}

