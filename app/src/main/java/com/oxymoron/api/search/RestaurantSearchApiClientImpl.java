package com.oxymoron.api.search;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gurumenaviapp.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapters;
import com.oxymoron.api.search.gson.data.RestaurantSearchResult;
import com.oxymoron.api.search.gson.typeadapter.IntegerTypeAdapter;
import com.oxymoron.api.search.serializable.LocationInformation;
import com.oxymoron.api.search.serializable.Range;
import com.oxymoron.api.search.serializable.RestaurantId;
import com.oxymoron.util.Consumer;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantSearchApiClientImpl implements RestaurantSearchApiClient {
    private final String token = "2d3e1a633f1ba26c5d7f0a3a037ab225";
    private final RestaurantSearchApi restaurantSearchApi = createGurumeNaviApi();

    private static final RestaurantSearchApiClientImpl ourInstance = new RestaurantSearchApiClientImpl();

    public static RestaurantSearchApiClientImpl getInstance() {
        return ourInstance;
    }

    private RestaurantSearchApiClientImpl() {
    }

    @Override
    public void loadRestaurantDetail(RestaurantId restaurantId, Consumer<RestaurantSearchResult> function) {
        restaurantSearchApi.getRestaurantSearchResult(token, restaurantId.getId())
                .enqueue(new Callback<RestaurantSearchResult>() {
                    @Override
                    public void onResponse(@NonNull Call<RestaurantSearchResult> call, @NonNull Response<RestaurantSearchResult> response) {
                        if (response.isSuccessful())
                            function.accept(response.body());
                    }

                    @Override
                    public void onFailure(@Nullable Call<RestaurantSearchResult> call, @NonNull Throwable t) {

                    }
                });
    }

    @Override
    public void loadRestaurantList(Range range, LocationInformation locationInformation, Consumer<RestaurantSearchResult> function) {
        restaurantSearchApi.getRestaurantSearchResult(
                token,
                range.getRadius(),
                locationInformation.getLatitude().toString(),
                locationInformation.getLongitude().toString()
        ).enqueue(new Callback<RestaurantSearchResult>() {
            @Override
            public void onResponse(@NonNull Call<RestaurantSearchResult> call, @NonNull Response<RestaurantSearchResult> response) {
                if (response.isSuccessful())
                    function.accept(response.body());
            }

            @Override
            public void onFailure(@Nullable Call<RestaurantSearchResult> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    public void loadRestaurantList(
            Range range,
            LocationInformation locationInformation,
            PageState pageState,
            Consumer<RestaurantSearchResult> function) {

        restaurantSearchApi.getRestaurantSearchResult(token,
                range.getRadius(),
                locationInformation.getLatitude().toString(),
                locationInformation.getLongitude().toString(),
                pageState.getOffsetPage().toString()
        ).enqueue(new Callback<RestaurantSearchResult>() {
            @Override
            public void onResponse(@NonNull Call<RestaurantSearchResult> call, @NonNull Response<RestaurantSearchResult> response) {
                if (response.isSuccessful())
                    function.accept(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<RestaurantSearchResult> call, @Nullable Throwable t) {

            }
        });
    }

    private RestaurantSearchApi createGurumeNaviApi() {
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

        return retrofit.create(RestaurantSearchApi.class);
    }
}
