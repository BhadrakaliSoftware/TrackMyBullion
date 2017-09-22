package com.bhadrasoft.trackmybullion.global.graph;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.sql.Date;
import java.text.DateFormat;

/**
 * Created by riddhi on 8/13/2017.
 */

public class DayAxisValueFormatter implements IAxisValueFormatter {

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        long timeInSeconds = (long) value;
        Date date = new Date(timeInSeconds);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        return dateFormat.format(date);
    }
}
