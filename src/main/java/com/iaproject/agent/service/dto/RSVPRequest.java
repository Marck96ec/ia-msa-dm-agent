package com.iaproject.agent.service.dto;

import com.iaproject.agent.domain.enums.RSVPStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de solicitud para registrar o actualizar un RSVP.
 * Usado en endpoints POST /api/v1/events/{eventId}/rsvp y PUT.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RSVPRequest {
    
    @NotBlank(message = "El userId es obligatorio")
    private String userId;
    
    private String guestName;
    private String guestEmail;
    private String guestPhone;
    
    @NotNull(message = "El estado es obligatorio")
    private RSVPStatus status;
    
    private Integer guestsCount;
    private String notes;
}
