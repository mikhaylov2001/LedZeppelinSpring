package com.javarush.led.lesson12.message;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class NoteClientConfiguration {
    @Bean
    public RestClient getRestClient() {
        return RestClient
                .builder()
                .baseUrl("http://localhost:24130/api/v1.0/notes")
                .build();
    }
}
