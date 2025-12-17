package com.iaproject.agent.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

/**
 * Entidad que representa un dominio permitido para validación de alcance (OUT_OF_SCOPE).
 * Los dominios se usan cuando mode=EVENT para verificar que el mensaje del usuario
 * esté dentro del alcance permitido.
 * 
 * Ejemplos de dominios:
 * - baby shower, babyshower, baby-shower
 * - carros, autos, vehículos, automotriz
 * - bodas, wedding, matrimonio
 */
@Entity
@Table(name = "allowed_domain", indexes = {
    @Index(name = "idx_domain_category", columnList = "category"),
    @Index(name = "idx_domain_active", columnList = "active")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllowedDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Palabra clave o frase del dominio permitido.
     * Ejemplos: "baby shower", "carros", "invitados", "decoración"
     */
    @Column(name = "keyword", nullable = false, unique = true, length = 100)
    private String keyword;

    /**
     * Categoría o agrupación del dominio.
     * Ejemplos: "baby-shower", "automotive", "wedding"
     * Permite agrupar keywords relacionadas.
     */
    @Column(name = "category", nullable = false, length = 50)
    private String category;

    /**
     * Descripción opcional del dominio.
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Indica si el dominio está activo (habilitado para validación).
     * Permite desactivar temporalmente dominios sin eliminarlos.
     */
    @Column(name = "active", nullable = false)
    @Builder.Default
    private Boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
