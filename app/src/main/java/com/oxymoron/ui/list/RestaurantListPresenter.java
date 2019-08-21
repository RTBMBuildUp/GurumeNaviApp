package com.oxymoron.ui.list;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.oxymoron.api.GurumeNaviApiClient;
import com.oxymoron.gson.data.Rest;
import com.oxymoron.request.RequestIds;
import com.oxymoron.request.RequestMap;
import com.oxymoron.ui.list.data.RestaurantThumbnail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.oxymoron.request.RequestIds.hit_per_page;
import static com.oxymoron.request.RequestIds.latitude;
import static com.oxymoron.request.RequestIds.longitude;
import static com.oxymoron.request.RequestIds.offset_page;

public class RestaurantListPresenter implements RestaurantListContract.Presenter {
    private GurumeNaviApiClient apiClient;
    private RestaurantListContract.View view;

    RestaurantListPresenter(RestaurantListContract.View view, GurumeNaviApiClient apiClient) {
        this.view = view;
        this.apiClient = apiClient;
    }

    @Override
    public void search(String latitude, String longitude) {
        showThumbnail(latitude, longitude);
    }

    @Override
    public void search(String latitude, String longitude, String hit_per_page, String offset_page) {
        showThumbnail(latitude, longitude, hit_per_page, offset_page);
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
    public void onScrolled(RecyclerView recyclerView, RequestMap requestMap, int itemCount) {
        final int bottom = 1;
        if (!recyclerView.canScrollVertically(bottom)) {
            try {
                int hitPerPage = Integer.parseInt(requestMap.getOrElse(hit_per_page, "0"));
                int offset = calculateNextOffset(itemCount, hitPerPage);
                RequestMap newRequestMap = new RequestMap();

                //filter
                newRequestMap.put(offset_page, Integer.toString(offset));
                for (Map.Entry<RequestIds, String> entry : requestMap.entrySet()) {
                    if (entry.getKey() != offset_page)
                        newRequestMap.put(entry.getKey(), entry.getValue());
                }

                search(newRequestMap.get(latitude), newRequestMap.get(longitude), newRequestMap.get(hit_per_page), newRequestMap.get(offset_page));
            } catch (ArithmeticException e) {
                Log.d("RestaurantListPresenter", "onScrolled: " + e);
            }
        }
    }

    @Override
    public void start() {

    }

    private int calculateNextOffset(int itemCount, int hitPerPage) {
        int currentOffset = itemCount / hitPerPage;
        return currentOffset + (itemCount % hitPerPage == 0 ? 0 : 1) + 1;
    }

    private void showThumbnail(String latitude, String longitude) {
        apiClient.loadRestaurantList(latitude, longitude, parsedObj -> {
            List<Rest> restaurantList = parsedObj.getRest();
            List<RestaurantThumbnail> restaurantThumbnailList = createRestaurantThumbnailList(restaurantList);

            Collections.reverse(restaurantThumbnailList);
            for (RestaurantThumbnail restaurantThumbnail : restaurantThumbnailList) {
                view.addRecyclerViewItem(restaurantThumbnail);
            }
        });
    }

    private void showThumbnail(String latitude, String longitude, String hit_per_page, String offset_page) {
        apiClient.loadRestaurantList(latitude, longitude, hit_per_page, offset_page, parsedObj -> {
            List<Rest> restaurantList = parsedObj.getRest();
            List<RestaurantThumbnail> restaurantThumbnailList = createRestaurantThumbnailList(restaurantList);

            Collections.reverse(restaurantThumbnailList);
            for (RestaurantThumbnail restaurantThumbnail : restaurantThumbnailList) {
                view.addRecyclerViewItem(restaurantThumbnail);
            }
        });
    }

    private List<RestaurantThumbnail> createRestaurantThumbnailList(List<Rest> restaurantList) {
        final List<RestaurantThumbnail> restaurantThumbnailList = new ArrayList<>();

        for (Rest restaurant : restaurantList) {
            RestaurantThumbnail restaurantThumbnail = new RestaurantThumbnail(restaurant);
            restaurantThumbnailList.add(restaurantThumbnail);
        }

        return restaurantThumbnailList;
    }
}