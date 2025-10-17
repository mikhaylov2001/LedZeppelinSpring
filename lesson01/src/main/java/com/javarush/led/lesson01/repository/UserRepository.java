package com.javarush.led.lesson01.repository;

import com.javarush.led.lesson01.config.SessionCreator;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@ToString
public class UserRepository {
    private final SessionCreator sessionCreator;
}
