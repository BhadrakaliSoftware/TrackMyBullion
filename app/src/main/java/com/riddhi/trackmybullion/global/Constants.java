package com.riddhi.trackmybullion.global;

/**
 * Created by Rahul on 8/7/2017.
 */

public class Constants {

    public enum REQUEST_TYPE {GET, POST}
    public enum RETURN_TYPE {JSON, XML}

    private static final String quandl_api_key = "YOUR-API-KEY";

    /*
    * URLs : Urls for the service call
    * */
    private static final String host_name = "https://www.quandl.com";
    public static final String url_gold_prices = host_name+"/api/v3/datasets/BUNDESBANK/BBK01_WT5511"+
            "?api_key="+quandl_api_key;
}