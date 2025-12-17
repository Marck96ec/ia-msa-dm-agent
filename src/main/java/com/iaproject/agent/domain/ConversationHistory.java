package com.iaproject.agent.domain;

import com.iaproject.agent.domain.enums.GuardrailAction;
import com.iaproject.agent.domain.enums.GuardrailReason;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Entidad que representa el historial de conversaciones con la IA.
 * Almacena mensajes y respuestas para trazabilidad y análisis.
 * 
 * Campos extendidos para Guardrails + Memoria por Personalidad:
 * - userId: identifica al usuario (para vincular con perfil)
 * - domainId/eventId: contexto del dominio (ej: baby-shower-123)
 * - intent: intención detectada (futuro)
 * - guardrailAction/guardrailReason: decisiones de guardrails
 * - quickReplies: sugerencias mostradas al usuario
 */
@Entity
@Table(name = "conversation_history", indexes = {
    @Index(name = "idx_conversation_id", columnList = "conversationId"),
    @Index(name = "idx_user_id_created", columnList = "userId, createdAt"),
    @Index(name = "idx_domain_id_created", columnList = "domainId, createdAt"),
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

    /**
     * ID del usuario (phone, sessionId, anonymousId).
     * Vincula la conversación con el perfil del usuario.
     */
    @Column(name = "user_id", length = 100)
    private String userId;

    /**
     * ID del dominio (ej: "baby-shower", "wedding", "event-planning").
     * Opcional, usado para contexto y análisis.
     */
    @Column(name = "domain_id", length = 100)
    private String domainId;

    /**
     * ID del evento específico (ej: "baby-shower-123").
     * Opcional, más granular que domainId.
     */
    @Column(name = "event_id", length = 100)
    private String eventId;

    /**
     * Intención detectada del usuario (ej: "pregunta", "comando", "feedback").
     * Campo reservado para futuras mejoras con NLU.
     */
    @Column(name = "intent", length = 50)
    private String intent;

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

    /**
     * Acción de guardrail aplicada (ALLOW, BLOCK, REDIRECT).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "guardrail_action", length = 20)
    private GuardrailAction guardrailAction;

    /**
     * Razón del guardrail (NONE, TOO_LONG, INJECTION, OUT_OF_SCOPE, UNSAFE).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "guardrail_reason", length = 20)
    private GuardrailReason guardrailReason;

    /**
     * Quick replies mostrados al usuario (almacenados como JSONB).
     * Formato: ["Opción 1", "Opción 2", "Opción 3"]
     */
    @Type(JsonBinaryType.class)
    @Column(name = "quick_replies", columnDefinition = "jsonb")
    private List<String> quickReplies;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
