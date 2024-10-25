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
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests(auth -> auth
                        // .requestMatchers(HttpMethod.POST, "/users").permitAll() // Allow user registration without login
                        // .requestMatchers(HttpMethod.POST, "/users/login").permitAll() // Allow login without authentication
                        .requestMatchers(HttpMethod.POST, "/users/create").permitAll()
                        .requestMatchers("/", "/login", "/create", "/error").permitAll() // Allow unrestricted access to home page
                        // .requestMatchers("/users").authenticated() // Only allow authenticated users to access /users
                        .anyRequest().authenticated()) // All other endpoints require authentication
                .formLogin(form -> form
                        .loginPage("/login") // Specify custom login page
                        .usernameParameter("user_emailaddress") // Use "user_emailaddress" instead of "username"
                        .passwordParameter("user_password") // Keep the default password parameter
                        .defaultSuccessUrl("/", true) // Redirect to home page on successful login
                        .permitAll()) // Allow everyone to access the login page
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .build();
    }
}