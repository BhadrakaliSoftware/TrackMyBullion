package com.bhadrasoft.trackmybullion.global.utils;

import com.bhadrasoft.trackmybullion.global.Constants;

import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by riddhi on 8/12/2017.
 */

public class DateUtils {

    public final static String DD_MM_YYYY = "dd/MM/yyyy";
    final static String MM_DD_YYYY = "MM/dd/yyyy";
    public static String yyyyMMdd_HHmmaz = "yyyy-MM-dd HH:mm a z";

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

    public static String getDateForHistoryRange(Constants.HistoryRange range) {
        Calendar calendar = Calendar.getInstance();

        switch (range) {
            case DAY:
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                break;
            case WEEK:
                calendar.add(Calendar.DAY_OF_YEAR, -7);
                break;
            case MONTH:
                calendar.add(Calendar.MONTH, -1);
                break;
            case SIX_MONTH:
                calendar.add(Calendar.MONTH, -6);
                break;
            case YEAR:
                calendar.add(Calendar.YEAR, -1);
                break;
            case FIVE_YEAR:
                calendar.add(Calendar.YEAR, -5);
                break;
            case TEN_YEAR:
                calendar.add(Calendar.YEAR, -10);
                break;
        }
        Date currentDate = calendar.getTime();
        return DateUtils.getDateString(currentDate, DateUtils.DD_MM_YYYY);
    }

}
