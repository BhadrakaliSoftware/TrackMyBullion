package com.bhadrasoft.trackmybullion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.bhadrasoft.trackmybullion.global.Constants;
import com.bhadrasoft.trackmybullion.global.utils.DateUtils;
import com.bhadrasoft.trackmybullion.models.Investment;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityAddInvestment extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.activity_add_investment_button_save)
    Button buttonSave;

    @BindView(R.id.activity_add_investment_et_amount)
    EditText etAmount;

    @BindView(R.id.activity_add_investment_et_weight)
    EditText etWeight;

    @BindView(R.id.activity_add_investment_tv_date)
    TextView tvDate;

    DatabaseReference mFirebaseDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_investment);

        init();
        initData();
    }

    private void initData() {

        //Firebase instance
        if (mFirebaseDatabaseRef == null) {
            mFirebaseDatabaseRef = FirebaseDatabase.getInstance().getReference();
        }
    }

    private void init() {
       ButterKnife.bind(this);

        //click listener
       buttonSave.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.activity_add_investment_button_save:
                saveInvestment();
                break;
        }
    }

    private void saveInvestment() {
        //save investment to database
        Investment investment = new Investment();
        investment.setAmount(Integer.valueOf(etAmount.getText().toString().trim()));
        investment.setWeight(Integer.valueOf(etWeight.getText().toString().trim()));
        investment.setDate(DateUtils.getDateString(new Date(), DateUtils.yyyyMMdd_HHmmaz));

        mFirebaseDatabaseRef.child(Constants.NODE_INVESTMENTS).push().setValue(investment);
    }
}
