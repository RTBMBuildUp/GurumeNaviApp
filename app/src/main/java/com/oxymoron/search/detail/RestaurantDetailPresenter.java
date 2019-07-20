package com.oxymoron.search.detail;

import android.os.AsyncTask;
import com.oxymoron.request.RequestIds;
import com.oxymoron.request.RequestMap;
import com.oxymoron.gson.data.GurumeNavi;
import com.oxymoron.gson.data.Rest;
import com.oxymoron.search.detail.data.RestaurantDetail;
import com.oxymoron.util.Consumer;
import com.oxymoron.util.Function;
import com.oxymoron.util.GurumeNaviUtil;
import com.oxymoron.util.Optional;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static com.oxymoron.request.Request.makeRequest;
import static com.oxymoron.util.GurumeNaviUtil.createUrlForGurumeNavi;

public class RestaurantDetailPresenter implements RestaurantDetailContract.Presenter {
    private RestaurantDetailContract.View view;

    RestaurantDetailPresenter(RestaurantDetailContract.View view) {
        this.view = view;
    }

    private class ShowDetailTask extends AsyncTask<String, Void, Optional<RestaurantDetail>> {
        @Override
        protected Optional<RestaurantDetail> doInBackground(String... strings) {
            final Optional<GurumeNavi> gurumeNavi = GurumeNaviUtil.parseGurumeNaviJson(strings[0]);

            return gurumeNavi.map(new Function<GurumeNavi, RestaurantDetail>() {
                @Override
                public RestaurantDetail apply(GurumeNavi value) {
                    List<Rest> restaurantList = value.getRest();
                    return new RestaurantDetail(restaurantList.get(0));
                }
            });

//            if (gurumeNavi != null && gurumeNavi.getRest() != null && gurumeNavi.getRest().get(0) != null) {
//                Rest restaurant = gurumeNavi.getRest().get(0);
//                return new RestaurantDetail(restaurant);
//            }
//
//            return new RestaurantDetail();
        }

        @Override
        protected void onPostExecute(Optional<RestaurantDetail> restaurantDetail) {
            restaurantDetail.ifPresent(new Consumer<RestaurantDetail>() {
                @Override
                public void accept(RestaurantDetail value) {
                    setDetail(value);
                }
            });
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void searchDetail(String restaurantId) {
        URL url = createUrlForGurumeNavi(new RequestMap(Arrays.asList(makeRequest(RequestIds.restaurant_id, restaurantId))));

        new ShowDetailTask().execute(url.toString());
    }

    private void setDetail(RestaurantDetail detail) {
        view.showDetail(detail);
    }
}
