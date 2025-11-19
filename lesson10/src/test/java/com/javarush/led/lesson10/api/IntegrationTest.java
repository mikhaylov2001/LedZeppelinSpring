package com.javarush.led.lesson10.api;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import java.lang.annotation.*;

@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
//include testcontainers in annotation.
@Import(IntegrationTest.PostgresTestContainer.class)
//optional: if context not build you can off auto-replace:
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public @interface IntegrationTest {
    @TestConfiguration
    class PostgresTestContainer {

        public static final String POSTGRES_CONTAINER_NAME = "postgres.container.name";
        public static final String DEFAULT_POSTGRES = "postgres:latest";

        @Bean
        @ServiceConnection //replace @DynamicProperties
        public PostgreSQLContainer<?> postgresContainer(Environment environment) {
            String postgresContainerName = environment.getProperty(POSTGRES_CONTAINER_NAME, DEFAULT_POSTGRES);
            return new PostgreSQLContainer<>(postgresContainerName);
        }
    }
}