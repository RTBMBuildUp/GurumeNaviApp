package com.oxymoron.request;

import java.io.Serializable;

public class PageState implements Serializable {
    public final static String name = "PageState";

    private Integer offsetPage;

    public PageState(Integer offsetPage) {
        this.offsetPage = offsetPage;
    }

    public Integer getOffsetPage() {
        return this.offsetPage;
    }

    public PageState getNextPageState() {
        return new PageState(this.offsetPage + 1);
    }
}
