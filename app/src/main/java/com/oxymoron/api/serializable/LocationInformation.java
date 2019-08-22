package com.oxymoron.api.serializable;

import java.io.Serializable;

public class LocationInformation implements Serializable {
    private final Double latitude;
    private final Double longitude;

    public LocationInformation(Double latitude, Double longitude) {
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
