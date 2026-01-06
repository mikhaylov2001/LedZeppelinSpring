package com.javarush.lesson21.repository;

import com.javarush.lesson21.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Repo extends JpaRepository<User, Long> {

    User findByLoginIgnoreCase(String login);

}
