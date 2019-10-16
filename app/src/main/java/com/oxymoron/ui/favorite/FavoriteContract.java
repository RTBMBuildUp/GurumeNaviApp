package com.oxymoron.ui.favorite;

import android.view.animation.Animation;
import android.widget.ImageView;

import com.oxymoron.data.source.local.data.RestaurantId;
import com.oxymoron.ui.list.data.RestaurantThumbnail;

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

        void saveFavoriteRestaurants(List<RestaurantThumbnail> restaurantThumbnailList);

        void onClickItem(RestaurantThumbnail restaurantThumbnail);

        void onClickFavoriteIcon(ImageView favoriteIcon, RestaurantThumbnail restaurantThumbnail, Animation animation);

        void onLoadMore(int page, int totalItemsCount);
    }
}
