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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/users/makeAdmin", "/users/demoteAdmin", "/users/changePassword").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users", "/users/login", "/users/CreateUser", "/users/PromoteUser", "/users/EditUserProfile","/users/DeleteUser", "/users/ToggleUserEnabled", "/users/createTestPasswords", "/users/setUserPFP", "/create", "/message/ChatCreate", "/message/MessageCreate", "/message", "transaction/TransactionCreate", "transaction/UpdateTransactionStatus").permitAll()
                        .requestMatchers("/", "/login", "/users/get-image-dynamic-type/**", "/users/getPfp","/create", "/error", "/users", "users/RandomSearchByCity", "users/FindByUserID/**", "/users/SearchByCity/**", "/users/ReturnPfp/**", "/message/FindAllChats/**", "users/GetUserIDs/**", "/message/MessageCreate", "/message/UpdateChat/**", "/transaction", "transaction/GetTransactionsByUserID/**", "/profilePicture/**", "/**").permitAll() // Allow unrestricted access to home page
                        .anyRequest().authenticated()) // All other endpoints require authentication
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200")); // Angular origin
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setAllowCredentials(true); // Allow credentials for secure cookies
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}