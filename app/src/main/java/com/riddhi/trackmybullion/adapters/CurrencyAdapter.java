package com.riddhi.trackmybullion.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riddhi.trackmybullion.R;
import com.riddhi.trackmybullion.models.Currency;

import java.lang.reflect.Field;

/**
 * Created by riddhi on 8/19/2017.
 */

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyViewHolder> {

    private Currency currency;
    private final String TAG = CurrencyAdapter.class.getSimpleName();

    public CurrencyAdapter(Currency currency) {
        this.currency = currency;
        Log.d(TAG,"");
    }

    @Override
    public CurrencyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_currency_exchange_lv_item,parent,false);
        return new CurrencyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CurrencyViewHolder holder, int position) {
        String strCode = this.currency.getRates().getClass().getDeclaredFields()[position].getName().toUpperCase();
        holder.tvCode.setText(strCode);

        //check for the value of the requested field
        Object object = this.currency.getRates();
        Field field = this.currency.getRates().getClass().getDeclaredFields()[position];
        field.setAccessible(true);

        try {
            Object valueCurrency = field.get(object);
            Log.d(TAG, String.valueOf(valueCurrency));

            holder.tvCodeValue.setText(String.valueOf(valueCurrency));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG,String.valueOf(this.currency.getRates().getClass().getDeclaredFields().length) );
        return this.currency.getRates().getClass().getDeclaredFields().length;
    }
}