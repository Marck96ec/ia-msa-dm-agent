package com.iaproject.agent.repository;

import com.iaproject.agent.domain.Event;
import com.iaproject.agent.domain.RSVP;
import com.iaproject.agent.domain.enums.RSVPStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestionar confirmaciones de asistencia (RSVPs).
 * Proporciona métodos de consulta por evento, usuario y estado.
 */
@Repository
public interface RSVPRepository extends JpaRepository<RSVP, Long> {

    /**
     * Busca el RSVP de un usuario específico para un evento.
     * 
     * @param event evento de referencia
     * @param userId identificador del usuario
     * @return Optional con el RSVP si existe
     */
    Optional<RSVP> findByEventAndUserId(Event event, String userId);

    /**
     * Obtiene todos los RSVPs de un evento específico.
     * 
     * @param event evento de referencia
     * @return lista de RSVPs del evento
     */
    List<RSVP> findByEvent(Event event);

    /**
     * Obtiene todos los RSVPs de un evento con un estado específico.
     * Útil para listar solo los que confirmaron asistencia (YES).
     * 
     * @param event evento de referencia
     * @param status estado del RSVP (YES, NO, PENDING)
     * @return lista de RSVPs con el estado indicado
     */
    List<RSVP> findByEventAndStatus(Event event, RSVPStatus status);

    /**
     * Cuenta cuántos RSVPs de un evento tienen un estado específico.
     * 
     * @param event evento de referencia
     * @param status estado del RSVP
     * @return cantidad de RSVPs con ese estado
     */
    long countByEventAndStatus(Event event, RSVPStatus status);
}
