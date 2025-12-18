package com.iaproject.agent.service;

import com.iaproject.agent.domain.Event;
import com.iaproject.agent.domain.Gift;
import com.iaproject.agent.domain.GiftCommitment;
import com.iaproject.agent.domain.enums.CommitmentType;
import com.iaproject.agent.domain.enums.GiftStatus;
import com.iaproject.agent.repository.GiftCommitmentRepository;
import com.iaproject.agent.repository.GiftRepository;
import com.iaproject.agent.service.dto.*;
import com.iaproject.agent.service.mapper.EventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar regalos de eventos.
 * Responsabilidades:
 * - CRUD de regalos
 * - Reservas y aportes (commitments)
 * - Cálculo de estados y porcentajes de financiamiento
 * - Resúmenes y estadísticas
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GiftService {

    private final GiftRepository giftRepository;
    private final GiftCommitmentRepository commitmentRepository;
    private final EventService eventService;
    private final EventMapper eventMapper;

    /**
     * Obtiene todos los regalos activos de un evento.
     */
    @Transactional(readOnly = true)
    public List<GiftResponse> getGiftsByEvent(Long eventId) {
        Event event = eventService.getEventById(eventId);
        List<Gift> gifts = giftRepository.findByEventAndIsActiveTrueOrderByPriorityAsc(event);
        
        return gifts.stream()
                .map(this::toGiftResponseWithCalculations)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todos los regalos activos de un evento usando slug.
     */
    @Transactional(readOnly = true)
    public List<GiftResponse> getGiftsByEventSlug(String eventSlug) {
        Event event = eventService.getEventEntityBySlug(eventSlug);
        return getGiftsByEvent(event.getId());
    }

    /**
     * Obtiene un regalo por su ID con cálculos de financiamiento.
     */
    @Transactional(readOnly = true)
    public GiftResponse getGiftById(Long giftId) {
        Gift gift = giftRepository.findById(giftId)
                .orElseThrow(() -> new IllegalArgumentException("Regalo no encontrado: " + giftId));
        
        return toGiftResponseWithCalculations(gift);
    }

    /**
     * Crea un nuevo regalo para un evento.
     */
    @Transactional
    public GiftResponse createGift(Long eventId, CreateGiftRequest request) {
        log.info("Creando regalo: eventId={}, nombre={}", eventId, request.getName());
        
        Event event = eventService.getEventById(eventId);

        Gift gift = Gift.builder()
                .event(event)
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .imageUrl(request.getImageUrl())
                .allowSplit(request.getAllowSplit() != null ? request.getAllowSplit() : false)
                .priority(request.getPriority())
                .status(GiftStatus.AVAILABLE)
                .isActive(true)
                .quantity(request.getQuantity() != null ? request.getQuantity() : 1)
                .purchaseUrl(request.getPurchaseUrl())
                .build();

        Gift saved = giftRepository.save(gift);
        log.info("✅ Regalo creado: id={}, nombre={}", saved.getId(), saved.getName());
        
        return toGiftResponseWithCalculations(saved);
    }

    /**
     * Crea un nuevo regalo para un evento usando slug.
     */
    @Transactional
    public GiftResponse createGiftBySlug(String eventSlug, CreateGiftRequest request) {
        Event event = eventService.getEventEntityBySlug(eventSlug);
        return createGift(event.getId(), request);
    }

    /**
     * Actualiza un regalo existente.
     */
    @Transactional
    public GiftResponse updateGift(Long giftId, UpdateGiftRequest request) {
        log.info("Actualizando regalo: id={}", giftId);
        
        Gift gift = giftRepository.findById(giftId)
                .orElseThrow(() -> new IllegalArgumentException("Regalo no encontrado: " + giftId));

        if (request.getName() != null) gift.setName(request.getName());
        if (request.getDescription() != null) gift.setDescription(request.getDescription());
        if (request.getPrice() != null) gift.setPrice(request.getPrice());
        if (request.getImageUrl() != null) gift.setImageUrl(request.getImageUrl());
        if (request.getAllowSplit() != null) gift.setAllowSplit(request.getAllowSplit());
        if (request.getPriority() != null) gift.setPriority(request.getPriority());
        if (request.getIsActive() != null) gift.setIsActive(request.getIsActive());
        if (request.getQuantity() != null) gift.setQuantity(request.getQuantity());
        if (request.getPurchaseUrl() != null) gift.setPurchaseUrl(request.getPurchaseUrl());

        Gift saved = giftRepository.save(gift);
        updateGiftStatus(saved);
        
        log.info("✅ Regalo actualizado: id={}", saved.getId());
        return toGiftResponseWithCalculations(saved);
    }

    /**
     * Elimina (desactiva) un regalo.
     */
    @Transactional
    public void deleteGift(Long giftId) {
        Gift gift = giftRepository.findById(giftId)
                .orElseThrow(() -> new IllegalArgumentException("Regalo no encontrado: " + giftId));
        
        gift.setIsActive(false);
        gift.setStatus(GiftStatus.INACTIVE);
        giftRepository.save(gift);
        
        log.info("✅ Regalo desactivado: id={}", giftId);
    }

    /**
     * Reserva completa de un regalo.
     */
    @Transactional
    public CommitmentResponse reserveGift(Long giftId, ReserveGiftRequest request) {
        log.info("Reservando regalo: giftId={}, userId={}", giftId, request.getUserId());
        
        Gift gift = giftRepository.findById(giftId)
                .orElseThrow(() -> new IllegalArgumentException("Regalo no encontrado: " + giftId));
        
        if (gift.getStatus() == GiftStatus.RESERVED && !gift.getAllowSplit()) {
            throw new IllegalStateException("Este regalo ya está reservado");
        }
        
        if (commitmentRepository.existsByGiftAndUserIdAndIsActiveTrue(gift, request.getUserId())) {
            throw new IllegalStateException("Ya tienes un compromiso activo con este regalo");
        }

        GiftCommitment commitment = GiftCommitment.builder()
                .gift(gift)
                .userId(request.getUserId())
                .guestName(request.getGuestName())
                .guestEmail(request.getGuestEmail())
                .guestPhone(request.getGuestPhone())
                .commitmentType(CommitmentType.FULL_RESERVATION)
                .contributionAmount(gift.getPrice())
                .token(UUID.randomUUID().toString())
                .isActive(true)
                .notes(request.getNotes())
                .build();

        GiftCommitment saved = commitmentRepository.save(commitment);
        updateGiftStatus(gift);
        
        log.info("✅ Regalo reservado: commitmentId={}, token={}", saved.getId(), saved.getToken());
        return eventMapper.toCommitmentResponse(saved);
    }

    /**
     * Aporte parcial a un regalo compartido.
     */
    @Transactional
    public CommitmentResponse contributeToGift(Long giftId, ContributeGiftRequest request) {
        log.info("Aportando a regalo: giftId={}, userId={}, monto={}", giftId, request.getUserId(), request.getContributionAmount());
        
        Gift gift = giftRepository.findById(giftId)
                .orElseThrow(() -> new IllegalArgumentException("Regalo no encontrado: " + giftId));
        
        if (!gift.getAllowSplit()) {
            throw new IllegalStateException("Este regalo no permite aportes compartidos");
        }
        
        BigDecimal currentFunding = commitmentRepository.sumActiveContributions(giftId);
        BigDecimal newTotal = currentFunding.add(request.getContributionAmount());
        
        if (newTotal.compareTo(gift.getPrice()) > 0) {
            throw new IllegalStateException("El aporte excede el precio del regalo");
        }

        GiftCommitment commitment = GiftCommitment.builder()
                .gift(gift)
                .userId(request.getUserId())
                .guestName(request.getGuestName())
                .guestEmail(request.getGuestEmail())
                .guestPhone(request.getGuestPhone())
                .commitmentType(CommitmentType.PARTIAL_CONTRIBUTION)
                .contributionAmount(request.getContributionAmount())
                .token(UUID.randomUUID().toString())
                .isActive(true)
                .notes(request.getNotes())
                .build();

        GiftCommitment saved = commitmentRepository.save(commitment);
        updateGiftStatus(gift);
        
        log.info("✅ Aporte registrado: commitmentId={}, token={}", saved.getId(), saved.getToken());
        return eventMapper.toCommitmentResponse(saved);
    }

    /**
     * Obtiene resumen de regalos de un evento.
     */
    @Transactional(readOnly = true)
    public GiftSummaryResponse getGiftSummary(Long eventId) {
        Event event = eventService.getEventById(eventId);
        List<Gift> gifts = giftRepository.findByEventAndIsActiveTrue(event);
        
        long total = gifts.size();
        long available = gifts.stream().filter(g -> g.getStatus() == GiftStatus.AVAILABLE).count();
        long reserved = gifts.stream().filter(g -> g.getStatus() == GiftStatus.RESERVED).count();
        long partiallyFunded = gifts.stream().filter(g -> g.getStatus() == GiftStatus.PARTIALLY_FUNDED).count();
        long fullyFunded = gifts.stream().filter(g -> g.getStatus() == GiftStatus.FULLY_FUNDED).count();
        
        BigDecimal totalBudget = gifts.stream()
                .map(Gift::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal coveredBudget = gifts.stream()
                .filter(g -> g.getStatus() == GiftStatus.RESERVED || g.getStatus() == GiftStatus.FULLY_FUNDED)
                .map(Gift::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal remainingBudget = totalBudget.subtract(coveredBudget);
        BigDecimal coveragePercentage = totalBudget.compareTo(BigDecimal.ZERO) > 0
                ? coveredBudget.divide(totalBudget, 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100))
                : BigDecimal.ZERO;

        return GiftSummaryResponse.builder()
                .totalGifts(total)
                .availableGifts(available)
                .reservedGifts(reserved)
                .partiallyFundedGifts(partiallyFunded)
                .fullyFundedGifts(fullyFunded)
                .totalBudget(totalBudget)
                .coveredBudget(coveredBudget)
                .remainingBudget(remainingBudget)
                .coveragePercentage(coveragePercentage)
                .build();
    }

    /**
     * Obtiene el resumen de regalos de un evento usando slug.
     */
    @Transactional(readOnly = true)
    public GiftSummaryResponse getGiftSummaryBySlug(String eventSlug) {
        Event event = eventService.getEventEntityBySlug(eventSlug);
        return getGiftSummary(event.getId());
    }

    /**
     * Actualiza el estado de un regalo según sus commitments.
     */
    private void updateGiftStatus(Gift gift) {
        BigDecimal currentFunding = commitmentRepository.sumActiveContributions(gift.getId());
        long commitmentCount = commitmentRepository.countByGiftAndIsActiveTrue(gift);

        if (commitmentCount == 0) {
            gift.setStatus(GiftStatus.AVAILABLE);
        } else if (gift.getAllowSplit()) {
            if (currentFunding.compareTo(gift.getPrice()) >= 0) {
                gift.setStatus(GiftStatus.FULLY_FUNDED);
            } else {
                gift.setStatus(GiftStatus.PARTIALLY_FUNDED);
            }
        } else {
            gift.setStatus(GiftStatus.RESERVED);
        }
        
        giftRepository.save(gift);
    }

    /**
     * Convierte un Gift a GiftResponse con cálculos de financiamiento.
     */
    private GiftResponse toGiftResponseWithCalculations(Gift gift) {
        BigDecimal currentFunding = commitmentRepository.sumActiveContributions(gift.getId());
        Integer commitmentCount = (int) commitmentRepository.countByGiftAndIsActiveTrue(gift);
        
        return eventMapper.toGiftResponse(gift, currentFunding, commitmentCount);
    }
}
