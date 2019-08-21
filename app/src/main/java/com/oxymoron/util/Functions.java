package com.oxymoron.util;

public interface Functions {
    public interface Function2<A, B, R> {
        public R apply(A left, B right);
    }

    public interface Function3<A, B, C, R> {
        public R apply(A first, B second, C third);
    }
}
