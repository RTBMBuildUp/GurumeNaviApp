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
import com.oxymoron.search.candidate.data.RestaurantThumbnail;
import com.oxymoron.search.detail.RestaurantDetailActivity;
import com.oxymoron.util.Optional;

import java.util.List;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {
    private List<RestaurantThumbnail> restaurantThumbnailList;

    private View view;

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

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                RestaurantThumbnail restaurantThumbnail = restaurantThumbnailList.get(position);
                System.out.println(position + " " + restaurantThumbnail);

                Context context = view.getContext();

                Intent intent = new Intent(context, RestaurantDetailActivity.class);
                intent.putExtra(RequestIds.id.toString(), restaurantThumbnail.getRestaurantId());
                context.startActivity(intent);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder restaurantViewHolder, int position) {
        final Resources resources = view.getResources();
        final String notFound = resources.getString(R.string.not_found);
        final Bitmap notFountImage = BitmapFactory.decodeResource(
                view.getResources(), R.drawable.default_not_found
        );

        final RestaurantThumbnail result = restaurantThumbnailList.get(position);
        final Access access = Optional.of(result.getAccess()).getOrElse(new Access());

        restaurantViewHolder.name.setText(
                Optional.of(result.getName()).getOrElse(notFound)
        );

        restaurantViewHolder.access.setText(
                Optional.of(access.showUserAround()).getOrElse(notFound)
        );

        restaurantViewHolder.imageView.setImageBitmap(
                Optional.of(result.getImage()).getOrElse(notFountImage)
        );
    }

    @Override
    public int getItemCount() {
        return restaurantThumbnailList.size();
    }
}
