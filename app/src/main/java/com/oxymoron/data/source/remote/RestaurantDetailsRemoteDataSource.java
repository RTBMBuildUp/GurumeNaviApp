package com.oxymoron.data.source.remote;

import androidx.annotation.NonNull;

import com.oxymoron.api.search.RestaurantSearchApiClient;
import com.oxymoron.data.RestaurantDetail;
import com.oxymoron.data.room.RestaurantId;
import com.oxymoron.data.source.RestaurantDetailsDataSource;

public class RestaurantDetailsRemoteDataSource implements RestaurantDetailsDataSource {
    private static RestaurantDetailsRemoteDataSource INSTANCE;

    private final RestaurantSearchApiClient apiClient;

    public static RestaurantDetailsRemoteDataSource getInstance(RestaurantSearchApiClient apiClient) {
        if (INSTANCE == null) {
            INSTANCE = new RestaurantDetailsRemoteDataSource(apiClient);
        }
        return INSTANCE;
    }

    private RestaurantDetailsRemoteDataSource(RestaurantSearchApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Override
    public void getRestaurantDetails(@NonNull LoadRestaurantDetailsCallback callback) {

    }

    @Override
    public void getRestaurantDetail(@NonNull RestaurantId id, @NonNull GetRestaurantDetailsCallback callback) {
        this.apiClient.loadRestaurantDetail(new com.oxymoron.api.search.serializable.RestaurantId(id.getId()),
                gurumeNavi -> gurumeNavi.getRest().ifPresentOrElse(
                        restaurantList -> {
                            RestaurantDetail restaurantDetail =
                                    new RestaurantDetail(restaurantList.get(0));

                            callback.onRestaurantDetailLoaded(restaurantDetail);
                        }, callback::onDataNotAvailable));
    }

    @Override
    public void saveRestaurantDetail(@NonNull RestaurantDetail restaurantDetail) {

    }

    @Override
    public void deleteRestaurantDetail(@NonNull RestaurantId id) {

    }

    @Override
    public void deleteAllRestaurantDetail() {

    }
}
