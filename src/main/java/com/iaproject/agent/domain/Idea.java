package com.iaproject.agent.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

/**
 * Entidad que representa una idea de apoyo propuesta por un invitado.
 * Ejemplos: "Llevo bocaditos", "Ayudo con fotos", "Pongo música", etc.
 * 
 * Permite a los invitados colaborar más allá de los regalos materiales.
 */
@Entity
@Table(name = "ideas", indexes = {
    @Index(name = "idx_idea_event", columnList = "event_id"),
    @Index(name = "idx_idea_user", columnList = "user_id"),
    @Index(name = "idx_idea_approved", columnList = "isApproved")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Idea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Referencia al evento asociado.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false, foreignKey = @ForeignKey(name = "fk_idea_event"))
    private Event event;

    /**
     * Identificador del usuario que propuso la idea.
     */
    @Column(name = "user_id", nullable = false, length = 100)
    private String userId;

    /**
     * Nombre del invitado que propuso la idea.
     */
    @Column(name = "guest_name", length = 200)
    private String guestName;

    /**
     * Descripción de la idea propuesta.
     */
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    /**
     * Indica si la idea fue aprobada/vista por el organizador.
     */
    @Column(name = "is_approved")
    @Builder.Default
    private Boolean isApproved = false;

    /**
     * Comentario del organizador sobre la idea (opcional).
     */
    @Column(name = "organizer_comment", columnDefinition = "TEXT")
    private String organizerComment;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}
