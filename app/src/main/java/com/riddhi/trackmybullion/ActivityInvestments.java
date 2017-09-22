package com.riddhi.trackmybullion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.FirebaseDatabase;
import com.riddhi.trackmybullion.global.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityInvestments extends AppCompatActivity {

    @BindView(R.id.activity_investments_rv_investments)
    RecyclerView rvInvestments;

    FirebaseDatabase mFirebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investments);

        init();
    }

    private void init() {
        ButterKnife.bind(this);

        //Firebase instance
        if (mFirebaseDatabase == null) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            mFirebaseDatabase = firebaseDatabase;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_insvestments_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add_intvestment:
                showAddInvestmentItem();
                break;
        }
        return true;
    }

    private void showAddInvestmentItem() {
        Intent intent =  new Intent(this, ActivityAddInvestment.class);
        startActivityForResult(intent, Constants.REQUEST_ADD_INVESTMENT_ACTIVITY);
    }

}
