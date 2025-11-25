package com.javarush.led.lesson12.message;

import java.util.List;

public interface RestService<Q,A> {
    List<A> findAll();

    A findById(long id);

    A create(Q userTo);

    A update(Q userTo);

    boolean removeById(Long id);
}
