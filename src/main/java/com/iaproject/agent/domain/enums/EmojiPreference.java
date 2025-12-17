package com.iaproject.agent.domain.enums;

/**
 * Preferencia de uso de emojis en las respuestas de la IA.
 */
public enum EmojiPreference {
    /**
     * Sin emojis en las respuestas.
     * Para usuarios que prefieren comunicación textual pura.
     */
    NONE,
    
    /**
     * Uso ligero de emojis (1-2 por respuesta).
     * Balance entre expresividad y profesionalismo.
     */
    LIGHT,
    
    /**
     * Uso frecuente de emojis (3+ por respuesta).
     * Para comunicación expresiva y casual.
     */
    HEAVY
}
