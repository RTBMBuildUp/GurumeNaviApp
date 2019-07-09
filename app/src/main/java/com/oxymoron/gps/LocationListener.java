package com.oxymoron.gps;

import android.location.Location;
import android.os.Bundle;
import com.oxymoron.search.screen.SearchScreenContract;

public class LocationListener implements android.location.LocationListener {
    private SearchScreenContract.Presenter presenter;

    private StringBuilder stringBuilder;

    public LocationListener(SearchScreenContract.Presenter presenter) {
        this.presenter = presenter;
        this.stringBuilder = new StringBuilder();
    }

    @Override
    public void onLocationChanged(Location location) {
        presenter.onLocationChanged(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        presenter.onStatusChanged(stringBuilder, status);
    }

    @Override
    public void onProviderEnabled(String provider) {
        presenter.onProviderEnabled(stringBuilder, provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        presenter.onProviderDisabled(stringBuilder, provider);
    }
}
