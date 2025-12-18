package com.iaproject.agent.domain.enums;

/**
 * Estado de confirmación de asistencia a un evento.
 * Representa las tres posibles respuestas del invitado.
 */
public enum RSVPStatus {
    /**
     * El invitado confirmó su asistencia.
     */
    CONFIRMED,
    
    /**
     * El invitado declinó la invitación.
     */
    DECLINED,
    
    /**
     * El invitado aún no ha respondido (estado inicial).
     */
    PENDING
}
