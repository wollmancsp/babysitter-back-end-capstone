package com.findasitter.sitter.config;

import com.findasitter.sitter.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // SecurityFilterChain to define security configurations
    @Bean
    public SecurityFilterChain FilterChain(HttpSecurity http) throws Exception {
        http
                // .csrf().disable() // Disable CSRF protection
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users/register").permitAll() // Allow access to registration endpoint
                        .anyRequest().authenticated() // All other requests require authentication
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/users/login") // Define login endpoint
                        .failureUrl("/login?error=true") // Redirect on login failure
                        .permitAll() // Allow access to login form
                )
                .logout(logout -> logout
                        .logoutUrl("/users/logout") // Define logout endpoint
                        .permitAll() // Allow access to log out URL
                );
                http.sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Adjust session management as needed
                );
        return http.build();
    }


    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // AuthenticationManager bean to manage authentication
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
