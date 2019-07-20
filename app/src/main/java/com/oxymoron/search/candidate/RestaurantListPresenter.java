package com.oxymoron.search.candidate;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import com.oxymoron.gson.data.GurumeNavi;
import com.oxymoron.gson.data.Rest;
import com.oxymoron.request.RequestIds;
import com.oxymoron.request.RequestMap;
import com.oxymoron.search.candidate.data.RestaurantThumbnail;
import com.oxymoron.util.Consumer;
import com.oxymoron.util.Function;
import com.oxymoron.util.Optional;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.oxymoron.request.RequestIds.hit_per_page;
import static com.oxymoron.request.RequestIds.offset_page;
import static com.oxymoron.util.GurumeNaviUtil.createUrlForGurumeNavi;
import static com.oxymoron.util.GurumeNaviUtil.parseGurumeNaviJson;

public class RestaurantListPresenter implements RestaurantListContract.Presenter {
    private RestaurantListContract.View view;

    RestaurantListPresenter(RestaurantListContract.View view) {
        this.view = view;
    }

    @Override
    public void search(RequestMap requestMap) {
        final URL url = createUrlForGurumeNavi(requestMap);

        new ShowThumbnailTask().execute(url.toString());
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
                    if (entry.getKey() != offset_page) newRequestMap.put(entry.getKey(), entry.getValue());
                }

                search(newRequestMap);
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

    private class ShowThumbnailTask extends AsyncTask<String, Void, Optional<List<RestaurantThumbnail>>> {
        @Override
        protected Optional<List<RestaurantThumbnail>> doInBackground(String... strings) {
            Optional<GurumeNavi> gurumeNavi = parseGurumeNaviJson(strings[0]);
            return gurumeNavi.map(new Function<GurumeNavi, List<RestaurantThumbnail>>() {
                @Override
                public List<RestaurantThumbnail> apply(GurumeNavi value) {
                    List<Rest> restaurantList = value.getRest();
                    return createRestaurantThumbnailList(restaurantList);
                }
            });
        }

        @Override
        protected void onPostExecute(Optional<List<RestaurantThumbnail>> results) {
            results.ifPresent(new Consumer<List<RestaurantThumbnail>>() {
                @Override
                public void accept(List<RestaurantThumbnail> value) {
                    Collections.reverse(value);
                    for (RestaurantThumbnail restaurantThumbnail : value) {
                        view.addRecyclerViewItem(restaurantThumbnail);
                    }
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
}