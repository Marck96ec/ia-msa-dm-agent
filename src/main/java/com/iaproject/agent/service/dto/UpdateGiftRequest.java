package com.iaproject.agent.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO de solicitud para actualizar un regalo.
 * Usado en endpoint PUT /api/v1/gifts/{giftId}.
 * Todos los campos son opcionales.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateGiftRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private Boolean allowSplit;
    private Integer priority;
    private Boolean isActive;
    private Integer quantity;
    private String purchaseUrl;
}
