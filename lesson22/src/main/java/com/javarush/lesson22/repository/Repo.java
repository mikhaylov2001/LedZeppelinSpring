package com.javarush.lesson22.repository;

import com.javarush.lesson22.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Repo extends JpaRepository<User, Long> {

    User findByLogin(String login);

}
