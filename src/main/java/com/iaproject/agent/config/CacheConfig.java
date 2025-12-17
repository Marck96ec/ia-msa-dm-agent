package com.iaproject.agent.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de caché para la aplicación.
 * Habilita caching en servicios anotados con @Cacheable.
 * 
 * Usado principalmente en AllowedDomainService para cachear
 * dominios permitidos y evitar consultas frecuentes a BD.
 */
@Configuration
@EnableCaching
public class CacheConfig {
    // Spring Boot auto-configura un CacheManager simple por defecto
    // Si necesitas configuración avanzada (Redis, Caffeine, etc.), hazlo aquí
}
