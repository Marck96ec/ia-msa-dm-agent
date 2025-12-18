package com.iaproject.agent.service.dto;

import com.iaproject.agent.domain.enums.GiftStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO de respuesta para el resumen de regalos de un evento.
 * Usado en endpoint GET /api/v1/events/{eventId}/gifts/summary.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftSummaryResponse {
    private long totalGifts;
    private long availableGifts;
    private long reservedGifts;
    private long partiallyFundedGifts;
    private long fullyFundedGifts;
    private BigDecimal totalBudget;
    private BigDecimal coveredBudget;
    private BigDecimal remainingBudget;
    private BigDecimal coveragePercentage;
    private List<StatusCount> statusBreakdown;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusCount {
        private GiftStatus status;
        private long count;
    }
}
