package com.oxymoron.ui.favorite;

import com.oxymoron.ui.list.data.RestaurantThumbnail;

import java.util.List;

public interface FavoriteContract {
    interface View {
        void addRecyclerView(RestaurantThumbnail item);

        void clearRecyclerView();
    }

    interface Presenter {
        void setItem(List<RestaurantThumbnail> restaurantThumbnailList, RestaurantThumbnail item);

        void clearItem(List<RestaurantThumbnail> restaurantThumbnailList);

        void clearThumbnail();

        void showThumbnails();
    }
}
