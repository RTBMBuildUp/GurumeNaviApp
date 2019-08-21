package com.oxymoron.search.search;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.widget.RadioGroup;
import com.oxymoron.BasePresenter;
import com.oxymoron.BaseView;

public interface SearchScreenContract {
    interface View extends BaseView<Presenter> {
        Activity getViewActivity();

        void startActivity(Intent intent);

        void toast(String message);
    }

    interface Presenter extends BasePresenter {
        void checkPermission();

        void activateGps();

        void searchRestaurant();

        void onLocationChanged(Location location);

        void onStatusChanged(StringBuilder stringBuilder, int status);

        void onProviderEnabled(StringBuilder stringBuilder, String provider);

        void onProviderDisabled(StringBuilder stringBuilder, String provider);

        void OnCheckedChange(RadioGroup group, int checkedId);
    }
}
