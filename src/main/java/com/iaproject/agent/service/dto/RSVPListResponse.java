package com.iaproject.agent.service.dto;

import com.iaproject.agent.domain.enums.RSVPStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO de respuesta para el listado de RSVPs de un evento.
 * Usado en endpoint GET /api/v1/events/{eventId}/rsvps.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RSVPListResponse {
    private List<RSVPResponse> rsvps;
    private RSVPSummary summary;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RSVPSummary {
        private long totalYes;
        private long totalNo;
        private long totalPending;
        private long totalGuests; // Suma de guestsCount
    }
}
