/**
 * https://stackoverflow.com/questions/34193555/okhttp-trigger-callback-in-originated-class-after-finishing-network-actions
 */
package com.example.wifiadvisor.moshi.models;

import com.example.wifiadvisor.moshi.models.testsTimestamp.DevicesTimestamp;
import com.example.wifiadvisor.moshi.models.testsTimestamp.DnsTimestamp;
import com.example.wifiadvisor.moshi.models.testsTimestamp.NdtTestOoniTimestamp;
import com.example.wifiadvisor.moshi.models.testsTimestamp.ProtocolsTimestamp;
import com.example.wifiadvisor.moshi.models.testsTimestamp.TestTimestampI;
import com.example.wifiadvisor.moshi.models.testsTimestamp.WebTestOoniTimestamp;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Results {
    ArrayList<ProtocolsTimestamp> protocols_test;
    ArrayList<DnsTimestamp> dns_tests;
    ArrayList<DevicesTimestamp> devices_test;
    ArrayList<NdtTestOoniTimestamp> ndt_tests_ooni;
    ArrayList<WebTestOoniTimestamp> web_tests_ooni;

    public Results(){}

    @Override
    public String toString() {
        return "Results{" +
                "protocols_test=" + protocols_test +
                ", devices_test=" + devices_test +
                ", dns_tests=" + dns_tests +
                ", ndt_tests_ooni=" + ndt_tests_ooni +
                ", web_tests_ooni=" + web_tests_ooni +
                '}';
    }

    public ArrayList<DevicesTimestamp> getDevicesTest() {
        return devices_test;
    }

    public ArrayList<DnsTimestamp> getDnsTests() {
        return dns_tests;
    }

    public ArrayList<NdtTestOoniTimestamp> getNdtTestsOoni() {
        return ndt_tests_ooni;
    }

    public ArrayList<ProtocolsTimestamp> getProtocolsTest() {
        return protocols_test;
    }

    public ArrayList<WebTestOoniTimestamp> getWebTestsOoni() {
        return web_tests_ooni;
    }

    public ArrayList<TestTimestampI> getBasicMesuarement(){
        ArrayList<TestTimestampI> list = new ArrayList<>();
        list.addAll(dns_tests);
        list.addAll(protocols_test);
        return list;
    }
    public ArrayList<TestTimestampI> joinTests(){
        ArrayList<TestTimestampI> list = new ArrayList<>();
        list.addAll(dns_tests);
        list.addAll(devices_test);
        list.addAll(ndt_tests_ooni);
        list.addAll(protocols_test);
        list.addAll(web_tests_ooni);
        return list;
    }

}

