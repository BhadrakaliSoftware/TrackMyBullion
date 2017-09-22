package com.bhadrasoft.trackmybullion.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bhadrasoft.trackmybullion.R;

/**
 * Created by riddhi on 8/19/2017.
 */

class CurrencyViewHolder extends RecyclerView.ViewHolder{

    public TextView tvCode, tvCodeValue;
    public ImageView ivCountryFlag;

    public CurrencyViewHolder(View itemView) {
        super(itemView);
        this.tvCode = (TextView) itemView.findViewById(R.id.tv_Code);
        this.tvCodeValue = (TextView) itemView.findViewById(R.id.tv_Code_Value);
        this.ivCountryFlag = (ImageView) itemView.findViewById(R.id.iv_Currency);
    }

}
