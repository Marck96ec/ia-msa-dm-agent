package com.iaproject.agent.controller;

import com.iaproject.agent.api.EventsApi;
import com.iaproject.agent.model.CreateEventRequest;
import com.iaproject.agent.model.EventDashboardResponse;
import com.iaproject.agent.model.EventPublicResponse;
import com.iaproject.agent.model.UpdateEventRequest;
import com.iaproject.agent.service.*;
import com.iaproject.agent.service.mapper.ModelToDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Implementación del controlador REST para gestionar eventos.
 * Implementa la interfaz generada desde OpenAPI (API-First).
 * 
 * NO contiene lógica de negocio - delega a servicios.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class EventController implements EventsApi {

    private final EventService eventService;
    private final DashboardService dashboardService;
    private final ModelToDtoMapper mapper;

    @Override
    public ResponseEntity<EventPublicResponse> getEventBySlug(String slug) {
        log.info("GET /api/v1/events/{}", slug);
        com.iaproject.agent.service.dto.EventPublicResponse dto = eventService.getEventBySlug(slug);
        return ResponseEntity.ok(mapper.toModel(dto));
    }

    @Override
    public ResponseEntity<EventPublicResponse> createEvent(CreateEventRequest request) {
        log.info("POST /api/v1/events - slug={}", request.getSlug());
        com.iaproject.agent.service.dto.EventPublicResponse dto = eventService.createEvent(mapper.toServiceDto(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toModel(dto));
    }

    @Override
    public ResponseEntity<EventPublicResponse> updateEvent(String eventSlug, UpdateEventRequest request) {
        log.info("PUT /api/v1/events/{}", eventSlug);
        com.iaproject.agent.service.dto.EventPublicResponse dto = eventService.updateEventBySlug(eventSlug, mapper.toServiceDto(request));
        return ResponseEntity.ok(mapper.toModel(dto));
    }

    @Override
    public ResponseEntity<EventDashboardResponse> getEventDashboard(String eventSlug) {
        log.info("GET /api/v1/events/{}/dashboard", eventSlug);
        com.iaproject.agent.service.dto.EventDashboardResponse dto = dashboardService.getEventDashboard(eventSlug);
        return ResponseEntity.ok(mapper.toModel(dto));
    }
}
