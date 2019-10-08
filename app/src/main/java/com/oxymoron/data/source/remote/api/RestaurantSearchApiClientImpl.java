package com.oxymoron.data.source.remote.api;

import android.text.TextUtils;

import com.example.gurumenaviapp.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapters;
import com.oxymoron.data.room.RestaurantId;
import com.oxymoron.data.source.remote.api.gson.data.RestaurantSearchResult;
import com.oxymoron.data.source.remote.api.gson.typeadapter.IntegerTypeAdapter;
import com.oxymoron.data.source.remote.api.serializable.LocationInformation;
import com.oxymoron.data.source.remote.api.serializable.Range;
import com.oxymoron.util.Consumer;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Callback;
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
    public void loadRestaurantDetails(List<RestaurantId> restaurantIdList, Callback<RestaurantSearchResult> callback) {
        loadRestaurantDetailsRepeatedly(restaurantIdList, stringList ->
                restaurantSearchApi.getRestaurantSearchResult(
                        token,
                        TextUtils.join(",", stringList)
                ).enqueue(callback)
        );
    }

    @Override
    public void loadRestaurantDetails(Range range, LocationInformation locationInformation, Callback<RestaurantSearchResult> callback) {
        this.restaurantSearchApi.getRestaurantSearchResult(
                token,
                range.getRadius(),
                locationInformation.getLatitude().toString(),
                locationInformation.getLongitude().toString()
        ).enqueue(callback);
    }

    @Override
    public void loadRestaurantDetails(
            Range range, LocationInformation locationInformation,
            PageState pageState, Callback<RestaurantSearchResult> callback) {

        this.restaurantSearchApi.getRestaurantSearchResult(
                token,
                range.getRadius(),
                locationInformation.getLatitude().toString(),
                locationInformation.getLongitude().toString(),
                pageState.getOffsetPage().toString()
        ).enqueue(callback);
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

    private void loadRestaurantDetailsRepeatedly(List<RestaurantId> restaurantIdList, Consumer<List<String>> consumer) {
        final Partition<RestaurantId> idList = new Partition<>(restaurantIdList, 10);
        for (List<RestaurantId> restaurantIds : idList) {
            List<String> stringList = new ArrayList<>();
            for (RestaurantId restaurantId : restaurantIds) {
                stringList.add(restaurantId.getId());
            }

            consumer.accept(stringList);
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
