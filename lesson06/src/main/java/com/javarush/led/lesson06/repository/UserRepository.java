package com.javarush.led.lesson06.repository;

import com.javarush.led.lesson06.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

        Optional<User> findByLogin(String login);

        @Query("""
                select u from User u
                where u.login like concat(:login, '%') and upper(u.role) like upper(concat('%', :role, '%'))""")
        Stream<User> findUserBy(@Param("login") String loginStartingWith, @Param("role") String roleContaining);


        @Query("select u from User u where u.id > :id and u.role = :role")
        Stream<User> getByMinIdAndRole(@Param("id") Long idHoHo, String role);
}
