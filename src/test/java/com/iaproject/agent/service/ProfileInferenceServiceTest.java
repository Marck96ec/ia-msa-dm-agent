package com.iaproject.agent.service;

import com.iaproject.agent.domain.UserProfile;
import com.iaproject.agent.domain.enums.EmojiPreference;
import com.iaproject.agent.domain.enums.Tone;
import com.iaproject.agent.domain.enums.Verbosity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para ProfileInferenceService.
 * Valida detección de comandos explícitos y construcción de Profile Prompt.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ProfileInferenceService Tests")
class ProfileInferenceServiceTest {

    @InjectMocks
    private ProfileInferenceService profileInferenceService;

    @Mock
    private UserProfileService userProfileService;

    private String userId;
    private UserProfile profile;

    @BeforeEach
    void setUp() {
        userId = "test-user";
        profile = UserProfile.builder()
                .userId(userId)
                .preferredLanguage("es-EC")
                .tone(Tone.WARM)
                .verbosity(Verbosity.MEDIUM)
                .emojiPreference(EmojiPreference.LIGHT)
                .styleNotes("Usuario valora brevedad y precisión")
                .build();
    }

    @Test
    @DisplayName("Debe detectar comando 'más corto' y actualizar verbosity=SHORT")
    void shouldDetectShortCommand() {
        // Given
        String message = "Por favor, responde más corto la próxima vez";

        // When
        boolean updated = profileInferenceService.inferAndUpdateProfile(userId, message, 5);

        // Then
        assertThat(updated).isTrue();
        verify(userProfileService).updateProfile(eq(userId), argThat(patch ->
                patch.getVerbosity() == Verbosity.SHORT
        ));
    }

    @Test
    @DisplayName("Debe detectar comando 'sin emojis' y actualizar emojiPreference=NONE")
    void shouldDetectNoEmojiCommand() {
        // Given
        String message = "No uses emojis en tus respuestas";

        // When
        boolean updated = profileInferenceService.inferAndUpdateProfile(userId, message, 3);

        // Then
        assertThat(updated).isTrue();
        verify(userProfileService).updateProfile(eq(userId), argThat(patch ->
                patch.getEmojiPreference() == EmojiPreference.NONE
        ));
    }

    @Test
    @DisplayName("Debe detectar comando 'háblame formal' y actualizar tone=FORMAL")
    void shouldDetectFormalToneCommand() {
        // Given
        String message = "Háblame más formal por favor";

        // When
        boolean updated = profileInferenceService.inferAndUpdateProfile(userId, message, 2);

        // Then
        assertThat(updated).isTrue();
        verify(userProfileService).updateProfile(eq(userId), argThat(patch ->
                patch.getTone() == Tone.FORMAL
        ));
    }

    @Test
    @DisplayName("Debe detectar múltiples comandos en un mensaje")
    void shouldDetectMultipleCommands() {
        // Given
        String message = "Responde más corto y sin emojis";

        // When
        boolean updated = profileInferenceService.inferAndUpdateProfile(userId, message, 4);

        // Then
        assertThat(updated).isTrue();
        verify(userProfileService).updateProfile(eq(userId), argThat(patch ->
                patch.getVerbosity() == Verbosity.SHORT &&
                patch.getEmojiPreference() == EmojiPreference.NONE
        ));
    }

    @Test
    @DisplayName("No debe actualizar perfil si no detecta comandos")
    void shouldNotUpdateProfileWithoutCommands() {
        // Given
        String message = "¿Cuáles son las mejores ideas para un baby shower?";

        // When
        boolean updated = profileInferenceService.inferAndUpdateProfile(userId, message, 2);

        // Then
        assertThat(updated).isFalse();
        verify(userProfileService, never()).updateProfile(any(), any());
    }

    @Test
    @DisplayName("Debe construir Profile Prompt correctamente")
    void shouldBuildProfilePrompt() {
        // When
        String prompt = profileInferenceService.buildProfilePrompt(profile);

        // Then
        assertThat(prompt).contains("Idioma preferido: es-EC");
        assertThat(prompt).contains("respuestas moderadas");
        assertThat(prompt).contains("emojis de forma ligera");
        assertThat(prompt).contains("Tono: cálido y humano");
        assertThat(prompt).contains("Usuario valora brevedad y precisión");
        assertThat(prompt).contains("1 pregunta de cierre");
    }

    @Test
    @DisplayName("Profile Prompt debe variar según verbosity")
    void shouldBuildProfilePromptWithShortVerbosity() {
        // Given
        profile.setVerbosity(Verbosity.SHORT);

        // When
        String prompt = profileInferenceService.buildProfilePrompt(profile);

        // Then
        assertThat(prompt).contains("cortas y directas");
        assertThat(prompt).contains("máximo 6 líneas");
    }

    @Test
    @DisplayName("Profile Prompt debe variar según emoji preference")
    void shouldBuildProfilePromptWithNoEmoji() {
        // Given
        profile.setEmojiPreference(EmojiPreference.NONE);

        // When
        String prompt = profileInferenceService.buildProfilePrompt(profile);

        // Then
        assertThat(prompt).contains("NO uses emojis");
    }

    @Test
    @DisplayName("Debe detectar comando 'con emojis' y actualizar emojiPreference=HEAVY")
    void shouldDetectWithEmojiCommand() {
        // Given
        String message = "Usa más emojis en tus respuestas";

        // When
        boolean updated = profileInferenceService.inferAndUpdateProfile(userId, message, 2);

        // Then
        assertThat(updated).isTrue();
        verify(userProfileService).updateProfile(eq(userId), argThat(patch ->
                patch.getEmojiPreference() == EmojiPreference.HEAVY
        ));
    }

    @Test
    @DisplayName("Debe detectar comando 'más detallado' y actualizar verbosity=DETAILED")
    void shouldDetectDetailedCommand() {
        // Given
        String message = "Dame más detalle en tus explicaciones";

        // When
        boolean updated = profileInferenceService.inferAndUpdateProfile(userId, message, 3);

        // Then
        assertThat(updated).isTrue();
        verify(userProfileService).updateProfile(eq(userId), argThat(patch ->
                patch.getVerbosity() == Verbosity.DETAILED
        ));
    }
}
