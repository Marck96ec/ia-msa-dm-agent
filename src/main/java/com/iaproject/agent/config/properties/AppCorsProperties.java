package com.iaproject.agent.config.properties;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.cors")
public class AppCorsProperties {

    /**
     * Lista de patrones de origen permitidos para CORS.
     * Se puede sobreescribir mediante la variable APP_CORS_ALLOWED_ORIGINS (separados por coma).
     */
    private List<String> allowedOriginPatterns = List.of(
        "http://localhost:*",
        "http://127.0.0.1:*"
    );

    public List<String> getAllowedOriginPatterns() {
        return allowedOriginPatterns;
    }

    public void setAllowedOriginPatterns(List<String> allowedOriginPatterns) {
        this.allowedOriginPatterns = allowedOriginPatterns;
    }
}
