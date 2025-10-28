package com.javarush.led.lesson03;

import com.javarush.led.lesson03.config.Config;
import com.javarush.led.lesson03.entity.User;
import com.javarush.led.lesson03.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App03 {


    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        UserService userService = context.getBean(UserService.class);
        User[] users={
                new User(1L,"admin","pass","ADMIN"),
                new User(3L,"guest","asda","GUEST"),
                new User(2L,"user","qweqwe","USER"),
        };
        userService.save(users);
        for (Long i = 1L; i <= 3; i++) {
            User user = userService.getById(i);
            System.err.println(user+"\n");
        }
    }
}
