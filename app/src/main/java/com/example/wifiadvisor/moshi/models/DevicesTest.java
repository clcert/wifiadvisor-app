package com.example.wifiadvisor.moshi.models;

public class DevicesTest {
    String mask;
    String mac;
    String manuf;
    String private_ip;
    boolean router;
    @Override
    public String toString() {
        return "Devices{" +
                "mask='" + mask + '\'' +
                ", mac='" + mac + '\'' +
                ", manuf='" + manuf + '\'' +
                ", private_ip ='" + private_ip + '\'' +
                ", router ='" + router + '\'' +

                '}';
    }

    public String getMac() {
        return mac;
    }

    public String getManuf() {
        return manuf;
    }

    public String getMask() {
        return mask;
    }

    public String getPrivateIp() {
        return private_ip;
    }

    public String getPrivate_ip() {
        return private_ip;
    }

    public boolean isRouter() {
        return router;
    }
}