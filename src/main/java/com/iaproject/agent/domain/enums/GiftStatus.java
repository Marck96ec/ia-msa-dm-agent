package com.iaproject.agent.domain.enums;

/**
 * Estado de un regalo en la lista de regalos del evento.
 * Indica el nivel de compromiso de los invitados para ese regalo.
 */
public enum GiftStatus {
    /**
     * El regalo está disponible para ser reservado o recibir aportes.
     */
    AVAILABLE,
    
    /**
     * El regalo está completamente reservado por uno o más invitados.
     */
    RESERVED,
    
    /**
     * Regalo compartido con aportes parciales (aún acepta más contribuciones).
     */
    PARTIALLY_FUNDED,
    
    /**
     * Regalo compartido que alcanzó el 100% del monto objetivo.
     */
    FULLY_FUNDED,
    
    /**
     * El regalo fue desactivado por los administradores (ya no se muestra).
     */
    INACTIVE
}
