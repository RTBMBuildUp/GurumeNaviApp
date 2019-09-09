package com.oxymoron.data.room;

import androidx.annotation.Nullable;

import java.util.Objects;

public class RestaurantId {
    private final String id;

    public RestaurantId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        RestaurantId restaurantId = ((RestaurantId) obj);

        return Objects.equals(restaurantId.getId(), this.getId());
    }
}
