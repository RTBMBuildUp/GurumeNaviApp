package com.example.gurumenaviapp.search.detail;

import android.content.Context;
import android.os.AsyncTask;
import com.example.gurumenaviapp.data.request.RequestIds;
import com.example.gurumenaviapp.data.request.RequestMap;
import com.example.gurumenaviapp.gson.data.GurumeNavi;
import com.example.gurumenaviapp.gson.data.Rest;
import com.example.gurumenaviapp.search.detail.data.RestaurantDetail;
import com.example.gurumenaviapp.util.GurumeNaviUtil;

import java.net.URL;
import java.util.Arrays;

import static com.example.gurumenaviapp.data.request.Request.makeRequest;
import static com.example.gurumenaviapp.util.GurumeNaviUtil.createUrlForGurumeNavi;

public class RestaurantDetailPresenter implements RestaurantDetailContract.Presenter {
    private RestaurantDetailContract.View view;
    private Context context;

    RestaurantDetailPresenter(RestaurantDetailContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    private class ShowDetailTask extends AsyncTask<String, Void, RestaurantDetail> {
        @Override
        protected RestaurantDetail doInBackground(String... strings) {
            GurumeNavi gurumeNavi = GurumeNaviUtil.parseGurumeNaviJson(strings[0]);

            if (gurumeNavi != null && gurumeNavi.getRest() != null && gurumeNavi.getRest().get(0) != null) {
                System.out.println("not null");
                Rest restaurant = gurumeNavi.getRest().get(0);
                return new RestaurantDetail(restaurant);
            }

            return new RestaurantDetail();
        }

        @Override
        protected void onPostExecute(RestaurantDetail restaurantDetail) {
            showDetail(restaurantDetail);
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void searchDetail(String restaurantId) {
        URL url = createUrlForGurumeNavi(new RequestMap(Arrays.asList(makeRequest(RequestIds.id, restaurantId))));

        new ShowDetailTask().execute(url.toString());
    }

    private void showDetail(RestaurantDetail detail) {
        view.setDetail(detail);
    }
}
