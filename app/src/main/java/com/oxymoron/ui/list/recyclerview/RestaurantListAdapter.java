package com.oxymoron.ui.list.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gurumenaviapp.R;
import com.oxymoron.data.RestaurantThumbnail;
import com.oxymoron.ui.list.recyclerview.onclicklistener.OnClickItemListener;
import com.oxymoron.ui.list.recyclerview.onclicklistener.OnClickedListener;
import com.oxymoron.ui.list.recyclerview.onclicklistener.OnClickedSafelyListener;

import java.util.List;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {
    private final List<RestaurantThumbnail> restaurantThumbnailList;

    private OnClickItemListener onClickItemListener;
    private OnUpdateFavorites onUpdateFavorites;
    private OnClickedListener<RestaurantThumbnail> onClickFavoritesListener;

    public RestaurantListAdapter(List<RestaurantThumbnail> restaurantThumbnailList) {
        this.restaurantThumbnailList = restaurantThumbnailList;
    }

    public void setOnClickItemListener(OnClickItemListener clickItemListener) {
        this.onClickItemListener = clickItemListener;
    }

    public void setOnClickFavoritesListener(OnClickedListener<RestaurantThumbnail> onClickFavoritesListener) {
        this.onClickFavoritesListener = onClickFavoritesListener;
    }

    public void setOnUpdateFavorites(OnUpdateFavorites updateFavorites) {
        this.onUpdateFavorites = updateFavorites;
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
        final OnClickedSafelyListener<RestaurantThumbnail> onClickedSafelyListener =
                new OnClickedSafelyListener<RestaurantThumbnail>() {
                    @Override
                    public void onClicked(RestaurantThumbnail restaurantThumbnail) {
                        onClickFavoritesListener.onClicked(restaurantThumbnail);
                    }
                };

        restaurantViewHolder.setThumbnail(restaurantThumbnail);

        restaurantViewHolder.itemView.setOnClickListener(v ->
                this.onClickItemListener.onClick(restaurantThumbnail));

        restaurantViewHolder.favoriteIcon.setOnClickListener(v -> {
            onClickedSafelyListener.onClick(restaurantThumbnail);

            this.onUpdateFavorites.onUpdateFavorites(
                    restaurantThumbnail,
                    restaurantViewHolder.favoriteIcon
            );
        });
    }


    @Override
    public int getItemCount() {
        return restaurantThumbnailList.size();
    }
}
