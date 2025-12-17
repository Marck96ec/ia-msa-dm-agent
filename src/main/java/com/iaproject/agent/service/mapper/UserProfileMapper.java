package com.iaproject.agent.service.mapper;

import com.iaproject.agent.domain.UserProfile;
import com.iaproject.agent.model.UserProfileDto;

/**
 * Mapper para convertir entre la entidad UserProfile y el DTO generado por OpenAPI.
 */
public class UserProfileMapper {

    /**
     * Convierte un UserProfile (entidad de dominio) a UserProfileDto (modelo OpenAPI).
     *
     * @param profile entidad UserProfile
     * @return DTO generado por OpenAPI
     */
    public static UserProfileDto toDto(UserProfile profile) {
        if (profile == null) {
            return null;
        }

        UserProfileDto dto = new UserProfileDto();
        dto.setUserId(profile.getUserId());
        dto.setPreferredLanguage(profile.getPreferredLanguage());
        
        // Mapear Tone
        if (profile.getTone() != null) {
            dto.setTone(UserProfileDto.ToneEnum.fromValue(profile.getTone().name()));
        }
        
        // Mapear Verbosity
        if (profile.getVerbosity() != null) {
            dto.setVerbosity(UserProfileDto.VerbosityEnum.fromValue(profile.getVerbosity().name()));
        }
        
        // Mapear EmojiPreference
        if (profile.getEmojiPreference() != null) {
            dto.setEmojiPreference(UserProfileDto.EmojiPreferenceEnum.fromValue(profile.getEmojiPreference().name()));
        }
        
        return dto;
    }
}
