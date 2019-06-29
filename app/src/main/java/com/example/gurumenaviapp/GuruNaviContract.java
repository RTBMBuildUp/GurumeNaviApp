package com.example.gurumenaviapp;

import android.app.Activity;
import android.location.Location;
import com.example.gurumenaviapp.data.ShowedInformation;

import java.util.List;

public interface GuruNaviContract {
    interface View extends BaseView<Presenter> {
        Activity getViewActivity();

        void addRecyclerViewItem(ShowedInformation item);
    }

    interface Presenter extends BasePresenter {
        void setItem(List<ShowedInformation> itemList, ShowedInformation item);

        void checkPermission();

        void searchNearRestaurant(String token);

        void startGpsPrepares();

        void onLocationChanged(Location location);

        void onStatusChanged(StringBuilder stringBuilder, int status);

        void onProviderEnabled(StringBuilder stringBuilder, String provider);

        void onProviderDisabled(StringBuilder stringBuilder, String provider);

    }
}
