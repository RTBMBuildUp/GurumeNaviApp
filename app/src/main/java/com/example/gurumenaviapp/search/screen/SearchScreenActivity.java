package com.example.gurumenaviapp.search.screen;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.example.gurumenaviapp.R;

public class SearchScreenActivity extends AppCompatActivity implements SearchScreenContract.View {
    private final String token = "bf565ef4fdb696cfb6ff5a911941fa8d";

    private SearchScreenContract.Presenter presenter;

    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new SearchScreenPresenter(this, this);

        // Android 6, API 23以上でパーミッシンの確認
        if (Build.VERSION.SDK_INT >= 23) {
            System.out.println("check");
            presenter.checkPermission();
        } else {
            System.out.println("else");
            presenter.activateGps();
        }

        findViews();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.searchRestaurant(token);
            }
        });
    }

    private void findViews() {
        this.searchButton = findViewById(R.id.button_search);
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
