package com.example.gurumenaviapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;
import com.example.gurumenaviapp.data.LocationData;
import com.example.gurumenaviapp.data.ShowedInformation;
import com.example.gurumenaviapp.data.request.Request;
import com.example.gurumenaviapp.gps.LocationListener;
import com.example.gurumenaviapp.gson.data.GuruNavi;
import com.example.gurumenaviapp.gson.data.Rest;
import com.example.gurumenaviapp.gson.typeadapter.IntegerTypeAdapter;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapters;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static com.example.gurumenaviapp.data.request.Requests.*;

public class GuruNaviPresenter implements GuruNaviContract.Presenter {
    private final int REQUEST_PERMISSION = 1000;
    private GuruNaviContract.View view;
    private Context context;

    private final int MinTime = 1000;
    private final float MinDistance = 50;

    private LocationData locationData;
    private TypeAdapterFactory typeAdapterFactory = TypeAdapters.newFactory(int.class, Integer.class, new IntegerTypeAdapter());

    public GuruNaviPresenter(GuruNaviContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void searchNearRestaurant(String token) {
        if (locationData != null) {

            HashSet<Request> requestSet = new HashSet<>(Arrays.asList(
                    new Request(keyid, token),
                    new Request(latitude, locationData.getLatitude()),
                    new Request(longitude, locationData.getLongitude())
            ));

            System.out.println(createGuruNaviUrl(token, requestSet).toString());

            new DownloadJsonTask().execute(createGuruNaviUrl(token, requestSet).toString());
        } else {
            Toast toast = Toast.makeText(context, "現在地を取得できません。", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private class DownloadJsonTask extends AsyncTask<String, Void, List<ShowedInformation>> {
        @Override
        protected List<ShowedInformation> doInBackground(String... strings) {
            GuruNavi guruNavi = parseGuruNaviJson(strings[0]);

            if (guruNavi != null) {
                List<Rest> restaurantList = guruNavi.getRest();

                return createShowedInformationList(restaurantList);
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<ShowedInformation> results) {
            if (results != null) {
                view.cleanRecyclerViewItem();
                for (ShowedInformation result : results) view.addRecyclerViewItem(result);
            } else {
                view.addRecyclerViewItem(new ShowedInformation());
            }
        }

        private GuruNavi parseGuruNaviJson(String rawStringUrl) {
            try {
                return new GsonBuilder()
                        .registerTypeAdapterFactory(typeAdapterFactory)
                        .create()
                        .fromJson(
                                new InputStreamReader(
                                        new URL(rawStringUrl).openStream()
                                ),
                                GuruNavi.class
                        );
            } catch (IOException e) {
                Log.d("error", "parseGuruNaviJson: " + e);
            }
            return null;
        }

        private List<ShowedInformation> createShowedInformationList(List<Rest> restaurantList) {
            List<ShowedInformation> showedInformationList = new ArrayList<>();

            try {
                for (Rest restaurant : restaurantList) {
                    ShowedInformation showedInformation = new ShowedInformation(
                            restaurant.getName(),
                            restaurant.getAddress(),
                            restaurant.getTel(),
                            restaurant.getAccess(),
                            BitmapFactory.decodeStream(new URL(restaurant.getImageUrl().getShopImage()).openStream())
                    );
                    showedInformationList.add(showedInformation);
                }
            } catch (IOException e) {
                Log.d("error", "createShowedInformationList: " + e);
            }

            return showedInformationList;
        }
    }

    @Override
    public void setItem(List<ShowedInformation> itemList, ShowedInformation item) {
        try {
            if (itemList != null) {
                int index = itemList.indexOf(item);
                if (-1 == index) {
                    itemList.add(0, item);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            Log.d("error", "setItem: " + e);
        }
    }

    @Override
    public void removeItem(List<ShowedInformation> itemList, int position) {
        try {
            if (itemList != null) {
                itemList.remove(position);
            }
        } catch (IndexOutOfBoundsException e) {
            Log.d("error", "removeItem: " + e);
        }
    }

    @Override
    public void cleanItem(List<ShowedInformation> itemList) {
        while (itemList != null && itemList.size() != 0)
            view.removeRecyclerViewItem(0);
    }

    @Override
    public void startGpsPrepares() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        LocationListener listener = new LocationListener(this);

        this.startGPS(locationManager, listener);
    }

    @Override
    public void start() {

    }

    @Override
    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            startGpsPrepares();
        } else {
            requestLocationPermission();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        this.updateLocation(latitude, longitude);
        System.out.println("change the location");

        Toast toast = Toast.makeText(context, "change the location ", Toast.LENGTH_SHORT);
        toast.show();
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

        System.out.println("longitude: " + locationData.getLongitude());
        System.out.println("altitude: " + locationData.getLatitude());
    }

    private URL createGuruNaviUrl(String token, HashSet<Request> requestSet) {
        GuruNaviUrl guruNaviUrl = new GuruNaviUrl(token);

        for (Request request : requestSet) {
            guruNaviUrl.addRequest(request);
        }

        return guruNaviUrl.buildUrl();
    }

    private void startGPS(LocationManager manager, android.location.LocationListener listener) {
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

            Toast toast = Toast.makeText(context,
                    "例外が発生、位置情報のPermissionを許可していますか？",
                    Toast.LENGTH_SHORT);
            toast.show();
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
            Toast toast = Toast.makeText(context,
                    "許可されないとアプリが実行できません", Toast.LENGTH_SHORT);
            toast.show();

            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,},
                    REQUEST_PERMISSION);
        }
    }
}