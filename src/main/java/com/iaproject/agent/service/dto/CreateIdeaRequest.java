package com.iaproject.agent.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de solicitud para crear una idea de apoyo.
 * Usado en endpoint POST /api/v1/events/{eventId}/ideas.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateIdeaRequest {
    
    @NotBlank(message = "El userId es obligatorio")
    private String userId;
    
    private String guestName;
    
    @NotBlank(message = "La descripción de la idea es obligatoria")
    private String description;
}
