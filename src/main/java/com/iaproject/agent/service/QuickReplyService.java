package com.iaproject.agent.service;

import com.iaproject.agent.domain.ConversationHistory;
import com.iaproject.agent.domain.UserProfile;
import com.iaproject.agent.domain.enums.GuardrailAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para generar quick replies (respuestas rápidas sugeridas).
 * Los quick replies guían al usuario en la conversación y mejoran la UX.
 * 
 * Se generan basándose en:
 * - Contexto de la conversación
 * - Perfil del usuario
 * - Estado de guardrails
 * - Dominio (si aplica)
 */
@Slf4j
@Service
public class QuickReplyService {

    /**
     * Genera quick replies para una conversación activa después de una respuesta exitosa.
     *
     * @param profile perfil del usuario
     * @param history historial de conversación (últimos mensajes)
     * @param domainId dominio de la conversación (opcional)
     * @return lista de quick replies
     */
    public List<String> generateQuickReplies(
            UserProfile profile,
            List<ConversationHistory> history,
            String domainId) {

        List<String> quickReplies = new ArrayList<>();

        // Si el dominio es "baby shower" o evento similar, sugerir acciones comunes
        if (isBabyShowerDomain(domainId)) {
            quickReplies.add("Ideas para juegos");
            quickReplies.add("Lista de invitados");
            quickReplies.add("Sugerencias de regalos");
            quickReplies.add("Decoración");
        } else {
            // Quick replies genéricos
            quickReplies.add("Cuéntame más");
            quickReplies.add("Dame un ejemplo");
            quickReplies.add("¿Qué más puedes hacer?");
        }

        // Limitar a máximo 4 quick replies
        if (quickReplies.size() > 4) {
            quickReplies = quickReplies.subList(0, 4);
        }

        log.debug("Quick replies generados: {}", quickReplies);
        return quickReplies;
    }

    /**
     * Genera quick replies para guardrails (ya están definidos en GuardrailPolicyService).
     * Este método está para mantener consistencia, pero los quick replies de guardrails
     * se generan directamente en GuardrailPolicyService.
     *
     * @param action acción de guardrail
     * @return lista de quick replies
     */
    public List<String> generateGuardrailQuickReplies(GuardrailAction action) {
        return switch (action) {
            case BLOCK -> List.of(
                    "Reformular pregunta",
                    "Ver opciones",
                    "Ayuda"
            );
            case REDIRECT -> List.of(
                    "Ideas para el evento",
                    "Planificación",
                    "Ayuda"
            );
            default -> List.of();
        };
    }

    private boolean isBabyShowerDomain(String domainId) {
        if (domainId == null) {
            return false;
        }
        String lower = domainId.toLowerCase();
        return lower.contains("baby") 
                || lower.contains("shower") 
                || lower.contains("evento");
    }
}
