package com.javarush.led.lesson08.repository;

import java.util.Optional;
import java.util.stream.Stream;

public interface Repo<T> {
    Stream<T> getAll();

    Optional<T> get(Long id);

    Optional<T> create(T input);

    Optional<T> update(T input);

    boolean delete(Long id);
}
