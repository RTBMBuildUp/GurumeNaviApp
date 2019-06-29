
package com.example.gurumenaviapp.gson.data;

import android.support.annotation.Nullable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.net.MalformedURLException;
import java.net.URL;

public class ImageUrl {

    @SerializedName("shop_image1")
    @Expose
    private String shopImage1;
    @SerializedName("shop_image2")
    @Expose
    private String shopImage2;
    @SerializedName("qrcode")
    @Expose
    private String qrcode;

    public String getShopImage1() {
        return shopImage1;
    }

    public void setShopImage1(String shopImage1) {
        this.shopImage1 = shopImage1;
    }

    public String getShopImage2() {
        return shopImage2;
    }

    public void setShopImage2(String shopImage2) {
        this.shopImage2 = shopImage2;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public Boolean isExistShopImage() {
        //bad practice???
        try {
            new URL(shopImage1);
            new URL(shopImage2);

            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    @Nullable
    public String getShopImage() {
        //bad practice???
        try {
            new URL(shopImage1);
        } catch (MalformedURLException e) {
            try {
                new URL(shopImage2);
            } catch (MalformedURLException error) {
                return "https://rr.img.naver.jp/mig?src=http%3A%2F%2Fblog-imgs-21.fc2.com%2Fq%2Fr%2Fe%2Fqreia%2F3d38c4a1.jpg&twidth=1200&theight=1200&qlt=80&res_format=jpg&op=r";
            }

            return shopImage2;
        }

        return shopImage1;
    }

}
