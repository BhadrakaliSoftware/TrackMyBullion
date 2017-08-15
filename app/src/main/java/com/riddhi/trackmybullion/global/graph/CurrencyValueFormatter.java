package com.riddhi.trackmybullion.global.graph;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.Currency;

/**
 * Created by riddhi on 8/13/2017.
 */

public class CurrencyValueFormatter implements IAxisValueFormatter {

    private String currencyCode = "USD";
    private String currencySymbol = "$";

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return currencySymbol+" "+String.format("%2.2f",value);
    }

    public CurrencyValueFormatter(String code) {
        this.currencyCode = code;

        try {
            this.currencySymbol = Currency.getInstance(code).getSymbol();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
