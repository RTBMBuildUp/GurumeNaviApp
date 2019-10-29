package com.oxymoron.ui.list.recyclerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gurumenaviapp.R;
import com.oxymoron.data.RestaurantThumbnail;

class RestaurantViewHolder extends RecyclerView.ViewHolder {
    private Context context;

    TextView name;
    TextView access;

    ImageView thumbnailImage;
    ImageView favoriteImage;

    RestaurantViewHolder(View view) {
        super(view);
        this.context = view.getContext();

        this.name = view.findViewById(R.id.restaurant_item_name);
        this.access = view.findViewById(R.id.restaurant_item_access);

        this.thumbnailImage = view.findViewById(R.id.restaurant_item_thumbnail);
        this.favoriteImage = view.findViewById(R.id.restaurant_item_favorite_image);
    }

    void setThumbnail(RestaurantThumbnail thumbnail) {
        final Bitmap notFoundImage = BitmapFactory.decodeResource(
                this.context.getResources(), R.drawable.default_not_found
        );

        this.setName(thumbnail.getName());

        this.setAccess(thumbnail.getAccess().getOrElse(""));

        thumbnail.getImageUrl().ifPresentOrElse(
                this::setThumbnailImage,
                () -> this.thumbnailImage.setImageBitmap(notFoundImage)
        );

        this.favoriteImage.setImageResource(thumbnail.isFavorite() ? R.drawable.ic_favorite_pink_24dp : R.drawable.ic_favorite_border_gray_24dp);
//        this.favoriteImage.setOnClickListener(v -> {
//            ImageView favoriteImage = (ImageView) v;
//
//            switchImage(thumbnail, favoriteImage);
//        });
    }

    private void setName(String name) {
        this.name.setText(name);
    }

    private void setAccess(String access) {
        this.access.setText(access);
    }

    private void setThumbnailImage(String imageUrl) {
        Glide.with(context).load(imageUrl).into(thumbnailImage);
    }

//    private void switchImage(RestaurantThumbnail restaurantThumbnail, ImageView favoriteImage) {
//        final int favoriteBorder = R.drawable.ic_favorite_border_gray_24dp;
//        final int favorite = R.drawable.ic_favorite_pink_24dp;
//
//        if (restaurantThumbnail.isFavorite()) {
//            restaurantThumbnail.removeFromFavorities();
//
//            favoriteImage.setImageResource(favoriteBorder);
//        } else {
//            restaurantThumbnail.addToFavorities();
//
//            favoriteImage.setImageResource(favorite);
//            favoriteImage.startAnimation(animation);
//        }
//    }
}
