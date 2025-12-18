package com.iaproject.agent.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de solicitud para reservar un regalo completo.
 * Usado en endpoint POST /api/v1/gifts/{giftId}/reserve.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReserveGiftRequest {
    
    @NotBlank(message = "El userId es obligatorio")
    private String userId;
    
    private String guestName;
    private String guestEmail;
    private String guestPhone;
    private String notes;
}
