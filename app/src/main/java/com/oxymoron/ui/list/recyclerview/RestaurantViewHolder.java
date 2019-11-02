package com.oxymoron.ui.list.recyclerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gurumenaviapp.R;
import com.oxymoron.data.RestaurantThumbnail;

class RestaurantViewHolder extends RecyclerView.ViewHolder {
    private final static int favoriteBorderId = R.drawable.ic_favorite_border_gray_24dp;
    private final static int favoritePinkId = R.drawable.ic_favorite_pink_24dp;

    private final Context context;
    private final Animation animation;

    TextView name;
    TextView access;

    ImageView thumbnailImage;
    ImageView imageView;

    RestaurantViewHolder(View view) {
        super(view);

        this.context = view.getContext();
        this.animation = AnimationUtils.loadAnimation(this.context, R.anim.favorite_animation);

        this.name = view.findViewById(R.id.restaurant_item_name);
        this.access = view.findViewById(R.id.restaurant_item_access);

        this.thumbnailImage = view.findViewById(R.id.restaurant_item_thumbnail);
        this.imageView = view.findViewById(R.id.restaurant_item_favorite_image);
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

        this.setFavoriteIcon(thumbnail);
        thumbnail.setOnUpdateFavorites(isFavorite -> {
            if (isFavorite)
                this.imageView.startAnimation(animation);

            this.setFavoriteIcon(thumbnail);
        });
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

    private void setFavoriteIcon(RestaurantThumbnail restaurantThumbnail) {
        this.imageView.setImageResource(restaurantThumbnail.isFavorite() ? favoritePinkId : favoriteBorderId);
    }

}
