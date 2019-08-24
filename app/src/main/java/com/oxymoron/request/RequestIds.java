package com.oxymoron.request;

public enum RequestIds {
    key_id("keyid"), restaurant_id("restaurant_id"), latitude("latitude"),
    longitude("longitude"), range("range"), hit_per_page("hit_per_page"),
    offset_page("offset_page");

    private final String id;

    RequestIds(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
