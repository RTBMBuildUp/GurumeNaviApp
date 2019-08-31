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
import com.oxymoron.api.GurumeNaviApiClientImpl;
import com.oxymoron.api.PageState;
import com.oxymoron.api.serializable.LocationInformation;
import com.oxymoron.api.serializable.Range;
import com.oxymoron.api.serializable.RestaurantId;
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
    private final List<RestaurantThumbnail> itemList = new ArrayList<>();

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

    public static Intent createIntent(Context packageContext, Range range, LocationInformation locationInformation) {
        final Intent intent = new Intent(packageContext, RestaurantListActivity.class);

        intent.putExtra(KEY_LOCATION_INFORMATION, locationInformation);
        intent.putExtra(KEY_RANGE, range);

        return intent;
    }

    private void prepareRecyclerView() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        this.adapter = new RestaurantListAdapter(this.itemList);
        this.adapter.setOnClickListener(thumbnail -> {
            final RestaurantId restaurantId = new RestaurantId(thumbnail.getRestaurantId());
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
        this.recyclerView = findViewById(R.id.restaurant_list_recyclerview);
    }
}
