package com.javarush.led.lesson01.config;

import com.javarush.led.lesson01.repository.UserRepository;
import com.javarush.led.lesson01.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class Config {

    @Bean
    public ApplicationProperties applicationProperties() {
        return new ApplicationProperties();
    }

    @Bean
    public SessionCreator sessionCreator(ApplicationProperties applicationProperties) {
        return new SessionCreator(applicationProperties);
    }

    @Bean
    public UserRepository userRepository(SessionCreator sessionCreator) {
        return new UserRepository(sessionCreator);
    }

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserService(userRepository);
    }
}
