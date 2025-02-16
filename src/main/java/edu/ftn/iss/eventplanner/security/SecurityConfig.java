package edu.ftn.iss.eventplanner.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
        http
                .csrf().disable()  // Disabling CSRF if not needed (adjust as per your need)
                .authorizeHttpRequests()
                .requestMatchers("/api/providers/**", "/api/users/**", "/api/organizers/**", "/api/notifications",
                        "/api/events/filter", "/api/events/top5", "/api/events/categories", "/api/events/locations",
                        "/api/events/all", "/api/solutions/top5", "/api/solutions/all", "/api/solutions/filter",
                        "/api/solutions/categories", "/api/solutions/locations", "/api/comments/*",
                        "/api/providers/register", "/api/providers/activate", "/api/users/register",
                        "/api/users/activate", "/api/users/login", "/ws/**").permitAll()  // Dodata podrška za WebSocket
                .anyRequest().authenticated()  // Ostale rute zahtevaju autentifikaciju
                .and()
                .formLogin().disable();  // Disable form login (ako koristiš JWT ili neki drugi metod)

        return http.build();
    }
}
