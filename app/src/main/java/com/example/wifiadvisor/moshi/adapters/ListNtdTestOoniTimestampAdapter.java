package com.example.wifiadvisor.moshi.adapters;

import com.example.wifiadvisor.moshi.models.testsTimestamp.NdtTestOoniTimestamp;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.util.ArrayList;
import java.util.List;

public class ListNtdTestOoniTimestampAdapter{
    @ToJson
    List<NdtTestOoniTimestamp> fromJson(ArrayList<NdtTestOoniTimestamp> list){
        return list;
    }

    @FromJson
    ArrayList<NdtTestOoniTimestamp> fromJson(List<NdtTestOoniTimestamp> list){
        return new ArrayList<>(list);
    }
}
