package com.oxymoron.data.source.remote;

import androidx.annotation.NonNull;

import com.oxymoron.data.RestaurantDetail;
import com.oxymoron.data.room.RestaurantId;
import com.oxymoron.data.source.RestaurantDetailsDataSource;
import com.oxymoron.data.source.remote.api.RestaurantSearchApiClient;
import com.oxymoron.data.source.remote.api.gson.data.RestaurantSearchResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        this.apiClient.loadRestaurantDetail(id, new Callback<RestaurantSearchResult>() {
                    @Override
                    public void onResponse(@NonNull Call<RestaurantSearchResult> call, @NonNull Response<RestaurantSearchResult> response) {
                        RestaurantSearchResult body = response.body();
                        if (response.isSuccessful() && body != null) {
                            body.getRest().ifPresent(restList -> {
                                List<RestaurantDetail> restaurantDetailList = RestaurantDetail.createRestaurantDetailList(restList);
                                callback.onRestaurantDetailLoaded(restaurantDetailList.get(0));
                            });
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<RestaurantSearchResult> call, @NonNull Throwable t) {
                        callback.onDataNotAvailable();
                    }
                }
        );
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
