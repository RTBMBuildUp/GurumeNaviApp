package com.example.gurumenaviapp.search.screen;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import com.example.gurumenaviapp.data.GuruNaviUrl;
import com.example.gurumenaviapp.data.request.Request;
import com.example.gurumenaviapp.gps.LocationListener;
import com.example.gurumenaviapp.gps.data.LocationData;
import com.example.gurumenaviapp.search.result.SearchResultActivity;
import com.example.gurumenaviapp.util.Toaster;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.gurumenaviapp.data.request.Requests.*;

public class SearchScreenPresenter implements SearchScreenContract.Presenter {
    private final int REQUEST_PERMISSION = 1000;
    private final int MinTime = 1000;
    private final float MinDistance = 50;

    private LocationData locationData;
    private Context context;
    private SearchScreenContract.View view;

    SearchScreenPresenter(Context context, SearchScreenContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void start() {

    }

    @Override
    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            activateGps();
        } else {
            requestLocationPermission();
        }
    }

    @Override
    public void activateGps() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        LocationListener listener = new LocationListener(this);

        this.checkState(locationManager, listener);
    }

    @Override
    public void searchRestaurant(String token) {
        List<Request> requestList = generateRequests(token);

        Intent searchResult = new Intent(context, SearchResultActivity.class);
        if (requestList != null) {
            for (Request request : requestList) {
                searchResult.putExtra(request.getName(), request.getContent());
            }

            view.getViewActivity().startActivity(searchResult);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        this.updateLocation(latitude, longitude);
        System.out.println("change the location");

        Toaster.toast(context, "現在地が更新されました。");
    }

    @Override
    public void onStatusChanged(StringBuilder stringBuilder, int status) {
        switch (status) {
            case LocationProvider.AVAILABLE:
                stringBuilder.append("LocationProvider.AVAILABLE\n");
                System.out.println(stringBuilder);
                break;
            case LocationProvider.OUT_OF_SERVICE:
                stringBuilder.append("LocationProvider.OUT_OF_SERVICE\n");
                System.out.println(stringBuilder);
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                stringBuilder.append("LocationProvider.TEMPORARILY_UNAVAILABLE\n");
                System.out.println(stringBuilder);
                break;
        }
    }

    @Override
    public void onProviderEnabled(StringBuilder stringBuilder, String provider) {
        stringBuilder.append(provider).append("is enabled\n");
        System.out.println(stringBuilder);
    }

    @Override
    public void onProviderDisabled(StringBuilder stringBuilder, String provider) {
        stringBuilder.append(provider).append("is disabled\n");
        System.out.println(stringBuilder);
    }

    private void updateLocation(double latitude, double longitude) {
        if (locationData == null) {
            locationData = new LocationData(latitude, longitude);
        } else {
            locationData.setLatitude(latitude);
            locationData.setLongitude(longitude);
        }

        System.out.println("laltitude: " + locationData.getLatitude());
        System.out.println("longitude: " + locationData.getLongitude());
    }

    private URL createGuruNaviUrl(String token, List<Request> requestList) {
        GuruNaviUrl guruNaviUrl = new GuruNaviUrl(token);

        for (Request request : requestList) {
            guruNaviUrl.addRequest(request);
        }

        return guruNaviUrl.buildUrl();
    }

    private List<Request> generateRequests(String token) {
        if (locationData != null) {

            List<Request> requestList = new ArrayList<>(Arrays.asList(
                    new Request(keyid, token),
                    new Request(latitude, locationData.getLatitude()),
                    new Request(longitude, locationData.getLongitude())
            ));

            System.out.println(createGuruNaviUrl(token, requestList).toString());

            return requestList;
        } else {
            Toaster.toast(context, "現在地を取得できません。");

            return null;
        }
    }

    private void checkState(LocationManager manager, android.location.LocationListener listener) {
        final String provider = LocationManager.NETWORK_PROVIDER;
        final boolean gpsEnabled
                = manager.isProviderEnabled(provider);

        if (!gpsEnabled) {
            enableLocationSettings();
        }

        Log.d("LocationActivity", "locationManager.requestLocationUpdateUpdates");

        //FusedLocationProviderClient...
        try {
            if (ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                System.out.println("permmison error");
                return;
            }

            manager.requestLocationUpdates(provider,
                    MinTime, MinDistance, listener);
        } catch (Exception e) {
            e.printStackTrace();

            Toaster.toast(context, "例外: 位置情報の権限を与えていますか？");
        }
    }

    private void enableLocationSettings() {
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

        this.context.startActivity(settingsIntent);
    }

    private void requestLocationPermission() {
        Activity activity = view.getViewActivity();
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSION);

        } else {
            Toaster.toast(context,
                    "許可されないとアプリが実行できません");

            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,},
                    REQUEST_PERMISSION);
        }
    }
}
