package com.oxymoron.api;

import com.example.gurumenaviapp.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapters;
import com.oxymoron.api.gson.data.GurumeNavi;
import com.oxymoron.api.gson.typeadapter.IntegerTypeAdapter;
import com.oxymoron.util.Consumer;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GurumeNaviApiClientImpl implements GurumeNaviApiClient {
    private String token = "bf565ef4fdb696cfb6ff5a911941fa8d";
    private GurumeNaviApi gurumeNaviApi = createGurumeNaviApi();

    private static final GurumeNaviApiClientImpl ourInstance = new GurumeNaviApiClientImpl();

    public static GurumeNaviApiClientImpl getInstance() {
        return ourInstance;
    }

    private GurumeNaviApiClientImpl() {
    }

    @Override
    public void loadRestaurantDetail(String restaurantId, Consumer<GurumeNavi> function) {
        gurumeNaviApi.getGurumeNavi(token, restaurantId)
                .enqueue(new Callback<GurumeNavi>() {
                    @Override
                    public void onResponse(Call<GurumeNavi> call, Response<GurumeNavi> response) {
                        if (response.isSuccessful())
                            function.accept(response.body());
                    }

                    @Override
                    public void onFailure(Call<GurumeNavi> call, Throwable t) {

                    }
                });
    }

    @Override
    public void loadRestaurantList(String latitude, String longitude, Consumer<GurumeNavi> function) {
        gurumeNaviApi.getGurumeNavi(token, latitude, longitude)
                .enqueue(new Callback<GurumeNavi>() {
                    @Override
                    public void onResponse(Call<GurumeNavi> call, Response<GurumeNavi> response) {
                        if (response.isSuccessful())
                            function.accept(response.body());
                    }

                    @Override
                    public void onFailure(Call<GurumeNavi> call, Throwable t) {

                    }
                });
    }

    @Override
    public void loadRestaurantList(String latitude, String longitude, String offset_page, Consumer<GurumeNavi> function) {
        gurumeNaviApi.getGurumeNavi(token, latitude, longitude, offset_page)
                .enqueue(new Callback<GurumeNavi>() {
                    @Override
                    public void onResponse(Call<GurumeNavi> call, Response<GurumeNavi> response) {
                        if (response.isSuccessful())
                            function.accept(response.body());
                    }

                    @Override
                    public void onFailure(Call<GurumeNavi> call, Throwable t) {

                    }
                });
    }

    private GurumeNaviApi createGurumeNaviApi() {
        TypeAdapterFactory typeAdapterFactory =
                TypeAdapters.newFactory(int.class, Integer.class, new IntegerTypeAdapter());

        Gson myGson = new GsonBuilder()
                .registerTypeAdapterFactory(typeAdapterFactory)
                .create();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }

        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.gnavi.co.jp/")
                .addConverterFactory(GsonConverterFactory.create(myGson))
                .client(client)
                .build();

        return retrofit.create(GurumeNaviApi.class);
    }
}
