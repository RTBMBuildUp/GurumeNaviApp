package com.oxymoron.search.candidate.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.gurumenaviapp.R;

class RestaurantViewHolder extends RecyclerView.ViewHolder {
    TextView name;
    TextView access;

    ImageView imageView;

    RestaurantViewHolder(View view) {
        super(view);

        name = view.findViewById(R.id.restaurant_item_name);
        access = view.findViewById(R.id.restaurant_item_access);

        imageView = view.findViewById(R.id.restaurant_item_image);
    }
}
