package com.oxymoron.api.search;

import android.text.TextUtils;

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
import com.oxymoron.data.RestaurantDetail;
import com.oxymoron.data.room.RestaurantId;
import com.oxymoron.util.Consumer;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantSearchApiClientImpl implements RestaurantSearchApiClient {
    private final String token;
    private final RestaurantSearchApi restaurantSearchApi = createGurumeNaviApi();

    private static RestaurantSearchApiClientImpl INSTANCE;

    public static RestaurantSearchApiClientImpl getInstance(String token) {
        if (INSTANCE == null) {
            INSTANCE = new RestaurantSearchApiClientImpl(token);
        }

        return INSTANCE;
    }

    private RestaurantSearchApiClientImpl(String token) {
        this.token = token;
    }

    @Override
    public void loadRestaurantDetail(RestaurantId restaurantId, Callback<RestaurantSearchResult> callback) {
        restaurantSearchApi.getRestaurantSearchResult(token, restaurantId.getId()).enqueue(callback);
    }

    @Override
    public void loadRestaurantDetails(List<RestaurantId> restaurantIdList, Consumer<List<RestaurantDetail>> function) {
        List<String> restaurantIdsAsStringList = new ArrayList<>();
        for (RestaurantId restaurantId : restaurantIdList) {
            restaurantIdsAsStringList.add(restaurantId.getId());
        }

        loadRestaurantDetailsRepeatedly(restaurantIdsAsStringList, stringList ->
                restaurantSearchApi.getRestaurantSearchResult(
                        token,
                        TextUtils.join(",", stringList)
                ).enqueue(new Callback<RestaurantSearchResult>() {
                    @Override
                    public void onResponse(@NonNull Call<RestaurantSearchResult> call, @NonNull Response<RestaurantSearchResult> response) {
                        if (response.isSuccessful()) {
                            RestaurantSearchResult body = response.body();
                            if (body != null) {
                                body.getRest().ifPresent(restList -> {
                                    List<RestaurantDetail> restaurantDetailList = RestaurantDetail.createRestaurantDetailList(restList);

                                    function.accept(restaurantDetailList);
                                });
                            }
                        }
                    }

                    @Override
                    public void onFailure(@Nullable Call<RestaurantSearchResult> call, @NonNull Throwable t) {

                    }
                })
        );
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

    private static RestaurantSearchApi createGurumeNaviApi() {
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

    private void loadRestaurantDetailsRepeatedly(List<String> restaurantIdsAsStringList, Consumer<List<String>> consumer) {
        final Partition<String> idList = new Partition<>(restaurantIdsAsStringList, 10);
        for (List<String> ids : idList) {
            consumer.accept(ids);
        }
    }

    private class Partition<T> extends AbstractList<List<T>> {

        private final List<T> list;
        private final int chunkSize;

        Partition(List<T> list, int chunkSize) {
            this.list = new ArrayList<>(list);
            this.chunkSize = chunkSize;
        }

        @Override
        public List<T> get(int index) {
            int start = index * chunkSize;
            int end = Math.min(start + chunkSize, list.size());

            if (start > end) {
                throw new IndexOutOfBoundsException("Index " + index + " is out of the list range <0," + (size() - 1) + ">");
            }

            return new ArrayList<>(list.subList(start, end));
        }

        @Override
        public int size() {
            return (int) Math.ceil((double) list.size() / (double) chunkSize);
        }
    }
}
