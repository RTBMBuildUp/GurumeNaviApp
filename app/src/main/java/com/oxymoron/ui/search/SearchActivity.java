package com.oxymoron.ui.search;

import android.Manifest;
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
import android.widget.Button;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.gurumenaviapp.R;
import com.oxymoron.api.serializable.LocationInformation;
import com.oxymoron.api.serializable.Range;
import com.oxymoron.ui.list.RestaurantListActivity;
import com.oxymoron.util.toaster.Toaster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    private Button searchButton;

    private LocationInformation locationInformation;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private Map<Integer, Integer> idRangeMap;
    private List<RadioButton> radioButtonList;

    private final int LOCATION_REQUEST_PERMISSION = 1000;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_screen);

        if (Build.VERSION.SDK_INT >= 23) {
            checkPermission();
        } else {
            activateGps();
        }

        this.findViews();

        idRangeMap = initialIdRangeMap();
        radioButtonList = initialRadioButtonList();

        this.searchButton.setOnClickListener(v -> this.searchRestaurant());
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("log", "onStop: ");

        this.inactivateGps();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d("log", "onPostResume: ");

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
        this.locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        this.locationListener = new LocationListener(this);

        if (locationManager != null) {
            this.checkState(locationManager, this.locationListener);
        } else {
            Toaster.toast(this, "例外発生: GPSの起動に失敗しました。");
        }
    }

    private void inactivateGps() {
        this.locationManager.removeUpdates(this.locationListener);
    }

    private void searchRestaurant() {
        if (locationInformation != null) {
            final Intent intent = RestaurantListActivity.createIntent(this, loadRange(), locationInformation);
            this.startActivity(intent);
        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            activateGps();
        } else {
            requestLocationPermission();
        }
    }

    private void findViews() {
        this.searchButton = findViewById(R.id.search_screen_search_button);
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

            locationInformation = new LocationInformation(latitude, longitude);

            Toaster.toast(context, "現在地が更新されました。");
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
        return new Range(idRangeMap.get(R.id.search_screen_range_radio_button_2));
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
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                Log.d("LocationActivity", "permission error");
                return;
            }

            manager.requestLocationUpdates(provider,
                    MinTime, MinDistance, listener);
        } catch (Exception e) {
            e.printStackTrace();

            Toaster.toast(this, "例外: 位置情報の権限を与えていますか？");
        }
    }

    private void enableLocationSettings() {
        final Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

        startActivity(settingsIntent);
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_PERMISSION);
        } else {
            Toaster.toast(this, "許可されないとアプリが実行できません");

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,},
                    LOCATION_REQUEST_PERMISSION);
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
        final List<RadioButton> radioButtonList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : idRangeMap.entrySet()) {
            radioButtonList.add(this.findViewById(entry.getKey()));
        }

        return radioButtonList;
    }
}
