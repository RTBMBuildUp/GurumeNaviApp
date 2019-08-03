package com.oxymoron.search.candidate.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.gurumenaviapp.R;
import com.oxymoron.search.candidate.data.RestaurantThumbnail;

public class RestaurantViewHolder extends RecyclerView.ViewHolder implements RecyclerViewContract.View {
    private Context context;

    TextView name;
    TextView access;

    ImageView imageView;

    RestaurantViewHolder(View view) {
        super(view);
        this.context = view.getContext();


        this.name = view.findViewById(R.id.restaurant_item_name);
        this.access = view.findViewById(R.id.restaurant_item_access);

        this.imageView = view.findViewById(R.id.restaurant_item_image);
    }

    @Override
    public void setThumbnail(RestaurantThumbnail thumbnail) {
        final String notFound = this.context.getResources().getString(R.string.not_found);

        setName(thumbnail.getName());
        setAccess(thumbnail.getAccess().showUserAround());
        setImageView(thumbnail.getImage());
    }

    @Override
    public void startActivity(Intent intent) {
        this.context.startActivity(intent);
    }

    @Override
    public void setPresenter(RecyclerViewContract.Presenter presenter) {

    }

    private void setName(String name) {
        this.name.setText(name);
    }

    private void setAccess(String access) {
        this.access.setText(access);
    }

    private void setImageView(Bitmap bitmapImage) {
        this.imageView.setImageBitmap(bitmapImage);
    }
}
