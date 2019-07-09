
package com.oxymoron.gson.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Code {

    @SerializedName("areacode")
    @Expose
    private String areacode;
    @SerializedName("areaname")
    @Expose
    private String areaname;
    @SerializedName("prefcode")
    @Expose
    private String prefcode;
    @SerializedName("prefname")
    @Expose
    private String prefname;
    @SerializedName("areacode_s")
    @Expose
    private String areacodeS;
    @SerializedName("areaname_s")
    @Expose
    private String areanameS;
    @SerializedName("category_code_l")
    @Expose
    private List<String> categoryCodeL = null;
    @SerializedName("category_name_l")
    @Expose
    private List<String> categoryNameL = null;
    @SerializedName("category_code_s")
    @Expose
    private List<String> categoryCodeS = null;
    @SerializedName("category_name_s")
    @Expose
    private List<String> categoryNameS = null;

    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }

    public String getPrefcode() {
        return prefcode;
    }

    public void setPrefcode(String prefcode) {
        this.prefcode = prefcode;
    }

    public String getPrefname() {
        return prefname;
    }

    public void setPrefname(String prefname) {
        this.prefname = prefname;
    }

    public String getAreacodeS() {
        return areacodeS;
    }

    public void setAreacodeS(String areacodeS) {
        this.areacodeS = areacodeS;
    }

    public String getAreanameS() {
        return areanameS;
    }

    public void setAreanameS(String areanameS) {
        this.areanameS = areanameS;
    }

    public List<String> getCategoryCodeL() {
        return categoryCodeL;
    }

    public void setCategoryCodeL(List<String> categoryCodeL) {
        this.categoryCodeL = categoryCodeL;
    }

    public List<String> getCategoryNameL() {
        return categoryNameL;
    }

    public void setCategoryNameL(List<String> categoryNameL) {
        this.categoryNameL = categoryNameL;
    }

    public List<String> getCategoryCodeS() {
        return categoryCodeS;
    }

    public void setCategoryCodeS(List<String> categoryCodeS) {
        this.categoryCodeS = categoryCodeS;
    }

    public List<String> getCategoryNameS() {
        return categoryNameS;
    }

    public void setCategoryNameS(List<String> categoryNameS) {
        this.categoryNameS = categoryNameS;
    }

}
