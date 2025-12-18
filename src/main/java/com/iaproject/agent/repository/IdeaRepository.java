package com.iaproject.agent.repository;

import com.iaproject.agent.domain.Event;
import com.iaproject.agent.domain.Idea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para gestionar ideas de apoyo propuestas por invitados.
 * Proporciona métodos de consulta por evento y estado de aprobación.
 */
@Repository
public interface IdeaRepository extends JpaRepository<Idea, Long> {

    /**
     * Obtiene todas las ideas de un evento.
     * 
     * @param event evento de referencia
     * @return lista de ideas del evento
     */
    List<Idea> findByEvent(Event event);

    /**
     * Obtiene todas las ideas de un evento ordenadas por fecha de creación.
     * 
     * @param event evento de referencia
     * @return lista de ideas ordenadas de más reciente a más antigua
     */
    List<Idea> findByEventOrderByCreatedAtDesc(Event event);

    /**
     * Cuenta cuántas ideas tiene un evento.
     * 
     * @param event evento de referencia
     * @return cantidad de ideas
     */
    long countByEvent(Event event);
}
