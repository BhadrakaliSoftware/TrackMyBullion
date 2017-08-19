package com.riddhi.trackmybullion;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riddhi.trackmybullion.global.Constants;
import com.riddhi.trackmybullion.global.graph.AxisValueFormatter;
import com.riddhi.trackmybullion.global.graph.CurrencyValueFormatter;
import com.riddhi.trackmybullion.global.graph.DayAxisValueFormatter;
import com.riddhi.trackmybullion.global.graph.ValueFormatter;
import com.riddhi.trackmybullion.global.utils.DateUtils;
import com.riddhi.trackmybullion.models.Currency;
import com.riddhi.trackmybullion.models.FinancialData;
import com.riddhi.trackmybullion.service.ServiceHandler;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by riddhi on 8/5/2017.
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public class GoldFragment extends Fragment implements Callback, View.OnClickListener {

    @BindView(R.id.fragment_gold_price_linechart)
    LineChart goldPriceLineChart;

    @BindView(R.id.fragment_gold_price_button_currency_rate)
    Button buttonCurrencyRate;

    @BindView(R.id.fragment_gold_price_tv_oneday)
    TextView tvOneDay;

    @BindView(R.id.fragment_gold_price_tv_fivedays)
    TextView tvFiveDays;

    @BindView(R.id.fragment_gold_price_tv_onemonth)
    TextView tvOneMonth;

    @BindView(R.id.fragment_gold_price_tv_sixmonth)
    TextView tvSixMonths;

    @BindView(R.id.fragment_gold_price_tv_oneyear)
    TextView tvOneYear;

    @BindView(R.id.fragment_gold_price_tv_fiveyear)
    TextView tvFiveYears;

    @BindView(R.id.fragment_gold_price_tv_tenyear)
    TextView tvTenYears;

    @BindView(R.id.fragment_gold_price_tv_detail)
    TextView tvDetails;

    @BindView(R.id.fragment_gold_price_tv_title)
    TextView tvTitle;

    @BindView(R.id.fragment_gold_price_tv_last_updated)
    TextView tvLastUpated;

    private enum HistoryRange {DAY, WEEK, MONTH, SIX_MONTH, YEAR, FIVE_YEAR, TEN_YEAR}

    ;
    Currency currency;
    HistoryRange historyRange = HistoryRange.WEEK;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gold_price, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init();
        initData();
    }

    private void initData() {

        //Load default view
        tvFiveDays.setSelected(true);
    }


    ProgressDialog progressDialog;

    private void init() {

        fetchGoldPriceWithHistoryRange(HistoryRange.WEEK);

        buttonCurrencyRate.setOnClickListener(this);
        tvOneDay.setOnClickListener(this);
        tvFiveDays.setOnClickListener(this);
        tvOneMonth.setOnClickListener(this);
        tvSixMonths.setOnClickListener(this);
        tvOneYear.setOnClickListener(this);
        tvFiveYears.setOnClickListener(this);
        tvTenYears.setOnClickListener(this);
    }

    private void fetchGoldPriceWithHistoryRange(HistoryRange range) {

        progressDialog = ProgressDialog.show(getContext(), "", "");

        String startDate = getDate(range);
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
    public void onFailure(Call call, IOException e) {

        progressDialog.dismiss();
        switch (call.request().url().toString()) {
            case Constants.url_gold_prices:
                break;
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {

        progressDialog.dismiss();
        if (response.code() == Constants.HTTP_OK) {

            switch (call.request().tag().toString()) {
                case Constants.url_currency_rates:
                    handleResonseCurrencyRate(response);
                    break;
                case Constants.url_gold_prices_lsx:
                    handleResponseForRequestGoldPrice(response);
                    break;
                case Constants.url_silver_prices_lsx:
                    handleResponseForRequestGoldPrice(response);
            }
        }
    }

    private void handleResonseCurrencyRate(Response response) {
        Gson gson = new Gson();
        try {
            String jsonString = response.body().string();
            Type currencyRateType = new TypeToken<Currency>() {
            }.getType();
            Currency currency = gson.fromJson(jsonString, currencyRateType);
            Log.d("CURRENCY", String.valueOf(currency));

            this.currency = currency;
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleResponseForRequestGoldPrice(Response response) {

        Gson gson = new Gson();
        try {
            String jsonString = response.body().string();
            Type goldPriceType = new TypeToken<FinancialData>() {
            }.getType();
            final FinancialData financialData = gson.fromJson(jsonString, goldPriceType);
            Log.d("GOLD", String.valueOf(financialData));

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvDetails.setText(financialData.getDataset().getDescription());
                    tvTitle.setText(financialData.getDataset().getDatasetCode());
                    tvLastUpated.setText(getString(R.string.lastupdatedat) +
                            financialData.getDataset().getNewestAvailableDate());
                }
            });

            ///////////////////////////
            List<Entry> entries = new ArrayList<Entry>();

            for (List<String> data : financialData.getDataset().getData()) {
                String date = data.get(0);
                String usd_am = data.get(1);
                String usd_pm = data.get(2);
                Timestamp timestamp = DateUtils.getTimeStamp(date);
                Float usd_price = this.currencyPrice(usd_am, "INR");
                Long time = timestamp.getTime();
                Entry entry = new Entry(time, usd_price);
                entries.add(entry);
            }

            Collections.reverse(entries);

            //LineDataSet
            LineDataSet dataSet = new LineDataSet(entries, "GOLD");
            dataSet.setCircleColor(this.colorFromResource(R.color.golden));
            dataSet.setColor(this.colorFromResource(R.color.goldenOld));
            dataSet.setValueTextColor(this.colorFromResource(R.color.goldenOld));
            dataSet.setLineWidth(3.0f);
            dataSet.setDrawFilled(true);
            dataSet.setFillColor(this.colorFromResource(R.color.vegasGold));
            dataSet.setValueFormatter(new ValueFormatter());

            //LineData
            LineData lineData = new LineData(dataSet);
            lineData.setValueTextColor(this.colorFromResource(R.color.goldenOld));

            goldPriceLineChart.setData(lineData);

            //Format Axis data
            goldPriceLineChart.getXAxis().setValueFormatter(new DayAxisValueFormatter());
            goldPriceLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            goldPriceLineChart.getAxisRight().setValueFormatter(new AxisValueFormatter());
            goldPriceLineChart.getAxisLeft().setValueFormatter(new CurrencyValueFormatter("USD"));
            goldPriceLineChart.getXAxis().setDrawGridLines(false);


            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    //Description For the LineChart
                    Description description = new Description();
                    description.setText(financialData.getDataset().getName());
                    goldPriceLineChart.setDescription(description);

                    //Color resources
                    int color = colorFromResource(R.color.white);
                    goldPriceLineChart.setBackgroundColor(color);
                    goldPriceLineChart.invalidate();
                }
            });

            //////////////////////////
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int colorFromResource(int colorResource) {
        return ResourcesCompat.getColor(getResources(), colorResource, null);
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
            case R.id.fragment_gold_price_button_currency_rate:
                fetchCurrencyRates();
                break;
            case R.id.fragment_gold_price_tv_oneday:
                this.historyRange = HistoryRange.DAY;
                break;
            case R.id.fragment_gold_price_tv_fivedays:
                this.historyRange = HistoryRange.WEEK;
                break;
            case R.id.fragment_gold_price_tv_onemonth:
                this.historyRange = HistoryRange.MONTH;
                break;
            case R.id.fragment_gold_price_tv_sixmonth:
                this.historyRange = HistoryRange.SIX_MONTH;
                break;
            case R.id.fragment_gold_price_tv_oneyear:
                this.historyRange = HistoryRange.YEAR;
                break;
            case R.id.fragment_gold_price_tv_fiveyear:
                this.historyRange = HistoryRange.FIVE_YEAR;
                break;
            case R.id.fragment_gold_price_tv_tenyear:
                this.historyRange = HistoryRange.TEN_YEAR;
                break;
        }

        fetchGoldPriceWithHistoryRange(historyRange);
    }

    private void fetchCurrencyRates() {

        if (progressDialog != null) {
            progressDialog.show();
        }
        String[] queryParameters = {"base=USD"};
        ServiceHandler.buildRequest(Constants.url_currency_rates, Constants.REQUEST_TYPE.GET, this, queryParameters);
    }

    private Float currencyPrice(String USD, String currencyTo) {

        Float priceInUSD = Float.parseFloat(USD);
        Float priceInCurrency = priceInUSD;
        if (this.currency != null && currencyTo.length() > 0) {
            Float currencyRate = Float.valueOf(String.valueOf(this.currency.getRates().getINR()));
            priceInCurrency = (currencyRate) * priceInUSD;
        }
        return priceInCurrency * (10 / Constants.ounce);
    }

    private String getDate(HistoryRange range) {
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
