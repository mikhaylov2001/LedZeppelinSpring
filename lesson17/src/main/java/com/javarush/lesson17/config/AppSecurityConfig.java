package com.javarush.lesson17.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class AppSecurityConfig {

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }


    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(SecurityProperties properties,
                                                                 ObjectProvider<PasswordEncoder> passwordEncoder) {
        UserDetails carl = User.withUsername("Carl")
                .password("{bcrypt}$2a$10$4OlZHUW/ykpj/UQd498K9uL2hd0ox3YQRJgFa4R0FxxxqE5RwEEwu")
                .roles("ADMIN","USER")
                .build();
        return new InMemoryUserDetailsManager(carl);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        PasswordEncoder delegatingPasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return delegatingPasswordEncoder;
    }


}
