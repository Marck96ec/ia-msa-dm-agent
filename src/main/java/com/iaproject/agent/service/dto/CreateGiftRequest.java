package com.iaproject.agent.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO de solicitud para crear un regalo en un evento.
 * Usado en endpoint POST /api/v1/events/{eventId}/gifts.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateGiftRequest {
    
    @NotBlank(message = "El nombre del regalo es obligatorio")
    private String name;
    
    private String description;
    
    @NotNull(message = "El precio es obligatorio")
    private BigDecimal price;
    
    private String imageUrl;
    private Boolean allowSplit;
    private Integer priority;
    private Integer quantity;
    private String purchaseUrl;
}
