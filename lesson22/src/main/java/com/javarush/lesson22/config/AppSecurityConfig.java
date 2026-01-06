package com.javarush.lesson22.config;

import com.javarush.lesson22.entity.Role;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class AppSecurityConfig {

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/registration").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/logout").permitAll()

                        .requestMatchers(HttpMethod.GET,"/users")
                        .hasAnyAuthority(Role.ADMIN.getAuthority(),Role.USER.getAuthority())

                        .requestMatchers(HttpMethod.POST,"/users")
                        .hasAuthority(Role.ADMIN.getAuthority())

                        .anyRequest()
                        .authenticated()
                )
                .formLogin(formConfig -> formConfig
                        .loginPage("/login")
                        .usernameParameter("login")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/users", true)
                        .failureUrl("/login?error")
                )
                .build();
    }


//    @Bean
//    public InMemoryUserDetailsManager inMemoryUserDetailsManager(SecurityProperties properties,
//                                                                 ObjectProvider<PasswordEncoder> passwordEncoder) {
//        UserDetails carl = User.withUsername("Carl")
//                .password("{bcrypt}$2a$10$4OlZHUW/ykpj/UQd498K9uL2hd0ox3YQRJgFa4R0FxxxqE5RwEEwu")
//                .roles("ADMIN", "USER")
//                .build();
//        return new InMemoryUserDetailsManager(carl);
//    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


}
