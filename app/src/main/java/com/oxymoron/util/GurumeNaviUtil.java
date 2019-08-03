package com.oxymoron.util;

import android.text.TextUtils;
import android.util.Log;
import com.oxymoron.request.Request;
import com.oxymoron.request.RequestIds;
import com.oxymoron.request.RequestMap;
import com.oxymoron.gson.data.GurumeNavi;
import com.oxymoron.gson.typeadapter.IntegerTypeAdapter;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapters;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.oxymoron.request.Request.makeRequest;
import static com.oxymoron.request.RequestIds.key_id;
import static com.oxymoron.request.Sign.And;
import static com.oxymoron.request.Sign.Question;

public class GurumeNaviUtil {
    private static String token = "bf565ef4fdb696cfb6ff5a911941fa8d";

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

    public static URL createUrlForGurumeNavi(RequestMap requestMap) {
        GurumeNaviUrl gurumeNaviUrl = new GurumeNaviUrl();

        for (Map.Entry<RequestIds, String> entry : requestMap.entrySet()) {
            gurumeNaviUrl.putRequest(makeRequest(entry.getKey(), entry.getValue()));
        }

        return gurumeNaviUrl.buildUrl();
    }

    private static class GurumeNaviUrl {
        private String host;
        private Request requestKey;

        private RequestMap requestMap;

        GurumeNaviUrl() {
            this.host = "https://api.gnavi.co.jp/RestSearchAPI/v3/";

            this.requestKey = makeRequest(key_id, token);
            this.requestMap = new RequestMap();

            this.putRequest(requestKey);
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