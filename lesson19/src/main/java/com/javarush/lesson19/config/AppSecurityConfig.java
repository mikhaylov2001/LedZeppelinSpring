package com.javarush.lesson19.config;

import com.javarush.lesson19.entity.Role;
import com.javarush.lesson19.service.AuthService;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Set;

@Configuration
public class AppSecurityConfig {

    private final AuthService authService;

    public AppSecurityConfig(AuthService authService) {
        this.authService = authService;
    }

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
                        .failureUrl("/login?error=true")
                )
                .oauth2Login(o2l -> o2l
                        .loginPage("/login")
                        .authorizationEndpoint(ae->ae.baseUri("/oauth"))
                        .successHandler(getSuccessHandler())
                )
                .logout(logoutConfig -> logoutConfig
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                        .logoutSuccessUrl("/login?logout=true"))
                .build();
    }


    private AuthenticationSuccessHandler getSuccessHandler() {
        return (request, response, authentication) -> {
            DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
            Set<String> targetAttributes = Set.of("preferred_username", "login", "email");
            String login = oAuth2User.getAttributes()
                    .entrySet()
                    .stream()
                    .filter(entry -> targetAttributes.contains(entry.getKey()))
                    .map(e -> e.getValue().toString().replaceAll("@.*", ""))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Attribute not found"));
            UserDetails userDetails = authService.loadUserByUsername(login);
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    )
            );
            response.sendRedirect("/users");
        };
    }



    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


}
