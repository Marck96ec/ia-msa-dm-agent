package com.iaproject.agent.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

/**
 * Entidad que representa un mensaje para el bebé dejado por un invitado.
 * Puede contener texto, emojis y eventualmente audio (URL).
 * 
 * Los organizadores pueden moderar los mensajes antes de publicarlos.
 */
@Entity
@Table(name = "baby_messages", indexes = {
    @Index(name = "idx_baby_message_event", columnList = "event_id"),
    @Index(name = "idx_baby_message_user", columnList = "user_id"),
    @Index(name = "idx_baby_message_published", columnList = "isPublished")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BabyMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Referencia al evento asociado.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false, foreignKey = @ForeignKey(name = "fk_baby_message_event"))
    private Event event;

    /**
     * Identificador del usuario que dejó el mensaje.
     */
    @Column(name = "user_id", nullable = false, length = 100)
    private String userId;

    /**
     * Nombre del invitado que dejó el mensaje.
     */
    @Column(name = "guest_name", length = 200)
    private String guestName;

    /**
     * Contenido del mensaje (texto/emojis).
     */
    @Column(name = "message_text", nullable = false, columnDefinition = "TEXT")
    private String messageText;

    /**
     * URL del audio del mensaje (opcional, para futuras versiones).
     */
    @Column(name = "audio_url", length = 500)
    private String audioUrl;

    /**
     * Indica si el mensaje está publicado (visible para todos).
     * Los organizadores pueden moderar antes de publicar.
     */
    @Column(name = "is_published", nullable = false)
    @Builder.Default
    private Boolean isPublished = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}
