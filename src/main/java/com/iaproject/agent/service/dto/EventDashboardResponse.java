package com.iaproject.agent.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO de respuesta para el dashboard del evento (organizadores).
 * Usado en endpoint GET /api/v1/events/{eventId}/dashboard.
 * Consolida toda la información relevante en un solo payload.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDashboardResponse {
    private EventPublicResponse event;
    private RSVPListResponse.RSVPSummary rsvpSummary;
    private GiftSummaryResponse giftSummary;
    private List<IdeaResponse> recentIdeas;
    private long totalBabyMessages;
    private long totalAttendees;
    private long pendingRSVPs;
}
