package com.iaproject.agent.service;

import com.iaproject.agent.domain.UserProfile;
import com.iaproject.agent.domain.enums.EmojiPreference;
import com.iaproject.agent.domain.enums.Tone;
import com.iaproject.agent.domain.enums.Verbosity;
import com.iaproject.agent.repository.UserProfileRepository;
import com.iaproject.agent.service.dto.UserProfilePatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Servicio para gestionar perfiles de usuario.
 * Responsabilidades:
 * - Crear perfiles con defaults inteligentes
 * - Recuperar perfiles existentes
 * - Actualizar perfiles de manera conservadora y segura
 * 
 * Principio: Los perfiles solo se actualizan con señales claras (comandos explícitos
 * o patrones consistentes), nunca por una sola interacción ambigua.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    /**
     * Obtiene el perfil de un usuario o lo crea si no existe.
     *
     * @param userId identificador del usuario
     * @return perfil del usuario (existente o recién creado)
     */
    @Transactional
    public UserProfile getOrCreate(String userId) {
        return userProfileRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultProfile(userId));
    }

    /**
     * Crea un perfil con valores por defecto.
     *
     * @param userId identificador del usuario
     * @return perfil creado y guardado
     */
    private UserProfile createDefaultProfile(String userId) {
        log.info("Creando perfil por defecto para userId: {}", userId);
        
        UserProfile profile = UserProfile.builder()
                .userId(userId)
                .preferredLanguage("es-EC")
                .tone(Tone.WARM)
                .verbosity(Verbosity.MEDIUM)
                .emojiPreference(EmojiPreference.LIGHT)
                .styleNotes(null)
                .build();

        UserProfile saved = userProfileRepository.save(profile);
        log.info("✅ Perfil creado: userId={}, id={}", saved.getUserId(), saved.getId());
        return saved;
    }

    /**
     * Actualiza el perfil de un usuario de manera parcial (patch).
     * Solo actualiza los campos presentes en el patch.
     *
     * @param userId identificador del usuario
     * @param patch campos a actualizar
     * @return perfil actualizado
     */
    @Transactional
    public UserProfile updateProfile(String userId, UserProfilePatch patch) {
        if (!patch.hasChanges()) {
            log.debug("Patch vacío, no se actualiza el perfil para userId: {}", userId);
            return getOrCreate(userId);
        }

        UserProfile profile = getOrCreate(userId);
        boolean hasChanges = applyPatch(profile, patch);

        if (hasChanges) {
            UserProfile saved = userProfileRepository.save(profile);
            log.info("✅ Perfil actualizado: userId={}, cambios={}", userId, patch);
            return saved;
        } else {
            log.debug("No hubo cambios reales en el perfil para userId: {}", userId);
            return profile;
        }
    }

    /**
     * Aplica un patch al perfil.
     *
     * @param profile perfil a modificar
     * @param patch cambios a aplicar
     * @return true si hubo algún cambio real
     */
    private boolean applyPatch(UserProfile profile, UserProfilePatch patch) {
        boolean changed = false;

        if (patch.getPreferredLanguage() != null 
                && !patch.getPreferredLanguage().equals(profile.getPreferredLanguage())) {
            profile.setPreferredLanguage(patch.getPreferredLanguage());
            changed = true;
        }

        if (patch.getTone() != null && patch.getTone() != profile.getTone()) {
            profile.setTone(patch.getTone());
            changed = true;
        }

        if (patch.getVerbosity() != null && patch.getVerbosity() != profile.getVerbosity()) {
            profile.setVerbosity(patch.getVerbosity());
            changed = true;
        }

        if (patch.getEmojiPreference() != null 
                && patch.getEmojiPreference() != profile.getEmojiPreference()) {
            profile.setEmojiPreference(patch.getEmojiPreference());
            changed = true;
        }

        if (patch.getStyleNotes() != null 
                && !patch.getStyleNotes().equals(profile.getStyleNotes())) {
            // Limitar styleNotes a 500 caracteres
            String notes = patch.getStyleNotes();
            if (notes.length() > 500) {
                notes = notes.substring(0, 500);
            }
            profile.setStyleNotes(notes);
            changed = true;
        }

        if (patch.getCurrentObjective() != null
                && !patch.getCurrentObjective().equals(profile.getCurrentObjective())) {
            profile.setCurrentObjective(patch.getCurrentObjective());
            changed = true;
        }

        if (patch.getPreferredFormat() != null
                && !patch.getPreferredFormat().equals(profile.getPreferredFormat())) {
            profile.setPreferredFormat(patch.getPreferredFormat());
            changed = true;
        }

        if (patch.getResponseSpeed() != null
                && !patch.getResponseSpeed().equals(profile.getResponseSpeed())) {
            profile.setResponseSpeed(patch.getResponseSpeed());
            changed = true;
        }

        if (patch.getPastDecisions() != null) {
            profile.setPastDecisions(patch.getPastDecisions());
            changed = true;
        }

        return changed;
    }

    /**
     * Genera un userId anónimo único para usuarios sin identificación.
     *
     * @return userId anónimo generado
     */
    public String generateAnonymousUserId() {
        String anonymousId = "anon-" + UUID.randomUUID().toString();
        log.debug("UserId anónimo generado: {}", anonymousId);
        return anonymousId;
    }

    /**
     * Verifica si un userId es anónimo.
     *
     * @param userId identificador del usuario
     * @return true si es anónimo
     */
    public boolean isAnonymousUser(String userId) {
        return userId != null && userId.startsWith("anon-");
    }
}
