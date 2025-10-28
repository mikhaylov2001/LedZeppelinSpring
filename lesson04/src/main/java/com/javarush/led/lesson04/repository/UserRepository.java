package com.javarush.led.lesson04.repository;

import com.javarush.led.lesson04.config.SessionCreator;
import com.javarush.led.lesson04.entity.User;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@AllArgsConstructor
@ToString
public class UserRepository {
    private final SessionCreator sessionCreator;
    private final Map<Long, User> users = new ConcurrentHashMap<>();

    public void save(User user) {
        users.put(user.getId(), user);
        System.out.println(user + " saved to database");
    }

    public User getById(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new NoSuchElementException("User not found with id " + id);
        }
        return user;
    }
}
