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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.example.gurumenaviapp.R;
import com.example.gurumenaviapp.data.request.Request;
import com.example.gurumenaviapp.gps.LocationListener;
import com.example.gurumenaviapp.gps.data.LocationData;
import com.example.gurumenaviapp.search.candidate.RestaurantListActivity;
import com.example.gurumenaviapp.util.Toaster;

import java.util.*;

import static com.example.gurumenaviapp.data.request.Requests.*;
import static com.example.gurumenaviapp.util.GurumeNaviUtil.createUrlForGurumeNavi;

public class SearchScreenPresenter implements SearchScreenContract.Presenter {
    private final int REQUEST_PERMISSION = 1000;
    private final int MinTime = 1000;
    private final float MinDistance = 50;

    private LocationData locationData;
    private Context context;
    private SearchScreenContract.View view;

    private Map<Integer, Integer> idRangeMap;
    private List<RadioButton> radioButtonList;

    SearchScreenPresenter(Context context, SearchScreenContract.View view) {
        this.context = context;
        this.view = view;

        idRangeMap = initialIdRangeMap();
        radioButtonList = initialRadioButtonList();
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
    public void searchRestaurant() {
        List<Request> requestList = generateRequests();

        Intent restaurantCandidate = new Intent(context, RestaurantListActivity.class);
        if (requestList != null) {
            for (Request request : requestList) {
                restaurantCandidate.putExtra(request.getName(), request.getContent());
            }

            view.getViewActivity().startActivity(restaurantCandidate);
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

    @Override
    public void OnCheckedChange(RadioGroup group, int checkedId) {

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

    private List<Request> generateRequests() {
        if (locationData != null) {

            List<Request> requestList = new ArrayList<>(Arrays.asList(
                    new Request(latitude, locationData.getLatitude()),
                    new Request(longitude, locationData.getLongitude()),
                    new Request(range, loadRange()),
                    new Request(hit_per_page, 30)
            ));

            System.out.println(createUrlForGurumeNavi(requestList).toString());

            return requestList;
        } else {
            Toaster.toast(context, "現在地を取得できません。");

            return null;
        }
    }

    private Integer loadRange() {
        for (RadioButton radioButton : radioButtonList) {
            if (radioButton.isChecked()) return idRangeMap.get(radioButton.getId());
        }

        return idRangeMap.get(R.id.search_screen_range_radio_button_2);
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

    private Map<Integer, Integer> initialIdRangeMap() {
        return new HashMap<Integer, Integer>() {{
            put(R.id.search_screen_range_radio_button_1, 1);
            put(R.id.search_screen_range_radio_button_2, 2);
            put(R.id.search_screen_range_radio_button_3, 3);
            put(R.id.search_screen_range_radio_button_4, 4);
            put(R.id.search_screen_range_radio_button_5, 5);
        }};
    }

    private List<RadioButton> initialRadioButtonList() {
        List<RadioButton> radioButtonList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : idRangeMap.entrySet()) {
            radioButtonList.add((RadioButton) view.getViewActivity().findViewById(entry.getKey()));
        }

        return radioButtonList;
    }
}
