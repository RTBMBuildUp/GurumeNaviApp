package com.oxymoron.search.candidate;

import android.arch.core.util.Function;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import com.oxymoron.request.RequestIds;
import com.oxymoron.request.RequestMap;
import com.oxymoron.gson.data.GurumeNavi;
import com.oxymoron.gson.data.Rest;
import com.oxymoron.search.candidate.data.RestaurantThumbnail;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.oxymoron.request.Request.makeRequest;
import static com.oxymoron.request.RequestIds.hit_per_page;
import static com.oxymoron.request.RequestIds.offset_page;
import static com.oxymoron.util.GurumeNaviUtil.createUrlForGurumeNavi;
import static com.oxymoron.util.GurumeNaviUtil.parseGurumeNaviJson;

public class RestaurantListPresenter implements RestaurantListContract.Presenter {
    private RestaurantListContract.View view;
    private Context context;

    RestaurantListPresenter(RestaurantListContract.View view, Context context) {
        this.view = view;
        this.context = context;
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
            Log.d("error", "setItem: " + e);
        }
    }

    @Override
    public void removeItem(List<RestaurantThumbnail> itemList, int position) {
        try {
            if (itemList != null) {
                itemList.remove(position);
            }
        } catch (IndexOutOfBoundsException e) {
            Log.d("error", "removeItem: " + e);
        }
    }

    @Override
    public void cleanItem(List<RestaurantThumbnail> itemList) {
        while (itemList != null && itemList.size() != 0)
            view.removeRecyclerViewItem(0);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, RequestMap requestMap, int itemCount) {
        if (!recyclerView.canScrollVertically(1)) {
            try {
                int hitPerPage = Integer.parseInt(requestMap.getOrElse(hit_per_page, "0"));
                int offset = itemCount / hitPerPage + (itemCount % hitPerPage == 0 ? 0 : 1) + 1;
                RequestMap newRequestMap = new RequestMap();

                //filter
                newRequestMap.put(makeRequest(offset_page, offset));
                for (Map.Entry<RequestIds, String> entry : requestMap.entrySet()) {
                    if (entry.getKey() == offset_page) newRequestMap.put(makeRequest(entry.getKey(), entry.getValue()));
                }

                search(newRequestMap);
            } catch (ArithmeticException e) {
                System.out.println(e);
            }
        }
    }

    @Override
    public void start() {

    }

    private class ShowThumbnailTask extends AsyncTask<String, Void, List<RestaurantThumbnail>> {
        @Override
        protected List<RestaurantThumbnail> doInBackground(String... strings) {
            GurumeNavi gurumeNavi = parseGurumeNaviJson(strings[0]);

            if (gurumeNavi != null) {
                List<Rest> restaurantList = gurumeNavi.getRest();
                return createRestaurantThumbnailList(restaurantList);
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<RestaurantThumbnail> results) {
            if (results != null) {
                Collections.reverse(results);
                for (RestaurantThumbnail result : results) {
                    view.addRecyclerViewItem(result);
                }
            }
        }

        private List<RestaurantThumbnail> createRestaurantThumbnailList(List<Rest> restaurantList) {
            List<RestaurantThumbnail> restaurantThumbnailList = new ArrayList<>();

            for (Rest restaurant : restaurantList) {
                RestaurantThumbnail restaurantThumbnail = new RestaurantThumbnail(restaurant);
                restaurantThumbnailList.add(restaurantThumbnail);
            }

            return restaurantThumbnailList;
        }
    }

    private List<RequestIds> filter(RequestIds[] requests, Function<RequestIds, Boolean> lambda) {
        List<RequestIds> result = new ArrayList<>();
        for (RequestIds request : requests) {
            if (lambda.apply(request)) result.add(request);
        }

        return result;
    }

    private <T, R> List<R> map(List<T> list, Function<T, R> factor) {
        List<R> result = new ArrayList<>();

        for (T value : list)
            result.add(factor.apply(value));

        return result;
    }

    private void redrawItem(List<RestaurantThumbnail> restaurantThumbnailList) {
        cleanItem(restaurantThumbnailList);
        if (restaurantThumbnailList != null) {
            for (RestaurantThumbnail result : restaurantThumbnailList) view.addRecyclerViewItem(result);
        } else {
            view.addRecyclerViewItem(new RestaurantThumbnail());
        }
    }

}