package com.example.wifiadvisor.moshi.adapters;

import com.example.wifiadvisor.moshi.models.testsTimestamp.ProtocolsTimestamp;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.util.ArrayList;
import java.util.List;

public class ListProtocolsTimestampAdapter{
    @ToJson
    List<ProtocolsTimestamp> fromJson(ArrayList<ProtocolsTimestamp> list){
        return list;
    }

    @FromJson
    ArrayList<ProtocolsTimestamp> fromJson(List<ProtocolsTimestamp> list){
        return new ArrayList<>(list);
    }
}
