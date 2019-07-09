package com.oxymoron.search.candidate.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.oxymoron.gson.data.Access;
import com.oxymoron.gson.data.Rest;

import java.io.IOException;
import java.net.URL;

public class RestaurantThumbnail {
    private String name;
    private Access access;

    private Bitmap image;

    private String restaurantId;

    public RestaurantThumbnail() {
    }

    public RestaurantThumbnail(Rest restaurant) {
        this.name = restaurant.getName();
        this.access = restaurant.getAccess();

        try {
            this.image = BitmapFactory.decodeStream(new URL(restaurant.getImageUrl().getShopImage()).openStream());
        } catch (IOException e) {
            this.image = null;
        }

        this.restaurantId = restaurant.getId();
    }

    public String getName() {
        return name;
    }

    public Access getAccess() {
        return access;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getRestaurantId() {
        return restaurantId;
    }
}