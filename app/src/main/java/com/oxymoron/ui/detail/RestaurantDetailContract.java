package com.oxymoron.ui.detail;

import com.oxymoron.BasePresenter;
import com.oxymoron.BaseView;
import com.oxymoron.data.RestaurantDetail;
import com.oxymoron.data.source.local.data.RestaurantId;

public interface RestaurantDetailContract {
    interface View extends BaseView<Presenter> {
        void showDetail(RestaurantDetail restaurantDetail);
    }

    interface Presenter extends BasePresenter {
        void searchDetail(RestaurantId restaurantId);
    }
}
