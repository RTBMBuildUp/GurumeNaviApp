package com.example.gurumenaviapp.search.candidate;

import com.example.gurumenaviapp.BasePresenter;
import com.example.gurumenaviapp.BaseView;
import com.example.gurumenaviapp.data.request.Request;
import com.example.gurumenaviapp.search.candidate.data.RestaurantThumbnail;

import java.util.List;

public interface RestaurantListContract {
    interface View extends BaseView<Presenter> {
        void addRecyclerViewItem(RestaurantThumbnail item);

        void removeRecyclerViewItem(int position);

        void cleanRecyclerViewItem();
    }

    interface Presenter extends BasePresenter {
        void setItem(List<RestaurantThumbnail> itemList, RestaurantThumbnail item);

        void removeItem(List<RestaurantThumbnail> itemList, int position);

        void cleanItem(List<RestaurantThumbnail> itemList);

        void searchWithRequest(List<Request> requestList);
    }
}
