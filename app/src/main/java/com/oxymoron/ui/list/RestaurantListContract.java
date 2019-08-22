package com.oxymoron.ui.list;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.oxymoron.BasePresenter;
import com.oxymoron.BaseView;
import com.oxymoron.request.LocationInformation;
import com.oxymoron.request.PageState;
import com.oxymoron.ui.list.data.RestaurantThumbnail;

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

        void search(LocationInformation locationInformation);

        void search(LocationInformation locationInformation, PageState pageState);

        void onScrolled(RecyclerView recyclerView, LocationInformation locationInformation, int itemCount);
    }
}
