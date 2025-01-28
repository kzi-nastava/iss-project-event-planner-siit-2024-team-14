package edu.ftn.iss.eventplanner.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // Dozvoljava slanje kolačića, tokena itd.

        // Dozvoliti samo Angular aplikaciji na localhost:4200
        config.setAllowedOrigins(List.of("http://localhost:4200"));

        // Dozvoljena zaglavlja (ako šalješ specifične zaglavlja kao što je Authorization, dodaj ih ovde)
        config.setAllowedHeaders(List.of("Origin", "Content-Type", "Accept", "Authorization"));

        // Dozvoliti sledeće HTTP metode (ako koristiš više od GET, možeš dodati i POST, PUT, DELETE...)
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));

        // Registruj ovu CORS konfiguraciju za sve rute
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
