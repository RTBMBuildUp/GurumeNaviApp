package com.oxymoron.data.typeconverter;

import com.oxymoron.data.room.PhoneNumber;
import com.oxymoron.util.Optional;

public class PhoneNumberConverter implements TypeConverter<PhoneNumber, String> {
    @androidx.room.TypeConverter
    @Override
    public String from(PhoneNumber wrapper) {
        return wrapper.getPhoneNumber().get();
    }

    @androidx.room.TypeConverter
    @Override
    public PhoneNumber to(String primitive) {
        return new PhoneNumber(Optional.of(primitive));
    }
}
