package com.oxymoron.ui.list;

import androidx.recyclerview.widget.RecyclerView;

import com.oxymoron.BasePresenter;
import com.oxymoron.BaseView;
import com.oxymoron.data.source.remote.api.PageState;
import com.oxymoron.data.source.remote.api.serializable.LocationInformation;
import com.oxymoron.data.source.remote.api.serializable.Range;
import com.oxymoron.ui.list.data.RestaurantThumbnail;

import java.util.List;

public interface RestaurantListContract {
    interface View extends BaseView<Presenter> {
        void addRecyclerViewItem(RestaurantThumbnail restaurantThumbnail);

        void removeRecyclerViewItem(int position);
    }

    interface Presenter extends BasePresenter {
        void saveRestaurantDetails(List<RestaurantThumbnail> restaurantThumbnailList);

        void setItem(List<RestaurantThumbnail> restaurantThumbnailList, RestaurantThumbnail restaurantThumbnail);

        void removeItem(List<RestaurantThumbnail> restaurantThumbnailList, int position);

        void cleanItem(List<RestaurantThumbnail> restaurantThumbnailList);

        void search(Range range, LocationInformation locationInformation);

        void search(Range range, LocationInformation locationInformation, PageState pageState);

        void onScrolled(RecyclerView recyclerView, Range range, LocationInformation locationInformation, int itemCount);
    }
}
