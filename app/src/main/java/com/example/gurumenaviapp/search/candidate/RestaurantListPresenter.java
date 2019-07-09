package com.example.gurumenaviapp.search.candidate;

import android.arch.core.util.Function;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import com.example.gurumenaviapp.data.request.Request;
import com.example.gurumenaviapp.data.request.Requests;
import com.example.gurumenaviapp.gson.data.GurumeNavi;
import com.example.gurumenaviapp.gson.data.Rest;
import com.example.gurumenaviapp.search.candidate.data.RestaurantThumbnail;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.gurumenaviapp.data.request.Requests.hit_per_page;
import static com.example.gurumenaviapp.data.request.Requests.offset_page;
import static com.example.gurumenaviapp.util.GurumeNaviUtil.createUrlForGurumeNavi;
import static com.example.gurumenaviapp.util.GurumeNaviUtil.parseGurumeNaviJson;

public class RestaurantListPresenter implements RestaurantListContract.Presenter {
    private RestaurantListContract.View view;
    private Context context;

    RestaurantListPresenter(RestaurantListContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void searchWithRequest(List<Request> requestList) {
        final URL url = createUrlForGurumeNavi(requestList);

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
    public void onScrolled(RecyclerView recyclerView, List<Request> requests, int itemCount) {
        if (!recyclerView.canScrollVertically(1)) {
            String hitPerPage = "0";
            int offset = 1;

            //filter
            List<Request> requestList = new ArrayList<>();
            for (Request request : requests) {
                if (!offset_page.toString().equals(request.getName())) {
                    requestList.add(request);
                }

                if (hit_per_page.toString().equals(request.getName())) {
                    hitPerPage = request.getContent();
                }
            }

            try {
                offset = itemCount / Integer.parseInt(hitPerPage) + 1 + (itemCount % Integer.parseInt(hitPerPage) != 0 ? 1 : 0);
                requestList.add(new Request(offset_page, offset));
                for (Request request : requestList) {
                    System.out.println(request.getName() + request.getContent());
                }

                searchWithRequest(requestList);
            } catch (ArithmeticException e) {
                Log.d("error", "onScrolled: " + e);
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

    private List<Requests> filter(Requests[] requests, Function<Requests, Boolean> lambda) {
        List<Requests> result = new ArrayList<>();
        for (Requests request : requests) {
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