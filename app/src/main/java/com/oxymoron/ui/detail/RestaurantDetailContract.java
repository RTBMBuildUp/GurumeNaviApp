package com.oxymoron.ui.detail;

import com.oxymoron.BasePresenter;
import com.oxymoron.BaseView;
import com.oxymoron.api.serializable.RestaurantId;
import com.oxymoron.data.RestaurantDetail;

public interface RestaurantDetailContract {
    interface View extends BaseView<Presenter> {
        void showDetail(RestaurantDetail detail);
    }

    interface Presenter extends BasePresenter {
        void searchDetail(RestaurantId restaurantId);
    }
}
