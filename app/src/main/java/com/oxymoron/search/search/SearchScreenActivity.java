package com.oxymoron.search.search;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioGroup;

import com.example.gurumenaviapp.R;
import com.oxymoron.util.Toaster;

import java.util.Map;

public class SearchScreenActivity extends AppCompatActivity implements SearchScreenContract.View {
    private SearchScreenContract.Presenter presenter;

    private Button searchButton;
    private RadioGroup rangeRadioGroup;

    private Map<Integer, Integer> radioButtonRangeMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_screen);

        presenter = new SearchScreenPresenter(this, this);

        if (Build.VERSION.SDK_INT >= 23) {
            presenter.checkPermission();
        } else {
            presenter.activateGps();
        }

        findViews();

        searchButton.setOnClickListener(v -> presenter.searchRestaurant());

        rangeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                presenter.OnCheckedChange(group, checkedId);
            }
        });
    }

    private void findViews() {
        this.searchButton = findViewById(R.id.search_screen_search_button);
        this.rangeRadioGroup = findViewById(R.id.search_screen_range_radio_button_groupe);
    }

    @Override
    public void setPresenter(SearchScreenContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public Activity getViewActivity() {
        return this;
    }

    @Override
    public void toast(String message) {
        Toaster.toast(this, message);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("log", "onRequestPermissionsResult: " + requestCode);
        if (requestCode == 1000) {
            Log.d("log", "onRequestPermissionsResult: request");
            if (PackageManager.PERMISSION_GRANTED != grantResults[0]) {
                Log.d("log", "onRequestPermissionsResult: deny");
            } else {
                presenter.activateGps();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
