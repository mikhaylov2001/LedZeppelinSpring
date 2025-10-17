package com.javarush.led.lesson01.config;

import com.javarush.led.lesson01.repository.UserRepository;
import com.javarush.led.lesson01.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import static com.javarush.led.lesson01.config.ApplicationProperties.*;

@Configuration
@PropertySource("classpath:application.properties")
public class Config {

    @Bean
    public ApplicationProperties applicationProperties(
            @Value("${" + HIBERNATE_CONNECTION_URL + "}") String url,
            @Value("${" + HIBERNATE_CONNECTION_USERNAME + "}") String username,
            @Value("${" + HIBERNATE_CONNECTION_PASSWORD + "}") String password,
            @Value("${" + HIBERNATE_CONNECTION_DRIVER_CLASS + "}") String driverClass
    ) {
        return new ApplicationProperties(
                url, username, password, driverClass
        );
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
