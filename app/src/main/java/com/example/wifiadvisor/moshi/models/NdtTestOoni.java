package com.example.wifiadvisor.moshi.models;

public class NdtTestOoni {
    String report_id;
    Float avg_rtt;
    Float download;
    Integer mss;
    Float max_rtt;
    Float min_rtt;
    Float ping;
    Float retransmit_rate;
    Float upload;

    @Override
    public String toString() {
        return "NdtTestOoni{" +
                "report_id='" + report_id + '\'' +
                ", avg_rtt=" + avg_rtt +
                ", download=" + download +
                ", mss=" + mss +
                ", max_rtt=" + max_rtt +
                ", min_rtt=" + min_rtt +
                ", ping=" + ping +
                ", retransmit_rate=" + retransmit_rate +
                ", upload=" + upload +
                '}';
    }
}
