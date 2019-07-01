package com.example.gurumenaviapp;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import com.example.gurumenaviapp.data.ShowedInformation;
import com.example.gurumenaviapp.recyclerview.SearchResultAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GuruNaviContract.View {
    private final int REQUEST_PERMISSION = 1000;
    private final String token = "bf565ef4fdb696cfb6ff5a911941fa8d";

    private GuruNaviContract.Presenter presenter;

    private Button startButton;
    private RecyclerView recyclerView;

    private SearchResultAdapter adapter;
    private List<ShowedInformation> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new GuruNaviPresenter(this, this);

        // Android 6, API 23以上でパーミッシンの確認
        if (Build.VERSION.SDK_INT >= 23) {
            System.out.println("check");
            presenter.checkPermission();
        } else {
            System.out.println("else");
            presenter.startGpsPrepares();
        }

        findViews();
        adapter = new SearchResultAdapter(itemList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.searchNearRestaurant(token);
            }
        });
    }

    @Override
    public Activity getViewActivity() {
        return this;
    }

    @Override
    public void setPresenter(GuruNaviContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void addRecyclerViewItem(ShowedInformation item) {
        presenter.setItem(itemList, item);
        adapter.notifyItemInserted(0);
    }

    @Override
    public void removeRecyclerViewItem(int position) {
        presenter.removeItem(itemList, position);
        adapter.notifyItemRemoved(position);
    }

    public void cleanRecyclerViewItem() {
        presenter.cleanItem(itemList);
    }

    private void findViews() {
        this.startButton = findViewById(R.id.button_start);
        this.recyclerView = findViewById(R.id.recyclerview);
    }
}
