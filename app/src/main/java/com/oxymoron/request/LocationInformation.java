package com.oxymoron.request;

import java.io.Serializable;

public class LocationInformation implements Serializable {
    public final static String name = "LocationInformation";

    private double latitude;
    private double longitude;

    public LocationInformation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
