package com.dawn.mall.dto;

public interface Converter<S, T> {

    T convert(S s);
}
