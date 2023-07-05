package com.cg.service;

import java.util.List;
import java.util.Optional;

public interface IGeneralService<E, T> {
    List<E> findAll();
    //Optional<E> để khi để xử lý trường hợp trả về null từ một phương thức,
    // giúp tránh được các lỗi NullPointerException
    Optional<E> findById(T id);


    E save(E e);

    void delete(E e);

    void deleteById(T id);
}
