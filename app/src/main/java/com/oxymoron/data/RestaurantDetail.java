package com.oxymoron.data;

import com.oxymoron.api.gson.data.Access;
import com.oxymoron.api.gson.data.Rest;
import com.oxymoron.util.Optional;

public class RestaurantDetail {
    private final String name;
    private final String tel;
    private final String address;
    private final Access access;
    private final String openTime;

    private final Optional<String> imageUrl;

    public RestaurantDetail(Rest restaurant) {
        this.name = restaurant.getName();
        this.tel = restaurant.getTel();
        this.address = restaurant.getAddress();
        this.access = restaurant.getAccess();
        this.openTime = restaurant.getOpentime();

        this.imageUrl = restaurant.getImageUrl().getShopImage();
    }

    public String getName() {
        return name;
    }

    public Optional<String> getTel() {
        return Optional.of(tel);
    }

    public String getAddress() {
        return address;
    }

    public Optional<Access> getAccess() {
        return Optional.of(access);
    }

    public Optional<String> getOpenTime() {
        return Optional.of(openTime);
    }

    public Optional<String> getImageUrl() {
        return imageUrl;
    }
}
