package com.magicscreencinema.persistence;

public interface QualifierKeyConverter<T> {
    String encode(T key);
    T decode(String key);
}
