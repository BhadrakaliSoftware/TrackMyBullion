package com.bhadrasoft.trackmybullion;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bhadrasoft.trackmybullion.adapters.CurrencyAdapter;
import com.bhadrasoft.trackmybullion.global.Constants;
import com.bhadrasoft.trackmybullion.interfaces.CurrancyChangeListener;
import com.bhadrasoft.trackmybullion.models.Country;
import com.bhadrasoft.trackmybullion.models.Currency;
import com.bhadrasoft.trackmybullion.service.ServiceHandler;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.bhadrasoft.trackmybullion.global.Constants.HTTP_OK;


/**
 * Created by riddhi on 8/19/2017.
 */


public class ActivityCurrencyExchange extends AppCompatActivity
        implements Callback, ValueEventListener, CurrancyChangeListener {

    final String TAG = ActivityCurrencyExchange.class.getSimpleName();

    public CurrancyChangeListener delegate = this;
    CurrencyAdapter mAdapter;
    ProgressDialog progressDialog;
    FirebaseDatabase mFirebaseDatabase;
    List<Country> mCountries;
    private Currency mCurrency;

    @BindView(R.id.activity_currency_exchange_rv)
    RecyclerView rv_currency_exchange;

    private AdView mAdView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_exchange);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        init();
    }

    private void init() {
        ButterKnife.bind(this);

        testFirebase();
    }

    private void testFirebase() {

        //Firebase instance
        if (mFirebaseDatabase == null) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            mFirebaseDatabase = firebaseDatabase;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog = ProgressDialog.show(ActivityCurrencyExchange.this, "", "");
            }
        });

        DatabaseReference databaseReference = mFirebaseDatabase.getReference().child(Constants.COUNTRIES);
        databaseReference.addValueEventListener(this);
    }

    private void fetchCurrenciesWithReference(Constants.CURRENCYCODE code) {

        progressDialog = ProgressDialog.show(this, "", "");
        String[] parameters = {"base=" + code.name()};
        ServiceHandler.buildRequest(Constants.url_currency_rates,
                Constants.REQUEST_TYPE.GET, this, parameters);
    }

    @Override
    public void onFailure(Call call, IOException e) {
        progressDialog.dismiss();
        Log.e(TAG, e.getLocalizedMessage());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {

        progressDialog.dismiss();
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
            Type currencyRateType = new TypeToken<Currency>() {
            }.getType();
            final Currency currency = gson.fromJson(jsonString, currencyRateType);
            this.mCurrency = currency;
            Log.d("CURRENCY", String.valueOf(currency));

            ///////////////////

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter = new CurrencyAdapter(currency, mCountries, ActivityCurrencyExchange.this);
                    rv_currency_exchange.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    rv_currency_exchange.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
            });

            ///////////////////

            //handle firebase
//            this.testFirebase();

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    //Database Reading
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {


        //Method is called with initial value and again when value changes
        try {
            GenericTypeIndicator<List<Country>> t = new GenericTypeIndicator<List<Country>>() {
            };
            List<Country> countries = dataSnapshot.getValue(t);
            this.mCountries = countries;
            Log.d(TAG, countries.get(0).getAlpha2Code());

            //set adapter changes

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                    fetchCurrenciesWithReference(Constants.CURRENCYCODE.USD);
                }
            });


        } catch (Exception ex) {
            ex.printStackTrace();
            progressDialog.dismiss();
            fetchCurrenciesWithReference(Constants.CURRENCYCODE.USD);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        progressDialog.dismiss();
        fetchCurrenciesWithReference(Constants.CURRENCYCODE.USD);
    }

    @Override
    public void currencyDidChanged(String currencyName) {
        Log.d(TAG, "currencyDidChanged: "+currencyName);

        Intent resultIntent = new Intent();
        resultIntent.putExtra(Constants.SELECTED_CURRENCY, currencyName);
        resultIntent.putExtra(Constants.CURRENCIES, mCurrency);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

}


