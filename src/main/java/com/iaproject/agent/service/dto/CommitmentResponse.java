package com.iaproject.agent.service.dto;

import com.iaproject.agent.domain.enums.CommitmentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para información de un compromiso (reserva/aporte).
 * Usado en endpoint GET /api/v1/commitments/{token}.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommitmentResponse {
    private Long id;
    private Long giftId;
    private String giftName;
    private String userId;
    private String guestName;
    private String guestEmail;
    private String guestPhone;
    private CommitmentType commitmentType;
    private BigDecimal contributionAmount;
    private String token;
    private Boolean isActive;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime cancelledAt;
}
