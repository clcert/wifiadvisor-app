package com.example.wifiadvisor.moshi.models;

public class TcpConnectWebTestOoni {
    String ip;
    Integer port;
    Boolean status_blocked;
    String status_failure_string;
    Boolean status_success;

    public TcpConnectWebTestOoni(String ip, Integer port, Boolean blocked, String failure, Boolean success) {
        this.ip = ip;
        this.port = port;
        this.status_blocked = blocked;
        this.status_failure_string = failure;
        this.status_success = success;
    }

    @Override
    public String toString() {
        return "TcpConnectWebTestOoni{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", status_blocked=" + status_blocked +
                ", status_failure_string='" + status_failure_string + '\'' +
                ", status_success=" + status_success +
                '}';
    }
}
