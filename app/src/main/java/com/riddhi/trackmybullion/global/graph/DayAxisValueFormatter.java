package com.riddhi.trackmybullion.global.graph;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by riddhi on 8/13/2017.
 */

public class DayAxisValueFormatter implements IAxisValueFormatter {

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        long timeInSeconds = (long) value;
        Date date = new Date(timeInSeconds);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }
}
