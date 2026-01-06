package com.javarush.lesson19;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class ApplicationWithKeycloak {
    public static void main(String[] args) {
        var context = SpringApplication.run(ApplicationWithKeycloak.class, args);
        System.out.println("http://localhost:8080/ - lesson19");
    }
}
