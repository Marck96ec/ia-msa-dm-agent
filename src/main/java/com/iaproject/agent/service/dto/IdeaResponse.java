package com.iaproject.agent.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para información de una idea.
 * Usado en endpoint GET /api/v1/events/{eventId}/ideas.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdeaResponse {
    private Long id;
    private Long eventId;
    private String userId;
    private String guestName;
    private String description;
    private Boolean isApproved;
    private String organizerComment;
    private LocalDateTime createdAt;
}
