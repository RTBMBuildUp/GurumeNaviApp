package com.oxymoron.data.room;

import com.oxymoron.util.Optional;

public class ImageUrl {
    private final String url;

    public ImageUrl(String url) {
        this.url = url;
    }

    public Optional<String> getUrl() {
        return Optional.of(url);
    }
}
