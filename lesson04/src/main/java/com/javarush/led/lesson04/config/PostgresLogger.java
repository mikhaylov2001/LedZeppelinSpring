package com.javarush.led.lesson04.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Conditional(PostgresqlCondition.class)
public class PostgresLogger {

    @PostConstruct
    public void init() {
        log.info("PostgresLogger found");
    }
}
