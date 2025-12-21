package com.iaproject.agent.controller;

import com.iaproject.agent.api.RsvpApi;
import com.iaproject.agent.model.AttendeeSummaryResponse;
import com.iaproject.agent.model.RSVPListResponse;
import com.iaproject.agent.model.RSVPRequest;
import com.iaproject.agent.model.RSVPResponse;
import com.iaproject.agent.service.RSVPService;
import com.iaproject.agent.service.mapper.ModelToDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Implementación del controlador REST para gestionar RSVPs.
 * Implementa la interfaz generada desde OpenAPI (API-First).
 * 
 * NO contiene lógica de negocio - delega a RSVPService.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class RSVPController implements RsvpApi {

    private final RSVPService rsvpService;
    private final ModelToDtoMapper mapper;

    @Override
    public ResponseEntity<RSVPResponse> createRSVP(String eventSlug, RSVPRequest request) {
        log.info("POST /api/v1/events/{}/rsvp - userId={}, status={}", eventSlug, request.getUserId(), request.getStatus());
        com.iaproject.agent.service.dto.RSVPResponse dto = rsvpService.createOrUpdateRSVPBySlug(eventSlug, mapper.toServiceDto(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toModel(dto));
    }

    @Override
    public ResponseEntity<RSVPResponse> getRSVP(String eventSlug, String userId) {
        log.info("GET /api/v1/events/{}/rsvp/{}", eventSlug, userId);
        com.iaproject.agent.service.dto.RSVPResponse dto = rsvpService.getRSVPByUserAndSlug(eventSlug, userId);
        return ResponseEntity.ok(mapper.toModel(dto));
    }

    @Override
    public ResponseEntity<RSVPResponse> updateRSVP(String eventSlug, String userId, RSVPRequest request) {
        log.info("PUT /api/v1/events/{}/rsvp/{} - status={}", eventSlug, userId, request.getStatus());
        request.setUserId(userId);
        com.iaproject.agent.service.dto.RSVPResponse dto = rsvpService.createOrUpdateRSVPBySlug(eventSlug, mapper.toServiceDto(request));
        return ResponseEntity.ok(mapper.toModel(dto));
    }

    @Override
    public ResponseEntity<List<RSVPResponse>> getAttendees(String eventSlug) {
        log.info("GET /api/v1/events/{}/attendees", eventSlug);
        List<com.iaproject.agent.service.dto.RSVPResponse> dtos = rsvpService.getAttendeesBySlug(eventSlug);
        return ResponseEntity.ok(mapper.toModelRSVPList(dtos));
    }

    @Override
    public ResponseEntity<AttendeeSummaryResponse> getAttendeeSummary(String eventSlug) {
        log.info("GET /api/v1/events/{}/attendees/summary", eventSlug);
        com.iaproject.agent.service.dto.AttendeeSummaryResponse dto = rsvpService.getAttendeeSummaryBySlug(eventSlug);
        return ResponseEntity.ok(mapper.toModel(dto));
    }

    @Override
    public ResponseEntity<RSVPListResponse> getAllRSVPs(String eventSlug) {
        log.info("GET /api/v1/events/{}/rsvps", eventSlug);
        com.iaproject.agent.service.dto.RSVPListResponse dto = rsvpService.getAllRSVPsBySlug(eventSlug);
        return ResponseEntity.ok(mapper.toModel(dto));
    }
}
