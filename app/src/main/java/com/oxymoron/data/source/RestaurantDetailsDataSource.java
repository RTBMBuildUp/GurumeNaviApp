package com.oxymoron.data.source;

import androidx.annotation.NonNull;

import com.oxymoron.data.RestaurantDetail;
import com.oxymoron.data.room.RestaurantId;
import com.oxymoron.data.source.remote.api.PageState;
import com.oxymoron.data.source.remote.api.gson.data.RestaurantSearchResult;
import com.oxymoron.data.source.remote.api.serializable.LocationInformation;
import com.oxymoron.data.source.remote.api.serializable.Range;

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

    interface GetRestaurantSearchResultCallback {
        void onRestaurantSearchResultLoaded(RestaurantSearchResult restaurantSearchResult);

        void onDataNotAvailable();
    }

    void getRestaurantDetails(@NonNull LoadRestaurantDetailsCallback callback);

    void getRestaurantDetails(@NonNull List<RestaurantId> restaurantIdList, @NonNull LoadRestaurantDetailsCallback callback);

    void getRestaurantDetails(@NonNull Range range, @NonNull LocationInformation locationInformation, @NonNull GetRestaurantSearchResultCallback callback);

    void getRestaurantDetails(@NonNull Range range, @NonNull LocationInformation locationInformation,
                              @NonNull PageState pageState, @NonNull GetRestaurantSearchResultCallback callback);

    void getRestaurantDetail(@NonNull RestaurantId id, @NonNull GetRestaurantDetailsCallback callback);

    void saveRestaurantDetail(@NonNull RestaurantDetail restaurantDetail);

    void deleteRestaurantDetail(@NonNull RestaurantId id);

    void deleteAllRestaurantDetail();
}
