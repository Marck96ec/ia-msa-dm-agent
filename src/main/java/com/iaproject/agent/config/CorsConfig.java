package com.iaproject.agent.config;

import com.iaproject.agent.config.properties.AppCorsProperties;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Collectors;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Configuración global de CORS para permitir peticiones desde el frontend.
 */
@Configuration
@EnableConfigurationProperties(AppCorsProperties.class)
public class CorsConfig {

    private final AppCorsProperties corsProperties;

    public CorsConfig(AppCorsProperties corsProperties) {
        this.corsProperties = corsProperties;
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        // Permitir credenciales (cookies, authorization headers)
        config.setAllowCredentials(true);
        
        // Orígenes permitidos
        List<String> allowedOrigins = corsProperties.getAllowedOriginPatterns();
        String envOverride = System.getenv("APP_CORS_ALLOWED_ORIGINS");
        if (envOverride != null && !envOverride.isBlank()) {
            allowedOrigins = Arrays.stream(envOverride.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        }
        if (allowedOrigins == null || allowedOrigins.isEmpty()) {
            allowedOrigins = List.of("*");
        }
        config.setAllowedOriginPatterns(allowedOrigins);
        
        // Headers permitidos
        config.setAllowedHeaders(Arrays.asList(
            "Origin",
            "Content-Type",
            "Accept",
            "Authorization",
            "X-Requested-With",
            "X-User-Id",
            "X-Conversation-Id"
        ));
        
        // Métodos HTTP permitidos
        config.setAllowedMethods(Arrays.asList(
            "GET",
            "POST",
            "PUT",
            "PATCH",
            "DELETE",
            "OPTIONS"
        ));
        
        // Headers expuestos al cliente
        config.setExposedHeaders(Arrays.asList(
            "Authorization",
            "Content-Disposition",
            "X-User-Id",
            "X-Conversation-Id"
        ));
        
        // Tiempo de cache para preflight requests (1 hora)
        config.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}
