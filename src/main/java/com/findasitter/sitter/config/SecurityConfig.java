package com.findasitter.sitter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(customizer -> customizer.disable())
                .authorizeRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/login").permitAll()
                        .requestMatchers("/users").permitAll()  // Allows unrestricted access to the /users endpoint
                        .requestMatchers("/users/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/login").permitAll()
                        .requestMatchers("/users/**").permitAll()  // Allows unrestricted access to the /users endpoint
                        .requestMatchers("/users/login").permitAll()
                        .requestMatchers("/message/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/message/**").permitAll()
                        .requestMatchers("/message/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/message").permitAll()
                        .requestMatchers("/message").permitAll()
                        .requestMatchers(HttpMethod.POST, "/message/UpdateChat").permitAll()
                        .requestMatchers("/message/UpdateChat").permitAll()
                        .requestMatchers("/").permitAll()
                        .anyRequest().authenticated()) // Other requests require authentication
                // .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
}