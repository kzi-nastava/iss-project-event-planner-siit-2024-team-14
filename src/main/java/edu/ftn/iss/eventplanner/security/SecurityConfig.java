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
                .cors().and()
                .csrf().disable()  // Disabling CSRF if not needed (adjust as per your need)
                .authorizeHttpRequests()
                .requestMatchers("/api/providers/**", "/api/users/**", "/api/organizers/**",
                        "/api/events/*", "/api/solutions/*", "/api/comments/*", "/api/notifications/*", "/api/notifications", "/api/notifications/**",
                        "/api/providers/register", "/api/providers/activate", "/api/users/register",
                        "/api/users/activate", "/api/users/login", "/ws/**", "/api/services", "/api/services/*", "/api/services/**", "/api/products", "/api/products/*").permitAll()  // Dodata podrška za WebSocket
                .anyRequest().authenticated()  // Ostale rute zahtevaju autentifikaciju
                .and()
                .formLogin().disable();  // Disable form login (ako koristiš JWT ili neki drugi metod)

        return http.build();
    }
}
