package com.example.gurumenaviapp.search.screen;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.example.gurumenaviapp.R;

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

        // Android 6, API 23以上でパーミッシンの確認
        if (Build.VERSION.SDK_INT >= 23) {
            presenter.checkPermission();
        } else {
            presenter.activateGps();
        }

        findViews();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.searchRestaurant();
            }
        });

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
}
