package com.oxymoron.ui.favorite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gurumenaviapp.R;
import com.oxymoron.data.RestaurantThumbnail;
import com.oxymoron.data.source.local.data.RestaurantId;
import com.oxymoron.injection.Injection;
import com.oxymoron.ui.detail.RestaurantDetailActivity;
import com.oxymoron.ui.list.recyclerview.EndlessScrollListener;
import com.oxymoron.ui.list.recyclerview.RestaurantListAdapter;
import com.oxymoron.ui.list.recyclerview.onclicklistener.OnClickSafelyListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FavoriteFragment extends Fragment implements FavoriteContract.View {
    private Context context;

    private boolean firstVisit = true;

    private RecyclerView recyclerView;
    private RestaurantListAdapter adapter;
    private final List<RestaurantThumbnail> itemList = new ArrayList<>();

    private FavoriteContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.context = this.getContext();
        this.presenter = new FavoritePresenter(this, Injection.provideRestaurantDetailsRepository(this.context));

        findViews(view);
        prepareRecyclerView();
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

    @Override
    public void startRestaurantDetailActivity(RestaurantId restaurantId) {
        final Intent intent = RestaurantDetailActivity.createIntent(this.context, restaurantId);

        this.startActivity(intent);
    }

    private void refreshRecyclerView() {
        presenter.clearThumbnail();
        presenter.showThumbnails();
    }

    private void prepareRecyclerView() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context);

        this.adapter = new RestaurantListAdapter(this.itemList);

        this.adapter.setOnClickItemListener(this.presenter::onClickItem);
        this.adapter.setOnClickSafelyListener(new OnClickSafelyListener() {
            @Override
            public void onClicked(RestaurantThumbnail restaurantThumbnail) {
                presenter.onClickFavoriteIcon(restaurantThumbnail);
            }
        });
        this.adapter.setOnUpdateFavorites((restaurantThumbnail, favoriteIcon) ->
                this.presenter.onUpdateFavorites(
                        restaurantThumbnail,
                        favoriteIcon,
                        AnimationUtils.loadAnimation(this.context, R.anim.favorite_animation)
                )
        );

        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(linearLayoutManager);
        this.recyclerView.setAdapter(adapter);
        this.recyclerView.addItemDecoration(new DividerItemDecoration(
                Objects.requireNonNull(this.context),
                DividerItemDecoration.VERTICAL)
        );

        this.recyclerView.addOnScrollListener(new EndlessScrollListener(linearLayoutManager) {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                presenter.onLoadMore(page, totalItemsCount);

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
