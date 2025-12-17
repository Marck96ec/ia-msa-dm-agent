package com.iaproject.agent.domain;

import com.iaproject.agent.domain.enums.EmojiPreference;
import com.iaproject.agent.domain.enums.Tone;
import com.iaproject.agent.domain.enums.Verbosity;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa el perfil conversacional de un usuario.
 * Almacena preferencias de personalidad para personalizar las respuestas de la IA:
 * tono, nivel de detalle, uso de emojis, idioma preferido, y notas de estilo.
 * 
 * La actualización del perfil es conservadora: solo se modifica cuando hay señales claras
 * del usuario (comandos explícitos o patrones consistentes en múltiples interacciones).
 */
@Entity
@Table(name = "user_profile", indexes = {
    @Index(name = "idx_user_id", columnList = "userId", unique = true),
    @Index(name = "idx_last_updated", columnList = "lastUpdatedAt")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Identificador único del usuario (puede ser: phone, sessionId, anonymousId).
     * Es el punto de entrada para recuperar o crear el perfil.
     */
    @Column(name = "user_id", nullable = false, unique = true, length = 100)
    private String userId;

    /**
     * Idioma preferido del usuario.
     * Default: es-EC (español Ecuador).
     * Valores comunes: es-EC, es-ES, en-US, etc.
     */
    @Column(name = "preferred_language", nullable = false, length = 10)
    @Builder.Default
    private String preferredLanguage = "es-EC";

    /**
     * Tono conversacional que prefiere el usuario.
     * Define la personalidad de las respuestas de la IA.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tone", nullable = false, length = 20)
    @Builder.Default
    private Tone tone = Tone.WARM;

    /**
     * Nivel de detalle que prefiere el usuario en las respuestas.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "verbosity", nullable = false, length = 20)
    @Builder.Default
    private Verbosity verbosity = Verbosity.MEDIUM;

    /**
     * Preferencia de uso de emojis en las respuestas.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "emoji_preference", nullable = false, length = 20)
    @Builder.Default
    private EmojiPreference emojiPreference = EmojiPreference.LIGHT;

    /**
     * Notas de estilo personalizadas inferidas de la conversación.
     * Ejemplos:
     * - "Prefiere respuestas directas, evita motivación clásica"
     * - "Le gusta el humor inteligente, no le gustan las listas largas"
     * - "Valora la precisión técnica sobre las explicaciones extensas"
     * 
     * Máximo 500 caracteres para mantener el enfoque.
     */
    @Column(name = "style_notes", columnDefinition = "TEXT", length = 500)
    private String styleNotes;

    /**
     * Objetivo actual de la conversación del usuario.
     * Ejemplos: "planear baby shower", "aprender Spring Boot", "resolver error 404"
     * Permite mantener contexto y evitar preguntas repetitivas.
     */
    @Column(name = "current_objective", columnDefinition = "TEXT")
    private String currentObjective;

    /**
     * Formato de respuesta preferido.
     * Valores: STEPS (pasos numerados), LIST (listas con bullets), DIRECT (directo sin formato especial)
     */
    @Column(name = "preferred_format", length = 20)
    private String preferredFormat;

    /**
     * Ritmo de respuesta preferido.
     * Valores: QUICK (rápidas y concretas), EXPLAINED (explicadas paso a paso)
     */
    @Column(name = "response_speed", length = 20)
    private String responseSpeed;

    /**
     * Decisiones importantes ya tomadas para evitar repetir preguntas.
     * Formato: lista JSON de strings.
     * Ejemplos: ["Presupuesto: $500", "Fecha: 15 de enero", "Invitados: 30 personas"]
     */
    @Type(JsonBinaryType.class)
    @Column(name = "past_decisions", columnDefinition = "jsonb")
    @Builder.Default
    private List<String> pastDecisions = new ArrayList<>();

    /**
     * Última actualización del perfil.
     * Permite rastrear la frecuencia de cambios y evitar actualizaciones prematuras.
     */
    @Column(name = "last_updated_at", nullable = false)
    @UpdateTimestamp
    private OffsetDateTime lastUpdatedAt;

    /**
     * Versión para optimistic locking.
     * Evita conflictos de actualización concurrente del perfil.
     */
    @Version
    @Column(name = "version", nullable = false)
    @Builder.Default
    private Long version = 0L;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}
