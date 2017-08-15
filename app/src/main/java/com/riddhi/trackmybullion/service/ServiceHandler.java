package com.riddhi.trackmybullion.service;

import com.riddhi.trackmybullion.global.Constants;

import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Rahul on 8/7/2017.
 */

public class ServiceHandler {
    private String url = "";

    private static OkHttpClient client = new OkHttpClient();

    public static void buildRequest(String url, Constants.REQUEST_TYPE type, Callback callback, String[] queryParameters) {

        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();

        //Parse query parameters
        if (queryParameters.length > 0) {
            for (String query : queryParameters) {
                String query_name = query.split("=")[0];
                String query_value = query.split("=")[1];
                httpBuilder.addEncodedQueryParameter(query_name, query_value);
            }
        }

        Request request = new Request.Builder()
                .url(httpBuilder.build())
                .tag(url)
                .build();

        client.newCall(request)
                .enqueue(callback);
    }
}
