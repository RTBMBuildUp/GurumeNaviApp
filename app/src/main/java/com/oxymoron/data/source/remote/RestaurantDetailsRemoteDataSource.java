package com.oxymoron.data.source.remote;

import androidx.annotation.NonNull;

import com.oxymoron.data.RestaurantDetail;
import com.oxymoron.data.source.RestaurantDetailsDataSource;
import com.oxymoron.data.source.local.data.RestaurantId;
import com.oxymoron.data.source.remote.api.PageState;
import com.oxymoron.data.source.remote.api.RestaurantSearchApiClient;
import com.oxymoron.data.source.remote.api.gson.data.RestaurantSearchResult;
import com.oxymoron.data.source.remote.api.serializable.LocationInformation;
import com.oxymoron.data.source.remote.api.serializable.Range;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantDetailsRemoteDataSource implements RestaurantDetailsDataSource {
    private static RestaurantDetailsRemoteDataSource INSTANCE;

    private final RestaurantSearchApiClient restaurantSearchApiClient;

    public static RestaurantDetailsRemoteDataSource getInstance(RestaurantSearchApiClient apiClient) {
        if (INSTANCE == null) {
            INSTANCE = new RestaurantDetailsRemoteDataSource(apiClient);
        }
        return INSTANCE;
    }

    private RestaurantDetailsRemoteDataSource(RestaurantSearchApiClient restaurantSearchApiClient) {
        this.restaurantSearchApiClient = restaurantSearchApiClient;
    }

    @Override
    public void getRestaurantDetails(@NonNull LoadRestaurantDetailsCallback callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void getRestaurantDetails(@NonNull List<RestaurantId> restaurantIdList, @NonNull LoadRestaurantDetailsCallback callback) {
        this.restaurantSearchApiClient.loadRestaurantDetails(restaurantIdList, new Callback<RestaurantSearchResult>() {
            @Override
            public void onResponse(@NonNull Call<RestaurantSearchResult> call, @NonNull Response<RestaurantSearchResult> response) {
                RestaurantSearchResult body = response.body();
                if (body != null && response.isSuccessful()) {
                    body.getRest().ifPresent(restList -> {
                        List<RestaurantDetail> restaurantDetailList = RestaurantDetail.createRestaurantDetailList(restList);

                        callback.onRestaurantDetailsLoaded(restaurantDetailList);
                    });
                } else {
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RestaurantSearchResult> call, @NonNull Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getRestaurantDetails(@NonNull Range range, @NonNull LocationInformation locationInformation,
                                     @NonNull GetRestaurantSearchResultCallback callback) {

        this.restaurantSearchApiClient.loadRestaurantDetails(range, locationInformation, new Callback<RestaurantSearchResult>() {
            @Override
            public void onResponse(@NonNull Call<RestaurantSearchResult> call, @NonNull Response<RestaurantSearchResult> response) {
                if (response.isSuccessful()) {
                    callback.onRestaurantSearchResultLoaded(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<RestaurantSearchResult> call, @NonNull Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getRestaurantDetails(@NonNull Range range, @NonNull LocationInformation locationInformation,
                                     @NonNull PageState pageState, @NonNull GetRestaurantSearchResultCallback callback) {

        this.restaurantSearchApiClient.loadRestaurantDetails(range, locationInformation, pageState, new Callback<RestaurantSearchResult>() {
                    @Override
                    public void onResponse(@NonNull Call<RestaurantSearchResult> call, @NonNull Response<RestaurantSearchResult> response) {
                        if (response.isSuccessful()) {
                            callback.onRestaurantSearchResultLoaded(response.body());
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
    public void getRestaurantDetail(@NonNull RestaurantId id, @NonNull GetRestaurantDetailsCallback callback) {
        this.restaurantSearchApiClient.loadRestaurantDetail(id, new Callback<RestaurantSearchResult>() {
                    @Override
                    public void onResponse(@NonNull Call<RestaurantSearchResult> call, @NonNull Response<RestaurantSearchResult> response) {
                        RestaurantSearchResult body = response.body();
                        if (response.isSuccessful() && body != null) {
                            body.getRest().ifPresent(restList -> {
                                List<RestaurantDetail> restaurantDetailList = RestaurantDetail.createRestaurantDetailList(restList);

                                callback.onRestaurantDetailLoaded(restaurantDetailList.get(0));
                            });
                        } else {
                            callback.onDataNotAvailable();
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
