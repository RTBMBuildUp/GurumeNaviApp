package com.example.gurumenaviapp;

import android.app.Activity;
import android.location.Location;
import com.example.gurumenaviapp.data.ShowedInformation;

import java.util.List;

public interface GuruNaviContract {
    interface View extends BaseView<Presenter> {
        Activity getViewActivity();

        void addRecyclerViewItem(ShowedInformation item);

        void removeRecyclerViewItem(int position);

        void cleanRecyclerViewItem();
    }

    interface Presenter extends BasePresenter {
        void setItem(List<ShowedInformation> itemList, ShowedInformation item);

        void removeItem(List<ShowedInformation> itemList, int position);

        void cleanItem(List<ShowedInformation> itemList);

        void checkPermission();

        void searchNearRestaurant(String token);

        void startGpsPrepares();

        void onLocationChanged(Location location);

        void onStatusChanged(StringBuilder stringBuilder, int status);

        void onProviderEnabled(StringBuilder stringBuilder, String provider);

        void onProviderDisabled(StringBuilder stringBuilder, String provider);
    }
}
