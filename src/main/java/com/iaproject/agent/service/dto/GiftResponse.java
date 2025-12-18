package com.iaproject.agent.service.dto;

import com.iaproject.agent.domain.enums.GiftStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para información de un regalo.
 * Usado en endpoints GET /api/v1/gifts/{giftId} y listados.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftResponse {
    private Long id;
    private Long eventId;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private Boolean allowSplit;
    private Integer priority;
    private GiftStatus status;
    private Boolean isActive;
    private Integer quantity;
    private String purchaseUrl;
    private BigDecimal currentFunding; // Para regalos compartidos
    private BigDecimal fundingPercentage; // Para regalos compartidos
    private Integer commitmentCount; // Cantidad de personas que aportaron/reservaron
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
