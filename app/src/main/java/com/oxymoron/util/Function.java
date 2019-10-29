package com.oxymoron.util;

public interface Function<T, R> {
    R apply(T value);
}