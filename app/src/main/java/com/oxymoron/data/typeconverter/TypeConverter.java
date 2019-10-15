package com.oxymoron.data.typeconverter;

public interface TypeConverter<W, P> {
    P from(W wrapper);

    W to(P primitive);
}
