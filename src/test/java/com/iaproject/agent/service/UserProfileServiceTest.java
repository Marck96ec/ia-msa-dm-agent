package com.iaproject.agent.service;

import com.iaproject.agent.domain.UserProfile;
import com.iaproject.agent.domain.enums.EmojiPreference;
import com.iaproject.agent.domain.enums.Tone;
import com.iaproject.agent.domain.enums.Verbosity;
import com.iaproject.agent.repository.UserProfileRepository;
import com.iaproject.agent.service.dto.UserProfilePatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para UserProfileService.
 * Valida creación, recuperación y actualización de perfiles.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserProfileService Tests")
class UserProfileServiceTest {

    @InjectMocks
    private UserProfileService userProfileService;

    @Mock
    private UserProfileRepository userProfileRepository;

    private String userId;
    private UserProfile existingProfile;

    @BeforeEach
    void setUp() {
        userId = "test-user";
        existingProfile = UserProfile.builder()
                .id(1L)
                .userId(userId)
                .preferredLanguage("es-EC")
                .tone(Tone.WARM)
                .verbosity(Verbosity.MEDIUM)
                .emojiPreference(EmojiPreference.LIGHT)
                .version(0L)
                .build();
    }

    @Test
    @DisplayName("Debe crear perfil si no existe")
    void shouldCreateProfileIfNotExists() {
        // Given
        when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(userProfileRepository.save(any(UserProfile.class))).thenAnswer(i -> i.getArgument(0));

        // When
        UserProfile profile = userProfileService.getOrCreate(userId);

        // Then
        assertThat(profile).isNotNull();
        assertThat(profile.getUserId()).isEqualTo(userId);
        assertThat(profile.getPreferredLanguage()).isEqualTo("es-EC");
        assertThat(profile.getTone()).isEqualTo(Tone.WARM);
        assertThat(profile.getVerbosity()).isEqualTo(Verbosity.MEDIUM);
        assertThat(profile.getEmojiPreference()).isEqualTo(EmojiPreference.LIGHT);
        
        verify(userProfileRepository).save(any(UserProfile.class));
    }

    @Test
    @DisplayName("Debe devolver perfil existente si ya existe")
    void shouldReturnExistingProfile() {
        // Given
        when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.of(existingProfile));

        // When
        UserProfile profile = userProfileService.getOrCreate(userId);

        // Then
        assertThat(profile).isEqualTo(existingProfile);
        verify(userProfileRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe actualizar perfil parcialmente con patch")
    void shouldUpdateProfileWithPatch() {
        // Given
        when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.of(existingProfile));
        when(userProfileRepository.save(any(UserProfile.class))).thenAnswer(i -> i.getArgument(0));

        UserProfilePatch patch = UserProfilePatch.builder()
                .tone(Tone.FORMAL)
                .verbosity(Verbosity.SHORT)
                .build();

        // When
        UserProfile updated = userProfileService.updateProfile(userId, patch);

        // Then
        assertThat(updated.getTone()).isEqualTo(Tone.FORMAL);
        assertThat(updated.getVerbosity()).isEqualTo(Verbosity.SHORT);
        assertThat(updated.getEmojiPreference()).isEqualTo(EmojiPreference.LIGHT); // No cambió
        
        verify(userProfileRepository).save(existingProfile);
    }

    @Test
    @DisplayName("No debe guardar si patch está vacío")
    void shouldNotSaveIfPatchIsEmpty() {
        // Given
        when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.of(existingProfile));

        UserProfilePatch emptyPatch = UserProfilePatch.builder().build();

        // When
        UserProfile profile = userProfileService.updateProfile(userId, emptyPatch);

        // Then
        assertThat(profile).isEqualTo(existingProfile);
        verify(userProfileRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe truncar styleNotes a 500 caracteres")
    void shouldTruncateStyleNotesToMaxLength() {
        // Given
        when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.of(existingProfile));
        when(userProfileRepository.save(any(UserProfile.class))).thenAnswer(i -> i.getArgument(0));

        String longNotes = "a".repeat(600); // Supera el límite de 500
        UserProfilePatch patch = UserProfilePatch.builder()
                .styleNotes(longNotes)
                .build();

        // When
        UserProfile updated = userProfileService.updateProfile(userId, patch);

        // Then
        assertThat(updated.getStyleNotes()).hasSize(500);
        verify(userProfileRepository).save(existingProfile);
    }

    @Test
    @DisplayName("Debe generar userId anónimo válido")
    void shouldGenerateValidAnonymousUserId() {
        // When
        String anonymousId = userProfileService.generateAnonymousUserId();

        // Then
        assertThat(anonymousId).startsWith("anon-");
        assertThat(anonymousId).hasSize(41); // "anon-" + UUID (36 caracteres)
    }

    @Test
    @DisplayName("Debe identificar correctamente userId anónimo")
    void shouldIdentifyAnonymousUserId() {
        // Given
        String anonymousId = "anon-550e8400-e29b-41d4-a716-446655440000";
        String normalId = "user-123";

        // When & Then
        assertThat(userProfileService.isAnonymousUser(anonymousId)).isTrue();
        assertThat(userProfileService.isAnonymousUser(normalId)).isFalse();
        assertThat(userProfileService.isAnonymousUser(null)).isFalse();
    }

    @Test
    @DisplayName("No debe actualizar perfil si los valores son iguales")
    void shouldNotUpdateIfValuesAreTheSame() {
        // Given
        when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.of(existingProfile));

        UserProfilePatch patch = UserProfilePatch.builder()
                .tone(Tone.WARM) // Mismo valor actual
                .verbosity(Verbosity.MEDIUM) // Mismo valor actual
                .build();

        // When
        UserProfile profile = userProfileService.updateProfile(userId, patch);

        // Then
        assertThat(profile).isEqualTo(existingProfile);
        verify(userProfileRepository, never()).save(any());
    }
}
