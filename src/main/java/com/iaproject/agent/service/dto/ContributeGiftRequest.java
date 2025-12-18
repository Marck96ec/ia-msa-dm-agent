package com.iaproject.agent.service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO de solicitud para aportar a un regalo compartido.
 * Usado en endpoint POST /api/v1/gifts/{giftId}/contribute.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContributeGiftRequest {
    
    @NotBlank(message = "El userId es obligatorio")
    private String userId;
    
    private String guestName;
    private String guestEmail;
    private String guestPhone;
    
    @NotNull(message = "El monto de contribución es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal contributionAmount;
    
    private String notes;
}
