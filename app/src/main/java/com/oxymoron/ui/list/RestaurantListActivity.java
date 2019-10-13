package com.oxymoron.ui.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gurumenaviapp.R;
import com.oxymoron.data.source.local.data.RestaurantId;
import com.oxymoron.data.source.remote.api.PageState;
import com.oxymoron.data.source.remote.api.serializable.LocationInformation;
import com.oxymoron.data.source.remote.api.serializable.Range;
import com.oxymoron.injection.Injection;
import com.oxymoron.ui.detail.RestaurantDetailActivity;
import com.oxymoron.ui.list.data.RestaurantThumbnail;
import com.oxymoron.ui.list.recyclerview.EndlessScrollListener;
import com.oxymoron.ui.list.recyclerview.RestaurantListAdapter;

import java.util.ArrayList;
import java.util.List;

public class RestaurantListActivity extends AppCompatActivity implements RestaurantListContract.View {
    private final static String KEY_LOCATION_INFORMATION = "KEY_LOCATION_INFORMATION";
    private final static String KEY_RANGE = "KEY_RANGE";

    private RestaurantListContract.Presenter presenter;

    private RecyclerView recyclerView;
    private RestaurantListAdapter adapter;
    private final List<RestaurantThumbnail> restaurantThumbnailList = new ArrayList<>();

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

        final Intent intent = this.getIntent();
        this.locationInformation =
                ((LocationInformation) intent.getSerializableExtra(KEY_LOCATION_INFORMATION));
        this.range = ((Range) intent.getSerializableExtra(KEY_RANGE));

        prepareRecyclerView();

        presenter = new RestaurantListPresenter(
                this,
                Injection.provideRestaurantDetailsRepository(this)
        );

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
    protected void onStop() {
        super.onStop();
        presenter.refreshSavedItem(restaurantThumbnailList);
    }

    @Override
    public void addRecyclerViewItem(RestaurantThumbnail restaurantThumbnail) {
        presenter.setItem(restaurantThumbnailList, restaurantThumbnail);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void removeRecyclerViewItem(int position) {
        presenter.removeItem(restaurantThumbnailList, position);
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void setPresenter(RestaurantListContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public static Intent createIntent(Context packageContext, Range range, LocationInformation locationInformation) {
        final Intent intent = new Intent(packageContext, RestaurantListActivity.class);

        intent.putExtra(KEY_LOCATION_INFORMATION, locationInformation);
        intent.putExtra(KEY_RANGE, range);

        return intent;
    }

    private void prepareRecyclerView() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        this.adapter = new RestaurantListAdapter(this.restaurantThumbnailList);
        this.adapter.setOnClickListener(thumbnail -> {
            final RestaurantId restaurantId = thumbnail.getId();
            final Intent intent = RestaurantDetailActivity.createIntent(RestaurantListActivity.this, restaurantId);

            startActivity(intent);
        });

        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(linearLayoutManager);
        this.recyclerView.setAdapter(adapter);
        this.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        this.recyclerView.addOnScrollListener(new EndlessScrollListener(linearLayoutManager) {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                presenter.search(range, locationInformation, new PageState(page));

                return true;
            }
        });
    }

    private void findViews() {
        this.recyclerView = findViewById(R.id.restaurant_list_recycler_view);
    }
}
