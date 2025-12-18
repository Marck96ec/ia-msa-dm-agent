package com.iaproject.agent.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de solicitud para crear un mensaje para el bebé.
 * Usado en endpoint POST /api/v1/events/{eventId}/baby-messages.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBabyMessageRequest {
    
    @NotBlank(message = "El userId es obligatorio")
    private String userId;
    
    private String guestName;
    
    @NotBlank(message = "El texto del mensaje es obligatorio")
    private String messageText;
    
    private String audioUrl;
}
