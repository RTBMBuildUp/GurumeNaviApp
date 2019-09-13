package com.oxymoron.ui.detail;

import com.oxymoron.api.RestaurantDetail;
import com.oxymoron.api.search.RestaurantSearchApiClient;
import com.oxymoron.api.search.serializable.RestaurantId;

public class RestaurantDetailPresenter implements RestaurantDetailContract.Presenter {
    private final RestaurantDetailContract.View view;
    private final RestaurantSearchApiClient apiClient;

    RestaurantDetailPresenter(RestaurantDetailContract.View view, RestaurantSearchApiClient apiClient) {
        this.view = view;
        this.apiClient = apiClient;
    }

    private void showDetail(RestaurantId restaurantId) {
        apiClient.loadRestaurantDetail(restaurantId, parsedObj ->
                parsedObj.getRest().ifPresent(list -> {
                    RestaurantDetail restaurantDetail = new RestaurantDetail(list.get(0));
                    setDetail(restaurantDetail);
                }));
    }

    @Override
    public void start() {

    }

    @Override
    public void searchDetail(RestaurantId restaurantId) {
        showDetail(restaurantId);
    }

    private void setDetail(RestaurantDetail detail) {
        view.showDetail(detail);
    }
}
