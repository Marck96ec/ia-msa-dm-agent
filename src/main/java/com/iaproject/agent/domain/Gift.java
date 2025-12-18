package com.iaproject.agent.domain;

import com.iaproject.agent.domain.enums.GiftStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Entidad que representa un regalo en la lista de regalos de un evento.
 * Puede ser reservado completamente por un invitado o compartido entre varios (split).
 * 
 * El estado del regalo se calcula dinámicamente según los compromisos (commitments) asociados.
 */
@Entity
@Table(name = "gifts", indexes = {
    @Index(name = "idx_gift_event", columnList = "event_id"),
    @Index(name = "idx_gift_status", columnList = "status"),
    @Index(name = "idx_gift_active", columnList = "isActive")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Gift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Referencia al evento asociado.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false, foreignKey = @ForeignKey(name = "fk_gift_event"))
    private Event event;

    /**
     * Nombre del regalo (ej: "Cuna de madera", "Pañales Huggies").
     */
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    /**
     * Descripción detallada del regalo.
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Precio aproximado del regalo.
     */
    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * URL de la imagen del regalo (opcional).
     */
    @Column(name = "image_url", length = 500)
    private String imageUrl;

    /**
     * Indica si el regalo permite aportes compartidos (split).
     */
    @Column(name = "allow_split", nullable = false)
    @Builder.Default
    private Boolean allowSplit = false;

    /**
     * Prioridad del regalo (1 = alta, 5 = baja, opcional).
     * Sirve para ordenar la lista de regalos.
     */
    @Column(name = "priority")
    private Integer priority;

    /**
     * Estado actual del regalo (AVAILABLE, RESERVED, etc.).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    @Builder.Default
    private GiftStatus status = GiftStatus.AVAILABLE;

    /**
     * Indica si el regalo está activo (visible para invitados).
     */
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    /**
     * Cantidad de unidades del regalo (opcional, default 1).
     */
    @Column(name = "quantity")
    @Builder.Default
    private Integer quantity = 1;

    /**
     * URL externa para comprar el regalo (ej: Amazon, MercadoLibre).
     */
    @Column(name = "purchase_url", length = 500)
    private String purchaseUrl;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
