package com.findasitter.sitter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http.csrf(customizer -> customizer.disable()).
//                authorizeHttpRequests(request -> request.anyRequest().authenticated()).
//                httpBasic(Customizer.withDefaults()).
//                sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).build();
//    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                //.csrf().disable() // Disable CSRF for testing in Postman
//                .authorizeRequests()
//                .requestMatchers("/users/register", "/login").permitAll() // Allow access to registration and login endpoints
//                .anyRequest().authenticated() // Other requests need authentication
//                .and()
//                .formLogin()
//                .loginPage("/login") // Custom login page (optional)
//                .permitAll()
//                .and()
//                .logout()
//                .permitAll();
//
//        return http.build();
//    }
}