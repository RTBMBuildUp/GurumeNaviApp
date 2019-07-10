package com.oxymoron.request;

import com.oxymoron.util.Optional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RequestMap {
    private Map<RequestIds, String> requestMap = new HashMap<>();

    public RequestMap() {
    }

    public RequestMap(List<Request> requestList) {
        for (Request request : requestList) {
            requestMap.put(request.getRequestId(), request.getValue());
        }
    }

    public RequestMap(Request... requests) {
        for (Request request : requests) {
            requestMap.put(request.getRequestId(), request.getValue());
        }
    }

    public String put(RequestIds key, String value) {
        return requestMap.put(key, value);
    }

    public String get(RequestIds key) {
        return getOrElse(key, null);
    }

    public String getOrElse(RequestIds key, String defaultValue) {
        Optional<String> optional = Optional.of(requestMap.get(key));

        return optional.getOrElse(defaultValue);
    }

    public Set<Map.Entry<RequestIds, String>> entrySet() {
        return requestMap.entrySet();
    }
}
