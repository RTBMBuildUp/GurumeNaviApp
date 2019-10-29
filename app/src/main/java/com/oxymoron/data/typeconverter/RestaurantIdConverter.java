package com.oxymoron.data.typeconverter;

import androidx.room.TypeConverter;

import com.oxymoron.data.source.local.data.RestaurantId;

public class RestaurantIdConverter implements com.oxymoron.data.typeconverter.TypeConverter<RestaurantId, String> {
    @TypeConverter
    public String from(RestaurantId id) {
        return id.getId();
    }

    @TypeConverter
    public RestaurantId to(String id) {
        return new RestaurantId(id);
    }
}
