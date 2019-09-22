package com.oxymoron.ui.detail;

import androidx.annotation.NonNull;

import com.oxymoron.api.search.RestaurantSearchApiClient;
import com.oxymoron.api.search.gson.data.RestaurantSearchResult;
import com.oxymoron.data.RestaurantDetail;
import com.oxymoron.data.room.RestaurantId;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantDetailPresenter implements RestaurantDetailContract.Presenter {
    private final RestaurantDetailContract.View view;
    private final RestaurantSearchApiClient apiClient;

    RestaurantDetailPresenter(RestaurantDetailContract.View view, RestaurantSearchApiClient apiClient) {
        this.view = view;
        this.apiClient = apiClient;
    }

    private void showDetail(RestaurantId restaurantId) {
        apiClient.loadRestaurantDetail(restaurantId, new Callback<RestaurantSearchResult>() {
            @Override
            public void onResponse(@NonNull Call<RestaurantSearchResult> call, @NonNull Response<RestaurantSearchResult> response) {
                RestaurantSearchResult body = response.body();
                if (response.isSuccessful() && body != null) {
                    body.getRest().ifPresent(restList -> {
                        List<RestaurantDetail> restaurantDetailList = RestaurantDetail.createRestaurantDetailList(restList);
                        setDetail(restaurantDetailList.get(0));
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<RestaurantSearchResult> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    public void start() {

    }

    @Override
    public void searchDetail(RestaurantId restaurantId) {
        showDetail(restaurantId);
    }

    private void setDetail(RestaurantDetail restaurantDetail) {
        view.showDetail(restaurantDetail);
    }
}
