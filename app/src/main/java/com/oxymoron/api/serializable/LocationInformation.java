package com.oxymoron.api.serializable;

import java.io.Serializable;

public class LocationInformation implements Serializable {
    public final static String name = "LocationInformation";

    private double latitude;
    private double longitude;

    public LocationInformation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
