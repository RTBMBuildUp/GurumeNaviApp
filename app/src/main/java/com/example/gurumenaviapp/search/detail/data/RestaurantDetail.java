package com.example.gurumenaviapp.search.detail.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.example.gurumenaviapp.gson.data.Access;
import com.example.gurumenaviapp.gson.data.Rest;

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

    public String getTel() {
        return tel;
    }

    public String getAddress() {
        return address;
    }

    public Access getAccess() {
        return access;
    }

    public String getOpenTime() {
        return openTime;
    }

    public Bitmap getImage() {
        return image;
    }
}
