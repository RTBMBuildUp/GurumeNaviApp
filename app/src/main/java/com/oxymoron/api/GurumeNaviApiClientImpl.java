package com.oxymoron.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.gurumenaviapp.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapters;
import com.oxymoron.api.gson.data.GurumeNavi;
import com.oxymoron.api.gson.typeadapter.IntegerTypeAdapter;
import com.oxymoron.api.serializable.LocationInformation;
import com.oxymoron.api.serializable.Range;
import com.oxymoron.api.serializable.RestaurantId;
import com.oxymoron.util.Consumer;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GurumeNaviApiClientImpl implements GurumeNaviApiClient {
    private final String token = "2d3e1a633f1ba26c5d7f0a3a037ab225";
    private final GurumeNaviApi gurumeNaviApi = createGurumeNaviApi();

    private static final GurumeNaviApiClientImpl ourInstance = new GurumeNaviApiClientImpl();

    public static GurumeNaviApiClientImpl getInstance() {
        return ourInstance;
    }

    private GurumeNaviApiClientImpl() {
    }

    @Override
    public void loadRestaurantDetail(RestaurantId restaurantId, Consumer<GurumeNavi> function) {
        gurumeNaviApi.getGurumeNavi(token, restaurantId.getId())
                .enqueue(new Callback<GurumeNavi>() {
                    @Override
                    public void onResponse(@NonNull Call<GurumeNavi> call, @NonNull Response<GurumeNavi> response) {
                        if (response.isSuccessful())
                            function.accept(response.body());
                    }

                    @Override
                    public void onFailure(@Nullable Call<GurumeNavi> call, @NonNull Throwable t) {

                    }
                });
    }

    @Override
    public void loadRestaurantList(Range range, LocationInformation locationInformation, Consumer<GurumeNavi> function) {
        gurumeNaviApi.getGurumeNavi(
                token,
                range.getRadius(),
                locationInformation.getLatitude().toString(),
                locationInformation.getLongitude().toString()
        ).enqueue(new Callback<GurumeNavi>() {
            @Override
            public void onResponse(@NonNull Call<GurumeNavi> call, @NonNull Response<GurumeNavi> response) {
                if (response.isSuccessful())
                    function.accept(response.body());
            }

            @Override
            public void onFailure(@Nullable Call<GurumeNavi> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    public void loadRestaurantList(
            Range range,
            LocationInformation locationInformation,
            PageState pageState,
            Consumer<GurumeNavi> function) {

        gurumeNaviApi.getGurumeNavi(token,
                range.getRadius(),
                locationInformation.getLatitude().toString(),
                locationInformation.getLongitude().toString(),
                pageState.getOffsetPage().toString()
        ).enqueue(new Callback<GurumeNavi>() {
            @Override
            public void onResponse(@NonNull Call<GurumeNavi> call, @NonNull Response<GurumeNavi> response) {
                if (response.isSuccessful())
                    function.accept(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<GurumeNavi> call, @Nullable Throwable t) {

            }
        });
    }

    private GurumeNaviApi createGurumeNaviApi() {
        final TypeAdapterFactory typeAdapterFactory =
                TypeAdapters.newFactory(int.class, Integer.class, new IntegerTypeAdapter());

        final Gson myGson = new GsonBuilder()
                .registerTypeAdapterFactory(typeAdapterFactory)
                .create();

        final OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }

        OkHttpClient client = builder.build();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.gnavi.co.jp/")
                .addConverterFactory(GsonConverterFactory.create(myGson))
                .client(client)
                .build();

        return retrofit.create(GurumeNaviApi.class);
    }
}
