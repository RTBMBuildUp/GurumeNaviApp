package com.oxymoron.ui.list.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gurumenaviapp.R;
import com.oxymoron.data.RestaurantThumbnail;
import com.oxymoron.ui.list.recyclerview.onclicklistener.OnClickFavoriteIconListener;
import com.oxymoron.ui.list.recyclerview.onclicklistener.OnClickItemListener;

import java.util.List;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {
    private final List<RestaurantThumbnail> restaurantThumbnailList;

    private OnClickItemListener clickItemListener;
    private OnClickFavoriteIconListener clickFavoriteIconListener;

    public RestaurantListAdapter(List<RestaurantThumbnail> restaurantThumbnailList) {
        this.restaurantThumbnailList = restaurantThumbnailList;
    }

    public void setOnClickItemListener(OnClickItemListener clickItemListener) {
        this.clickItemListener = clickItemListener;
    }

    public void setOnClickFavoriteIconListener(OnClickFavoriteIconListener clickFavoriteIconListener) {
        this.clickFavoriteIconListener = clickFavoriteIconListener;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        final View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.restaurant_item, viewGroup, false);

        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder restaurantViewHolder, int position) {
        final RestaurantThumbnail restaurantThumbnail = restaurantThumbnailList.get(position);

        restaurantViewHolder.setThumbnail(restaurantThumbnail);
        restaurantViewHolder.itemView.setOnClickListener(v -> this.clickItemListener.onClick(restaurantThumbnail));
        restaurantViewHolder.favoriteImage.setOnClickListener(v -> this.clickFavoriteIconListener.onClick((ImageView) v, restaurantThumbnail));
    }

    @Override
    public int getItemCount() {
        return restaurantThumbnailList.size();
    }
}
