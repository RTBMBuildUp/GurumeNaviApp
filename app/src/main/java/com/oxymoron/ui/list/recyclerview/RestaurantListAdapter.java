package com.oxymoron.ui.list.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gurumenaviapp.R;
import com.oxymoron.data.RestaurantThumbnail;
import com.oxymoron.ui.list.recyclerview.onclicklistener.OnClickItemListener;
import com.oxymoron.ui.list.recyclerview.onclicklistener.OnClickSafelyListener;

import java.util.List;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {
    private final List<RestaurantThumbnail> restaurantThumbnailList;

    private OnClickItemListener clickItemListener;
    private OnClickSafelyListener clickSafelyListener;

    public RestaurantListAdapter(List<RestaurantThumbnail> restaurantThumbnailList) {
        this.restaurantThumbnailList = restaurantThumbnailList;
    }

    public void setOnClickItemListener(OnClickItemListener clickItemListener) {
        this.clickItemListener = clickItemListener;
    }

    public void setOnClickSafelyListener(OnClickSafelyListener clickSafelyListener) {
        this.clickSafelyListener = clickSafelyListener;
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
        restaurantViewHolder.imageView.setOnClickListener(v -> this.clickSafelyListener.onClick(restaurantThumbnail));
    }


    @Override
    public int getItemCount() {
        return restaurantThumbnailList.size();
    }
}
