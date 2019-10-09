package com.oxymoron.ui.favorite;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gurumenaviapp.R;
import com.oxymoron.data.source.local.data.RestaurantId;
import com.oxymoron.injection.Injection;
import com.oxymoron.ui.detail.RestaurantDetailActivity;
import com.oxymoron.ui.list.data.RestaurantThumbnail;
import com.oxymoron.ui.list.recyclerview.EndlessScrollListener;
import com.oxymoron.ui.list.recyclerview.RestaurantListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FavoriteFragment extends Fragment implements FavoriteContract.View {
    private FavoriteContract.Presenter presenter;

    private RecyclerView recyclerView;
    private RestaurantListAdapter adapter;
    private final List<RestaurantThumbnail> itemList = new ArrayList<>();

    private boolean firstVisit = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);
        prepareRecyclerView();

        this.presenter = new FavoritePresenter(this, Injection.provideRestaurantDetailsRepository(this.getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();

        if (this.isFirstVisit()) {
            refreshRecyclerView();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        this.visited();
    }

    @Override
    public void addRecyclerView(RestaurantThumbnail item) {
        presenter.setItem(itemList, item);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void clearRecyclerView() {
        presenter.clearItem(itemList);
        adapter.notifyDataSetChanged();
    }

    private void refreshRecyclerView() {
        presenter.clearThumbnail();
        presenter.showThumbnails();
    }

    private void prepareRecyclerView() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());

        this.adapter = new RestaurantListAdapter(this.itemList);
        this.adapter.setOnClickListener(thumbnail -> {
            final RestaurantId restaurantId = thumbnail.getId();
            final Intent intent = RestaurantDetailActivity.createIntent(FavoriteFragment.this.getContext(), restaurantId);

            startActivity(intent);
        });

        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(linearLayoutManager);
        this.recyclerView.setAdapter(adapter);
        this.recyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(this.getContext()), DividerItemDecoration.VERTICAL));

        this.recyclerView.addOnScrollListener(new EndlessScrollListener(linearLayoutManager) {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {


                return true;
            }
        });
    }

    private void findViews(View view) {
        this.recyclerView = view.findViewById(R.id.fragment_favorite_recycler_view);
    }

    private boolean isFirstVisit() {
        return this.firstVisit;
    }

    private void visited() {
        this.firstVisit = false;
    }
}
