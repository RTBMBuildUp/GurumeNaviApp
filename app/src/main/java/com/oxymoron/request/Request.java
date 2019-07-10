package com.oxymoron.request;

import static com.oxymoron.request.Sign.Equal;

public class Request {
    private RequestIds requestId;
    private String value;

    public static Request makeRequest(RequestIds requestId, String value) {
        return new Request(requestId, value);
    }

    private Request(RequestIds requestId, String value) {
        this.requestId = requestId;
        this.value = value;
    }

    public RequestIds getRequestId() {
        return requestId;
    }

    public String getValue() {
        return value;
    }

    public String toStringForUrl() {
        return this.requestId + Equal.toString() + this.value;
    }
}
