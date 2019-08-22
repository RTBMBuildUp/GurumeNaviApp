package com.oxymoron.api;

import java.io.Serializable;

public class PageState {
    public final static String name = "PageState";

    private Integer offsetPage;

    public PageState(Integer offsetPage) {
        this.offsetPage = offsetPage;
    }

    Integer getOffsetPage() {
        return this.offsetPage;
    }

    public PageState getNextPageState() {
        return new PageState(this.offsetPage + 1);
    }
}
