package com.example.wifiadvisor.moshi.models;

import com.example.wifiadvisor.enums.RatingOarcEnum;

public class DnsTest {
    String dns1_android;
    String dns2_android;
    String ns_akamai;
    String ecs_akamai;
    String ip_akamai;
    Boolean do_flag;
    Boolean ad_flag;
    Boolean rrsig;
    String resolver_ip_oarc;
    RatingOarcEnum rating_source_port;
    RatingOarcEnum rating_transaction_id;
    Integer std_source_port;
    Integer std_transaction_id;
    Float bits_of_entropy_source_port;
    Float bits_of_entropy_transaction_id;

    @Override
    public String toString() {
        return "DnsTest{" +
                "dns1_android='" + dns1_android + '\'' +
                ", dns2_android='" + dns2_android + '\'' +
                ", ns_akamai='" + ns_akamai + '\'' +
                ", ecs_akamai='" + ecs_akamai + '\'' +
                ", ip_akamai='" + ip_akamai + '\'' +
                ", do_flag=" + do_flag +
                ", ad_flag=" + ad_flag +
                ", rrsig=" + rrsig +
                ", resolver_ip_oarc='" + resolver_ip_oarc + '\'' +
                ", rating_source_port=" + rating_source_port +
                ", rating_transaction_id=" + rating_transaction_id +
                ", std_source_port=" + std_source_port +
                ", std_transaction_id=" + std_transaction_id +
                ", bits_of_entropy_source_port=" + bits_of_entropy_source_port +
                ", bits_of_entropy_transaction_id=" + bits_of_entropy_transaction_id +
                '}';
    }

    public String getResolverIpOarc() {
        return resolver_ip_oarc;
    }

    public RatingOarcEnum getRatingTransactionId() {
        return rating_transaction_id;
    }

    public RatingOarcEnum getRatingSourcePort() {
        return rating_source_port;
    }

    public String getIpAkamai() {
        return ip_akamai;
    }

    public String getEcsAkamai() {
        return ecs_akamai;
    }

    public String getNsAkamai() {
        return ns_akamai;
    }

    public Boolean getAdFlag() {
        return ad_flag;
    }

    public Boolean getDoFlag() {
        return do_flag;
    }

    public Boolean getRrsig() {
        return rrsig;
    }

    public Float getBitsOfEntropySourcePort() {
        return bits_of_entropy_source_port;
    }

    public Float getBitsOfEntropyTransactionId() {
        return bits_of_entropy_transaction_id;
    }

    public Integer getStdSourcePort() {
        return std_source_port;
    }

    public String getDns1Android() {
        return dns1_android;
    }

    public Integer getStdTransactionId() {
        return std_transaction_id;
    }

    public String getDns2Android() {
        return dns2_android;
    }
}

