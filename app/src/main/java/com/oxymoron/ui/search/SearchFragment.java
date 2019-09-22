package com.oxymoron.ui.search;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.gurumenaviapp.R;
import com.oxymoron.data.source.remote.api.serializable.LocationInformation;
import com.oxymoron.data.source.remote.api.serializable.Range;
import com.oxymoron.ui.list.RestaurantListActivity;
import com.oxymoron.util.toaster.Toaster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SearchFragment extends Fragment {
    private Button searchButton;

    private LocationInformation locationInformation;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private Map<Integer, Integer> idRangeMap;
    private List<RadioButton> radioButtonList;

    private Context context;
    private Activity activity;

    private final int LOCATION_REQUEST_PERMISSION = 1000;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.activity = this.getActivity();
        this.context = this.getContext();

        if (Build.VERSION.SDK_INT >= 23) {
            checkPermission();
        } else {
            activateGps();
        }

        this.findViews(view);

        idRangeMap = initialIdRangeMap();
        radioButtonList = initialRadioButtonList(view);

        this.searchButton.setOnClickListener(v -> this.searchRestaurant());
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("log", "onStop: ");

        this.inactivateGps();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("log", "onResume: ");

        this.activateGps();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("log", "onRequestPermissionsResult: " + requestCode);
        if (requestCode == LOCATION_REQUEST_PERMISSION) {
            Log.d("log", "onRequestPermissionsResult: request");
            if (PackageManager.PERMISSION_GRANTED != grantResults[0]) {
                Log.d("log", "onRequestPermissionsResult: deny");
            } else {
                this.activateGps();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void activateGps() {
        this.locationManager =
                (LocationManager) Objects.requireNonNull(this.context).getSystemService(Context.LOCATION_SERVICE);

        this.locationListener = new LocationListener(this.context);

        if (locationManager != null) {
            this.checkState(locationManager, this.locationListener);
        } else {
            Toaster.toast(this.context, "例外発生: GPSの起動に失敗しました。");
        }
    }

    private void inactivateGps() {
        this.locationManager.removeUpdates(this.locationListener);
    }

    private void searchRestaurant() {
        if (locationInformation != null) {
            final Intent intent = RestaurantListActivity.createIntent(this.context, loadRange(), locationInformation);
            this.startActivity(intent);
        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            activateGps();
        } else {
            requestLocationPermission();
        }
    }

    private void findViews(View view) {
        this.searchButton = view.findViewById(R.id.fragment_search_search_button);
    }

    private class LocationListener implements android.location.LocationListener {

        private final Context context;

        private final StringBuilder stringBuilder;

        LocationListener(Context context) {
            this.context = context;
            this.stringBuilder = new StringBuilder();
        }

        @Override
        public void onLocationChanged(Location location) {
            final double latitude = location.getLatitude();
            final double longitude = location.getLongitude();

            if (locationInformation == null) {
                locationInformation = new LocationInformation(latitude, longitude);
            } else if (locationInformation.equals(new LocationInformation(latitude, longitude))) {
                locationInformation = new LocationInformation(latitude, longitude);

                Toaster.toast(context, "現在地が更新されました。");
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    stringBuilder.append("LocationProvider.AVAILABLE\n");
                    Log.d("log", "onStatusChanged: " + stringBuilder);
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    stringBuilder.append("LocationProvider.OUT_OF_SERVICE\n");
                    Log.d("log", "onStatusChanged: " + stringBuilder);
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    stringBuilder.append("LocationProvider.TEMPORARILY_UNAVAILABLE\n");
                    Log.d("log", "onStatusChanged: " + stringBuilder);
                    break;
            }
        }


        @Override
        public void onProviderEnabled(String provider) {
            stringBuilder.append(provider).append("is enabled\n");
            Log.d("log", "onStatusChanged: " + stringBuilder);
        }

        @Override
        public void onProviderDisabled(String provider) {
            stringBuilder.append(provider).append("is disabled\n");
            Log.d("log", "onStatusChanged: " + stringBuilder);
        }
    }

    private Range loadRange() {
        for (RadioButton radioButton : radioButtonList) {
            if (radioButton.isChecked()) return new Range(idRangeMap.get(radioButton.getId()));
        }
        return new Range(idRangeMap.get(R.id.fragment_search_range_radio_button_2));
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
            if (ActivityCompat.checkSelfPermission(this.context,
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                Log.d("LocationActivity", "permission error");
                return;
            }

            manager.requestLocationUpdates(provider,
                    MinTime, MinDistance, listener);
        } catch (Exception e) {
            e.printStackTrace();

            Toaster.toast(this.context, "例外: 位置情報の権限を与えていますか？");
        }
    }

    private void enableLocationSettings() {
        final Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

        startActivity(settingsIntent);
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this.activity,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this.activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_PERMISSION);
        } else {
            Toaster.toast(this.context, "許可されないとアプリが実行できません");

            ActivityCompat.requestPermissions(this.activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,},
                    LOCATION_REQUEST_PERMISSION);
        }
    }

    private Map<Integer, Integer> initialIdRangeMap() {
        return new HashMap<Integer, Integer>() {{
            put(R.id.fragment_search_range_radio_button_1, 1);
            put(R.id.fragment_search_range_radio_button_2, 2);
            put(R.id.fragment_search_range_radio_button_3, 3);
            put(R.id.fragment_search_range_radio_button_4, 4);
            put(R.id.fragment_search_range_radio_button_5, 5);
        }};
    }

    private List<RadioButton> initialRadioButtonList(View view) {
        final List<RadioButton> radioButtonList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : idRangeMap.entrySet()) {
            radioButtonList.add(view.findViewById(entry.getKey()));
        }

        return radioButtonList;
    }
}
