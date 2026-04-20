package com.hr.management.system.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // ✅ Allow frontend origins
        config.setAllowedOrigins(List.of(
                "http://localhost:5173", // Vue web
                "http://localhost:8081"  // mobile dev (if using)
        ));

        // ✅ Allow HTTP methods
        config.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
        ));

        // ✅ Allow all headers (important for JWT)
        config.setAllowedHeaders(List.of("*"));

        // ✅ Allow sending cookies / Authorization header
        config.setAllowCredentials(true);

        // (Optional but good)
        config.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}