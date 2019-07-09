package com.example.gurumenaviapp.search.candidate;

import android.support.v7.widget.RecyclerView;
import com.example.gurumenaviapp.BasePresenter;
import com.example.gurumenaviapp.BaseView;
import com.example.gurumenaviapp.data.request.RequestMap;
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

        void search(RequestMap requestMap);

        void onScrolled(RecyclerView recyclerView, RequestMap requestMap, int itemCount);
    }
}
