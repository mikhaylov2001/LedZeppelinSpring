package com.javarush.led.lesson06.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;


@Data
@Configuration
@ConfigurationProperties(prefix = "spring.app-config")
public class AppConfig {

    private int tryCount;
    private String filePath="NIL";
    private Long[] longs;
    private Set<String> data;
    private Map<Long, String> map;
}
