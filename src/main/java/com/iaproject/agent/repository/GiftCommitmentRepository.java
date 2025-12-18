package com.iaproject.agent.repository;

import com.iaproject.agent.domain.Gift;
import com.iaproject.agent.domain.GiftCommitment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestionar compromisos de regalos (reservas y aportes).
 * Proporciona métodos de consulta por regalo, usuario y token.
 */
@Repository
public interface GiftCommitmentRepository extends JpaRepository<GiftCommitment, Long> {

    /**
     * Busca un compromiso por su token único.
     * Usado para que invitados consulten/cancelen su reserva sin login.
     * 
     * @param token token único del compromiso
     * @return Optional con el compromiso si existe
     */
    Optional<GiftCommitment> findByToken(String token);

    /**
     * Obtiene todos los compromisos activos de un regalo.
     * 
     * @param gift regalo de referencia
     * @return lista de compromisos activos
     */
    List<GiftCommitment> findByGiftAndIsActiveTrue(Gift gift);

    /**
     * Obtiene todos los compromisos activos de un usuario.
     * 
     * @param userId identificador del usuario
     * @return lista de compromisos del usuario
     */
    List<GiftCommitment> findByUserIdAndIsActiveTrue(String userId);

    /**
     * Verifica si un usuario ya tiene un compromiso activo con un regalo.
     * 
     * @param gift regalo de referencia
     * @param userId identificador del usuario
     * @return true si ya tiene un compromiso activo
     */
    boolean existsByGiftAndUserIdAndIsActiveTrue(Gift gift, String userId);

    /**
     * Calcula el monto total acumulado de contribuciones activas para un regalo.
     * Usado para calcular el porcentaje de financiamiento en regalos compartidos.
     * 
     * @param giftId ID del regalo
     * @return suma total de contribuciones activas
     */
    @Query("SELECT COALESCE(SUM(gc.contributionAmount), 0) " +
           "FROM GiftCommitment gc WHERE gc.gift.id = :giftId AND gc.isActive = true")
    BigDecimal sumActiveContributions(@Param("giftId") Long giftId);

    /**
     * Cuenta cuántos compromisos activos tiene un regalo.
     * 
     * @param gift regalo de referencia
     * @return cantidad de compromisos activos
     */
    long countByGiftAndIsActiveTrue(Gift gift);
}
