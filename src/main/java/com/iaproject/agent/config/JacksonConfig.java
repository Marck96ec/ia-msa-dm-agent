package com.iaproject.agent.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Configuración de Jackson para serialización/deserialización de fechas.
 * Permite aceptar fechas en formato ISO-8601 con o sin zona horaria.
 */
@Configuration
public class JacksonConfig {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        
        // Configurar serialización de LocalDateTime
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DATE_TIME_FORMATTER));
        
        // Configurar deserialización de LocalDateTime (acepta formato sin zona horaria)
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DATE_TIME_FORMATTER));

        return new Jackson2ObjectMapperBuilder()
                .modules(javaTimeModule)
                .featuresToDisable(
                        SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
                        DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE
                )
                .featuresToEnable(
                        DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY
                );
    }
}
