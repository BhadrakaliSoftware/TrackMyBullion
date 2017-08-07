package com.riddhi.trackmybullion;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riddhi.trackmybullion.global.Constants;
import com.riddhi.trackmybullion.service.ServiceHandler;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by riddhi on 8/5/2017.
 */

public class GoldFragment extends Fragment implements Callback{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gold_price, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        init();
    }

    private void init() {

        try{
            ServiceHandler.buildRequest(Constants.url_gold_prices, Constants.REQUEST_TYPE.GET, this);
        }catch( Exception exception ) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {

        switch (call.request().url().toString()) {
            case Constants.url_gold_prices:
                break;
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {

        switch (call.request().url().toString()) {
            case Constants.url_gold_prices:
                System.out.println(response.body());
                break;
        }
    }
}
