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
                .cors().and().csrf().disable()  // Disabling CSRF if not needed (adjust as per your need)
                .authorizeHttpRequests()
                .requestMatchers("/api/providers/get/","/api/providers/deactivate/", "/api/providers/register", "/api/providers/activate", "/api/providers/update", "/api/providers/get-photos/", "/api/providers/update-photo/",
                        "/api/users/changePassword",  "/api/users/login","/api/users/register", "/api/users/activate",
                        "/api/organizers/register", "/api/organizers/update-photo/", "/api/organizers/update", "/api/organizers/get/", "/api/organizers/*", "/api/organizers/**", "/api/organizers/deactivate/", "/api/organizers/activate", "/api/organizers/get-photo/",
                        "/api/events/*/products", "/api/events/filter", "/api/events/categories", "/api/events/top5", "/api/events/create-event", "/api/events/all", "/api/events/by-organizer/", "/api/events/locations", "/api/events/*/budget", "/api/events/*/purchases", "/api/events/*/purchases/", "/api/events/*/budget/",
                        "/api/solutions/all", "/api/solutions/locations", "/api/solutions/filter", "/api/solutions/top5", "/api/solutions/categories",
                        "/api/comments/all", "/api/comments/approve", "/api/comments/approved", "/api/comments/delete", "/api/comments/pending",
                        "/api/notifications", "/api/notifications/mute-notifications", "/api/notifications/mark-all-as-read", "/api/notifications/mute-notifications/status", "/api/notifications/unread-count",
                        "/ws/**", "/api/location/search",
                        "/api/services",
                        "/api/products", "/api/products/search",
                        "/api/bookings", "/api/bookings/delete", "/api/bookings/reserve", "/api/bookings/approve", "/api/bookings/all", "/api/bookings/requests", "/api/bookings/available-start-times",
                        "/api/invitations", "/api/invitations/bulk","/api/invitations/register", "/api/invitations/activate",
                        "/api/reports", "/api/reports/approve", "/api/reports/delete", "/api/reports/pending", "/api/reports/all", "/api/reports/requests",
                        "/photos/**", "/event-photo/**",
                        "/api/chat", "/api/chat/blocked-users",
                        "/api/event-types/update", "/api/event-types/create", "/api/event-types/get-all-event", "/api/event-types/get-all-categories", "/api/event-types/de-activate/",
                        "/api/categories/get-all-et", "/api/categories/get-by-event-type").permitAll()
                .anyRequest().authenticated()  // Ostale rute zahtevaju autentifikaciju
                .and()
                .formLogin().disable();  // Disable form login (ako koristiš JWT ili neki drugi metod)

        return http.build();
    }
}
