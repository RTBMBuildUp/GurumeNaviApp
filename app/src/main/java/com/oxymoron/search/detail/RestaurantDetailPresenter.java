package com.oxymoron.search.detail;

import com.oxymoron.gson.data.Rest;
import com.oxymoron.search.detail.data.RestaurantDetail;
import com.oxymoron.util.GurumeNaviUtil;

import java.util.List;

public class RestaurantDetailPresenter implements RestaurantDetailContract.Presenter {
    private RestaurantDetailContract.View view;

    RestaurantDetailPresenter(RestaurantDetailContract.View view) {
        this.view = view;
    }

    private void showDetail(String restaurantId) {
        GurumeNaviUtil.parseGurumeNaviJson(restaurantId, parsedObj -> {
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
