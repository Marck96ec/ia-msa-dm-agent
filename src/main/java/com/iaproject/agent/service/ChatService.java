package com.iaproject.agent.service;

import com.iaproject.agent.domain.ConversationHistory;
import com.iaproject.agent.model.ChatRequest;
import com.iaproject.agent.model.ChatResponse;
import com.iaproject.agent.model.TokenUsage;
import com.iaproject.agent.repository.ConversationHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse as AiChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
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
    private final ConversationHistoryRepository conversationHistoryRepository;

    /**
     * Procesa un mensaje y devuelve la respuesta del modelo de IA.
     * Guarda el historial de la conversación en la base de datos.
     *
     * @param request solicitud con el mensaje del usuario
     * @return respuesta del modelo con información de uso de tokens
     */
    @Transactional
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

            // Construir la respuesta usando el modelo generado
            ChatResponse response = new ChatResponse();
            response.setResponse(aiResponse.getResult().getOutput().getContent());
            response.setConversationId(conversationId);
            response.setTimestamp(OffsetDateTime.now().toString());
            response.setTokenUsage(buildTokenUsage(aiResponse));

            log.info("Respuesta generada exitosamente. Tokens usados: {}", 
            // Guardar historial en base de datos
            saveConversationHistory(request, response);

                    response.getTokenUsage() != null ? response.getTokenUsage().getTotalTokens() : 0);

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
    private TokenUsage buildTokenUsage(AiChatResponse aiResponse) {
        if (aiResponse.getMetadata() != null && aiResponse.getMetadata().getUsage() != null) {
            var usage = aiResponse.getMetadata().getUsage();
            TokenUsage tokenUsage = new TokenUsage();
            tokenUsage.setPromptTokens(usage.getPromptTokens().intValue());
            tokenUsage.setCompletionTokens(usage.getGenerationTokens().intValue());
            tokenUsage.setTotalTokens(usage.getTotalTokens().intValue());
            return tokenUsage;

    /**
     * Guarda el historial de conversación en la base de datos.
     */
    private void saveConversationHistory(ChatRequest request, ChatResponse response) {
        try {
            ConversationHistory history = ConversationHistory.builder()
                    .conversationId(response.getConversationId())
                    .userMessage(request.getMessage())
                    .aiResponse(response.getResponse())
                    .modelUsed("gpt-4o-mini") // Obtener del contexto si es posible
                    .temperature(request.getTemperature())
                    .promptTokens(response.getTokenUsage() != null ? response.getTokenUsage().getPromptTokens() : null)
                    .completionTokens(response.getTokenUsage() != null ? response.getTokenUsage().getCompletionTokens() : null)
                    .totalTokens(response.getTokenUsage() != null ? response.getTokenUsage().getTotalTokens() : null)
                    .build();

            conversationHistoryRepository.save(history);
            log.debug("Historial de conversación guardado: {}", history.getId());
        } catch (Exception e) {
            log.error("Error al guardar historial de conversación: {}", e.getMessage(), e);
            // No lanzar excepción para no interrumpir el flujo principal
        }
    }
        }
        return null;
    }
}
