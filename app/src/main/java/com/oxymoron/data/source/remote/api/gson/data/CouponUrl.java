
package com.oxymoron.data.source.remote.api.gson.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CouponUrl {

    @SerializedName("pc")
    @Expose
    private String pc;
    @SerializedName("mobile")
    @Expose
    private String mobile;

    public String getPc() {
        return pc;
    }

    public void setPc(String pc) {
        this.pc = pc;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}
