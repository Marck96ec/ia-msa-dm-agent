package com.iaproject.agent.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para información de un mensaje para el bebé.
 * Usado en endpoint GET /api/v1/events/{eventId}/baby-messages.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BabyMessageResponse {
    private Long id;
    private Long eventId;
    private String userId;
    private String guestName;
    private String messageText;
    private String audioUrl;
    private Boolean isPublished;
    private LocalDateTime createdAt;
}
