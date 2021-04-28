package com.example.wifiadvisor.moshi.adapters;

import com.example.wifiadvisor.moshi.models.testsTimestamp.WebTestOoniTimestamp;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.util.ArrayList;
import java.util.List;

public class ListWebTestOoniTimestampAdapter{
    @ToJson
    List<WebTestOoniTimestamp> fromJson(ArrayList<WebTestOoniTimestamp> list){
        return list;
    }

    @FromJson
    ArrayList<WebTestOoniTimestamp> fromJson(List<WebTestOoniTimestamp> list){
        return new ArrayList<>(list);
    }
}
