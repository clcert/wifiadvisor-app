package com.example.wifiadvisor.moshi.models.testsTimestamp;

import android.util.Pair;

import com.example.wifiadvisor.R;
import com.example.wifiadvisor.enums.BlockingEnum;
import com.example.wifiadvisor.moshi.models.TcpConnectWebTestOoni;
import com.example.wifiadvisor.moshi.models.WebTestOoni;

import java.util.Date;
import java.util.List;

public class WebTestOoniTimestamp implements TestTimestampI{
    WebTestOoni test;
    Date timestamp;
    List<TcpConnectWebTestOoni> tcp_connect;

    @Override
    public String toString() {
        return "WebTestOoniTimestamp{" +
                "test=" + test +
                ", timestamp=" + timestamp +
                ", tcp_connect=" + tcp_connect +
                '}';
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public List<TcpConnectWebTestOoni> getTcp_connect() {
        return tcp_connect;
    }

    public WebTestOoni getTest() {
        return test;
    }

    public Pair<Boolean, Integer> check(){
        BlockingEnum blocking = getTest().getBlocking();

        if (blocking==null){
            return new Pair<>(null, R.string.explanation_for_ooni_web_site_not_found_result);
        }
        if (blocking.equals(BlockingEnum.not_blocking)) {
            return new Pair<>(true, getTest().getMap().get(blocking));
        }
        return new Pair<>(false, getTest().getMap().get(blocking));
    }

}
