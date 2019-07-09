package com.oxymoron.search.screen;

import android.app.Activity;
import android.location.Location;
import android.widget.RadioGroup;
import com.oxymoron.BasePresenter;
import com.oxymoron.BaseView;

public interface SearchScreenContract {
    interface View extends BaseView<Presenter> {
        Activity getViewActivity();
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
