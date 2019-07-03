package com.example.gurumenaviapp.data.request;

import static com.example.gurumenaviapp.data.request.Sign.Equal;

public class Request {
    private Requests requests;
    private String content;

    public Request(Requests requests, Object content) {
        this.requests = requests;
        this.content = content.toString();
    }

    public String getName() {
        return this.requests.toString();
    }

    public String getContent() {
        return this.content;
    }

    public String toStringForUrl() {
        return this.requests + Equal.toString() + this.content;
    }
}
