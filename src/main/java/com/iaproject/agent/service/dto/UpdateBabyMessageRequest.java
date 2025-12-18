package com.iaproject.agent.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de solicitud para actualizar estado de publicación de un mensaje.
 * Usado en endpoint PATCH /api/v1/baby-messages/{messageId}.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBabyMessageRequest {
    private Boolean isPublished;
}
