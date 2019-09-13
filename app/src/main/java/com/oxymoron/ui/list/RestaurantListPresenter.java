package com.oxymoron.ui.list;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.oxymoron.api.search.PageState;
import com.oxymoron.api.search.RestaurantSearchApiClient;
import com.oxymoron.api.search.gson.data.Rest;
import com.oxymoron.api.search.serializable.LocationInformation;
import com.oxymoron.api.search.serializable.Range;
import com.oxymoron.ui.list.data.RestaurantThumbnail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RestaurantListPresenter implements RestaurantListContract.Presenter {
    private final RestaurantListContract.View view;
    private final RestaurantSearchApiClient apiClient;

    private PageState pageState;

    RestaurantListPresenter(RestaurantListContract.View view, RestaurantSearchApiClient apiClient) {
        this.view = view;
        this.apiClient = apiClient;
    }

    @Override
    public void search(Range range, LocationInformation locationInformation) {
        showThumbnail(range, locationInformation);
    }

    @Override
    public void search(Range range, LocationInformation locationInformation, PageState pageState) {
        showThumbnail(range, locationInformation, pageState);
    }

    @Override
    public void setItem(List<RestaurantThumbnail> itemList, RestaurantThumbnail item) {
        try {
            if (itemList != null) {
                int index = itemList.indexOf(item);
                if (-1 == index) {
                    itemList.add(item);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            Log.d("RestaurantListPresenter", "setItem: " + e);
        }
    }

    @Override
    public void removeItem(List<RestaurantThumbnail> itemList, int position) {
        try {
            if (itemList != null) {
                itemList.remove(position);
            }
        } catch (IndexOutOfBoundsException e) {
            Log.d("RestaurantListPresenter", "removeItem: " + e);
        }
    }

    @Override
    public void cleanItem(List<RestaurantThumbnail> itemList) {
        while (itemList != null && itemList.size() != 0)
            view.removeRecyclerViewItem(0);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, Range range, LocationInformation locationInformation, int itemCount) {
        if (this.pageState != null) {
            final int bottom = 1;
            if (!recyclerView.canScrollVertically(bottom)) {
                try {
                    search(range, locationInformation, this.pageState.getNextPageState());
                } catch (ArithmeticException e) {
                    Log.d("RestaurantListPresenter", "onScrolled: " + e);
                }
            }
        }
    }

    @Override
    public void start() {

    }

    private void showThumbnail(Range range, LocationInformation locationInformation) {
        apiClient.loadRestaurantList(range, locationInformation, parsedObj -> {
            pageState = new PageState(parsedObj.getPageOffset());

            parsedObj.getRest().ifPresent(list -> {
                final List<RestaurantThumbnail> restaurantThumbnailList = createRestaurantThumbnailList(list);

                Collections.reverse(restaurantThumbnailList);
                for (RestaurantThumbnail restaurantThumbnail : restaurantThumbnailList) {
                    view.addRecyclerViewItem(restaurantThumbnail);
                }
            });
        });
    }

    private void showThumbnail(Range range, LocationInformation locationInformation, PageState pageState) {
        apiClient.loadRestaurantList(range, locationInformation, pageState, parsedObj -> {
            this.pageState = pageState;

            parsedObj.getRest().ifPresent(list -> {
                final List<RestaurantThumbnail> restaurantThumbnailList = createRestaurantThumbnailList(list);

                Collections.reverse(restaurantThumbnailList);
                for (RestaurantThumbnail restaurantThumbnail : restaurantThumbnailList) {
                    view.addRecyclerViewItem(restaurantThumbnail);
                }
            });
        });
    }

    private List<RestaurantThumbnail> createRestaurantThumbnailList(List<Rest> restaurantList) {
        final List<RestaurantThumbnail> restaurantThumbnailList = new ArrayList<>();

        for (Rest restaurant : restaurantList) {
            final RestaurantThumbnail restaurantThumbnail = new RestaurantThumbnail(restaurant);
            restaurantThumbnailList.add(restaurantThumbnail);
        }

        return restaurantThumbnailList;
    }
}