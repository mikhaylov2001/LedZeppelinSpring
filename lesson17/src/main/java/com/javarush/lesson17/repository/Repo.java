package com.javarush.lesson17.repository;

import com.javarush.lesson17.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Repo extends JpaRepository<User, Long> {


}
