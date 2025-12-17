package com.iaproject.agent.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

/**
 * Entidad que representa el historial de conversaciones con la IA.
 * Almacena mensajes y respuestas para trazabilidad y análisis.
 */
@Entity
@Table(name = "conversation_history", indexes = {
    @Index(name = "idx_conversation_id", columnList = "conversationId"),
    @Index(name = "idx_created_at", columnList = "createdAt")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "conversation_id", nullable = false, length = 100)
    private String conversationId;

    @Column(name = "user_message", nullable = false, columnDefinition = "TEXT")
    private String userMessage;

    @Column(name = "ai_response", nullable = false, columnDefinition = "TEXT")
    private String aiResponse;

    @Column(name = "model_used", length = 50)
    private String modelUsed;

    @Column(name = "temperature")
    private Double temperature;

    @Column(name = "prompt_tokens")
    private Integer promptTokens;

    @Column(name = "completion_tokens")
    private Integer completionTokens;

    @Column(name = "total_tokens")
    private Integer totalTokens;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
