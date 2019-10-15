package com.oxymoron.ui.list;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gurumenaviapp.R;
import com.oxymoron.data.source.local.data.RestaurantId;
import com.oxymoron.data.source.remote.api.PageState;
import com.oxymoron.data.source.remote.api.serializable.LocationInformation;
import com.oxymoron.data.source.remote.api.serializable.Range;
import com.oxymoron.injection.Injection;
import com.oxymoron.ui.detail.RestaurantDetailActivity;
import com.oxymoron.ui.list.data.RestaurantThumbnail;
import com.oxymoron.ui.list.recyclerview.EndlessScrollListener;
import com.oxymoron.ui.list.recyclerview.RestaurantListAdapter;
import com.oxymoron.util.toaster.Toaster;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RestaurantListFragment extends Fragment implements RestaurantListContract.View {
    private final static int LOCATION_REQUEST_PERMISSION = 1000;
    private final static int PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED;
    private final static String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;

    private Context context;
    private Activity activity;

    private LocationInformation locationInformation;
    private LocationListener locationListener;
    private LocationManager locationManager;

    private RecyclerView recyclerView;
    private RestaurantListAdapter adapter;
    private final List<RestaurantThumbnail> restaurantThumbnailList = new ArrayList<>();

    private RestaurantListContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.restaurant_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.context = this.getContext();
        this.activity = this.getActivity();

        this.presenter = new RestaurantListPresenter(
                this,
                Injection.provideRestaurantDetailsRepository(this.context)
        );

        this.findView();
        this.prepareRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();

        this.activateGps();

        Log.d("log", "onResume: restList");
    }

    @Override
    public void onPause() {
        super.onPause();

        this.inactivateGps();
        this.presenter.saveRestaurantDetails(this.restaurantThumbnailList);

        Log.d("log", "onPause: restList");
    }

    @Override
    public void addRecyclerViewItem(RestaurantThumbnail restaurantThumbnail) {
        this.presenter.setItem(this.restaurantThumbnailList, restaurantThumbnail);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void removeRecyclerViewItem(int position) {
        this.presenter.removeItem(this.restaurantThumbnailList, position);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(RestaurantListContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private void activateGps() {
        final boolean isGranted =
                ActivityCompat.checkSelfPermission(this.context, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED;

        if (!(Build.VERSION.SDK_INT >= 23 || isGranted)) {
            this.requestLocationPermission();
        }

        deployGps();
    }

    private void inactivateGps() {
        this.locationManager.removeUpdates(this.locationListener);
    }

    private void deployGps() {
        this.locationManager = (LocationManager) Objects
                .requireNonNull(this.context)
                .getSystemService(Context.LOCATION_SERVICE);

        this.locationListener = new LocationListener(this.context);

        if (this.locationManager != null) {
            this.checkState(this.locationManager, this.locationListener);
        }
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this.activity,
                ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this.activity,
                    new String[]{ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_PERMISSION);
        } else {
            Toaster.toast(this.context, "許可されないとアプリが実行できません");

            ActivityCompat.requestPermissions(this.activity,
                    new String[]{ACCESS_FINE_LOCATION,},
                    LOCATION_REQUEST_PERMISSION);
        }
    }

    private void checkState(LocationManager locationManager, android.location.LocationListener locationListener) {
        final String provider = LocationManager.NETWORK_PROVIDER;
        final int MinTime = 1000;
        final float MinDistance = 50;
        final boolean gpsEnabled = locationManager.isProviderEnabled(provider);

        if (!gpsEnabled) {
            final Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

            startActivity(settingsIntent);
        }

        Log.d("LocationActivity", "locationManager.requestLocationUpdateUpdates");

        //FusedLocationProviderClient...
        final boolean isGranted =
                ActivityCompat.checkSelfPermission(this.context, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED;

        if (!isGranted) {
            Log.d("LocationActivity", "permission error");
            return;
        }

        locationManager.requestLocationUpdates(
                provider,
                MinTime,
                MinDistance,
                locationListener
        );
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

            presenter.search(new Range(2), locationInformation);
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

    private void prepareRecyclerView() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context);

        this.adapter = new RestaurantListAdapter(this.restaurantThumbnailList);
        this.adapter.setOnClickListener(thumbnail -> {
            final RestaurantId restaurantId = thumbnail.getId();
            final Intent intent = RestaurantDetailActivity.createIntent(
                    RestaurantListFragment.this.context,
                    restaurantId
            );

            startActivity(intent);
        });

        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(linearLayoutManager);
        this.recyclerView.setAdapter(adapter);
        this.recyclerView.addItemDecoration(new DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL));

        this.recyclerView.addOnScrollListener(new EndlessScrollListener(linearLayoutManager) {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                if (locationInformation != null) {
                    presenter.search(new Range(2), locationInformation, new PageState(page));
                }

                return true;
            }
        });
    }

    private void findView() {
        this.recyclerView = this.activity.findViewById(R.id.restaurant_list_recycler_view);
    }
}
