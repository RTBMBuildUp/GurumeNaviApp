package com.example.gurumenaviapp.util;

import java.util.NoSuchElementException;

public class Optional<T> {
    private T value;

    public Optional(T value) {
        this.value = value;
    }

    public static <T> Optional<T> of(T value) {
        return new Optional<T>(value);
    }

    public T get() {
        if (value != null) {
            return value;
        } else {
            throw new NoSuchElementException();
        }
    }

    public T getOrElse(T defaultValue) {
        try {
            return this.get();
        } catch (NoSuchElementException e) {
            return defaultValue;
        }
    }
}
