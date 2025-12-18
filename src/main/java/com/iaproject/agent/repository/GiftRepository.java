package com.iaproject.agent.repository;

import com.iaproject.agent.domain.Event;
import com.iaproject.agent.domain.Gift;
import com.iaproject.agent.domain.enums.GiftStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para gestionar regalos de eventos.
 * Proporciona métodos de consulta por evento, estado y disponibilidad.
 */
@Repository
public interface GiftRepository extends JpaRepository<Gift, Long> {

    /**
     * Obtiene todos los regalos activos de un evento.
     * 
     * @param event evento de referencia
     * @return lista de regalos activos
     */
    List<Gift> findByEventAndIsActiveTrue(Event event);

    /**
     * Obtiene todos los regalos de un evento con un estado específico.
     * 
     * @param event evento de referencia
     * @param status estado del regalo
     * @return lista de regalos con ese estado
     */
    List<Gift> findByEventAndStatus(Event event, GiftStatus status);

    /**
     * Obtiene todos los regalos activos de un evento ordenados por prioridad.
     * 
     * @param event evento de referencia
     * @return lista de regalos ordenados por prioridad ascendente (1 primero)
     */
    List<Gift> findByEventAndIsActiveTrueOrderByPriorityAsc(Event event);

    /**
     * Cuenta cuántos regalos de un evento tienen un estado específico.
     * 
     * @param event evento de referencia
     * @param status estado del regalo
     * @return cantidad de regalos con ese estado
     */
    long countByEventAndStatus(Event event, GiftStatus status);

    /**
     * Obtiene estadísticas resumidas de regalos por evento.
     * Usado para el dashboard de administración.
     * 
     * @param eventId ID del evento
     * @return lista de objetos con status y conteo
     */
    @Query("SELECT g.status as status, COUNT(g) as count " +
           "FROM Gift g WHERE g.event.id = :eventId AND g.isActive = true " +
           "GROUP BY g.status")
    List<GiftStatusSummary> getGiftStatusSummary(@Param("eventId") Long eventId);

    /**
     * Interfaz de proyección para el resumen de estados de regalos.
     */
    interface GiftStatusSummary {
        GiftStatus getStatus();
        Long getCount();
    }
}
