package com.example.gurumenaviapp.search.detail;

import com.example.gurumenaviapp.BasePresenter;
import com.example.gurumenaviapp.BaseView;
import com.example.gurumenaviapp.search.detail.data.RestaurantDetail;

public interface RestaurantDetailContract {
    interface View extends BaseView<Presenter> {
        void setDetail(RestaurantDetail detail);
    }

    interface Presenter extends BasePresenter {
        void searchDetail(String restaurantId);
    }
}
