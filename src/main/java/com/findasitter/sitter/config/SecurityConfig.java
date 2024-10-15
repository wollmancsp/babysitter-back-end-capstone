package com.findasitter.sitter.config;

import com.findasitter.sitter.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // SecurityFilterChain to define security configurations
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Permit access to the registration and login-related endpoints
                        .requestMatchers("/users/register", "/login", "/users/login", "/users/logout", "/login?error=true").permitAll()
                        .anyRequest().authenticated() // Require authentication for all other requests
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/users/login") // Define login processing endpoint
                        .loginPage("/login") // Define custom login page
                        .failureUrl("/login?error=true") // Redirect on login failure
                        .permitAll() // Allow access to login page
                )
                .logout(logout -> logout
                        .logoutUrl("/users/logout") // Define logout endpoint
                        .permitAll() // Allow access to log out
                )
                .sessionManagement(session -> session
                        // Use STATELESS policy temporarily to rule out session-related issues
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
//        http
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/users/register").permitAll()
//                        .requestMatchers("/login","/users/login", "/users/logout").permitAll()// Allow access to registration endpoint
//                        .anyRequest().authenticated() // All other requests require authentication
//                )
//                .formLogin(form -> form
//                        .loginProcessingUrl("/users/login") // Define login endpoint
//                        .loginPage("/login") // Define login page
//                        .failureUrl("/login?error=true") // Redirect on login failure
//                        .permitAll() // Allow access to login form
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/users/logout") // Define logout endpoint
//                        .permitAll() // Allow access to log out URL
//                );
//                http.sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Adjust session management as needed
//                );
        return http.build();
    }


    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    // AuthenticationManager bean to manage authentication
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
