package com.iaproject.agent.controller;

import com.iaproject.agent.dto.ChatRequest;
import com.iaproject.agent.dto.ChatResponse;
import com.iaproject.agent.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para operaciones de chat con IA.
 * Expone endpoints para interactuar con modelos de lenguaje.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    /**
     * Procesa un mensaje y devuelve la respuesta del modelo de IA.
     *
     * @param request solicitud con el mensaje del usuario
     * @return respuesta del modelo con metadatos
     */
    @PostMapping
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        log.info("Recibida solicitud de chat");
        ChatResponse response = chatService.processMessage(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint simple para mensajes rápidos sin configuración adicional.
     *
     * @param message mensaje del usuario
     * @return respuesta del modelo
     */
    @GetMapping("/simple")
    public ResponseEntity<String> simpleChat(@RequestParam String message) {
        log.info("Recibida solicitud de chat simple");
        String response = chatService.processMessageStream(message);
        return ResponseEntity.ok(response);
    }
}
