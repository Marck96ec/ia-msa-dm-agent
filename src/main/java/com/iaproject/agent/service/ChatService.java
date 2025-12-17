package com.iaproject.agent.service;

import com.iaproject.agent.dto.ChatRequest;
import com.iaproject.agent.dto.ChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse as AiChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Servicio que gestiona las interacciones con el modelo de IA.
 * Implementa la lógica de negocio para el procesamiento de mensajes.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatClient chatClient;

    /**
     * Procesa un mensaje y devuelve la respuesta del modelo de IA.
     *
     * @param request solicitud con el mensaje del usuario
     * @return respuesta del modelo con información de uso de tokens
     */
    public ChatResponse processMessage(ChatRequest request) {
        log.info("Procesando mensaje: {}", request.getMessage());

        try {
            // Generar o usar el ID de conversación existente
            String conversationId = request.getConversationId() != null 
                    ? request.getConversationId() 
                    : UUID.randomUUID().toString();

            // Configurar opciones del chat si se proporcionan
            ChatClient.ChatClientRequest chatClientRequest = chatClient
                    .prompt()
                    .user(request.getMessage());

            // Aplicar configuraciones opcionales
            if (request.getTemperature() != null || request.getMaxTokens() != null) {
                OpenAiChatOptions.Builder optionsBuilder = OpenAiChatOptions.builder();
                
                if (request.getTemperature() != null) {
                    optionsBuilder.temperature(request.getTemperature());
                }
                
                if (request.getMaxTokens() != null) {
                    optionsBuilder.maxTokens(request.getMaxTokens());
                }
                
                chatClientRequest.options(optionsBuilder.build());
            }

            // Ejecutar la llamada al modelo
            AiChatResponse aiResponse = chatClientRequest.call().chatResponse();

            // Construir la respuesta
            ChatResponse response = ChatResponse.builder()
                    .response(aiResponse.getResult().getOutput().getContent())
                    .conversationId(conversationId)
                    .timestamp(LocalDateTime.now())
                    .tokenUsage(buildTokenUsage(aiResponse))
                    .build();

            log.info("Respuesta generada exitosamente. Tokens usados: {}", 
                    response.getTokenUsage().getTotalTokens());

            return response;

        } catch (Exception e) {
            log.error("Error al procesar mensaje: {}", e.getMessage(), e);
            throw new RuntimeException("Error al procesar el mensaje con el modelo de IA", e);
        }
    }

    /**
     * Genera una respuesta en streaming (útil para respuestas largas).
     *
     * @param message mensaje del usuario
     * @return respuesta del modelo
     */
    public String processMessageStream(String message) {
        log.info("Procesando mensaje en modo streaming: {}", message);

        try {
            return chatClient
                    .prompt()
                    .user(message)
                    .call()
                    .content();

        } catch (Exception e) {
            log.error("Error al procesar mensaje en streaming: {}", e.getMessage(), e);
            throw new RuntimeException("Error al procesar el mensaje", e);
        }
    }

    /**
     * Construye el objeto de uso de tokens desde la respuesta de la IA.
     */
    private ChatResponse.TokenUsage buildTokenUsage(AiChatResponse aiResponse) {
        if (aiResponse.getMetadata() != null && aiResponse.getMetadata().getUsage() != null) {
            var usage = aiResponse.getMetadata().getUsage();
            return ChatResponse.TokenUsage.builder()
                    .promptTokens(usage.getPromptTokens().intValue())
                    .completionTokens(usage.getGenerationTokens().intValue())
                    .totalTokens(usage.getTotalTokens().intValue())
                    .build();
        }
        return null;
    }
}
