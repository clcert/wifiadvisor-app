package com.example.wifiadvisor.data;

import okhttp3.OkHttpClient;

/**
 *code get from StackOverflow: https://stackoverflow.com/questions/46430416/how-to-create-singleton-okhtpp-class-and-handle-previous-requests
 * **/
public class SingletonOkHttpCLient {
    private static SingletonOkHttpCLient instance;
    private OkHttpClient client;

    private SingletonOkHttpCLient() {
        client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .build();
    }

    public static synchronized SingletonOkHttpCLient getInstance() {
        if (instance == null) {
            instance = new SingletonOkHttpCLient();
        }
        return instance;
    }
    // In case you just need the unique OkHttpClient instance.
    public OkHttpClient getClient() {
        return client;
    }

    public void closeConnections() {
        client.dispatcher().cancelAll();
    }

}
