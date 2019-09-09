package com.oxymoron.data.room;

import com.oxymoron.util.Optional;

public class ImageUrl {
    private final Optional<String> url;

    public ImageUrl(String url) {
        this.url = Optional.of(url);
    }

    public Optional<String> getUrl() {
        return url;
    }
}
