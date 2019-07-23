package com.oxymoron.search.detail.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.oxymoron.gson.data.Access;
import com.oxymoron.gson.data.Rest;
import com.oxymoron.util.Optional;

import java.io.IOException;
import java.net.URL;

public class RestaurantDetail {
    private String name;
    private String tel;
    private String address;
    private Access access;
    private String openTime;

    private Bitmap image;

    public RestaurantDetail() {
    }

    public RestaurantDetail(Rest restaurant) {
        this.name = restaurant.getName();
        this.tel = restaurant.getTel();
        this.address = restaurant.getAddress();
        this.access = restaurant.getAccess();
        this.openTime = restaurant.getOpentime();

        try {
            this.image = BitmapFactory.decodeStream(new URL(restaurant.getImageUrl().getShopImage()).openStream());
        } catch (IOException e) {
            this.image = null;
            System.out.println(e);
        }
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

    public Optional<Bitmap> getImage() {
        return Optional.of(image);
    }
}
