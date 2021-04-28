/**
 * the regex code for the mac address was obtained from stackoverflow (https://stackoverflow.com/questions/4260467/what-is-a-regular-expression-for-a-mac-address)
 * the regex code for the private ip address was obtained from stackoverflow (https://stackoverflow.com/questions/2814002/private-ip-address-identifier-in-regular-expression)
 * The way for calculate the subnet and the mask was obtained from stackoverflow:
 * https://stackoverflow.com/questions/14327022/calculate-ip-range-by-subnet-mask
 * https://stackoverflow.com/questions/9900284/converting-an-ip-address-to-binary-values-java
 * https://stackoverflow.com/questions/1146581/how-can-i-convert-the-decimal-representation-of-an-ip-address-into-binary
 * https://stackoverflow.com/questions/2942299/converting-cidr-address-to-subnet-mask-and-network-address
 **/

package com.example.wifiadvisor.usertests;

import android.app.Activity;
import android.widget.TextView;

import com.example.wifiadvisor.BuildConfig;
import com.example.wifiadvisor.R;
import com.example.wifiadvisor.data.SingletonOkHttpCLient;
import com.example.wifiadvisor.moshi.models.TestBase;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static java.lang.Integer.parseInt;
import static java.lang.String.*;

public class Devices {
    ArrayList<String> macs;
    ArrayList<String> hosts;
    Hashtable<String, String> neighbourTable;
    String network;
    int mask;
    int maxHosts;
    BigInteger firstIp;
    DevicesAllInfo[] devicesAllInfo;
    String wifiInterface;
    String thisDeviceMac;
    String thisDevicePrivateIp;
    String router_mac;
    TextView logText;
    Activity activity;

    public Devices(TextView logText, Activity activity) {
        this.neighbourTable = new Hashtable<>();
        this.logText = logText;
        this.activity = activity;
    }

    public BigInteger getFirstIp() {
        return firstIp;
    }

    public ArrayList<String> getHosts() {
        return hosts;
    }

    public ArrayList<String> getMacs() {
        return macs;
    }

    public Hashtable<String, String> getNeighbourTable() {
        return neighbourTable;
    }

    public int getMask() {
        return mask;
    }

    public int getMaxHosts() {
        return maxHosts;
    }

    public String getNetwork() {
        return network;
    }

