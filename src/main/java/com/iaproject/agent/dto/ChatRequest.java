package com.iaproject.agent.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Objeto de transferencia de datos para solicitudes de chat.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {

    @NotBlank(message = "El mensaje no puede estar vacío")
    private String message;

    private String conversationId;
    
    private Double temperature;
    
    private Integer maxTokens;
}
