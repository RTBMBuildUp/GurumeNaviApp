package com.oxymoron.ui.detail;

import com.oxymoron.data.RestaurantDetail;
import com.oxymoron.data.source.RestaurantDetailsDataSource;
import com.oxymoron.data.source.RestaurantDetailsRepository;
import com.oxymoron.data.source.local.data.RestaurantId;

public class RestaurantDetailPresenter implements RestaurantDetailContract.Presenter {
    private final RestaurantDetailContract.View view;
    private final RestaurantDetailsRepository restaurantDetailsRepository;

    RestaurantDetailPresenter(RestaurantDetailContract.View view, RestaurantDetailsRepository restaurantDetailsRepository) {
        this.view = view;
        this.restaurantDetailsRepository = restaurantDetailsRepository;
    }

    private void showDetail(RestaurantId restaurantId) {
        this.restaurantDetailsRepository.getRestaurantDetail(restaurantId, new RestaurantDetailsDataSource.GetRestaurantDetailsCallback() {
            @Override
            public void onRestaurantDetailLoaded(RestaurantDetail restaurantDetail) {
                setDetail(restaurantDetail);
            }

            @Override
            public void onDataNotAvailable() {

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
