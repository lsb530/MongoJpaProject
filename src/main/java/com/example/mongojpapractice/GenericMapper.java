package com.example.mongojpapractice;

public interface GenericMapper<D, E> {
    D toDto(E e);
    E toEntity(D d);
}
