package com.example.wifiadvisor.moshi.models;

public class Asn {
    public String client_host;
    String asn;

    @Override
    public String toString() {
        return "Asn{" +
                "client_host='" + client_host + '\'' +
                ", asn='" + asn + '\'' +
                '}';
    }
}
