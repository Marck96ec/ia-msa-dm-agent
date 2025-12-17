package com.iaproject.agent.repository;

import com.iaproject.agent.domain.ConversationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Repositorio para gestionar el historial de conversaciones.
 * Proporciona métodos de consulta personalizados para análisis y búsqueda.
 */
@Repository
public interface ConversationHistoryRepository extends JpaRepository<ConversationHistory, Long> {

    /**
     * Encuentra todas las conversaciones por ID de conversación ordenadas por fecha.
     *
     * @param conversationId ID de la conversación
     * @return lista de historiales de conversación
     */
    List<ConversationHistory> findByConversationIdOrderByCreatedAtAsc(String conversationId);

    /**
     * Encuentra conversaciones creadas después de una fecha específica.
     *
     * @param date fecha de inicio
     * @return lista de historiales
     */
    List<ConversationHistory> findByCreatedAtAfter(OffsetDateTime date);

    /**
     * Calcula el total de tokens usados en un rango de fechas.
     *
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return suma total de tokens
     */
    @Query("SELECT COALESCE(SUM(ch.totalTokens), 0) FROM ConversationHistory ch " +
           "WHERE ch.createdAt BETWEEN :startDate AND :endDate")
    Long calculateTotalTokensUsed(@Param("startDate") OffsetDateTime startDate,
                                   @Param("endDate") OffsetDateTime endDate);

    /**
     * Encuentra las últimas N conversaciones.
     *
     * @param limit número de conversaciones a retornar
     * @return lista de historiales recientes
     */
    @Query("SELECT ch FROM ConversationHistory ch ORDER BY ch.createdAt DESC LIMIT :limit")
    List<ConversationHistory> findRecentConversations(@Param("limit") int limit);

    /**
     * Cuenta el número de conversaciones por modelo usado.
     *
     * @return número de conversaciones
     */
    Long countByModelUsed(String modelUsed);
}
