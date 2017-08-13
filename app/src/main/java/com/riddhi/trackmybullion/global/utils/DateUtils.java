package com.riddhi.trackmybullion.global.utils;

import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by riddhi on 8/12/2017.
 */

public class DateUtils {
    public static Timestamp getTimeStamp(String dateString)  throws InvalidParameterException{

        Date date = getDate(dateString,"yyyy-MM-dd");
        return new Timestamp(date.getTime());
    }

    private static Date getDate(String dateString, String format) {

        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
