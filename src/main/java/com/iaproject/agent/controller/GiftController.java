package com.iaproject.agent.controller;

import com.iaproject.agent.api.GiftsApi;
import com.iaproject.agent.model.*;
import com.iaproject.agent.service.GiftService;
import com.iaproject.agent.service.mapper.ModelToDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Implementación del controlador REST para gestionar regalos.
 * Implementa la interfaz generada desde OpenAPI (API-First).
 * 
 * NO contiene lógica de negocio - delega a GiftService.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class GiftController implements GiftsApi {

    private final GiftService giftService;
    private final ModelToDtoMapper mapper;

    @Override
    public ResponseEntity<List<GiftResponse>> getGiftsByEvent(String eventSlug) {
        log.info("GET /api/v1/events/{}/gifts", eventSlug);
        List<com.iaproject.agent.service.dto.GiftResponse> dtos = giftService.getGiftsByEventSlug(eventSlug);
        return ResponseEntity.ok(mapper.toModelGiftList(dtos));
    }

    @Override
    public ResponseEntity<GiftResponse> createGift(String eventSlug, CreateGiftRequest request) {
        log.info("POST /api/v1/events/{}/gifts - name={}", eventSlug, request.getName());
        com.iaproject.agent.service.dto.GiftResponse dto = giftService.createGiftBySlug(eventSlug, mapper.toServiceDto(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toModel(dto));
    }

    @Override
    public ResponseEntity<GiftSummaryResponse> getGiftSummary(String eventSlug) {
        log.info("GET /api/v1/events/{}/gifts/summary", eventSlug);
        com.iaproject.agent.service.dto.GiftSummaryResponse dto = giftService.getGiftSummaryBySlug(eventSlug);
        return ResponseEntity.ok(mapper.toModel(dto));
    }

    @Override
    public ResponseEntity<GiftResponse> getGiftById(Long giftId) {
        log.info("GET /api/v1/gifts/{}", giftId);
        com.iaproject.agent.service.dto.GiftResponse dto = giftService.getGiftById(giftId);
        return ResponseEntity.ok(mapper.toModel(dto));
    }

    @Override
    public ResponseEntity<GiftResponse> updateGift(Long giftId, UpdateGiftRequest request) {
        log.info("PUT /api/v1/gifts/{}", giftId);
        com.iaproject.agent.service.dto.GiftResponse dto = giftService.updateGift(giftId, mapper.toServiceDto(request));
        return ResponseEntity.ok(mapper.toModel(dto));
    }

    @Override
    public ResponseEntity<Void> deleteGift(Long giftId) {
        log.info("DELETE /api/v1/gifts/{}", giftId);
        giftService.deleteGift(giftId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<CommitmentResponse> reserveGift(Long giftId, ReserveGiftRequest request) {
        log.info("POST /api/v1/gifts/{}/reserve - userId={}", giftId, request.getUserId());
        com.iaproject.agent.service.dto.CommitmentResponse dto = giftService.reserveGift(giftId, mapper.toServiceDto(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toModel(dto));
    }

    @Override
    public ResponseEntity<CommitmentResponse> contributeToGift(Long giftId, ContributeGiftRequest request) {
        log.info("POST /api/v1/gifts/{}/contribute - userId={}, amount={}", giftId, request.getUserId(), request.getContributionAmount());
        com.iaproject.agent.service.dto.CommitmentResponse dto = giftService.contributeToGift(giftId, mapper.toServiceDto(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toModel(dto));
    }
}
