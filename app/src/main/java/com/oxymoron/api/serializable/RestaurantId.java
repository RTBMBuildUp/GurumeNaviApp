package com.oxymoron.api.serializable;

import java.io.Serializable;

public class RestaurantId implements Serializable {
    private final String id;

    public RestaurantId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
