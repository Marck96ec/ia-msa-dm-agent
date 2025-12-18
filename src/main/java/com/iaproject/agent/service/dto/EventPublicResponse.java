package com.iaproject.agent.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para información pública de un evento.
 * Usado en el endpoint GET /api/v1/events/{slug} para bienvenida inicial.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventPublicResponse {
    private Long id;
    private String slug;
    private String name;
    private String description;
    private LocalDateTime eventDate;
    private String location;
    private String locationUrl;
    private String welcomeMessage;
    private String imageUrl;
    private Boolean allowSharedGifts;
    private Boolean allowBabyMessages;
    private Boolean allowIdeas;
}
