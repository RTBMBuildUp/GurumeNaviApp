
package com.oxymoron.api.gson.data;

import android.support.annotation.Nullable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.oxymoron.util.Optional;

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

    public Optional<String> getShopImage1() {
        return Optional.of(shopImage1);
    }

    public void setShopImage1(String shopImage1) {
        this.shopImage1 = shopImage1;
    }

    public Optional<String> getShopImage2() {
        return Optional.of(shopImage2);
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

    @Nullable
    public Optional<String> getShopImage() {
        //bad practice???
        if (getShopImage1().isPresent()) return getShopImage1();
        if (getShopImage2().isPresent()) return  getShopImage2();

        return Optional.empty();
    }

}
