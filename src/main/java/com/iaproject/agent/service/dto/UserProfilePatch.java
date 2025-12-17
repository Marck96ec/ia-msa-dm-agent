package com.iaproject.agent.service.dto;

import com.iaproject.agent.domain.enums.EmojiPreference;
import com.iaproject.agent.domain.enums.Tone;
import com.iaproject.agent.domain.enums.Verbosity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para actualización parcial del perfil de usuario.
 * Permite modificar solo los campos especificados.
 * Todos los campos son opcionales.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfilePatch {

    private String preferredLanguage;
    private Tone tone;
    private Verbosity verbosity;
    private EmojiPreference emojiPreference;
    private String styleNotes;
    
    // Nuevos campos de memoria progresiva
    private String currentObjective;
    private String preferredFormat;
    private String responseSpeed;
    private List<String> pastDecisions;

    /**
     * Verifica si el patch tiene al menos un campo no nulo.
     *
     * @return true si hay al menos un campo para actualizar
     */
    public boolean hasChanges() {
        return preferredLanguage != null 
            || tone != null 
            || verbosity != null 
            || emojiPreference != null 
            || styleNotes != null
            || currentObjective != null
            || preferredFormat != null
            || responseSpeed != null
            || pastDecisions != null;
    }
}
