
package com.oxymoron.data.source.remote.api.gson.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pr {

    @SerializedName("pr_short")
    @Expose
    private String prShort;
    @SerializedName("pr_long")
    @Expose
    private String prLong;

    public String getPrShort() {
        return prShort;
    }

    public void setPrShort(String prShort) {
        this.prShort = prShort;
    }

    public String getPrLong() {
        return prLong;
    }

    public void setPrLong(String prLong) {
        this.prLong = prLong;
    }

}
