package com.example.gurumenaviapp.search.candidate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.gurumenaviapp.R;
import com.example.gurumenaviapp.data.request.Request;
import com.example.gurumenaviapp.data.request.Requests;
import com.example.gurumenaviapp.search.candidate.data.RestaurantThumbnail;
import com.example.gurumenaviapp.search.candidate.recyclerview.RestaurantListAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.example.gurumenaviapp.data.request.Requests.keyid;

public class RestaurantListActivity extends AppCompatActivity implements RestaurantListContract.View {
    private RestaurantListContract.Presenter presenter;

    private RecyclerView recyclerView;
    private RestaurantListAdapter adapter;
    private List<RestaurantThumbnail> itemList = new ArrayList<>();

    private List<Request> requestList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_list);
        findViews();

        requestList = initialRequestList();

        prepareRecyclerView();

        presenter = new RestaurantListPresenter(this, this);

        String token = getIntent().getStringExtra(keyid.toString());
        presenter.searchWithRequest(requestList);
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

    private List<Request> initialRequestList() {
        List<Request> initialList = new ArrayList<>();

        final Intent intent = getIntent();
        for (Requests request : Requests.values()) {
            final String value = intent.getStringExtra(request.toString());

            if (value != null) {
                initialList.add(new Request(request, value));
            }
        }

        return initialList;
    }

    private void prepareRecyclerView() {
        adapter = new RestaurantListAdapter(itemList);

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
                presenter.onScrolled(recyclerView, requestList, itemList.size());
            }
        });
    }

    private void findViews() {
        this.recyclerView = findViewById(R.id.restaurant_list_recyclerview);
    }
}
