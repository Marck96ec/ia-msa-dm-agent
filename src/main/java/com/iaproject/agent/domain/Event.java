package com.iaproject.agent.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * Entidad que representa un evento (baby shower, cumpleaños, etc.).
 * Almacena toda la información del evento: datos básicos, configuraciones
 * conversacionales y metadatos de gestión.
 * 
 * Un evento tiene asociados: RSVPs, regalos, ideas de apoyo y mensajes para el bebé.
 */
@Entity
@Table(name = "events", indexes = {
    @Index(name = "idx_event_slug", columnList = "slug", unique = true),
    @Index(name = "idx_event_date", columnList = "eventDate"),
    @Index(name = "idx_is_active", columnList = "isActive")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Identificador único amigable para URLs (ej: baby-shower-maria-2025).
     * Se usa en QR codes y links compartidos.
     */
    @Column(name = "slug", nullable = false, unique = true, length = 100)
    private String slug;

    /**
     * Nombre del evento (ej: "Baby Shower de María").
     */
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    /**
     * Descripción del evento para contexto adicional.
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Fecha y hora del evento.
     */
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    /**
     * Dirección completa donde se realizará el evento.
     */
    @Column(name = "location", columnDefinition = "TEXT")
    private String location;

    /**
     * URL de Google Maps o coordenadas para facilitar la ubicación.
     */
    @Column(name = "location_url", length = 500)
    private String locationUrl;

    /**
     * Mensaje de bienvenida personalizado para el chatbot (MCG).
     */
    @Column(name = "welcome_message", columnDefinition = "TEXT")
    private String welcomeMessage;

    /**
     * Mensaje de cierre/agradecimiento del chatbot.
     */
    @Column(name = "closing_message", columnDefinition = "TEXT")
    private String closingMessage;

    /**
     * Instrucciones especiales para el chatbot (ej: tono, restricciones).
     */
    @Column(name = "chatbot_instructions", columnDefinition = "TEXT")
    private String chatbotInstructions;

    /**
     * Indica si el evento está activo (visible y funcional).
     */
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    /**
     * Límite máximo de invitados permitidos (opcional).
     */
    @Column(name = "max_attendees")
    private Integer maxAttendees;

    /**
     * Presupuesto total estimado para regalos (opcional, informativo).
     */
    @Column(name = "gift_budget", precision = 10, scale = 2)
    private BigDecimal giftBudget;

    /**
     * ID del usuario organizador/creador del evento.
     */
    @Column(name = "organizer_user_id", nullable = false, length = 100)
    private String organizerUserId;

    /**
     * Nombre del organizador (padres, familia, etc.).
     */
    @Column(name = "organizer_name", length = 200)
    private String organizerName;

    /**
     * Email de contacto del organizador.
     */
    @Column(name = "organizer_email", length = 150)
    private String organizerEmail;

    /**
     * Teléfono de contacto del organizador.
     */
    @Column(name = "organizer_phone", length = 50)
    private String organizerPhone;

    /**
     * Indica si se permite reservar regalos compartidos (split).
     */
    @Column(name = "allow_shared_gifts", nullable = false)
    @Builder.Default
    private Boolean allowSharedGifts = true;

    /**
     * Indica si se permite dejar mensajes para el bebé.
     */
    @Column(name = "allow_baby_messages", nullable = false)
    @Builder.Default
    private Boolean allowBabyMessages = true;

    /**
     * Indica si se permite proponer ideas de apoyo.
     */
    @Column(name = "allow_ideas", nullable = false)
    @Builder.Default
    private Boolean allowIdeas = true;

    /**
     * URL de imagen o logo del evento (opcional).
     */
    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
