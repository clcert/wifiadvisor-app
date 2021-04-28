package com.example.wifiadvisor.moshi.models;

public class Mlab {
    int p_upload;
    int p_download;
    int p_rtt;

    @Override
    public String toString() {
        return "Mlab{" +
                "p_upload=" + p_upload +
                ", p_download=" + p_download +
                ", p_rtt_download=" + p_rtt +
                '}';
    }

    public int getP_download() {
        return p_download;
    }

    public int getP_rtt() {
        return p_rtt;
    }

    public int getP_upload() {
        return p_upload;
    }
}
