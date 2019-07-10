package com.oxymoron.search.detail;

import com.oxymoron.BasePresenter;
import com.oxymoron.BaseView;
import com.oxymoron.search.detail.data.RestaurantDetail;

public interface RestaurantDetailContract {
    interface View extends BaseView<Presenter> {
        void showDetail(RestaurantDetail detail);
    }

    interface Presenter extends BasePresenter {
        void searchDetail(String restaurantId);
    }
}
