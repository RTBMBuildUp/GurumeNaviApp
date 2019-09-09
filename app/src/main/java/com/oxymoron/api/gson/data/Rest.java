
package com.oxymoron.api.gson.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.oxymoron.util.Optional;

public class Rest {

    @SerializedName("@attributes")
    @Expose
    private Attributes_ attributes;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("update_date")
    @Expose
    private String updateDate;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("name_kana")
    @Expose
    private String nameKana;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("url_mobile")
    @Expose
    private String urlMobile;
    @SerializedName("coupon_url")
    @Expose
    private CouponUrl couponUrl;
    @SerializedName("image_url")
    @Expose
    private ImageUrl imageUrl;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("tel")
    @Expose
    private String tel;
    @SerializedName("tel_sub")
    @Expose
    private String telSub;
    @SerializedName("fax")
    @Expose
    private String fax;
    @SerializedName("opentime")
    @Expose
    private String opentime;
    @SerializedName("holiday")
    @Expose
    private String holiday;
    @SerializedName("access")
    @Expose
    private Access access;
    @SerializedName("parking_lots")
    @Expose
    private String parkingLots;
    @SerializedName("pr")
    @Expose
    private Pr pr;
    @SerializedName("code")
    @Expose
    private Code code;
    @SerializedName("budget")
    @Expose
    private Integer budget;
    @SerializedName("party")
    @Expose
    private String party;
    @SerializedName("lunch")
    @Expose
    private String lunch;
    @SerializedName("credit_card")
    @Expose
    private String creditCard;
    @SerializedName("e_money")
    @Expose
    private String eMoney;
    @SerializedName("flags")
    @Expose
    private Flags flags;

    public Attributes_ getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes_ attributes) {
        this.attributes = attributes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameKana() {
        return nameKana;
    }

    public void setNameKana(String nameKana) {
        this.nameKana = nameKana;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlMobile() {
        return urlMobile;
    }

    public void setUrlMobile(String urlMobile) {
        this.urlMobile = urlMobile;
    }

    public CouponUrl getCouponUrl() {
        return couponUrl;
    }

    public void setCouponUrl(CouponUrl couponUrl) {
        this.couponUrl = couponUrl;
    }

    public ImageUrl getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(ImageUrl imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Optional<String> getTel() {
        return tel.equals("") ? Optional.empty() : Optional.of(tel);
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTelSub() {
        return telSub;
    }

    public void setTelSub(String telSub) {
        this.telSub = telSub;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getOpentime() {
        return opentime;
    }

    public void setOpentime(String opentime) {
        this.opentime = opentime;
    }

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }

    public Access getAccess() {
        return access;
    }

    public void setAccess(Access access) {
        this.access = access;
    }

    public String getParkingLots() {
        return parkingLots;
    }

    public void setParkingLots(String parkingLots) {
        this.parkingLots = parkingLots;
    }

    public Pr getPr() {
        return pr;
    }

    public void setPr(Pr pr) {
        this.pr = pr;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getLunch() {
        return lunch;
    }

    public void setLunch(String lunch) {
        this.lunch = lunch;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public String getEMoney() {
        return eMoney;
    }

    public void setEMoney(String eMoney) {
        this.eMoney = eMoney;
    }

    public Flags getFlags() {
        return flags;
    }

    public void setFlags(Flags flags) {
        this.flags = flags;
    }

}
