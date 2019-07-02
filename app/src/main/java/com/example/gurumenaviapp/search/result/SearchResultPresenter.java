package com.example.gurumenaviapp.search.result;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.example.gurumenaviapp.GuruNaviUrl;
import com.example.gurumenaviapp.data.LocationData;
import com.example.gurumenaviapp.data.ShowedInformation;
import com.example.gurumenaviapp.data.request.Request;
import com.example.gurumenaviapp.gson.data.GuruNavi;
import com.example.gurumenaviapp.gson.data.Rest;
import com.example.gurumenaviapp.gson.typeadapter.IntegerTypeAdapter;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapters;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static com.example.gurumenaviapp.data.request.Requests.*;

public class SearchResultPresenter implements SearchResultContract.Presenter {
    private SearchResultContract.View view;
    private Context context;

    private TypeAdapterFactory typeAdapterFactory = TypeAdapters.newFactory(int.class, Integer.class, new IntegerTypeAdapter());

    SearchResultPresenter(SearchResultContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    private class DownloadJsonTask extends AsyncTask<String, Void, List<ShowedInformation>> {
        @Override
        protected List<ShowedInformation> doInBackground(String... strings) {
            GuruNavi guruNavi = parseGuruNaviJson(strings[0]);

            if (guruNavi != null) {
                List<Rest> restaurantList = guruNavi.getRest();

                return createShowedInformationList(restaurantList);
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<ShowedInformation> results) {
            view.cleanRecyclerViewItem();

            if (results != null) {
                for (ShowedInformation result : results) view.addRecyclerViewItem(result);
            } else {
                view.addRecyclerViewItem(new ShowedInformation());
            }
        }

        private GuruNavi parseGuruNaviJson(String rawStringUrl) {
            try {
                return new GsonBuilder()
                        .registerTypeAdapterFactory(typeAdapterFactory)
                        .create()
                        .fromJson(
                                new InputStreamReader(
                                        new URL(rawStringUrl).openStream()
                                ),
                                GuruNavi.class
                        );
            } catch (IOException e) {
                Log.d("error", "parseGuruNaviJson: " + e);
            }
            return null;
        }

        private List<ShowedInformation> createShowedInformationList(List<Rest> restaurantList) {
            List<ShowedInformation> showedInformationList = new ArrayList<>();

            try {
                for (Rest restaurant : restaurantList) {
                    ShowedInformation showedInformation = new ShowedInformation(
                            restaurant.getName(),
                            restaurant.getAddress(),
                            restaurant.getTel(),
                            restaurant.getAccess(),
                            BitmapFactory.decodeStream(new URL(restaurant.getImageUrl().getShopImage()).openStream())
                    );
                    showedInformationList.add(showedInformation);
                }
            } catch (IOException e) {
                Log.d("error", "createShowedInformationList: " + e);
            }

            return showedInformationList;
        }
    }

    @Override
    public void searchWithRequest(String token, List<Request> requestList) {
        final URL url = createGuruNaviUrl(token, requestList);

        new DownloadJsonTask().execute(url.toString());
    }

    private URL createGuruNaviUrl(String token, List<Request> requestList) {
        GuruNaviUrl guruNaviUrl = new GuruNaviUrl(token);

        for (Request request : requestList) {
            guruNaviUrl.addRequest(request);
        }

        return guruNaviUrl.buildUrl();
    }

    @Override
    public void setItem(List<ShowedInformation> itemList, ShowedInformation item) {
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
    public void removeItem(List<ShowedInformation> itemList, int position) {
        try {
            if (itemList != null) {
                itemList.remove(position);
            }
        } catch (IndexOutOfBoundsException e) {
            Log.d("error", "removeItem: " + e);
        }
    }

    @Override
    public void cleanItem(List<ShowedInformation> itemList) {
        while (itemList != null && itemList.size() != 0)
            view.removeRecyclerViewItem(0);
    }

    @Override
    public void start() {

    }

}