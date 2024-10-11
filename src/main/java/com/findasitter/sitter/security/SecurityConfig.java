package com.findasitter.sitter.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/register").permitAll() // Allow public access to /api/register
                        .anyRequest().authenticated() // Require authentication for all other requests
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/api/login") // Custom login URL
                        .permitAll() // Allow everyone to access the login URL
                )
                .logout(logout -> logout
                        .logoutUrl("/api/logout") // Custom logout URL
                        .permitAll() // Allow everyone to access the logout URL
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
