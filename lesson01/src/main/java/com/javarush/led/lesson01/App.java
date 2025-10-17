package com.javarush.led.lesson01;

import com.javarush.led.lesson01.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {

    public static final String BASE_PACKAGE = "com.javarush.led.lesson01";

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(BASE_PACKAGE);
        UserService userService = context.getBean(UserService.class);
        System.out.println(userService);
    }
}
