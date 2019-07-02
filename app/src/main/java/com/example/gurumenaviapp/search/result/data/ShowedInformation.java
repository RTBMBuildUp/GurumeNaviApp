package com.example.gurumenaviapp.search.result.data;

import android.graphics.Bitmap;
import com.example.gurumenaviapp.gson.data.Access;

public class ShowedInformation {
    private String name;
    private String address;
    private String tel;
    private Access access;

    private Bitmap image;

    public ShowedInformation() {}

    public ShowedInformation(String name, String address, String tel, Access access, Bitmap image) {
        this.name = name;
        this.address = address;
        this.tel = tel;
        this.access = access;

        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getTel() {
        return tel;
    }

    public Access getAccess() { return access; }

    public Bitmap getImage() {
        return image;
    }
}
