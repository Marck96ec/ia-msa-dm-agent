package com.iaproject.agent.service;

import com.iaproject.agent.domain.UserProfile;
import com.iaproject.agent.domain.enums.EmojiPreference;
import com.iaproject.agent.domain.enums.Tone;
import com.iaproject.agent.domain.enums.Verbosity;
import com.iaproject.agent.service.dto.UserProfilePatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 * Servicio para inferir y actualizar automáticamente el perfil del usuario
 * basándose en comandos explícitos o patrones detectados en la conversación.
 * 
 * Principio: Actualizaciones conservadoras.
 * - Modo 1 (prioridad): Detectar comandos explícitos del usuario
 * - Modo 2 (futuro): Inferencia con IA controlada y validada
 * 
 * Solo se actualiza el perfil cuando hay señales claras, nunca por frases ambiguas.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileInferenceService {

    private final UserProfileService userProfileService;

    // Patrones para comandos explícitos de preferencias
    private static final Pattern CMD_SHORT = Pattern.compile(
            "\\b(más\\s+corto|respuestas\\s+cortas|sé\\s+breve|brevemente|responde\\s+corto)\\b",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    private static final Pattern CMD_DETAILED = Pattern.compile(
            "\\b(más\\s+detalle|detallado|explicación\\s+completa|profundiza|extiendet)\\b",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    private static final Pattern CMD_NO_EMOJI = Pattern.compile(
            "\\b(sin\\s+emojis?|no\\s+uses?\\s+emojis?|evita\\s+emojis?)\\b",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    private static final Pattern CMD_WITH_EMOJI = Pattern.compile(
            "\\b(usa\\s+emojis?|con\\s+emojis?|agrega\\s+emojis?|más\\s+emojis?)\\b",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    private static final Pattern CMD_FORMAL = Pattern.compile(
            "\\b(háblame\\s+formal|más\\s+formal|tono\\s+formal|profesional)\\b",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    private static final Pattern CMD_WARM = Pattern.compile(
            "\\b(háblame\\s+cálido|más\\s+cercano|tono\\s+cálido|amigable|amistoso)\\b",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    private static final Pattern CMD_FUNNY = Pattern.compile(
            "\\b(háblame\\s+divertido|con\\s+humor|gracioso|tono\\s+relajado)\\b",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    private static final Pattern CMD_NEUTRAL = Pattern.compile(
            "\\b(háblame\\s+neutral|tono\\s+neutral|objetivo|sin\\s+emociones?)\\b",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    // Patrones para formato de respuesta
    private static final Pattern CMD_FORMAT_STEPS = Pattern.compile(
            "\\b(en\\s+pasos|paso\\s+a\\s+paso|numerado|enumera)\\b",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    private static final Pattern CMD_FORMAT_LIST = Pattern.compile(
            "\\b(en\\s+lista|listado|bullets|viñetas)\\b",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    private static final Pattern CMD_FORMAT_DIRECT = Pattern.compile(
            "\\b(directo|sin\\s+formato|al\\s+grano|vamos\\s+al\\s+grano)\\b",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    // Patrones para ritmo de respuesta
    private static final Pattern CMD_SPEED_QUICK = Pattern.compile(
            "\\b(rápido|responde\\s+rápido|conciso|sin\\s+explicaciones)\\b",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    private static final Pattern CMD_SPEED_EXPLAINED = Pattern.compile(
            "\\b(explicado|explícame|con\\s+ejemplos|detalla)\\b",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    /**
     * Analiza el mensaje del usuario en busca de comandos explícitos
     * que indiquen cambios en sus preferencias de perfil.
     * Si se detectan cambios, actualiza el perfil automáticamente.
     *
     * @param userId identificador del usuario
     * @param userMessage mensaje enviado por el usuario
     * @param messageCount número total de mensajes del usuario (para decidir si inferir)
     * @return true si se actualizó el perfil
     */
    public boolean inferAndUpdateProfile(String userId, String userMessage, int messageCount) {
        log.debug("Analizando mensaje para inferencia de perfil: userId={}, messageCount={}", 
                userId, messageCount);

        UserProfilePatch patch = detectExplicitCommands(userMessage);

        if (patch.hasChanges()) {
            log.info("Comandos explícitos detectados en el mensaje, actualizando perfil: userId={}", userId);
            userProfileService.updateProfile(userId, patch);
            return true;
        }

        // Futuro: Modo 2 - Inferencia con IA (solo cada N mensajes para ser conservador)
        // Ejemplo: cada 10 mensajes, usar Spring AI para resumir preferencias y proponer cambios
        // con validación estricta de schema

        return false;
    }

    /**
     * Detecta comandos explícitos del usuario para cambiar preferencias.
     *
     * @param message mensaje del usuario
     * @return patch con cambios detectados (puede estar vacío)
     */
    private UserProfilePatch detectExplicitCommands(String message) {
        UserProfilePatch.UserProfilePatchBuilder patchBuilder = UserProfilePatch.builder();
        boolean hasChanges = false;

        // Detectar verbosity
        if (CMD_SHORT.matcher(message).find()) {
            patchBuilder.verbosity(Verbosity.SHORT);
            hasChanges = true;
            log.debug("Comando detectado: verbosity=SHORT");
        } else if (CMD_DETAILED.matcher(message).find()) {
            patchBuilder.verbosity(Verbosity.DETAILED);
            hasChanges = true;
            log.debug("Comando detectado: verbosity=DETAILED");
        }

        // Detectar emoji preference
        if (CMD_NO_EMOJI.matcher(message).find()) {
            patchBuilder.emojiPreference(EmojiPreference.NONE);
            hasChanges = true;
            log.debug("Comando detectado: emojiPreference=NONE");
        } else if (CMD_WITH_EMOJI.matcher(message).find()) {
            patchBuilder.emojiPreference(EmojiPreference.HEAVY);
            hasChanges = true;
            log.debug("Comando detectado: emojiPreference=HEAVY");
        }

        // Detectar tone
        if (CMD_FORMAL.matcher(message).find()) {
            patchBuilder.tone(Tone.FORMAL);
            hasChanges = true;
            log.debug("Comando detectado: tone=FORMAL");
        } else if (CMD_WARM.matcher(message).find()) {
            patchBuilder.tone(Tone.WARM);
            hasChanges = true;
            log.debug("Comando detectado: tone=WARM");
        } else if (CMD_FUNNY.matcher(message).find()) {
            patchBuilder.tone(Tone.FUNNY);
            hasChanges = true;
            log.debug("Comando detectado: tone=FUNNY");
        } else if (CMD_NEUTRAL.matcher(message).find()) {
            patchBuilder.tone(Tone.NEUTRAL);
            hasChanges = true;
            log.debug("Comando detectado: tone=NEUTRAL");
        }

        // Detectar formato preferido
        if (CMD_FORMAT_STEPS.matcher(message).find()) {
            patchBuilder.preferredFormat("STEPS");
            hasChanges = true;
            log.debug("Comando detectado: preferredFormat=STEPS");
        } else if (CMD_FORMAT_LIST.matcher(message).find()) {
            patchBuilder.preferredFormat("LIST");
            hasChanges = true;
            log.debug("Comando detectado: preferredFormat=LIST");
        } else if (CMD_FORMAT_DIRECT.matcher(message).find()) {
            patchBuilder.preferredFormat("DIRECT");
            hasChanges = true;
            log.debug("Comando detectado: preferredFormat=DIRECT");
        }

        // Detectar ritmo de respuesta
        if (CMD_SPEED_QUICK.matcher(message).find()) {
            patchBuilder.responseSpeed("QUICK");
            hasChanges = true;
            log.debug("Comando detectado: responseSpeed=QUICK");
        } else if (CMD_SPEED_EXPLAINED.matcher(message).find()) {
            patchBuilder.responseSpeed("EXPLAINED");
            hasChanges = true;
            log.debug("Comando detectado: responseSpeed=EXPLAINED");
        }

        return patchBuilder.build();
    }

    /**
     * Construye el Profile Prompt para inyectar en el System Prompt.
     * Este prompt guía a la IA para responder según las preferencias del usuario.
     *
     * @param profile perfil del usuario
     * @return texto del Profile Prompt
     */
    public String buildProfilePrompt(UserProfile profile) {
        if (profile == null) {
            return "";
        }

        StringBuilder prompt = new StringBuilder();
        prompt.append("\n--- PERFIL DEL USUARIO ---\n");

        // Idioma
        prompt.append(String.format("Idioma preferido: %s\n", profile.getPreferredLanguage()));

        // Verbosity
        switch (profile.getVerbosity()) {
            case SHORT:
                prompt.append("Estilo: respuestas cortas y directas (máximo 6 líneas).\n");
                break;
            case MEDIUM:
                prompt.append("Estilo: respuestas moderadas (8-12 líneas).\n");
                break;
            case DETAILED:
                prompt.append("Estilo: respuestas detalladas y completas.\n");
                break;
        }

        // Emoji preference
        switch (profile.getEmojiPreference()) {
            case NONE:
                prompt.append("NO uses emojis en las respuestas.\n");
                break;
            case LIGHT:
                prompt.append("Usa emojis de forma ligera (1-2 por respuesta).\n");
                break;
            case HEAVY:
                prompt.append("Usa emojis frecuentemente (3+ por respuesta) para expresividad.\n");
                break;
        }

        // Tone
        switch (profile.getTone()) {
            case WARM:
                prompt.append("Tono: cálido y humano, empático y cercano.\n");
                break;
            case NEUTRAL:
                prompt.append("Tono: neutral, profesional y objetivo.\n");
                break;
            case FORMAL:
                prompt.append("Tono: formal, cortés y respetuoso.\n");
                break;
            case FUNNY:
                prompt.append("Tono: divertido y relajado, con humor inteligente.\n");
                break;
        }

        // Style notes
        if (profile.getStyleNotes() != null && !profile.getStyleNotes().isBlank()) {
            prompt.append(String.format("Notas de estilo: %s\n", profile.getStyleNotes()));
        }

        // Cierre
        prompt.append("Haz 1 pregunta de cierre para avanzar la conversación.\n");
        prompt.append("--- FIN PERFIL ---\n");

        return prompt.toString();
    }
}
