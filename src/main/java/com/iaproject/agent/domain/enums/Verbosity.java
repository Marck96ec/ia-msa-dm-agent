package com.iaproject.agent.domain.enums;

/**
 * Nivel de detalle que el usuario prefiere en las respuestas.
 */
public enum Verbosity {
    /**
     * Respuestas cortas y directas (máximo 6 líneas).
     * Ideal para usuarios que valoran la brevedad.
     */
    SHORT,
    
    /**
     * Respuestas moderadas con balance entre detalle y concisión (8-12 líneas).
     * Default para la mayoría de usuarios.
     */
    MEDIUM,
    
    /**
     * Respuestas detalladas y completas (más de 12 líneas).
     * Para usuarios que valoran explicaciones exhaustivas.
     */
    DETAILED
}
