
package com.oxymoron.api.gson.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GurumeNavi {

    @SerializedName("@attributes")
    @Expose
    private Attributes attributes;
    @SerializedName("total_hit_count")
    @Expose
    private Integer totalHitCount;
    @SerializedName("hit_per_page")
    @Expose
    private Integer hitPerPage;
    @SerializedName("page_offset")
    @Expose
    private Integer pageOffset;
    @SerializedName("rest")
    @Expose
    private List<Rest> rest = null;

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public Integer getTotalHitCount() {
        return totalHitCount;
    }

    public void setTotalHitCount(Integer totalHitCount) {
        this.totalHitCount = totalHitCount;
    }

    public Integer getHitPerPage() {
        return hitPerPage;
    }

    public void setHitPerPage(Integer hitPerPage) {
        this.hitPerPage = hitPerPage;
    }

    public Integer getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(Integer pageOffset) {
        this.pageOffset = pageOffset;
    }

    public List<Rest> getRest() {
        return rest;
    }

    public void setRest(List<Rest> rest) {
        this.rest = rest;
    }

}
