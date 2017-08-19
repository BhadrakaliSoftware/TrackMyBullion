package com.riddhi.trackmybullion.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.riddhi.trackmybullion.R;

/**
 * Created by riddhi on 8/19/2017.
 */

class CurrencyViewHolder extends RecyclerView.ViewHolder {

    public TextView tvCode, tvCodeValue;

    public CurrencyViewHolder(View itemView) {

        super(itemView);
        this.tvCode = (TextView) itemView.findViewById(R.id.tv_Code);
        this.tvCodeValue = (TextView) itemView.findViewById(R.id.tv_Code_Value);
    }
}
