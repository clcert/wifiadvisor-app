package com.example.wifiadvisor.moshi.adapters;

import com.example.wifiadvisor.moshi.models.testsTimestamp.DevicesTimestamp;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.util.ArrayList;
import java.util.List;

public class ListDevicesTimestampAdapter{
    @ToJson
    List<DevicesTimestamp> fromJson(ArrayList<DevicesTimestamp> list){
        return list;
    }

    @FromJson
    ArrayList<DevicesTimestamp> fromJson(List<DevicesTimestamp> list){
        return new ArrayList<>(list);
    }
}
