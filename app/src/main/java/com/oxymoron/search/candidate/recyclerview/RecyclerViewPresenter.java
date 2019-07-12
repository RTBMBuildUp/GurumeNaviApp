package com.oxymoron.search.candidate.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import com.example.gurumenaviapp.R;
import com.oxymoron.gson.data.Access;
import com.oxymoron.request.RequestIds;
import com.oxymoron.search.candidate.data.RestaurantThumbnail;
import com.oxymoron.search.detail.RestaurantDetailActivity;
import com.oxymoron.util.Optional;

import java.util.List;

public class RecyclerViewPresenter implements RecyclerViewContract.Presenter {
    private Context context;
    private RecyclerViewContract.View view;

    public RecyclerViewPresenter(Context context, RecyclerViewContract.View view) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void onClick(RecyclerView.ViewHolder viewHolder, List<RestaurantThumbnail> thumbnailList) {
        int position = viewHolder.getAdapterPosition();
        RestaurantThumbnail restaurantThumbnail = thumbnailList.get(position);

        Intent intent = new Intent(context, RestaurantDetailActivity.class);
        intent.putExtra(RequestIds.restaurant_id.toString(), restaurantThumbnail.getRestaurantId());
        view.startActivity(intent);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder restaurantViewHolder, int position, List<RestaurantThumbnail> thumbnailList) {
        final Resources resources = this.context.getResources();

        final String notFound = resources.getString(R.string.not_found);

        final RestaurantThumbnail result = thumbnailList.get(position);
        final Access access = Optional.of(result.getAccess()).getOrElse(new Access());
        final Bitmap notFountImage = BitmapFactory.decodeResource(
                this.context.getResources(), R.drawable.default_not_found
        );

        view.setName(Optional.of(result.getName()).getOrElse(notFound));
        view.setAccess(Optional.of(access.showUserAround()).getOrElse(notFound));
        view.setImageView(Optional.of(result.getImage()).getOrElse(notFountImage));
    }

    @Override
    public void start() {

    }
}
