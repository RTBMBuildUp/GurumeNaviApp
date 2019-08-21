package com.oxymoron.search.search;

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
import com.oxymoron.gps.LocationListener;
import com.oxymoron.gps.data.LocationData;
import com.oxymoron.request.RequestIds;
import com.oxymoron.request.RequestMap;
import com.oxymoron.search.list.RestaurantListActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.oxymoron.request.Request.makeRequest;
import static com.oxymoron.request.RequestIds.hit_per_page;
import static com.oxymoron.request.RequestIds.latitude;
import static com.oxymoron.request.RequestIds.longitude;
import static com.oxymoron.request.RequestIds.range;

public class SearchScreenPresenter implements SearchScreenContract.Presenter {
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

        if (locationManager != null) {
            this.checkState(locationManager, listener);
        } else {
            view.toast("例外発生: GPSの起動に失敗しました。");
        }
    }

    @Override
    public void searchRestaurant() {
        RequestMap requestMap = generateRequestMap();

        Intent restaurantCandidate = new Intent(context, RestaurantListActivity.class);
        if (requestMap != null) {
            for (Map.Entry<RequestIds, String> entry : requestMap.entrySet()) {
                restaurantCandidate.putExtra(entry.getKey().toString(), entry.getValue());
            }

            view.startActivity(restaurantCandidate);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        this.updateLocation(latitude, longitude);

        view.toast("現在地が更新されました。");
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
    }

    private RequestMap generateRequestMap() {
        if (locationData != null) {
            return new RequestMap(Arrays.asList(
                    makeRequest(latitude, locationData.getLatitude().toString()),
                    makeRequest(longitude, locationData.getLongitude().toString()),
                    makeRequest(range, loadRange().toString()),
                    makeRequest(hit_per_page, Integer.toString(15))
            ));
        } else {
            view.toast("現在地を取得できません。");

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
        final int MinTime = 1000;
        final float MinDistance = 50;
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
                Log.d("LocationActivity", "permission error");
                return;
            }

            manager.requestLocationUpdates(provider,
                    MinTime, MinDistance, listener);
        } catch (Exception e) {
            e.printStackTrace();

            view.toast("例外: 位置情報の権限を与えていますか？");
        }
    }

    private void enableLocationSettings() {
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

        view.startActivity(settingsIntent);
    }

    private void requestLocationPermission() {
        final Activity activity = view.getViewActivity();
        final int REQUEST_PERMISSION = 1000;

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSION);
        } else {
            view.toast("許可されないとアプリが実行できません");

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