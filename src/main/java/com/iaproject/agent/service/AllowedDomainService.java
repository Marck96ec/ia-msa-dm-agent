package com.iaproject.agent.service;

import com.iaproject.agent.domain.AllowedDomain;
import com.iaproject.agent.repository.AllowedDomainRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para gestionar dominios permitidos.
 * Proporciona operaciones CRUD y caching para optimizar consultas frecuentes.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AllowedDomainService {

    private final AllowedDomainRepository allowedDomainRepository;

    /**
     * Obtiene todas las keywords de dominios activos.
     * Resultado cacheado para optimizar validaciones frecuentes.
     *
     * @return lista de keywords en minúsculas
     */
    @Cacheable(value = "allowedDomains", key = "'all'")
    public List<String> getAllowedKeywords() {
        List<String> keywords = allowedDomainRepository.findAllActiveKeywords();
        log.debug("Cargadas {} keywords de dominios permitidos", keywords.size());
        return keywords;
    }

    /**
     * Obtiene dominios activos por categoría.
     *
     * @param category categoría del dominio
     * @return lista de dominios activos
     */
    @Cacheable(value = "allowedDomains", key = "#category")
    public List<AllowedDomain> getByCategory(String category) {
        return allowedDomainRepository.findByCategoryAndActiveTrue(category);
    }

    /**
     * Crea un nuevo dominio permitido.
     *
     * @param keyword palabra clave
     * @param category categoría
     * @param description descripción opcional
     * @return dominio creado
     */
    @Transactional
    @CacheEvict(value = "allowedDomains", allEntries = true)
    public AllowedDomain create(String keyword, String category, String description) {
        AllowedDomain domain = AllowedDomain.builder()
                .keyword(keyword.toLowerCase())
                .category(category)
                .description(description)
                .active(true)
                .build();

        AllowedDomain saved = allowedDomainRepository.save(domain);
        log.info("✅ Dominio permitido creado: keyword={}, category={}", keyword, category);
        return saved;
    }

    /**
     * Activa o desactiva un dominio.
     *
     * @param id ID del dominio
     * @param active true para activar, false para desactivar
     */
    @Transactional
    @CacheEvict(value = "allowedDomains", allEntries = true)
    public void setActive(Long id, boolean active) {
        AllowedDomain domain = allowedDomainRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Dominio no encontrado: " + id));
        
        domain.setActive(active);
        allowedDomainRepository.save(domain);
        log.info("✅ Dominio {} actualizado: keyword={}, active={}", id, domain.getKeyword(), active);
    }

    /**
     * Elimina un dominio.
     *
     * @param id ID del dominio
     */
    @Transactional
    @CacheEvict(value = "allowedDomains", allEntries = true)
    public void delete(Long id) {
        allowedDomainRepository.deleteById(id);
        log.info("✅ Dominio {} eliminado", id);
    }

    /**
     * Obtiene todos los dominios (activos e inactivos).
     *
     * @return lista completa de dominios
     */
    public List<AllowedDomain> getAll() {
        return allowedDomainRepository.findAll();
    }

    /**
     * Limpia manualmente la caché de dominios.
     * Útil para forzar recarga después de cambios masivos.
     */
    @CacheEvict(value = "allowedDomains", allEntries = true)
    public void clearCache() {
        log.info("✅ Caché de dominios permitidos limpiada");
    }
}
