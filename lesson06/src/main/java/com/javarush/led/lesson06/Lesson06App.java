package com.javarush.led.lesson06;

import com.javarush.led.lesson06.entity.User;
import com.javarush.led.lesson06.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@Slf4j
@SpringBootApplication
public class Lesson06App {

    public static void main(String[] args) {
        var context = SpringApplication.run(Lesson06App.class, args);
        UserService userService = context.getBean(UserService.class);

        for (long id = 1; id <= 3; id++) {
            User user = userService.getById(id);
            log.warn("data {}", user);
        }
        User alisa = userService.getByUsername("Alisa");
        log.warn("Alisa data {}", alisa);

        System.out.println("=".repeat(50));
        List<User> admins = userService.findAllAdminWithName("Ca");
        admins.forEach(System.out::println);

//        userService.deleteById(4L);
        Sort sortByLogin = Sort.by(Sort.Direction.DESC,"login");
        Pageable pageable = PageRequest.of(0, 3, sortByLogin);
        Page<User> page = userService.findAll(pageable);
        do {
            page.forEach(System.out::println);
            System.out.printf("Page %d of %d%n", page.getNumber(), page.getTotalPages());
            if (page.hasNext()) {
                pageable = pageable.next();
                page = userService.findAll(pageable);
            } else {
                break;
            }
        } while (true);

    }

}
