package com.oxymoron.api.search.serializable;

import java.io.Serializable;

public class Range implements Serializable {
    private final Integer radius;

    public Range(Integer radius) {
        this.radius = radius;
    }

    public Integer getRadius() {
        return radius;
    }
}
