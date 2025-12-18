package com.iaproject.agent.domain.enums;

/**
 * Tipo de compromiso adquirido por un invitado con un regalo.
 * Determina si el invitado se comprometió con el regalo completo o solo una parte.
 */
public enum CommitmentType {
    /**
     * El invitado se compromete a adquirir el regalo completo.
     */
    FULL_RESERVATION,
    
    /**
     * El invitado aporta parcialmente al costo del regalo (regalo compartido).
     */
    PARTIAL_CONTRIBUTION
}
