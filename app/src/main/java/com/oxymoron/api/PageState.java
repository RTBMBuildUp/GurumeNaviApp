package com.oxymoron.api;

public class PageState {
    private final Integer offsetPage;

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