    public void runTest() {
        try {
            setNetworkMask();
            this.maxHosts = calculateMaxHosts(getMask());
            this.firstIp = calculateFirstIp(getNetwork(), getMask());
            getPrivateIps();
            setNeighbourTable();
            int sizeHosts = getHosts().size();
            this.devicesAllInfo = new DevicesAllInfo[sizeHosts];
            String ip;
            String mac;
            for (int i = 0; i < sizeHosts; i++) {
                ip = getHosts().get(i);
                mac = getNeighbourTable().get(ip);
                devicesAllInfo[i] = new DevicesAllInfo(hideMac(mac), ip, mac.equals(router_mac));
            }
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    logText.setText("Enviando resultados al servidor");
                }
            });
            Moshi.Builder builder = new Moshi.Builder();
            final Moshi moshi = builder.build();
            final JsonAdapter<DevicesDataSend> jsonAdapter = moshi.adapter(DevicesDataSend.class);
            final OkHttpClient client = SingletonOkHttpCLient.getInstance().getClient();
            String json = jsonAdapter.toJson(new DevicesDataSend(this.devicesAllInfo, activity));
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(json, JSON);
            final Request request = new Request.Builder()
                    .url(BuildConfig.SERVER_URL+ activity.getString(R.string.url_devices_test))
                    .post(body)
                    .build();
            System.out.println(json);
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        Moshi moshi = new Moshi.Builder().build();
                        Type type = Types.newParameterizedType(List.class, DevicesAllInfo.class);
                        JsonAdapter<List<DevicesAllInfo>> adapter = moshi.adapter(type);
                        List<DevicesAllInfo> responseInfo = adapter.fromJson(Objects.requireNonNull(response.body()).source());
                        if(responseInfo != null){
                            for (int i = 0; i < sizeHosts; i++) {
                                devicesAllInfo[i].setManuf(responseInfo.get(i).manuf);
                            }
                        }

                    } else {
                        System.out.println(response.code());
                        System.out.println(response.toString());
                    }
                    Objects.requireNonNull(response.body()).close();
                }

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    String hideMac(String mac) {
        return format("%s%s", mac.substring(0, 8), ":00:00:00");
    }

    void getPrivateIps() {
        DeviceFinderPrivateIp discovery = new DeviceFinderPrivateIp(getNetwork(), getMaxHosts(), getFirstIp(), logText, activity);
        discovery.doInBackground();
        discovery.onCancelled();
        this.hosts = discovery.getHosts();
    }

    public void setNeighbourTable() {
        BufferedReader bufferedReader = null;
        try {
            Process process = Runtime.getRuntime().exec("ip n");
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            Pattern ipv4Pattern = Pattern.compile("(10|127|169\\.254|172\\.1[6-9]|172\\.2[0-9]|172\\.3[0-1]|192\\.168)\\.[0-9]{1,3}\\.[0-9]{1,3}");
            Pattern macPattern = Pattern.compile("([0-9A-Fa-f]{2}[:]){5}([0-9A-Fa-f]{2})", Pattern.CASE_INSENSITIVE);
            Pattern router = Pattern.compile("router");
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                Matcher ipv4Matcher = ipv4Pattern.matcher(line);
                Matcher macMatcher = macPattern.matcher(line);
                if (ipv4Matcher.find() && macMatcher.find()) {
                    neighbourTable.put(ipv4Matcher.group(0), macMatcher.group(0));
                } else if (router.matcher(line).find() && macMatcher.find()) {
                    this.router_mac = macMatcher.group(0);
                }
            }
            neighbourTable.put(thisDevicePrivateIp, thisDeviceMac);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    public void setNetworkMask() {
        BufferedReader bufferedReader = null;
        try {
            Process process = Runtime.getRuntime().exec("ip r");
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            Pattern networkPattern = Pattern.compile("(10|127|169\\.254|172\\.1[6-9]|172\\.2[0-9]|172\\.3[0-1]|192\\.168\\.[0-9]{1,3}\\.[0-9]{1,3})/([1-9]\\D|[1-3][0-9])", Pattern.CASE_INSENSITIVE);
            Pattern interfaceWifi = Pattern.compile("(dev\\s+)(wl\\w+)", Pattern.CASE_INSENSITIVE);
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                Matcher interfaceWifiMatcher = interfaceWifi.matcher(line);
                Matcher networkMatcher = networkPattern.matcher(line);
                if (interfaceWifiMatcher.find() && networkMatcher.find()) {
                    this.network = networkMatcher.group(1);
                    this.mask = parseInt(networkMatcher.group(2));
                    this.wifiInterface = interfaceWifiMatcher.group(2);
                }
            }
            process = Runtime.getRuntime().exec("ip a sh " + this.wifiInterface);
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            Pattern mac = Pattern.compile("(link/ether\\s)(([0-9A-Fa-f]{2}[:]){5}([0-9A-Fa-f]{2}))", Pattern.CASE_INSENSITIVE);
            Pattern ip = Pattern.compile("(inet\\s)(10|127|169\\.254|172\\.1[6-9]|172\\.2[0-9]|172\\.3[0-1]|192\\.168\\.[0-9]{1,3}\\.[0-9]{1,3})", Pattern.CASE_INSENSITIVE);
            while ((line = bufferedReader.readLine()) != null) {
                Matcher macMatcher = mac.matcher(line);
                Matcher ipMatcher = ip.matcher(line);
                if (macMatcher.find()) {
                    this.thisDeviceMac = macMatcher.group(2);
                }
                if (ipMatcher.find()) {
                    this.thisDevicePrivateIp = ipMatcher.group(2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        }

    }

    public int calculateMaxHosts(int mask) {
        return 1 << (32 - mask);
    }

    public BigInteger calculateFirstIp(String network, int mask) throws UnknownHostException {
        BigInteger ip = new BigInteger(1, InetAddress.getByName(network).getAddress());
        BigInteger subnet = BigInteger.valueOf(0xffffffff << (32 - mask) - 1);
        return subnet.and(ip);
    }

    public static String numberToIp(BigInteger value) {
        BigInteger ones = BigInteger.valueOf(0xff);
        return format("%d.%d.%d.%d", ((value.shiftRight(24)).and(ones)), ((value.shiftRight(16)).and(ones)), ((value.shiftRight(8)).and(ones)), (value.and(ones)));
    }

    @Override
    public String toString() {
        return "Devices{" +
                "macs=" + macs +
                ", hosts=" + hosts +
                ", neighbourTable=" + neighbourTable +
                ", network='" + network + '\'' +
                ", mask=" + mask +
                ", maxHosts=" + maxHosts +
                ", firstIp=" + firstIp +
                ", devicesAllInfo=" + Arrays.toString(devicesAllInfo) +
                ", wifiInterface='" + wifiInterface + '\'' +
                ", thisDeviceMac='" + thisDeviceMac + '\'' +
                ", thisDevicePrivateIp='" + thisDevicePrivateIp + '\'' +
                '}';
    }


}

class DevicesAllInfo {
    String mac;
    int mask;
    Boolean router;
    String manuf;
    String private_ip;


    public DevicesAllInfo(String mac, int mask, boolean router, String ip) {
        this.mac = mac;
        this.mask = mask;
        this.router = router;
        this.private_ip = ip;
    }

    public DevicesAllInfo(String mac, String private_ip, Boolean router) {
        this(mac, 24, router, private_ip);
    }

    public void setIp(String ip) {
        this.private_ip = private_ip;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public void setManuf(String manuf) {
        this.manuf = manuf;
    }

    @Override
    public String toString() {
        return "DevicesAllInfo{" +
                "mac='" + mac + '\'' +
                ", mask=" + mask +
                ", router=" + router +
                ", manuf='" + manuf + '\'' +
                ", ip='" + private_ip + '\'' +
                '}';
    }
}

class DevicesDataSend {
    DevicesAllInfo[] test;
    TestBase test_base;
    public DevicesDataSend(DevicesAllInfo[] devicesAllInfo, Activity activity) {
        test = devicesAllInfo;
        test_base = new TestBase(activity);
    }

    @Override
    public String toString() {
        return "DevicesDataSend{" +
                "test=" + Arrays.toString(test) +
                '}';
    }
}