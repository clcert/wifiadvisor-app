package com.example.wifiadvisor.moshi.models;

public class ProtocolTest {
    String protocol_name;
    String key_management;
    String cipher;

    @Override
    public String toString() {
        return "ProtocolTest{" +
                "protocol_name='" + protocol_name + '\'' +
                ", key_management='" + key_management + '\'' +
                ", cipher='" + cipher + '\'' +
                '}';
    }

    public String getProtocolName() {
        return protocol_name;
    }

    public String getCipher() {
        return cipher;
    }

    public String getKeyManagement() {
        return key_management;
    }
}
