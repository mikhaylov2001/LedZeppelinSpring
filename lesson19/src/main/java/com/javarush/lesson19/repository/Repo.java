package com.javarush.lesson19.repository;

import com.javarush.lesson19.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Repo extends JpaRepository<User, Long> {

    User findByLoginIgnoreCase(String login);

}
