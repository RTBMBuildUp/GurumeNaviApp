package com.example.gurumenaviapp.util;

import android.text.TextUtils;
import android.util.Log;
import com.example.gurumenaviapp.data.request.Request;
import com.example.gurumenaviapp.gson.data.GurumeNavi;
import com.example.gurumenaviapp.gson.typeadapter.IntegerTypeAdapter;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapters;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.gurumenaviapp.data.request.Requests.keyid;
import static com.example.gurumenaviapp.data.request.Sign.And;
import static com.example.gurumenaviapp.data.request.Sign.Question;

public class GurumeNaviUtil {
    private static String token = "bf565ef4fdb696cfb6ff5a911941fa8d";

    public static GurumeNavi parseGurumeNaviJson(String rawStringUrl) {
        TypeAdapterFactory typeAdapterFactory = TypeAdapters.newFactory(int.class, Integer.class, new IntegerTypeAdapter());
        try {
            return new GsonBuilder()
                    .registerTypeAdapterFactory(typeAdapterFactory)
                    .create()
                    .fromJson(
                            new InputStreamReader(
                                    new URL(rawStringUrl).openStream()
                            ),
                            GurumeNavi.class
                    );
        } catch (IOException e) {
            Log.d("error", "parseGurumeNaviJson: " + e);
        }
        return null;
    }

    public static URL createUrlForGurumeNavi(List<Request> requestList) {
        GurumeNaviUrl gurumeNaviUrl = new GurumeNaviUrl();

        for (Request request : requestList) {
            gurumeNaviUrl.addRequest(request);
        }

        return gurumeNaviUrl.buildUrl();
    }

    private static class GurumeNaviUrl {
        private String host;
        private Request requestKeyid;

        private List<Request> requestList;

        public GurumeNaviUrl() {
            this.host = "https://api.gnavi.co.jp/RestSearchAPI/v3/";

            this.requestKeyid = new Request(keyid, token);
            this.requestList = new ArrayList<>();

            this.requestList.add(requestKeyid);
        }

        public void addRequest(Request request) {
            requestList.add(request);
        }

        public URL buildUrl() {
            try {
                final String requests = makeRequests();
                final String rawStringUrl = this.host + Question + requests;

                System.out.println(rawStringUrl);
                return new URL(rawStringUrl);
            } catch (MalformedURLException e) {
                return null;
            }
        }

        private String makeRequests() {
            List<String> stringList = new ArrayList<>();
            for (Request request : requestList) {
                stringList.add(request.toStringForUrl());
            }

            return TextUtils.join(And.toString(), stringList);
        }
    }

}
