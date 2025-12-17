package com.iaproject.agent.controller;

import com.iaproject.agent.api.ChatApi;
import com.iaproject.agent.model.ChatRequest;
import com.iaproject.agent.model.ChatResponse;
import com.iaproject.agent.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Implementación del controlador REST para operaciones de chat con IA.
 * Implementa la interfaz generada a partir de la especificación OpenAPI (API-First).
 * Expone endpoints para interactuar con modelos de lenguaje.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController implements ChatApi {

    private final ChatService chatService;

    /**
     * Procesa un mensaje y devuelve la respuesta del modelo de IA.
     * Implementa el endpoint POST /api/v1/chat definido en la especificación OpenAPI.
     *
     * @param chatRequest solicitud con el mensaje del usuario
     * @return respuesta del modelo con metadatos
     */
    @Override
    public ResponseEntity<ChatResponse> chat(ChatRequest chatRequest) {
        log.info("Recibida solicitud de chat");
        ChatResponse response = chatService.processMessage(chatRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint simple para mensajes rápidos sin configuración adicional.
     * Implementa el endpoint GET /api/v1/chat/simple definido en la especificación OpenAPI.
     *
     * @param message mensaje del usuario
     * @return respuesta del modelo como texto plano
     */
    @Override
    public ResponseEntity<String> simpleChat(String message) {
        log.info("Recibida solicitud de chat simple: {}", message);
        String response = chatService.processMessageStream(message);
        return ResponseEntity.ok(response);
    }
}
