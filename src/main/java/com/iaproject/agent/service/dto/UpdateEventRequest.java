package com.iaproject.agent.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de solicitud para actualizar un evento existente.
 * Usado en el endpoint PUT /api/v1/events/{eventId}.
 * Todos los campos son opcionales (actualización parcial).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventRequest {
    private String name;
    private String description;
    private LocalDateTime eventDate;
    private String location;
    private String locationUrl;
    private String welcomeMessage;
    private String closingMessage;
    private String chatbotInstructions;
    private Boolean isActive;
    private Integer maxAttendees;
    private BigDecimal giftBudget;
    private String organizerName;
    private String organizerEmail;
    private String organizerPhone;
    private Boolean allowSharedGifts;
    private Boolean allowBabyMessages;
    private Boolean allowIdeas;
    private String imageUrl;
}
