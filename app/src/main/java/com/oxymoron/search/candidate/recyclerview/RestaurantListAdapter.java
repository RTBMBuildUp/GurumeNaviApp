package com.oxymoron.search.candidate.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.gurumenaviapp.R;
import com.oxymoron.request.RequestIds;
import com.oxymoron.gson.data.Access;
import com.oxymoron.search.candidate.RestaurantListContract;
import com.oxymoron.search.candidate.data.RestaurantThumbnail;
import com.oxymoron.search.detail.RestaurantDetailActivity;
import com.oxymoron.util.Optional;

import java.util.List;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {
    private List<RestaurantThumbnail> restaurantThumbnailList;

    private View view;
    private RecyclerViewContract.Presenter presenter;

    public RestaurantListAdapter(List<RestaurantThumbnail> restaurantThumbnailList) {
        this.restaurantThumbnailList = restaurantThumbnailList;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.restaurant_item, viewGroup, false);

        final RestaurantViewHolder viewHolder = new RestaurantViewHolder(view);

        this.presenter = new RecyclerViewPresenter(view.getContext(), viewHolder);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onClick(viewHolder, restaurantThumbnailList);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder restaurantViewHolder, int position) {
        presenter.onBindViewHolder(restaurantViewHolder, position, restaurantThumbnailList);
    }

    @Override
    public int getItemCount() {
        return restaurantThumbnailList.size();
    }
}
