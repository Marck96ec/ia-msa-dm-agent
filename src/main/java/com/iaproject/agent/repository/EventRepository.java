package com.iaproject.agent.repository;

import com.iaproject.agent.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para gestionar la persistencia de eventos.
 * Proporciona métodos de consulta por slug y organizador.
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    /**
     * Busca un evento por su slug único.
     * Usado para acceso público vía QR/link.
     * 
     * @param slug identificador único amigable del evento
     * @return Optional con el evento si existe
     */
    Optional<Event> findBySlug(String slug);

    /**
     * Verifica si existe un evento con el slug dado.
     * 
     * @param slug identificador único del evento
     * @return true si existe, false en caso contrario
     */
    boolean existsBySlug(String slug);
}
