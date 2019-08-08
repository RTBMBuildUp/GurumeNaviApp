package com.oxymoron.search.candidate.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.gurumenaviapp.R;
import com.oxymoron.gson.data.Access;
import com.oxymoron.search.candidate.data.RestaurantThumbnail;
import com.oxymoron.util.Function;

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
        final Bitmap notFoundImage = BitmapFactory.decodeResource(
                this.context.getResources(), R.drawable.default_not_found
        );

        this.setName(thumbnail.getName());

        this.setAccess(thumbnail.getAccess().map(new Function<Access, String>() {
            @Override
            public String apply(Access value) {
                return value.showUserAround();
            }
        }).getOrElse(""));

        this.setImageView(thumbnail.getImage().getOrElse(notFoundImage));
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
