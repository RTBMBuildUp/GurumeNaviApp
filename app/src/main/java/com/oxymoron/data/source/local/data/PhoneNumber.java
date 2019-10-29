package com.oxymoron.data.source.local.data;

import androidx.annotation.Nullable;

import com.oxymoron.util.Optional;

import java.util.Objects;

public class PhoneNumber {
    private final String phoneNumber;

    public PhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Optional<String> getPhoneNumber() {
        return Optional.of(this.phoneNumber);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof PhoneNumber)) return false;
        PhoneNumber phoneNumber = (PhoneNumber) obj;

        return Objects.equals(this.getPhoneNumber(), phoneNumber.getPhoneNumber());
    }
}
