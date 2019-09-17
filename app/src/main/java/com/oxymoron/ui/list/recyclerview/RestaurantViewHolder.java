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
import com.oxymoron.api.search.gson.data.Access;
import com.oxymoron.ui.list.data.RestaurantThumbnail;

class RestaurantViewHolder extends RecyclerView.ViewHolder {
    private Context context;

    TextView name;
    TextView access;

    ImageView thumbnailImage;

    RestaurantViewHolder(View view) {
        super(view);
        this.context = view.getContext();

        this.name = view.findViewById(R.id.restaurant_item_name);
        this.access = view.findViewById(R.id.restaurant_item_access);

        this.thumbnailImage = view.findViewById(R.id.restaurant_item_thumbnail);
    }

    void setThumbnail(RestaurantThumbnail thumbnail) {
        final Bitmap notFoundImage = BitmapFactory.decodeResource(
                this.context.getResources(), R.drawable.default_not_found
        );

        this.setName(thumbnail.getName());

        this.setAccess(thumbnail.getAccess().map(Access::showUserAround).getOrElse(""));

        thumbnail.getImageUrl().ifPresentOrElse(
                this::setThumbnailImage,
                () -> this.thumbnailImage.setImageBitmap(notFoundImage)
        );
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
}
