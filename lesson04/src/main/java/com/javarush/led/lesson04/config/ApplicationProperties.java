package com.javarush.led.lesson04.config;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Slf4j
@ToString
public class ApplicationProperties extends Properties {

    public static final String HIBERNATE_CONNECTION_URL = "hibernate.connection.url";
    public static final String HIBERNATE_CONNECTION_USERNAME = "hibernate.connection.username";
    public static final String HIBERNATE_CONNECTION_PASSWORD = "hibernate.connection.password";
    public static final String HIBERNATE_CONNECTION_DRIVER_CLASS = "hibernate.connection.driver_class";
    public static final String ENV_EXPRESSION = "\\$\\{[A-Z_]+:.+}";

    public ApplicationProperties(
            String url,
            String username,
            String password,
            String driverClass
    ) {
        this.put(HIBERNATE_CONNECTION_URL, url);
        this.put(HIBERNATE_CONNECTION_USERNAME, username);
        this.put(HIBERNATE_CONNECTION_PASSWORD, password);
        this.put(HIBERNATE_CONNECTION_DRIVER_CLASS, driverClass);
    }

}
