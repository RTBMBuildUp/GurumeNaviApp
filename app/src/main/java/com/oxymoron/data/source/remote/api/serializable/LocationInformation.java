package com.oxymoron.data.source.remote.api.serializable;

import androidx.annotation.Nullable;

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

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        LocationInformation locationInformation = ((LocationInformation) obj);

        return locationInformation.getLatitude().equals(this.getLatitude()) &&
                locationInformation.getLongitude().equals(this.getLongitude());
    }
}
