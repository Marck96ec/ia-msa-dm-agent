package com.iaproject.agent.repository;

import com.iaproject.agent.domain.AllowedDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para gestionar dominios permitidos.
 * Proporciona acceso a keywords para validación de alcance en guardrails.
 */
@Repository
public interface AllowedDomainRepository extends JpaRepository<AllowedDomain, Long> {

    /**
     * Encuentra todos los dominios activos.
     *
     * @return lista de dominios activos
     */
    List<AllowedDomain> findByActiveTrue();

    /**
     * Encuentra dominios activos por categoría.
     *
     * @param category categoría del dominio
     * @return lista de dominios activos de la categoría
     */
    List<AllowedDomain> findByCategoryAndActiveTrue(String category);

    /**
     * Obtiene solo las keywords de dominios activos (optimizado).
     *
     * @return lista de keywords activas en minúsculas
     */
    @Query("SELECT LOWER(d.keyword) FROM AllowedDomain d WHERE d.active = true")
    List<String> findAllActiveKeywords();

    /**
     * Verifica si existe un keyword específico activo.
     *
     * @param keyword palabra clave a verificar
     * @return true si existe y está activo
     */
    boolean existsByKeywordIgnoreCaseAndActiveTrue(String keyword);
}
