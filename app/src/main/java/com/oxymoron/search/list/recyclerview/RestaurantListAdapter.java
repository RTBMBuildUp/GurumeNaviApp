package com.oxymoron.search.list.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gurumenaviapp.R;
import com.oxymoron.search.list.OnClickListener;
import com.oxymoron.search.list.data.RestaurantThumbnail;

import java.util.List;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {
    private List<RestaurantThumbnail> restaurantThumbnailList;

    private OnClickListener clickListener;

    public RestaurantListAdapter(List<RestaurantThumbnail> restaurantThumbnailList) {
        this.restaurantThumbnailList = restaurantThumbnailList;
    }

    public void setOnClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.restaurant_item, viewGroup, false);

        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder restaurantViewHolder, int position) {
        final RestaurantThumbnail thumbnail = restaurantThumbnailList.get(position);

        restaurantViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(thumbnail);
            }
        });

        restaurantViewHolder.setThumbnail(thumbnail);
    }

    @Override
    public int getItemCount() {
        return restaurantThumbnailList.size();
    }
}
