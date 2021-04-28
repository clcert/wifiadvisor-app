package com.example.wifiadvisor.moshi.adapters;

import com.example.wifiadvisor.moshi.models.TcpConnectWebTestOoni;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.util.ArrayList;
import java.util.List;

public class ListTcpConnectWebTestOoni {
    @ToJson
    List<TcpConnectWebTestOoni> fromJson(ArrayList<TcpConnectWebTestOoni> list){
        return list;
    }

    @FromJson
    ArrayList<TcpConnectWebTestOoni> fromJson(List<TcpConnectWebTestOoni> list){
        return new ArrayList<>(list);
    }
}
