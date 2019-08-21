package com.oxymoron.search.list;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import com.oxymoron.BasePresenter;
import com.oxymoron.BaseView;
import com.oxymoron.request.RequestMap;
import com.oxymoron.search.list.data.RestaurantThumbnail;

import java.util.List;

public interface RestaurantListContract {
    interface View extends BaseView<Presenter> {
        void addRecyclerViewItem(RestaurantThumbnail item);

        void removeRecyclerViewItem(int position);

        void startActivity(Intent intent);

        void cleanRecyclerViewItem();
    }

    interface Presenter extends BasePresenter {
        void setItem(List<RestaurantThumbnail> itemList, RestaurantThumbnail item);

        void removeItem(List<RestaurantThumbnail> itemList, int position);

        void cleanItem(List<RestaurantThumbnail> itemList);

        void search(String latitude, String longitude, String hit_per_page, String offset_page);

        void search(String latitude, String longitude);

        void onScrolled(RecyclerView recyclerView, RequestMap requestMap, int itemCount);
    }
}
