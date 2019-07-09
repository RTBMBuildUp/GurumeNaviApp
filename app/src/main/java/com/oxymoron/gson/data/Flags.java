
package com.oxymoron.gson.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Flags {

    @SerializedName("mobile_site")
    @Expose
    private Integer mobileSite;
    @SerializedName("mobile_coupon")
    @Expose
    private Integer mobileCoupon;
    @SerializedName("pc_coupon")
    @Expose
    private Integer pcCoupon;

    public Integer getMobileSite() {
        return mobileSite;
    }

    public void setMobileSite(Integer mobileSite) {
        this.mobileSite = mobileSite;
    }

    public Integer getMobileCoupon() {
        return mobileCoupon;
    }

    public void setMobileCoupon(Integer mobileCoupon) {
        this.mobileCoupon = mobileCoupon;
    }

    public Integer getPcCoupon() {
        return pcCoupon;
    }

    public void setPcCoupon(Integer pcCoupon) {
        this.pcCoupon = pcCoupon;
    }

}
