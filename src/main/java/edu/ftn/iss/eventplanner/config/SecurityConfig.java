package edu.ftn.iss.eventplanner.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Isključi CSRF zaštitu (za testing)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")  // Admin može pristupiti /api/admin/**
                        .requestMatchers("/api/user/**").hasRole("USER")   // User može pristupiti /api/user/**
                        .anyRequest().authenticated()  // Svi drugi zahtevi zahtevaju autentifikaciju
                )
                .formLogin(withDefaults())  // Omogućava form-based login
                .httpBasic(withDefaults());  // Omogućava Basic Authentication

        return http.build();
    }
}
