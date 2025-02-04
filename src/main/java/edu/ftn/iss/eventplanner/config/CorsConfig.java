package edu.ftn.iss.eventplanner.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Cross-Origin Resource Sharing

@Configuration          // Spring configuration class
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Allow all endpoints
                .allowedOrigins("http://localhost:4200")  // Allow only your frontend URL (Angular running on port 4200)
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Allow specific methods
                .allowedHeaders("*")  // Allow any headers
                .allowCredentials(true);  // Allow credentials (JWT tokens, cookies,...)
    }
}