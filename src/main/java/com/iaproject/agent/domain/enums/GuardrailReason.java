package com.iaproject.agent.domain.enums;

/**
 * Razón específica por la cual se aplicó una acción de guardrail.
 */
public enum GuardrailReason {
    /**
     * No se detectó ninguna violación.
     * La solicitud es válida y segura.
     */
    NONE,
    
    /**
     * El mensaje es demasiado largo (supera el límite permitido).
     */
    TOO_LONG,
    
    /**
     * Se detectó intento de prompt injection o manipulación del sistema.
     */
    INJECTION,
    
    /**
     * La solicitud está fuera del dominio o alcance permitido.
     */
    OUT_OF_SCOPE,
    
    /**
     * La solicitud contiene contenido inseguro o prohibido.
     */
    UNSAFE
}
