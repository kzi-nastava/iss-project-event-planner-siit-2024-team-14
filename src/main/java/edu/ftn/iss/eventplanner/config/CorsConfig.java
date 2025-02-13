package edu.ftn.iss.eventplanner.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:4200");  // Allow the frontend origin
        configuration.addAllowedMethod("*");  // Allow any HTTP method (GET, POST, etc.)
        configuration.addAllowedHeader("*");  // Allow any headers
        configuration.setAllowCredentials(true);  // Allow credentials (cookies, HTTP auth, etc.)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);  // Apply the config to all endpoints
        return source;
    }
}
