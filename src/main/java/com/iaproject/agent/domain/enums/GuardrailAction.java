package com.iaproject.agent.domain.enums;

/**
 * Acción tomada por el sistema de guardrails después de evaluar una solicitud.
 */
public enum GuardrailAction {
    /**
     * La solicitud pasó todas las validaciones.
     * Se procede a llamar al modelo de IA.
     */
    ALLOW,
    
    /**
     * La solicitud está fuera del alcance permitido.
     * Se redirige al usuario al dominio correcto sin llamar a la IA.
     */
    REDIRECT,
    
    /**
     * La solicitud viola políticas de seguridad o límites.
     * Se bloquea completamente sin llamar a la IA.
     */
    BLOCK
}
