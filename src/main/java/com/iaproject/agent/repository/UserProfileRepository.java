package com.iaproject.agent.repository;

import com.iaproject.agent.domain.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para gestionar perfiles de usuario.
 * Proporciona acceso a datos de personalización conversacional.
 */
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    /**
     * Busca un perfil por userId.
     *
     * @param userId identificador único del usuario
     * @return perfil del usuario si existe
     */
    Optional<UserProfile> findByUserId(String userId);

    /**
     * Verifica si existe un perfil para el userId dado.
     *
     * @param userId identificador único del usuario
     * @return true si existe el perfil
     */
    boolean existsByUserId(String userId);
}
