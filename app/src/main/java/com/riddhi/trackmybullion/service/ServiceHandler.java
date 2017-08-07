package com.riddhi.trackmybullion.service;

import com.riddhi.trackmybullion.global.Constants;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Rahul on 8/7/2017.
 */

public class ServiceHandler {
    private String url = "";

    public static OkHttpClient client = new OkHttpClient();

    public static void buildRequest(String url, Constants.REQUEST_TYPE type, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request)
                .enqueue(callback);
    }

}
