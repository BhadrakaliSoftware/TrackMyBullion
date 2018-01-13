package com.bhadrasoft.trackmybullion;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.DashPathEffect;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bhadrasoft.trackmybullion.global.utils.ParseUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bhadrasoft.trackmybullion.global.Constants;
import com.bhadrasoft.trackmybullion.global.graph.AxisValueFormatter;
import com.bhadrasoft.trackmybullion.global.graph.DayAxisValueFormatter;
import com.bhadrasoft.trackmybullion.global.graph.ValueFormatter;
import com.bhadrasoft.trackmybullion.global.utils.DateUtils;
import com.bhadrasoft.trackmybullion.models.Currency;
import com.bhadrasoft.trackmybullion.models.FinancialData;
import com.bhadrasoft.trackmybullion.service.ServiceHandler;


import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, Callback, PopupMenu.OnMenuItemClickListener, OnChartValueSelectedListener {

    private Currency currency;
    private ProgressDialog progressDialog;
    private String selectedCurrency = "USD";
    private Constants.CURRENCY targetCurrency = Constants.CURRENCY.GOLD;
    private String selectedCurrencySymbol = "$";
    private double scaleWeight = 10;
    private Entry selectedEntry = null;
    private Entry firstEntryInPeriod = null;

    Constants.HistoryRange historyRange = Constants.HistoryRange.WEEK;

    @BindView(R.id.activity_main_lineChart)
    LineChart lineChart;

    @BindView(R.id.activity_main_tv_today)
    TextView tvOneDay;

    @BindView(R.id.activity_main_tv_btc)
    TextView tvBitCoin;

    @BindView(R.id.activity_main_tv_gold)
    TextView tvGold;

    @BindView(R.id.activity_main_tv_week)
    TextView tvFiveDays;

    @BindView(R.id.activity_main_tv_month)
    TextView tvOneMonth;

    @BindView(R.id.activity_main_tv_sixMonths)
    TextView tvSixMonths;

    @BindView(R.id.activity_main_tv_year)
    TextView tvOneYear;

    @BindView(R.id.activity_main_tv_fiveYears)
    TextView tvFiveYears;

    @BindView(R.id.activity_main_tv_tenYear)
    TextView tvTenYears;

    @BindView(R.id.activity_main_tv_price)
    TextView tvSelectedPrice;

    @BindView(R.id.activity_main_tv_symbol)
    TextView tvSymbol;

    @BindView(R.id.activity_main_tv_date)
    TextView tvSelectedDate;

    @BindView(R.id.activity_main_tv_current_price)
    TextView tvCurrentPrice;

    @BindView(R.id.activity_main_tv_compare_price)
    TextView tvDifferenceInPrice;

    @BindView(R.id.activity_main_tv_change_desc)
    TextView tvChangeDesc;

    @BindView(R.id.activity_main_ll_price_change)
    LinearLayout llPriceChange;

    @BindView(R.id.activity_main_ll_price_compare)
    LinearLayout llPriceCompare;

    @BindView(R.id.activity_main_scrollview)
    ScrollView scrollview;

    @BindView(R.id.activity_main_img_price_change)
    ImageView imgPriceChange;

    private AdView mAdView;

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        init();
        initData();
    }

    private void init() {
        ButterKnife.bind(this);
        getSupportActionBar().setElevation(0);

        //Load default view
        tvFiveDays.setSelected(true);
        tvOneDay.setOnClickListener(this);
        tvFiveDays.setOnClickListener(this);
        tvOneMonth.setOnClickListener(this);
        tvSixMonths.setOnClickListener(this);
        tvOneYear.setOnClickListener(this);
        tvFiveYears.setOnClickListener(this);
        tvTenYears.setOnClickListener(this);
        tvBitCoin.setOnClickListener(this);
        tvGold.setOnClickListener(this);
    }

    private void initData() {
        fetchPriceWithHistoryRange(Constants.HistoryRange.WEEK, Constants.CURRENCY.GOLD );
    }

    private void fetchPriceWithHistoryRange(Constants.HistoryRange range, Constants.CURRENCY currency) {

        String url = Constants.url_gold_prices_lsx;
        switch (currency) {
            case GOLD:
                url = Constants.url_gold_prices_lsx;
                break;
            case SILVER:
                url = Constants.url_silver_prices_lsx;
                break;
            case BITCOIN:
                url = Constants.url_bitcoin_bitcoinwatch;
                break;
        }

        this.showProgressDialog();
        String startDate = DateUtils.getDateForHistoryRange(range);
        try {
            String[] queryParameters = {"start_date=" + startDate};
            ServiceHandler.buildRequest(url,
                    Constants.REQUEST_TYPE.GET,
                    this, queryParameters);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_currency_conversion:
                showCountryCurrencies();
                break;
            case R.id.action_wechange_scaleight:
                View menuItemView = findViewById(R.id.action_wechange_scaleight);
                showPopup(menuItemView);
                break;
        }
        return true;
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_activity_main_scales, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }

    private void showCountryCurrencies() {

        Intent intent = new Intent(this, ActivityCurrencyExchange.class);
        startActivityForResult(intent, Constants.REQUEST_CURRENCY_CONVERSION_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (resultCode) {
            case RESULT_OK:
                handleActivityResult(requestCode, data);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

    private void dismissProgressDialog() {
        if ( progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading. Please wait ...");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    private void handleActivityResult(int requestCode, Intent data) {

        switch (requestCode) {
            case Constants.REQUEST_CURRENCY_CONVERSION_ACTIVITY:

                //get the selected Currency
                String selectedCurrency = data.getStringExtra(Constants.SELECTED_CURRENCY);
                this.selectedCurrency = selectedCurrency;
                this.currency = (Currency) data.getExtras().get(Constants.CURRENCIES);

                //relaod the graph
                this.fetchPriceWithHistoryRange(this.historyRange, Constants.CURRENCY.GOLD );
                break;
        }
    }

    @Override
    public void onClick(View v) {

        tvOneDay.setSelected(false);
        tvFiveDays.setSelected(false);
        tvOneMonth.setSelected(false);
        tvSixMonths.setSelected(false);
        tvOneYear.setSelected(false);
        tvFiveYears.setSelected(false);
        tvTenYears.setSelected(false);
        v.setSelected(!v.isSelected());

        switch (v.getId()) {
            case R.id.activity_main_tv_today:
                this.historyRange = Constants.HistoryRange.DAY;
                this.tvChangeDesc.setText("");
                break;
            case R.id.activity_main_tv_week:
                this.historyRange = Constants.HistoryRange.WEEK;
                this.tvChangeDesc.setText("last week");
                break;
            case R.id.activity_main_tv_month:
                this.historyRange = Constants.HistoryRange.MONTH;
                this.tvChangeDesc.setText("last month");
                break;
            case R.id.activity_main_tv_sixMonths:
                this.historyRange = Constants.HistoryRange.SIX_MONTH;
                this.tvChangeDesc.setText("last 6 month");
                break;
            case R.id.activity_main_tv_year:
                this.historyRange = Constants.HistoryRange.YEAR;
                this.tvChangeDesc.setText("last year");
                break;
            case R.id.activity_main_tv_fiveYears:
                this.historyRange = Constants.HistoryRange.FIVE_YEAR;
                this.tvChangeDesc.setText("five year");
                break;
            case R.id.activity_main_tv_tenYear:
                this.historyRange = Constants.HistoryRange.TEN_YEAR;
                this.tvChangeDesc.setText("last decade");
            case R.id.activity_main_tv_btc:
                this.targetCurrency = Constants.CURRENCY.BITCOIN;
                break;
            case R.id.activity_main_tv_gold:
                this.targetCurrency = Constants.CURRENCY.GOLD;
                break;
        }

        fetchPriceWithHistoryRange(historyRange, targetCurrency);
    }

    @Override
    public void onFailure(Call call, IOException e) {
        progressDialog.dismiss();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {

        progressDialog.dismiss();
        if (response.code() == Constants.HTTP_OK) {

            switch (call.request().tag().toString()) {
                case Constants.url_gold_prices_lsx:
                case Constants.url_bitcoin_bitcoinwatch:
                    handleResponseForRequest(response);
                    break;
            }
        }
    }

    private void handleResponseForRequest(Response response) {

        Gson gson = new Gson();
        try {
            String jsonString = response.body().string();
            Type goldPriceType = new TypeToken<FinancialData>() {
            }.getType();
            final FinancialData financialData = gson.fromJson(jsonString, goldPriceType);
            Log.d("GOLD", String.valueOf(financialData));

            ///////////////////////////
            final List<Entry> entries = new ArrayList<Entry>();

            for (List<String> data : financialData.getDataset().getData()) {
                String date = data.get(0);
                String usd_am = data.get(1);
                String usd_pm = data.get(2);
                Timestamp timestamp = DateUtils.getTimeStamp(date);
                float price = 0;
                switch (targetCurrency){
                    case GOLD:
                        price = this.currencyPrice(usd_am, this.selectedCurrency);
                        break;
                    case BITCOIN:
                        price = ParseUtils.parseBitcoinPrices(data);
                        break;
                }
//                Float usd_price = this.currencyPrice(usd_am, this.selectedCurrency);
                Long time = timestamp.getTime();
                Entry entry = new Entry(time, price);
                entries.add(entry);
            }
            Collections.reverse(entries);

            if (entries.size() > 0) {

                //LineDataSet
                LineDataSet dataSet = new LineDataSet(entries, "GOLD");
                dataSet.setDrawCircles(false);
                dataSet.setColor(this.colorFromResource(R.color.white));
                dataSet.setValueTextColor(this.colorFromResource(R.color.secondary_text));
                dataSet.setLineWidth(1.0f);
                dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                dataSet.setDrawFilled(true);
                dataSet.setFillColor(this.colorFromResource(R.color.vegasGold));
                dataSet.setValueFormatter(new ValueFormatter());

                //LineData
                LineData lineData = new LineData(dataSet);
                lineData.setValueTextColor(this.colorFromResource(R.color.white));
                lineChart.setData(lineData);

                //Format Axis data
                lineChart.getXAxis().setValueFormatter(new DayAxisValueFormatter());
                lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                lineChart.getXAxis().setDrawAxisLine(false);
                lineChart.getXAxis().setDrawGridLines(false);
                lineChart.getXAxis().setTextColor(this.colorFromResource(R.color.primary_text));
                lineChart.getAxisLeft().setValueFormatter(new AxisValueFormatter());
                lineChart.getAxisRight().setValueFormatter(new AxisValueFormatter());
                lineChart.getAxisLeft() .setDrawGridLines(false);
                lineChart.getAxisRight().setGridDashedLine(new DashPathEffect(new float[]{10.0f, 5.0f}, 1));
                lineChart.getAxisLeft().setDrawAxisLine(false);
                lineChart.getAxisRight().setDrawAxisLine(false);
                lineChart.setOnChartValueSelectedListener(this);
                lineChart.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                Log.d(TAG, "onTouch: down touch");
                                llPriceChange.setVisibility(View.VISIBLE);
                                llPriceCompare.setVisibility(View.GONE);
                                lineChart.setSelected(false);
                                break;
                            case MotionEvent.ACTION_UP:
                                llPriceChange.setVisibility(View.GONE);
                                llPriceCompare.setVisibility(View.VISIBLE);
                                lineChart.setSelected(false);
                                Log.d(TAG, "onTouch: up touch");
                                break;
                        }
                        return false;
                    }
                });
                lineChart.setScaleEnabled(false);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //Description For the LineChart
                        Description description = new Description();
                        description.setText(financialData.getDataset().getName());
                        lineChart.setDescription(description);
                        lineChart.invalidate();
                        lineChart.animateXY(500, 500);

                        //set the label prices
                        Entry lastEntry = entries.get(entries.size() - 1);
                        selectedEntry = lastEntry;

                        //set the price changes labels
                        Entry firstEntry = entries.get(0);
                        firstEntryInPeriod = entries.get(0);

                        tvSelectedPrice.setText(new DecimalFormat(".##").format(selectedEntry.getY()));
                        tvSelectedDate.setText(DateUtils.getDateString((long) selectedEntry.getX(), DateUtils.DD_MM_YYYY));
                        tvSymbol.setText(java.util.Currency.getInstance(selectedCurrency).getSymbol());
                        tvCurrentPrice.setText(new DecimalFormat(".##").format(lastEntry.getY()));

                        //price change different
                        float priceDifference = selectedEntry.getY() - firstEntryInPeriod.getY();
                        tvDifferenceInPrice.setText(new DecimalFormat(".##").format(priceDifference));

                        if (priceDifference > 0 ){
                            imgPriceChange.setVisibility(View.VISIBLE);
                            imgPriceChange.setImageResource(R.drawable.thumbs_up);

                        }else if (priceDifference  == 0) {
                            imgPriceChange.setVisibility(View.GONE);
                        }else {
                            imgPriceChange.setVisibility(View.VISIBLE);
                            imgPriceChange.setImageResource(R.drawable.thumbs_down);
                        }

                    }
                });

                //////////////////////////
            } else {
                lineChart.getAxisLeft().setAxisMinimum(0);
                lineChart.getAxisLeft().setAxisMaximum(100);
                lineChart.invalidate();
                lineChart.animate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int colorFromResource(int colorResource) {
        return ResourcesCompat.getColor(getResources(), colorResource, null);
    }

    private Float currencyPrice(String USD, String currencyTo) {
        Float priceInUSD = Float.parseFloat(USD);
        Float priceInCurrency = priceInUSD;
        if (this.currency != null && currencyTo.length() > 0) {
            Object value = "";

            //get the value from the field name
            try {
                Class aClass = this.currency.getRates().getClass();
                Field rateField = aClass.getDeclaredField(currencyTo);
                Object object = this.currency.getRates();
                rateField.setAccessible(true);
                value = rateField.get(object);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            Float currencyRate = Float.valueOf(String.valueOf(value));
            priceInCurrency = (currencyRate) * priceInUSD;
        }
        return priceInCurrency * (1 / Constants.ounce) *  (float)scaleWeight;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_tola:
                scaleWeight = 10;
                break;
            case R.id.item_ounce:
                scaleWeight = 28.3495;
                break;
            case R.id.item_pound:
                scaleWeight = 453.592;
                break;
            case R.id.item_stone:
                scaleWeight = 6350.29;
                break;
            case R.id.item_kilo:
                scaleWeight = 1000;
                break;
        }
        fetchPriceWithHistoryRange(historyRange, targetCurrency);
        return true;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        float priceDifference;
        priceDifference = selectedEntry.getY() - firstEntryInPeriod.getY();
        selectedEntry = e;
        tvSelectedPrice.setText( new DecimalFormat(".##").format(selectedEntry.getY()));
        tvSelectedDate.setText( DateUtils.getDateString((long) selectedEntry.getX(),DateUtils.DD_MM_YYYY));
        tvCurrentPrice.setText( new DecimalFormat(".##").format(selectedEntry.getY()));
        tvDifferenceInPrice.setText((new DecimalFormat(".##").format(priceDifference)));

        if (priceDifference > 0 ){
            imgPriceChange.setVisibility(View.VISIBLE);
            imgPriceChange.setImageResource(R.drawable.thumbs_up);
          }else if (priceDifference  == 0) {
            imgPriceChange.setVisibility(View.GONE);
        }else {
            imgPriceChange.setVisibility(View.VISIBLE);
            imgPriceChange.setImageResource(R.drawable.thumbs_down);
        }
    }

    @Override
    public void onNothingSelected() {
    }
}
