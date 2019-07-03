package com.example.gurumenaviapp.search.candidate;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.example.gurumenaviapp.data.request.Request;
import com.example.gurumenaviapp.gson.data.GurumeNavi;
import com.example.gurumenaviapp.gson.data.Rest;
import com.example.gurumenaviapp.search.candidate.data.RestaurantThumbnail;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
                    itemList.add(0, item);
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
            redrawItem(results);
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

    private void redrawItem(List<RestaurantThumbnail> restaurantThumbnailList) {
        view.cleanRecyclerViewItem();

        if (restaurantThumbnailList != null) {
            for (RestaurantThumbnail result : restaurantThumbnailList) view.addRecyclerViewItem(result);
        } else {
            view.addRecyclerViewItem(new RestaurantThumbnail());
        }
    }

}