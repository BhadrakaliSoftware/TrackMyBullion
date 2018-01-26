package com.bhadrasoft.trackmybullion.global.utils;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by riddhi on 13-Jan-18.
 */

public class ParseUtils {

    public static String parseGoldPrices(List<String> data) {
        /*
        "USD (AM)",
        "USD (PM)",
        "GBP (AM)",
        "GBP (PM)",
        "EURO (AM)",
        "EURO (PM)"*/
        String date = data.get(0);
        String usd_am = data.get(1);
        String usd_pm = data.get(2);
        Timestamp timestamp = DateUtils.getTimeStamp(date);
        return "";
    }

    public static float parseBitcoinPrices(List<String> data) {
      /*"Date",
        "Open",
        "High",
        "Low",
        "Close",
        "Volume (BTC)",
        "Volume (Currency)",
        "Weighted Price"*/

        String date = data.get(0);
        String open = data.get(1);
        String high = data.get(2);
        String low = data.get(3);
        String close = data.get(4);
        return Float.parseFloat(open);
    }
}
