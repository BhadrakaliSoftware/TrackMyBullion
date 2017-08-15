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

    public final static String DD_MM_YYYY = "dd/MM/yyyy";
    final static String MM_DD_YYYY = "MM/dd/yyyy";

    public static Timestamp getTimeStamp(String dateString)  throws InvalidParameterException{

        Date date = getDate(dateString,"yyyy-MM-dd");
        return new Timestamp(date.getTime());
    }

    public static String getDateString(long seconds, String format) {

        DateFormat dateFormat = DateFormat.getDateInstance();
        Date date = new Date(seconds);
        return dateFormat.format(date);
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

    public static String getDateString(Date date, String format) {

        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }
}
