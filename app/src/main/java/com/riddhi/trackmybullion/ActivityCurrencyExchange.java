package com.riddhi.trackmybullion;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riddhi.trackmybullion.adapters.CurrencyAdapter;
import com.riddhi.trackmybullion.global.Constants;
import com.riddhi.trackmybullion.models.Currency;
import com.riddhi.trackmybullion.service.ServiceHandler;

import java.io.IOException;
import java.lang.reflect.Type;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.riddhi.trackmybullion.global.Constants.HTTP_OK;


/**
 * Created by riddhi on 8/19/2017.
 */

public class ActivityCurrencyExchange extends Activity implements Callback {

    final String TAG = ActivityCurrencyExchange.class.getSimpleName();

    CurrencyAdapter mAdapter;

    @BindView(R.id.activity_currency_exchange_rv)
    RecyclerView rv_currency_exchange;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_exchange);

        init();
    }

    private void init() {

        ButterKnife.bind(this);
        fetchCurrenciesWithReference(Constants.CURRENCYCODE.USD);
    }

    private void fetchCurrenciesWithReference(Constants.CURRENCYCODE code) {

        String[] parameters = {"base="+code.name()};
        ServiceHandler.buildRequest(Constants.url_currency_rates,
                Constants.REQUEST_TYPE.GET,
                this,
                parameters);
    }

    @Override
    public void onFailure(Call call, IOException e) {

        Log.e(TAG,e.getLocalizedMessage());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {

        if (response.code() == HTTP_OK) {

            switch (call.request().tag().toString()) {
                case Constants.url_currency_rates:
                    handleResonseCurrencyRate(response);
                    break;
            }
        }
    }

    private void handleResonseCurrencyRate(Response response) {

        Gson gson = new Gson();
        try {
            String jsonString = response.body().string();
            Type currencyRateType = new TypeToken<Currency>() {}.getType();
            final Currency currency = gson.fromJson(jsonString, currencyRateType);
            Log.d("CURRENCY",String.valueOf(currency));

            ///////////////////

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter = new CurrencyAdapter(currency);
                    rv_currency_exchange.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    rv_currency_exchange.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
            });

            ///////////////////

        }catch (NullPointerException e) {
            e.printStackTrace();
        }catch (Exception  ex) {
            ex.printStackTrace();
        }
    }
}
