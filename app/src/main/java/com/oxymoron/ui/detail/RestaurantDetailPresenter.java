package com.oxymoron.ui.detail;

import com.oxymoron.api.GurumeNaviApiClient;
import com.oxymoron.api.gson.data.Rest;
import com.oxymoron.ui.detail.data.RestaurantDetail;

import java.util.List;

public class RestaurantDetailPresenter implements RestaurantDetailContract.Presenter {
    private RestaurantDetailContract.View view;
    private GurumeNaviApiClient apiClient;

    RestaurantDetailPresenter(RestaurantDetailContract.View view, GurumeNaviApiClient apiClient) {
        this.view = view;
        this.apiClient = apiClient;
    }

    private void showDetail(String restaurantId) {
        apiClient.loadRestaurantDetail(restaurantId, parsedObj -> {
            List<Rest> restaurantList = parsedObj.getRest();
            RestaurantDetail restaurantDetail = new RestaurantDetail(restaurantList.get(0));
            setDetail(restaurantDetail);
        });
    }

    @Override
    public void start() {

    }

    @Override
    public void searchDetail(String restaurantId) {
        showDetail(restaurantId);
    }

    private void setDetail(RestaurantDetail detail) {
        view.showDetail(detail);
    }
}
