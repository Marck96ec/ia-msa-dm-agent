package com.iaproject.agent.domain;

import com.iaproject.agent.domain.enums.RSVPStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

/**
 * Entidad que representa la confirmación de asistencia (RSVP) de un invitado a un evento.
 * Permite registrar si asiste o no, cuántos acompañantes trae y notas adicionales.
 * 
 * Cada usuario puede tener solo un RSVP por evento (constraint unique).
 */
@Entity
@Table(name = "rsvps", 
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_event_user", columnNames = {"event_id", "user_id"})
    },
    indexes = {
        @Index(name = "idx_rsvp_event", columnList = "event_id"),
        @Index(name = "idx_rsvp_user", columnList = "user_id"),
        @Index(name = "idx_rsvp_status", columnList = "status")
    }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RSVP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Referencia al evento asociado.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false, foreignKey = @ForeignKey(name = "fk_rsvp_event"))
    private Event event;

    /**
     * Identificador del usuario que confirma asistencia.
     * Puede ser: phone, email, sessionId, etc.
     */
    @Column(name = "user_id", nullable = false, length = 100)
    private String userId;

    /**
     * Nombre del invitado (capturado del chat o formulario).
     */
    @Column(name = "guest_name", length = 200)
    private String guestName;

    /**
     * Email del invitado (opcional).
     */
    @Column(name = "guest_email", length = 150)
    private String guestEmail;

    /**
     * Teléfono del invitado (opcional).
     */
    @Column(name = "guest_phone", length = 50)
    private String guestPhone;

    /**
     * Estado de la confirmación: YES, NO, PENDING.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private RSVPStatus status = RSVPStatus.PENDING;

    /**
     * Número de acompañantes que traerá (opcional).
     */
    @Column(name = "guests_count")
    private Integer guestsCount;

    /**
     * Notas adicionales del invitado (ej: "Llego un poco tarde", "Soy vegetariano").
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
