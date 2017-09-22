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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bhadrasoft.trackmybullion.global.Constants;
import com.bhadrasoft.trackmybullion.global.graph.AxisValueFormatter;
import com.bhadrasoft.trackmybullion.global.graph.CurrencyValueFormatter;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, Callback, PopupMenu.OnMenuItemClickListener {

    private Currency currency;
    private ProgressDialog progressDialog;
    private String selectedCurrency = "USD";
    private double scaleWeight = 10;

    Constants.HistoryRange historyRange = Constants.HistoryRange.WEEK;

    @BindView(R.id.activity_main_lineChart)
    LineChart lineChart;

    @BindView(R.id.activity_main_tv_today)
    TextView tvOneDay;

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

    @BindView(R.id.activity_main_button_show_investment)
    Button buttonShowInvestment;

    private AdView mAdView;

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
        buttonShowInvestment.setOnClickListener(this);
    }

    private void initData() {
        fetchGoldPriceWithHistoryRange(Constants.HistoryRange.WEEK);
    }

    private void fetchGoldPriceWithHistoryRange(Constants.HistoryRange range) {

        progressDialog = ProgressDialog.show(this, "", "");
        String startDate = DateUtils.getDateForHistoryRange(range);
        try {
            String[] queryParameters = {"start_date=" + startDate};
            ServiceHandler.buildRequest(Constants.url_gold_prices_lsx,
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

    private void handleActivityResult(int requestCode, Intent data) {

        switch (requestCode) {
            case Constants.REQUEST_CURRENCY_CONVERSION_ACTIVITY:

                //get the selected Currency
                String selectedCurrency = data.getStringExtra(Constants.SELECTED_CURRENCY);
                this.selectedCurrency = selectedCurrency;
                this.currency = (Currency) data.getExtras().get(Constants.CURRENCIES);

                //relaod the graph
                this.fetchGoldPriceWithHistoryRange(this.historyRange);
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
                break;
            case R.id.activity_main_tv_week:
                this.historyRange = Constants.HistoryRange.WEEK;
                break;
            case R.id.activity_main_tv_month:
                this.historyRange = Constants.HistoryRange.MONTH;
                break;
            case R.id.activity_main_tv_sixMonths:
                this.historyRange = Constants.HistoryRange.SIX_MONTH;
                break;
            case R.id.activity_main_tv_year:
                this.historyRange = Constants.HistoryRange.YEAR;
                break;
            case R.id.activity_main_tv_fiveYears:
                this.historyRange = Constants.HistoryRange.FIVE_YEAR;
                break;
            case R.id.activity_main_tv_tenYear:
                this.historyRange = Constants.HistoryRange.TEN_YEAR;
                break;
            case R.id.activity_main_button_show_investment:
                showInvestments();
                return;
        }
        fetchGoldPriceWithHistoryRange(historyRange);
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
                    handleResponseForRequestGoldPrice(response);
                    break;
            }
        }
    }

    private void showInvestments() {
        Intent intent = new Intent(this, ActivityInvestments.class);
        startActivityForResult(intent, Constants.REQUEST_INVESMENTS_ACTIVITY);
    }

    private void handleResponseForRequestGoldPrice(Response response) {

        Gson gson = new Gson();
        try {
            String jsonString = response.body().string();
            Type goldPriceType = new TypeToken<FinancialData>() {
            }.getType();
            final FinancialData financialData = gson.fromJson(jsonString, goldPriceType);
            Log.d("GOLD", String.valueOf(financialData));

            ///////////////////////////
            List<Entry> entries = new ArrayList<Entry>();

            for (List<String> data : financialData.getDataset().getData()) {
                String date = data.get(0);
                String usd_am = data.get(1);
                String usd_pm = data.get(2);
                Timestamp timestamp = DateUtils.getTimeStamp(date);
                Float usd_price = this.currencyPrice(usd_am, this.selectedCurrency);
                Long time = timestamp.getTime();
                Entry entry = new Entry(time, usd_price);
                entries.add(entry);
            }
            Collections.reverse(entries);

            if (entries.size() > 0) {

                //LineDataSet
                LineDataSet dataSet = new LineDataSet(entries, "GOLD");
                dataSet.setCircleColor(this.colorFromResource(R.color.goldenOld));
                dataSet.setColor(this.colorFromResource(R.color.goldenOld));
                dataSet.setValueTextColor(this.colorFromResource(R.color.goldenOld));
                dataSet.setLineWidth(3.0f);
                dataSet.setDrawFilled(true);
                dataSet.setFillColor(this.colorFromResource(R.color.vegasGold));
                dataSet.setValueFormatter(new ValueFormatter());

                //LineData
                LineData lineData = new LineData(dataSet);
                lineData.setValueTextColor(this.colorFromResource(R.color.goldenOld));
                lineChart.setData(lineData);

                //Format Axis data
                lineChart.getXAxis().setValueFormatter(new DayAxisValueFormatter());
                lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                lineChart.getXAxis().setDrawAxisLine(false);
                lineChart.getXAxis().setDrawGridLines(false);
                lineChart.getXAxis().setAxisLineColor(R.color.white);
                lineChart.getAxisLeft().setAxisLineColor(R.color.white);
                lineChart.getAxisRight().setValueFormatter(new AxisValueFormatter());
                lineChart.getAxisLeft().setValueFormatter(new CurrencyValueFormatter(this.selectedCurrency.toUpperCase()));
                lineChart.getAxisLeft() .setDrawGridLines(false);
                lineChart.getAxisRight().setGridDashedLine(new DashPathEffect(new float[]{10.0f, 5.0f}, 1));
                ;
                lineChart.getAxisLeft().setDrawAxisLine(false);
                lineChart.getAxisRight().setDrawAxisLine(false);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //Description For the LineChart
                        Description description = new Description();
                        description.setText(financialData.getDataset().getName());
                        lineChart.setDescription(description);

                        //Color resources
                        int color = colorFromResource(R.color.white);
                        lineChart.setBackgroundColor(color);
                        lineChart.invalidate();
                        lineChart.animateXY(500, 500);
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
        fetchGoldPriceWithHistoryRange(historyRange);
        return true;
    }
}
