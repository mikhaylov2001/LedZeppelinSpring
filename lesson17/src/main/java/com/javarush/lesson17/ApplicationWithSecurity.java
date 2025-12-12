package com.javarush.lesson17;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class ApplicationWithSecurity {
    public static void main(String[] args) {
        var context = SpringApplication.run(ApplicationWithSecurity.class, args);
        System.out.println();
    }
}
