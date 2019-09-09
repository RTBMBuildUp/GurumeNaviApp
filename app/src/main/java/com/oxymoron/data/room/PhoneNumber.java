package com.oxymoron.data.room;

import androidx.annotation.Nullable;

import com.oxymoron.util.Optional;

import java.util.Objects;

public class PhoneNumber {
    private final Optional<String> phoneNumber;

    public PhoneNumber(Optional<String> phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Optional<String> getPhoneNumber() {
        return this.phoneNumber;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        PhoneNumber phoneNumber = (PhoneNumber) obj;

        return Objects.equals(this.getPhoneNumber(), phoneNumber.getPhoneNumber());
    }
}
