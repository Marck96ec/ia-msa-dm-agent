package com.iaproject.agent.controller;

import com.iaproject.agent.api.IdeasApi;
import com.iaproject.agent.model.CreateIdeaRequest;
import com.iaproject.agent.model.IdeaResponse;
import com.iaproject.agent.service.IdeaService;
import com.iaproject.agent.service.mapper.ModelToDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Implementación del controlador REST para gestionar ideas.
 * Implementa la interfaz generada desde OpenAPI (API-First).
 * 
 * NO contiene lógica de negocio - delega a IdeaService.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class IdeaController implements IdeasApi {

    private final IdeaService ideaService;
    private final ModelToDtoMapper mapper;

    @Override
    public ResponseEntity<IdeaResponse> createIdea(String eventSlug, CreateIdeaRequest request) {
        log.info("POST /api/v1/events/{}/ideas - userId={}", eventSlug, request.getUserId());
        com.iaproject.agent.service.dto.IdeaResponse dto = ideaService.createIdeaBySlug(eventSlug, mapper.toServiceDto(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toModel(dto));
    }

    @Override
    public ResponseEntity<List<IdeaResponse>> getIdeas(String eventSlug) {
        log.info("GET /api/v1/events/{}/ideas", eventSlug);
        List<com.iaproject.agent.service.dto.IdeaResponse> dtos = ideaService.getIdeasByEventSlug(eventSlug);
        return ResponseEntity.ok(mapper.toModelIdeaList(dtos));
    }
}
