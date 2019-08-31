package com.oxymoron.ui.list;

import androidx.recyclerview.widget.RecyclerView;

import com.oxymoron.BasePresenter;
import com.oxymoron.BaseView;
import com.oxymoron.api.PageState;
import com.oxymoron.api.serializable.LocationInformation;
import com.oxymoron.api.serializable.Range;
import com.oxymoron.ui.list.data.RestaurantThumbnail;

import java.util.List;

public interface RestaurantListContract {
    interface View extends BaseView<Presenter> {
        void addRecyclerViewItem(RestaurantThumbnail item);

        void removeRecyclerViewItem(int position);
    }

    interface Presenter extends BasePresenter {
        void setItem(List<RestaurantThumbnail> itemList, RestaurantThumbnail item);

        void removeItem(List<RestaurantThumbnail> itemList, int position);

        void cleanItem(List<RestaurantThumbnail> itemList);

        void search(Range range, LocationInformation locationInformation);

        void search(Range range, LocationInformation locationInformation, PageState pageState);

        void onScrolled(RecyclerView recyclerView, Range range, LocationInformation locationInformation, int itemCount);
    }
}
