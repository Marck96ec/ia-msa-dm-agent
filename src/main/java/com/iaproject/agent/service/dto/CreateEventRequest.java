package com.iaproject.agent.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de solicitud para crear un nuevo evento.
 * Usado en el endpoint POST /api/v1/events.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventRequest {
    
    @NotBlank(message = "El slug es obligatorio")
    private String slug;
    
    @NotBlank(message = "El nombre del evento es obligatorio")
    private String name;
    
    private String description;
    
    @NotNull(message = "La fecha del evento es obligatoria")
    private LocalDateTime eventDate;
    
    private String location;
    private String locationUrl;
    private String welcomeMessage;
    private String closingMessage;
    private String chatbotInstructions;
    private Integer maxAttendees;
    private BigDecimal giftBudget;
    
    @NotBlank(message = "El ID del organizador es obligatorio")
    private String organizerUserId;
    
    private String organizerName;
    private String organizerEmail;
    private String organizerPhone;
    private Boolean allowSharedGifts;
    private Boolean allowBabyMessages;
    private Boolean allowIdeas;
    private String imageUrl;
}
