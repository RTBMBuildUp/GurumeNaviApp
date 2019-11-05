package com.oxymoron.ui.favorite;

import android.view.animation.Animation;
import android.widget.ImageView;

import com.oxymoron.data.RestaurantThumbnail;
import com.oxymoron.data.source.local.data.RestaurantId;

import java.util.List;

public interface FavoriteContract {
    interface View {
        void addRecyclerView(RestaurantThumbnail item);

        void clearRecyclerView();

        void startRestaurantDetailActivity(RestaurantId restaurantId);
    }

    interface Presenter {
        void setItem(List<RestaurantThumbnail> restaurantThumbnailList, RestaurantThumbnail item);

        void clearItem(List<RestaurantThumbnail> restaurantThumbnailList);

        void clearThumbnail();

        void showThumbnails();

        void onClickItem(RestaurantThumbnail restaurantThumbnail);

        void onClickFavoriteIcon(RestaurantThumbnail restaurantThumbnail);

        void onUpdateFavorites(RestaurantThumbnail restaurantThumbnail, ImageView favoriteIcon, Animation animation);

        void onLoadMore(int page, int totalItemsCount);
    }
}
