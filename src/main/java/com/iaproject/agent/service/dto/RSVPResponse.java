package com.iaproject.agent.service.dto;

import com.iaproject.agent.domain.enums.RSVPStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para información de un RSVP.
 * Usado en endpoints GET /api/v1/events/{eventId}/rsvp/{userId}.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RSVPResponse {
    private Long id;
    private Long eventId;
    private String userId;
    private String guestName;
    private String guestEmail;
    private String guestPhone;
    private RSVPStatus status;
    private Integer guestsCount;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
