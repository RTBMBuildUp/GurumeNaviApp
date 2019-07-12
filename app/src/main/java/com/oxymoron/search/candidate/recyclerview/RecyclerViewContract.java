package com.oxymoron.search.candidate.recyclerview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import com.oxymoron.BasePresenter;
import com.oxymoron.BaseView;
import com.oxymoron.search.candidate.data.RestaurantThumbnail;

import java.util.List;

public interface RecyclerViewContract {
    interface View extends BaseView<Presenter> {
        void setName(String name);

        void setAccess(String access);

        void setImageView(Bitmap bitmapImage);

        void startActivity(Intent intent);
    }

    interface Presenter extends BasePresenter {
        void onClick(RecyclerView.ViewHolder viewHolder, List<RestaurantThumbnail> thumbnailList);

        void onBindViewHolder(@NonNull RestaurantViewHolder restaurantViewHolder, int position, List<RestaurantThumbnail> thumbnailList);
    }
}
