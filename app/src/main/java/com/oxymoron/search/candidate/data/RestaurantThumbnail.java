package com.oxymoron.search.candidate.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bumptech.glide.Glide;
import com.oxymoron.gson.data.Access;
import com.oxymoron.gson.data.Rest;
import com.oxymoron.util.Optional;

import java.io.IOException;
import java.net.URL;

public class RestaurantThumbnail {
    private String name;
    private Access access;
    private Optional<String> imageUrl;

    private String restaurantId;

    public RestaurantThumbnail() {
    }

    public RestaurantThumbnail(Rest restaurant) {
        this.name = restaurant.getName();
        this.access = restaurant.getAccess();
        this.imageUrl = restaurant.getImageUrl().getShopImage();

        this.restaurantId = restaurant.getId();
    }

    public String getName() {
        return name;
    }

    public Optional<Access> getAccess() {
        return Optional.of(access);
    }

    public Optional<String> getImageUrl() {
        return imageUrl;
    }

    public String getRestaurantId() {
        return restaurantId;
    }
}
