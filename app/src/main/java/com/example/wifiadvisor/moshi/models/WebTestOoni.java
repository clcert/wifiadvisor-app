package com.example.wifiadvisor.moshi.models;

import com.example.wifiadvisor.R;
import com.example.wifiadvisor.enums.BlockingEnum;
import com.example.wifiadvisor.enums.DnsConsistencyEnum;

import java.util.HashMap;
import java.util.Map;

public class WebTestOoni {
    String report_id;
    String url;
    String resolver_asn;
    String resolver_ip;
    String resolver_network_name;
    String client_resolver;
    String dns_experiment_failure;
    String control_failure;
    String http_experiment_failure;
    DnsConsistencyEnum dns_consistency;
    Boolean body_length_match;
    Boolean headers_match;
    Boolean status_code_match;
    Boolean title_match;
    Boolean accessible;
    BlockingEnum blocking;

    public WebTestOoni(String report_id,
                       String url,
                       String resolver_asn,
                       String resolver_ip,
                       String resolver_network_name,
                       String client_resolver,
                       String dns_experiment_failure,
                       String control_failure,
                       String http_experiment_failure,
                       DnsConsistencyEnum dns_consistency,
                       Boolean body_length_match,
                       Boolean headers_match,
                       Boolean status_code_match,
                       Boolean title_match,
                       Boolean accessible,
                       BlockingEnum blocking) {
        this.report_id = report_id;
        this.url = url;
        this.resolver_asn = resolver_asn;
        this.resolver_ip = resolver_ip;
        this.resolver_network_name = resolver_network_name;
        this.client_resolver = client_resolver;
        this.dns_experiment_failure = dns_experiment_failure;
        this.control_failure = control_failure;
        this.http_experiment_failure = http_experiment_failure;
        this.dns_consistency = dns_consistency;
        this.body_length_match = body_length_match;
        this.headers_match = headers_match;
        this.status_code_match = status_code_match;
        this.title_match = title_match;
        this.accessible = accessible;
        this.blocking = blocking;

    }

    @Override
    public String toString() {
        return "WebTestOoni{" +
                "report_id='" + report_id + '\'' +
                ", url='" + url + '\'' +
                ", resolver_asn='" + resolver_asn + '\'' +
                ", resolver_ip='" + resolver_ip + '\'' +
                ", resolver_network_name='" + resolver_network_name + '\'' +
                ", client_resolver='" + client_resolver + '\'' +
                ", dns_experiment_failure='" + dns_experiment_failure + '\'' +
                ", control_failure='" + control_failure + '\'' +
                ", http_experiment_failure='" + http_experiment_failure + '\'' +
                ", dns_consistency=" + dns_consistency +
                ", body_length_match=" + body_length_match +
                ", headers_match=" + headers_match +
                ", status_code_match=" + status_code_match +
                ", title_match=" + title_match +
                ", accessible=" + accessible +
                ", blocking=" + blocking +
                '}';
    }

    public BlockingEnum getBlocking() {
        return blocking;
    }

    public Map<BlockingEnum, Integer> getMap() {
        Map<BlockingEnum, Integer> map = new HashMap<>();
        map.put(BlockingEnum.tcp_ip, R.string.explanation_for_ooni_web_tcp_ip_result);
        map.put(BlockingEnum.http_diff, R.string.explanation_for_ooni_web_http_diff_result);
        map.put(BlockingEnum.http_failure, R.string.explanation_for_ooni_web_http_failure_result);
        map.put(BlockingEnum.dns, R.string.explanation_for_ooni_web_dns_result);
        map.put(BlockingEnum.not_blocking, R.string.explanation_for_ooni_web_not_blocking_result);
        return map;
    }

    public String getUrl() {
        return url;
    }
}

