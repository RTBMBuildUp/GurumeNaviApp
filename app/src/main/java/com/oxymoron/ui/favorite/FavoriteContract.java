package com.oxymoron.ui.favorite;

import com.oxymoron.ui.list.data.RestaurantThumbnail;

import java.util.List;

public interface FavoriteContract {
    public interface View {
        void addRecyclerView(RestaurantThumbnail item);
    }

    public interface Presenter {
        void setItem(List<RestaurantThumbnail> restaurantThumbnailList, RestaurantThumbnail item);

        void showThumbnails();
    }
}
