package com.riddhi.trackmybullion;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riddhi.trackmybullion.global.Constants;
import com.riddhi.trackmybullion.global.graph.AxisValueFormatter;
import com.riddhi.trackmybullion.global.graph.DayAxisValueFormatter;
import com.riddhi.trackmybullion.global.graph.ValueFormatter;
import com.riddhi.trackmybullion.global.utils.DateUtils;
import com.riddhi.trackmybullion.models.FinancialData;
import com.riddhi.trackmybullion.service.ServiceHandler;


import java.io.IOException;
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

/**
 * Created by riddhi on 8/5/2017.
 */

public class GoldFragment extends Fragment implements Callback {

    @BindView(R.id.fragment_gold_price_linechart)
    LineChart goldPriceLineChart;

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
    }


    ProgressDialog progressDialog;
    private void init() {

        progressDialog = ProgressDialog.show(getContext(), "", "");
        try {
            String[] queryParameters = {"start_date=2017-07-01"};
            ServiceHandler.buildRequest(Constants.url_gol_prices_lsx,
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

        if (response.code() == 200) {
            handleResponseForRequestGoldPrice(response);
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

            ///////////////////////////
            List<Entry> entries = new ArrayList<Entry>();

            for (List<String> data : financialData.getDataset().getData()) {
                String date = data.get(0);
                String usd_am = data.get(1);
                String usd_pm = data.get(2);
                Timestamp timestamp = DateUtils.getTimeStamp(date);
                Float usd_price = Float.parseFloat(usd_am);
                Long time = timestamp.getTime();
                Entry entry = new Entry(time, usd_price);
                entries.add(entry);
            }

            Collections.reverse(entries);

            //LineDataSet
            LineDataSet dataSet = new LineDataSet(entries, "GOLD");
            dataSet.setColor(this.colorFromResource(R.color.goldenOld));
            dataSet.setValueTextColor(this.colorFromResource(R.color.goldenOld));
            dataSet.setLineWidth(3.0f);
            dataSet.setDrawFilled(true);
            dataSet.setFillColor(this.colorFromResource(R.color.vegasGold));
            dataSet.setValueFormatter( new ValueFormatter());

            //LineData
            LineData lineData = new LineData(dataSet);
            lineData.setValueTextColor(this.colorFromResource(R.color.goldenOld));
            goldPriceLineChart.setData(lineData);

            //Format Axis data
            goldPriceLineChart.getXAxis().setValueFormatter( new DayAxisValueFormatter());
            goldPriceLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            goldPriceLineChart.getAxisRight().setValueFormatter( new AxisValueFormatter());

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

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
        return ResourcesCompat.getColor(getResources(),colorResource,null);
    }
}
