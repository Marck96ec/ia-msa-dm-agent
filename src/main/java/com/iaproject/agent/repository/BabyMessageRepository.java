package com.iaproject.agent.repository;

import com.iaproject.agent.domain.BabyMessage;
import com.iaproject.agent.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para gestionar mensajes para el bebé.
 * Proporciona métodos de consulta por evento y estado de publicación.
 */
@Repository
public interface BabyMessageRepository extends JpaRepository<BabyMessage, Long> {

    /**
     * Obtiene todos los mensajes publicados de un evento.
     * 
     * @param event evento de referencia
     * @return lista de mensajes publicados
     */
    List<BabyMessage> findByEventAndIsPublishedTrue(Event event);

    /**
     * Obtiene todos los mensajes de un evento (incluidos no publicados).
     * Usado por organizadores para moderación.
     * 
     * @param event evento de referencia
     * @return lista de todos los mensajes del evento
     */
    List<BabyMessage> findByEvent(Event event);

    /**
     * Obtiene todos los mensajes publicados de un evento ordenados por fecha.
     * 
     * @param event evento de referencia
     * @return lista de mensajes ordenados de más reciente a más antiguo
     */
    List<BabyMessage> findByEventAndIsPublishedTrueOrderByCreatedAtDesc(Event event);

    /**
     * Cuenta cuántos mensajes publicados tiene un evento.
     * 
     * @param event evento de referencia
     * @return cantidad de mensajes publicados
     */
    long countByEventAndIsPublishedTrue(Event event);
}
