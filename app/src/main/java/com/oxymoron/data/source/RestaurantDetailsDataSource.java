package com.oxymoron.data.source;

import androidx.annotation.NonNull;

import com.oxymoron.data.RestaurantDetail;
import com.oxymoron.data.room.RestaurantId;

import java.util.List;

public interface RestaurantDetailsDataSource {
    interface LoadRestaurantDetailsCallback {
        void onRestaurantDetailsLoaded(List<RestaurantDetail> restaurantDetailList);

        void onDataNotAvailable();
    }

    interface GetRestaurantDetailsCallback {
        void onRestaurantDetailLoaded(RestaurantDetail restaurantDetail);

        void onDataNotAvailable();
    }

    void getRestaurantDetails(@NonNull LoadRestaurantDetailsCallback callback);

    void getRestaurantDetail(@NonNull RestaurantId id, @NonNull GetRestaurantDetailsCallback callback);

    void saveRestaurantDetail(@NonNull RestaurantDetail restaurantDetail);

    void deleteRestaurantDetail(@NonNull RestaurantId id);

    void deleteAllRestaurantDetail();
}
