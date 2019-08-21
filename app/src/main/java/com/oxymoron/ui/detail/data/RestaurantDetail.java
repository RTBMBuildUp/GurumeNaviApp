package com.oxymoron.ui.detail.data;

import com.oxymoron.gson.data.Access;
import com.oxymoron.gson.data.Rest;
import com.oxymoron.util.Optional;

public class RestaurantDetail {
    private String name;
    private String tel;
    private String address;
    private Access access;
    private String openTime;

    private Optional<String> imageUrl;

    public RestaurantDetail() {
    }

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
