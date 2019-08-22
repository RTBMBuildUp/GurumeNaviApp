package com.oxymoron.ui.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.example.gurumenaviapp.R;
import com.oxymoron.api.GurumeNaviApiClientImpl;
import com.oxymoron.request.LocationInformation;
import com.oxymoron.request.Range;
import com.oxymoron.request.RequestIds;
import com.oxymoron.ui.detail.RestaurantDetailActivity;
import com.oxymoron.ui.list.data.RestaurantThumbnail;
import com.oxymoron.ui.list.recyclerview.RestaurantListAdapter;

import java.util.ArrayList;
import java.util.List;

public class RestaurantListActivity extends AppCompatActivity implements RestaurantListContract.View {
    private final static String KEY_LOCATION_INFORMATION = "KEY_LOCATION_INFORMATION";
    private final static String KEY_RANGE = "KEY_RANGE";

    private RestaurantListContract.Presenter presenter;

    private RecyclerView recyclerView;
    private RestaurantListAdapter adapter;
    private List<RestaurantThumbnail> itemList = new ArrayList<>();

    private LocationInformation locationInformation;
    private Range range;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_list);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        findViews();

        Intent intent = this.getIntent();
        this.locationInformation =
                ((LocationInformation) intent.getSerializableExtra(KEY_LOCATION_INFORMATION));
        this.range = ((Range) intent.getSerializableExtra(KEY_RANGE));

        prepareRecyclerView();

        presenter = new RestaurantListPresenter(this, GurumeNaviApiClientImpl.getInstance());

        presenter.search(range, locationInformation);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(RestaurantListContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void addRecyclerViewItem(RestaurantThumbnail item) {
        presenter.setItem(itemList, item);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void removeRecyclerViewItem(int position) {
        presenter.removeItem(itemList, position);
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void cleanRecyclerViewItem() {
        presenter.cleanItem(itemList);
    }

    public static Intent createIntent(Context packageContext, Range range, LocationInformation locationInformation) {
        Intent intent = new Intent(packageContext, RestaurantListActivity.class);

        intent.putExtra(KEY_LOCATION_INFORMATION, locationInformation);
        intent.putExtra(KEY_RANGE, range);

        return intent;
    }

    private void prepareRecyclerView() {
        this.adapter = new RestaurantListAdapter(this.itemList);
        this.adapter.setOnClickListener(thumbnail -> {
            Intent intent = new Intent(RestaurantListActivity.this, RestaurantDetailActivity.class);
            intent.putExtra(RequestIds.restaurant_id.toString(), thumbnail.getRestaurantId());
            startActivity(intent);
        });

        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(adapter);
        this.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                presenter.onScrolled(recyclerView, range, locationInformation, itemList.size());
            }
        });
    }

    private void findViews() {
        this.recyclerView = findViewById(R.id.restaurant_list_recyclerview);
    }
}
