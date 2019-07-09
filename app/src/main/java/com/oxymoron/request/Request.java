package com.oxymoron.request;

import static com.oxymoron.request.Sign.Equal;

public class Request {
    private RequestIds requestIds;
    private String content;

    public static Request makeRequest(RequestIds requests, Object content) {
        return new Request(requests, content);
    }

    private Request(RequestIds requestIds, Object content) {
        this.requestIds = requestIds;
        this.content = content.toString();
    }

    public String getName() {
        return this.requestIds.toString();
    }

    public String getContent() {
        return this.content;
    }

    public String toStringForUrl() {
        return this.requestIds + Equal.toString() + this.content;
    }
}
