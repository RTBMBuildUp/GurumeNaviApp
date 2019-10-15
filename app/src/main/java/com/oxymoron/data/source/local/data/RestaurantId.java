package com.oxymoron.data.source.local.data;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Objects;

public class RestaurantId implements Serializable {
    private final String id;

    public RestaurantId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof RestaurantId)) return false;
        RestaurantId restaurantId = ((RestaurantId) obj);

        return Objects.equals(restaurantId.getId(), this.getId());
    }

    @Override
    public int hashCode() {
        int result = 69;

        return 31 * result + this.getId().hashCode();
    }
}
