package com.oxymoron.data.typeconverter;

import com.oxymoron.data.room.PhoneNumber;

public class PhoneNumberConverter implements TypeConverter<PhoneNumber, String> {
    @androidx.room.TypeConverter
    @Override
    public String from(PhoneNumber wrapper) {
        return wrapper.getPhoneNumber().getOrElse("");
    }

    @androidx.room.TypeConverter
    @Override
    public PhoneNumber to(String primitive) {
        return new PhoneNumber(primitive);
    }
}
