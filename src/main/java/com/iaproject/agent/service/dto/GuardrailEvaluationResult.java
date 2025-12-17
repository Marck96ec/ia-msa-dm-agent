package com.iaproject.agent.service.dto;

import com.iaproject.agent.domain.enums.GuardrailAction;
import com.iaproject.agent.domain.enums.GuardrailReason;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Resultado de la evaluación de guardrails.
 * Contiene la decisión tomada y los datos necesarios para responder al usuario.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuardrailEvaluationResult {

    /**
     * Acción determinada por el sistema de guardrails.
     */
    private GuardrailAction action;

    /**
     * Razón específica de la acción.
     */
    private GuardrailReason reason;

    /**
     * Mensaje predefinido para responder al usuario (solo si action != ALLOW).
     */
    private String predefinedResponse;

    /**
     * Quick replies sugeridos para guiar al usuario.
     */
    @Builder.Default
    private List<String> quickReplies = List.of();

    /**
     * Indica si la solicitud fue permitida (sin necesidad de bloqueo/redirección).
     *
     * @return true si action == ALLOW
     */
    public boolean isAllowed() {
        return action == GuardrailAction.ALLOW;
    }

    /**
     * Indica si la solicitud fue bloqueada completamente.
     *
     * @return true si action == BLOCK
     */
    public boolean isBlocked() {
        return action == GuardrailAction.BLOCK;
    }

    /**
     * Indica si la solicitud fue redirigida.
     *
     * @return true si action == REDIRECT
     */
    public boolean isRedirected() {
        return action == GuardrailAction.REDIRECT;
    }
}
