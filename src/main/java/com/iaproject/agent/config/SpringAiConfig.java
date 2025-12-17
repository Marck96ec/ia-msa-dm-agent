package com.iaproject.agent.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Spring AI.
 * Define beans necesarios para la interacción con modelos de IA.
 */
@Configuration
public class SpringAiConfig {

    /**
     * Configura el cliente de chat con opciones predeterminadas.
     * Este bean se puede inyectar en cualquier servicio que necesite interactuar con el modelo de IA.
     */
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem("Eres un asistente útil y profesional. Responde de manera clara, concisa y estructurada.")
                .build();
    }
}
