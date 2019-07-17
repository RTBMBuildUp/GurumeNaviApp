package com.oxymoron.util;

import java.util.NoSuchElementException;

public class Optional<T> {
    private T value;

    private Optional(T value) {
        this.value = value;
    }

    public static <T> Optional<T> of(T value) {
        return new Optional<T>(value);
    }

    public static <T> Optional<T> empty() { return of(null);}

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

    public Boolean isPresent() {
        try {
            get();
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void ifPresent(Consumer<T> consumer) {
        if (isPresent()) {
            consumer.accept(value);
        }
    }

    public <R> Optional<R> map(Function<T, R> function) {
        if (isPresent()) {
            return value == null ? Optional.<R>empty() : of(function.apply(value));
        }
        return Optional.<R>empty();
    }
}
