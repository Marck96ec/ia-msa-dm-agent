package com.iaproject.agent.controller;

import com.iaproject.agent.api.BabyMessagesApi;
import com.iaproject.agent.model.BabyMessageResponse;
import com.iaproject.agent.model.CreateBabyMessageRequest;
import com.iaproject.agent.model.UpdateBabyMessageRequest;
import com.iaproject.agent.service.BabyMessageService;
import com.iaproject.agent.service.mapper.ModelToDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Implementación del controlador REST para mensajes del bebé.
 * Implementa la interfaz generada desde OpenAPI (API-First).
 * 
 * NO contiene lógica de negocio - delega a BabyMessageService.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class BabyMessageController implements BabyMessagesApi {

    private final BabyMessageService babyMessageService;
    private final ModelToDtoMapper mapper;

    @Override
    public ResponseEntity<BabyMessageResponse> createBabyMessage(String eventSlug, CreateBabyMessageRequest request) {
        log.info("POST /api/v1/events/{}/baby-messages - userId={}", eventSlug, request.getUserId());
        com.iaproject.agent.service.dto.BabyMessageResponse dto = babyMessageService.createMessageBySlug(eventSlug, mapper.toServiceDto(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toModel(dto));
    }

    @Override
    public ResponseEntity<List<BabyMessageResponse>> getBabyMessages(String eventSlug, Boolean includeUnpublished) {
        log.info("GET /api/v1/events/{}/baby-messages - includeUnpublished={}", eventSlug, includeUnpublished);
        
        List<com.iaproject.agent.service.dto.BabyMessageResponse> dtos = (includeUnpublished != null && includeUnpublished)
                ? babyMessageService.getAllMessagesBySlug(eventSlug)
                : babyMessageService.getPublishedMessagesBySlug(eventSlug);
        
        return ResponseEntity.ok(mapper.toModelBabyMessageList(dtos));
    }

    @Override
    public ResponseEntity<BabyMessageResponse> updateBabyMessageStatus(Long messageId, UpdateBabyMessageRequest request) {
        log.info("PATCH /api/v1/baby-messages/{} - isPublished={}", messageId, request.getIsPublished());
        com.iaproject.agent.service.dto.BabyMessageResponse dto = babyMessageService.updateMessageStatus(messageId, mapper.toServiceDto(request));
        return ResponseEntity.ok(mapper.toModel(dto));
    }
}
