package com.oxymoron.data.typeconverter;

import com.oxymoron.api.gson.data.Access;

public class AccessConverter implements TypeConverter<Access, String> {
    @androidx.room.TypeConverter
    @Override
    public String from(Access wrapper) {
        return wrapper.showUserAround();
    }

    @androidx.room.TypeConverter
    @Override
    public Access to(String primitive) {
        return new Access();
    }
}
