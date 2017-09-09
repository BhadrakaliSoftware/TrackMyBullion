package com.riddhi.trackmybullion.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riddhi.trackmybullion.ActivityCurrencyExchange;
import com.riddhi.trackmybullion.R;
import com.riddhi.trackmybullion.global.Constants;
import com.riddhi.trackmybullion.models.Country;
import com.riddhi.trackmybullion.models.Currency;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by riddhi on 8/19/2017.
 */

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyViewHolder> implements View.OnClickListener {

    private Currency currency;
    private List<Country> mCountries;
    private Context mContext;
    private final String TAG = CurrencyAdapter.class.getSimpleName();

    public CurrencyAdapter(Currency currency, List<Country> countries, Context context) {
        this.currency = currency;
        this.mCountries = countries;
        this.mContext = context;
        Log.d(TAG, "");
    }


    @Override
    public CurrencyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_currency_exchange_lv_item, parent, false);
        CurrencyViewHolder holder = new CurrencyViewHolder(itemView);
        return holder;
    }

    @Override
    public void onViewAttachedToWindow(CurrencyViewHolder holder) {
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public void onBindViewHolder(CurrencyViewHolder holder, int position) {
        //setup the item position
        holder.itemView.setTag(position);

        String strCode = this.currency.getRates()
                .getClass().getDeclaredFields()[position]
                .getName().toUpperCase();
        holder.tvCode.setText(strCode);

        //check for the value of the requested field
        Object object = this.currency.getRates();
        Field field = this.currency.getRates().getClass()
                .getDeclaredFields()[position];
        field.setAccessible(true);

        try {
            Object valueCurrency = field.get(object);
            Log.d(TAG, String.valueOf(valueCurrency));
            holder.tvCodeValue.setText(String.valueOf(valueCurrency));

            if (mCountries != null && mCountries.size() > 0) {
                for (Country country : mCountries) {
                    if (country.getCurrencies().get(0).getCode() == null) {
                        continue;
                    }
                    if (country.getCurrencies().get(0).getCode().equals(strCode)) {
                        int resId = mContext.getResources()
                                .getIdentifier(country.getAlpha2Code().toLowerCase(),
                                        Constants.DRAWABLE, mContext.getPackageName());
                        if (resId > 0) {
                            holder.ivCountryFlag.setImageResource(resId);
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, String.valueOf(this.currency.getRates().getClass().getDeclaredFields().length));
        return this.currency.getRates().getClass().getDeclaredFields().length;
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: " + view.getParent());

        if (mContext instanceof ActivityCurrencyExchange) {
            ActivityCurrencyExchange activity = (ActivityCurrencyExchange) mContext;

            //get the selected currency position
            int position = (int) view.getTag();

            //check for the value of the requested field
            Object object = this.currency.getRates();
            Field field = this.currency.getRates().getClass()
                    .getDeclaredFields()[position];
            field.setAccessible(true);

            try {
                Object valueCurrency = field.get(object);
                Log.d(TAG, String.valueOf(valueCurrency));

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            activity.delegate.currencyDidChanged(field.getName());
            activity.finish();
        }
    }
}