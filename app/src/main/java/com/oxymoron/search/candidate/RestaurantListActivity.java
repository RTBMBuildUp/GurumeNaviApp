package com.oxymoron.search.candidate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.gurumenaviapp.R;
import com.oxymoron.request.RequestIds;
import com.oxymoron.request.RequestMap;
import com.oxymoron.search.candidate.data.RestaurantThumbnail;
import com.oxymoron.search.candidate.recyclerview.RecyclerViewPresenter;
import com.oxymoron.search.candidate.recyclerview.RestaurantListAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.oxymoron.request.RequestIds.key_id;

public class RestaurantListActivity extends AppCompatActivity implements RestaurantListContract.View {
    private RestaurantListContract.Presenter presenter;

    private RecyclerView recyclerView;
    private RestaurantListAdapter adapter;
    private List<RestaurantThumbnail> itemList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_list);
        findViews();

        RequestMap requestMap = initialRequestMap();

        prepareRecyclerView(requestMap);

        presenter = new RestaurantListPresenter(this, this);

        String token = getIntent().getStringExtra(key_id.toString());
        presenter.search(requestMap);
    }

    @Override
    public void setPresenter(RestaurantListContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void addRecyclerViewItem(RestaurantThumbnail item) {
        presenter.setItem(itemList, item);
        adapter.notifyItemInserted(itemList.size());
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

    private RequestMap initialRequestMap() {
        RequestMap result = new RequestMap();

        final Intent intent = getIntent();
        for (RequestIds request : RequestIds.values()) {
            final String value = intent.getStringExtra(request.toString());

            if (value != null) {
                result.put(request, value);
            }
        }

        return result;
    }

    private void prepareRecyclerView(final RequestMap requestMap) {
        adapter = new RestaurantListAdapter(this.itemList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                presenter.onScrolled(recyclerView, requestMap, itemList.size());
            }
        });
    }

    private void findViews() {
        this.recyclerView = findViewById(R.id.restaurant_list_recyclerview);
    }
}
