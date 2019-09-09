package com.oxymoron.data.source;

import androidx.annotation.NonNull;

import com.oxymoron.data.RestaurantDetail;
import com.oxymoron.data.room.RestaurantId;

import java.util.List;

public interface RestaurantDetailsDataSource {
    public interface LoadRestaurantDetailsCallback {
        public void onRestaurantDetailsLoaded(List<RestaurantDetail> restaurantDetailList);

        public void onDataNotAvailable();
    }

    public interface GetRestaurantDetailsCallback {
        public void onRestaurantDetailLoaded(RestaurantDetail restaurantDetail);

        public void onDataNotAvailable();
    }

    public void getRestaurantDetails(@NonNull LoadRestaurantDetailsCallback callback);

    public void getRestaurantDetail(@NonNull RestaurantId id, @NonNull GetRestaurantDetailsCallback callback);

    public void saveRestaurantDetail(@NonNull RestaurantDetail restaurantDetail);

    public void deleteRestaurantDetail(@NonNull RestaurantId id);

    public void deleteAllRestaurantDetail();
}
