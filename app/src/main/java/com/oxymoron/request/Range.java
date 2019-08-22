package com.oxymoron.request;

import java.io.Serializable;

public class Range implements Serializable {
    private Integer value;

    public Range(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}