package com.oxymoron.util;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapters;
import com.oxymoron.gson.data.GurumeNavi;
import com.oxymoron.gson.typeadapter.IntegerTypeAdapter;
import com.oxymoron.request.Request;
import com.oxymoron.request.RequestIds;
import com.oxymoron.request.RequestMap;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.oxymoron.request.Request.makeRequest;
import static com.oxymoron.request.RequestIds.key_id;
import static com.oxymoron.request.RequestIds.latitude;
import static com.oxymoron.request.RequestIds.longitude;
import static com.oxymoron.request.RequestIds.restaurant_id;
import static com.oxymoron.request.Sign.And;
import static com.oxymoron.request.Sign.Question;

public class GurumeNaviUtil {
    private static String token = "bf565ef4fdb696cfb6ff5a911941fa8d";
    private static GurumeNaviApi gurumeNaviApi = createGurumeNaviApi();

    public static Optional<GurumeNavi> parseGurumeNaviJson(String rawStringUrl) {
        TypeAdapterFactory typeAdapterFactory = TypeAdapters.newFactory(int.class, Integer.class, new IntegerTypeAdapter());
        try {
            return Optional.of(new GsonBuilder()
                    .registerTypeAdapterFactory(typeAdapterFactory)
                    .create()
                    .fromJson(
                            new InputStreamReader(
                                    new URL(rawStringUrl).openStream()
                            ),
                            GurumeNavi.class
                    ));
        } catch (IOException e) {
            Log.d("GurumeNaviUtil", "parseGurumeNaviJson: " + e);
        }
        return Optional.empty();
    }

    public static void parseGurumeNaviJson(String restaurantId, Consumer<GurumeNavi> function) {
        createGurumeNaviApi().getGurumeNavi(token, restaurantId)
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

    public static void parseGurumeNaviJson(String latitude, String longitude, Consumer<GurumeNavi> function) {
        createGurumeNaviApi().getGurumeNavi(token, latitude, longitude)
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

    public static void parseGurumeNaviJson(String latitude, String longitude, String hit_per_page, String offset_page, Consumer<GurumeNavi> function) {
        createGurumeNaviApi().getGurumeNavi(token, latitude, longitude, hit_per_page, offset_page)
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

    private static GurumeNaviApi createGurumeNaviApi() {
        TypeAdapterFactory typeAdapterFactory =
                TypeAdapters.newFactory(int.class, Integer.class, new IntegerTypeAdapter());

        Gson myGson = new GsonBuilder()
                .registerTypeAdapterFactory(typeAdapterFactory)
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.gnavi.co.jp/")
                .addConverterFactory(GsonConverterFactory.create(myGson))
                .build();

        return retrofit.create(GurumeNaviApi.class);
    }

    public static URL createUrlForGurumeNavi(RequestMap requestMap) {
        GurumeNaviUrl gurumeNaviUrl = new GurumeNaviUrl();

        for (Map.Entry<RequestIds, String> entry : requestMap.entrySet()) {
            gurumeNaviUrl.putRequest(makeRequest(entry.getKey(), entry.getValue()));
        }

        return gurumeNaviUrl.buildUrl();
    }

    private static class GurumeNaviUrl {
        private String host;
        private Request requestKeyid;

        private RequestMap requestMap;

        GurumeNaviUrl() {
            this.host = "https://api.gnavi.co.jp/RestSearchAPI/v3/";

            this.requestKeyid = makeRequest(key_id, token);
            this.requestMap = new RequestMap();

            this.putRequest(requestKeyid);
        }

        void putRequest(Request request) {
            requestMap.put(request.getRequestId(), request.getValue());
        }

        URL buildUrl() {
            try {
                final String requests = makeRequestString();
                final String rawStringUrl = this.host + Question + requests;

                System.out.println(rawStringUrl);
                return new URL(rawStringUrl);
            } catch (MalformedURLException e) {
                return null;
            }
        }

        private String makeRequestString() {
            List<String> stringList = new ArrayList<>();
            for (Map.Entry<RequestIds, String> entry : requestMap.entrySet()) {
                Request request = makeRequest(entry.getKey(), entry.getValue());
                stringList.add(request.toStringForUrl());
            }

            return TextUtils.join(And.toString(), stringList);
        }
    }

}