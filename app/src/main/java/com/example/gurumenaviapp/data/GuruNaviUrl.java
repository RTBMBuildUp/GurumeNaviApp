package com.example.gurumenaviapp.data;

import android.text.TextUtils;
import com.example.gurumenaviapp.data.request.Request;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.gurumenaviapp.data.request.Requests.keyid;
import static com.example.gurumenaviapp.data.request.Sign.And;
import static com.example.gurumenaviapp.data.request.Sign.Question;

public class GuruNaviUrl {
    private String host;
    private Request requestKeyid;

    private List<Request> requestList;

    public GuruNaviUrl(String token) {
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
            stringList.add(request.toString());
        }

        return TextUtils.join(And.toString(), stringList);
    }
}
