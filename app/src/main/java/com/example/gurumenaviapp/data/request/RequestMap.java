package com.example.gurumenaviapp.data.request;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class RequestMap {
    private Map<RequestIds, String> requestMap;

    public RequestMap() {
    }

    public RequestMap(List<Request> requestList) {
        for (Request request : requestList) {
            RequestIds requestIds = RequestIds.valueOf(request.getName());
            requestMap.put(requestIds, request.getContent());
        }
    }

    public RequestMap(Request... requests) {
        for (Request request : requests) {
            RequestIds requestIds = RequestIds.valueOf(request.getName());
            requestMap.put(requestIds, request.getContent());
        }
    }

    public String put(Request request) {
        RequestIds requestIds = RequestIds.valueOf(request.getName());
        return this.put(requestIds, request.getContent());
    }

    public String get(RequestIds key) {
        return getOrElse(key, null);
    }

    public String getOrElse(RequestIds key, String content) {
        String value = requestMap.get(key);
        return value == null ? content : value;
    }

    public Set<Map.Entry<RequestIds, String>> entrySet() {
        return requestMap.entrySet();
    }

    private String put(RequestIds key, String value) {
        return requestMap.put(key, value);
    }
}
