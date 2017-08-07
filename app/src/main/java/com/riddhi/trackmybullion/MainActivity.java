package com.riddhi.trackmybullion;

import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationItemView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        ButterKnife.bind(this);

        bottomNavigationItemView.setItemIconTintList(null);
        bottomNavigationItemView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        selectFragment(item);
        return true;
    }

    private void selectFragment(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_gold:
                showGoldPrices();
                break;
            case R.id.item_silver:
                showSilverPrices();
                break;
        }
    }

    private void showSilverPrices() {

        //Action to show silver prices fragment to main activity
        pushFragment(new SilverFragment());
    }

        private void showGoldPrices() {

        //Action  to show gold prices fragment to main activity
        pushFragment(new GoldFragment());
    }

    /*
    * Method to push any fragment into given id
    *
    * @param fragment An instance of Fragment to show into given id
    * */
   protected void pushFragment(Fragment fragment) {
       if (fragment == null)
           return;

       FragmentManager fragmentManager = getSupportFragmentManager();
       if (fragmentManager != null) {
           FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
           if(fragmentTransaction != null) {
               fragmentTransaction.replace(R.id.rootLayout,fragment);
               fragmentTransaction.commit();
           }
       }
   }
}
