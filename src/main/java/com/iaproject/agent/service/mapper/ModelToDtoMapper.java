package com.iaproject.agent.service.mapper;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre modelos generados de OpenAPI (com.iaproject.agent.model)
 * y DTOs de servicio (com.iaproject.agent.service.dto).
 * 
 * Esto permite mantener compatibilidad entre el contrato OpenAPI (API-First)
 * y la implementación interna de servicios.
 */
@Component
public class ModelToDtoMapper {

    // ============================================================================
    // EVENTOS
    // ============================================================================

    public com.iaproject.agent.service.dto.CreateEventRequest toServiceDto(com.iaproject.agent.model.CreateEventRequest model) {
        if (model == null) return null;
        return com.iaproject.agent.service.dto.CreateEventRequest.builder()
                .slug(model.getSlug())
                .name(model.getName())
                .description(model.getDescription())
                .eventDate(model.getEventDate())
                .location(model.getLocation())
                .locationUrl(model.getLocationUrl())
                .welcomeMessage(model.getWelcomeMessage())
                .closingMessage(model.getClosingMessage())
                .chatbotInstructions(model.getChatbotInstructions())
                .maxAttendees(model.getMaxAttendees())
                .giftBudget(model.getGiftBudget() != null ? BigDecimal.valueOf(model.getGiftBudget()) : null)
                .organizerUserId(model.getOrganizerUserId())
                .organizerName(model.getOrganizerName())
                .organizerEmail(model.getOrganizerEmail())
                .organizerPhone(model.getOrganizerPhone())
                .allowSharedGifts(model.getAllowSharedGifts())
                .allowBabyMessages(model.getAllowBabyMessages())
                .allowIdeas(model.getAllowIdeas())
                .imageUrl(model.getImageUrl())
                .build();
    }

    public com.iaproject.agent.service.dto.UpdateEventRequest toServiceDto(com.iaproject.agent.model.UpdateEventRequest model) {
        if (model == null) return null;
        return com.iaproject.agent.service.dto.UpdateEventRequest.builder()
                .name(model.getName())
                .description(model.getDescription())
                .eventDate(model.getEventDate())
                .location(model.getLocation())
                .locationUrl(model.getLocationUrl())
                .welcomeMessage(model.getWelcomeMessage())
                .closingMessage(model.getClosingMessage())
                .chatbotInstructions(model.getChatbotInstructions())
                .isActive(model.getIsActive())
                .maxAttendees(model.getMaxAttendees())
                .giftBudget(model.getGiftBudget() != null ? BigDecimal.valueOf(model.getGiftBudget()) : null)
                .organizerName(model.getOrganizerName())
                .organizerEmail(model.getOrganizerEmail())
                .organizerPhone(model.getOrganizerPhone())
                .allowSharedGifts(model.getAllowSharedGifts())
                .allowBabyMessages(model.getAllowBabyMessages())
                .allowIdeas(model.getAllowIdeas())
                .imageUrl(model.getImageUrl())
                .build();
    }

    public com.iaproject.agent.model.EventPublicResponse toModel(com.iaproject.agent.service.dto.EventPublicResponse dto) {
        if (dto == null) return null;
        com.iaproject.agent.model.EventPublicResponse model = new com.iaproject.agent.model.EventPublicResponse();
        model.setId(dto.getId());
        model.setSlug(dto.getSlug());
        model.setName(dto.getName());
        model.setDescription(dto.getDescription());
        model.setEventDate(dto.getEventDate());
        model.setLocation(dto.getLocation());
        model.setLocationUrl(dto.getLocationUrl());
        model.setWelcomeMessage(dto.getWelcomeMessage());
        model.setImageUrl(dto.getImageUrl());
        model.setAllowSharedGifts(dto.getAllowSharedGifts());
        model.setAllowBabyMessages(dto.getAllowBabyMessages());
        model.setAllowIdeas(dto.getAllowIdeas());
        return model;
    }

    public com.iaproject.agent.model.EventDashboardResponse toModel(com.iaproject.agent.service.dto.EventDashboardResponse dto) {
        if (dto == null) return null;
        com.iaproject.agent.model.EventDashboardResponse model = new com.iaproject.agent.model.EventDashboardResponse();
        model.setEvent(toModel(dto.getEvent()));
        model.setRsvpSummary(toModel(dto.getRsvpSummary()));
        model.setGiftSummary(toModel(dto.getGiftSummary()));
        model.setRecentIdeas(toModelIdeaList(dto.getRecentIdeas()));
        model.setTotalBabyMessages(dto.getTotalBabyMessages());
        model.setTotalAttendees(dto.getTotalAttendees());
        model.setPendingRSVPs(dto.getPendingRSVPs());
        return model;
    }

    // ============================================================================
    // RSVPs
    // ============================================================================

    public com.iaproject.agent.service.dto.RSVPRequest toServiceDto(com.iaproject.agent.model.RSVPRequest model) {
        if (model == null) return null;
        return com.iaproject.agent.service.dto.RSVPRequest.builder()
                .userId(model.getUserId())
                .guestName(model.getGuestName())
                .guestEmail(model.getGuestEmail())
                .guestPhone(model.getGuestPhone())
                .status(model.getStatus() != null ? com.iaproject.agent.domain.enums.RSVPStatus.valueOf(model.getStatus().name()) : null)
                .guestsCount(model.getGuestsCount())
                .notes(model.getNotes())
                .build();
    }

    public com.iaproject.agent.model.RSVPResponse toModel(com.iaproject.agent.service.dto.RSVPResponse dto) {
        if (dto == null) return null;
        com.iaproject.agent.model.RSVPResponse model = new com.iaproject.agent.model.RSVPResponse();
        model.setId(dto.getId());
        model.setEventId(dto.getEventId());
        model.setUserId(dto.getUserId());
        model.setGuestName(dto.getGuestName());
        model.setGuestEmail(dto.getGuestEmail());
        model.setGuestPhone(dto.getGuestPhone());
        model.setStatus(dto.getStatus() != null ? com.iaproject.agent.model.RSVPResponse.StatusEnum.valueOf(dto.getStatus().name()) : null);
        model.setGuestsCount(dto.getGuestsCount());
        model.setNotes(dto.getNotes());
        model.setCreatedAt(dto.getCreatedAt());
        model.setUpdatedAt(dto.getUpdatedAt());
        return model;
    }

    public List<com.iaproject.agent.model.RSVPResponse> toModelRSVPList(List<com.iaproject.agent.service.dto.RSVPResponse> dtos) {
        if (dtos == null) return null;
        return dtos.stream().map(this::toModel).collect(Collectors.toList());
    }

    public com.iaproject.agent.model.RSVPListResponse toModel(com.iaproject.agent.service.dto.RSVPListResponse dto) {
        if (dto == null) return null;
        com.iaproject.agent.model.RSVPListResponse model = new com.iaproject.agent.model.RSVPListResponse();
        model.setRsvps(toModelRSVPList(dto.getRsvps()));
        model.setSummary(toModel(dto.getSummary()));
        return model;
    }

    public com.iaproject.agent.model.RSVPSummary toModel(com.iaproject.agent.service.dto.RSVPListResponse.RSVPSummary dto) {
        if (dto == null) return null;
        com.iaproject.agent.model.RSVPSummary model = new com.iaproject.agent.model.RSVPSummary();
        model.setTotalYes(dto.getTotalYes());
        model.setTotalNo(dto.getTotalNo());
        model.setTotalPending(dto.getTotalPending());
        model.setTotalGuests(dto.getTotalGuests());
        return model;
    }

    // ============================================================================
    // GIFTS
    // ============================================================================

    public com.iaproject.agent.service.dto.CreateGiftRequest toServiceDto(com.iaproject.agent.model.CreateGiftRequest model) {
        if (model == null) return null;
        return com.iaproject.agent.service.dto.CreateGiftRequest.builder()
                .name(model.getName())
                .description(model.getDescription())
                .price(model.getPrice() != null ? BigDecimal.valueOf(model.getPrice()) : null)
                .imageUrl(model.getImageUrl())
                .allowSplit(model.getAllowSplit())
                .priority(model.getPriority())
                .quantity(model.getQuantity())
                .purchaseUrl(model.getPurchaseUrl())
                .build();
    }

    public com.iaproject.agent.service.dto.UpdateGiftRequest toServiceDto(com.iaproject.agent.model.UpdateGiftRequest model) {
        if (model == null) return null;
        return com.iaproject.agent.service.dto.UpdateGiftRequest.builder()
                .name(model.getName())
                .description(model.getDescription())
                .price(model.getPrice() != null ? BigDecimal.valueOf(model.getPrice()) : null)
                .imageUrl(model.getImageUrl())
                .allowSplit(model.getAllowSplit())
                .priority(model.getPriority())
                .isActive(model.getIsActive())
                .quantity(model.getQuantity())
                .purchaseUrl(model.getPurchaseUrl())
                .build();
    }

    public com.iaproject.agent.model.GiftResponse toModel(com.iaproject.agent.service.dto.GiftResponse dto) {
        if (dto == null) return null;
        com.iaproject.agent.model.GiftResponse model = new com.iaproject.agent.model.GiftResponse();
        model.setId(dto.getId());
        model.setEventId(dto.getEventId());
        model.setName(dto.getName());
        model.setDescription(dto.getDescription());
        model.setPrice(dto.getPrice() != null ? dto.getPrice().doubleValue() : null);
        model.setImageUrl(dto.getImageUrl());
        model.setAllowSplit(dto.getAllowSplit());
        model.setPriority(dto.getPriority());
        model.setStatus(dto.getStatus() != null ? com.iaproject.agent.model.GiftResponse.StatusEnum.valueOf(dto.getStatus().name()) : null);
        model.setIsActive(dto.getIsActive());
        model.setQuantity(dto.getQuantity());
        model.setPurchaseUrl(dto.getPurchaseUrl());
        model.setCurrentFunding(dto.getCurrentFunding() != null ? dto.getCurrentFunding().doubleValue() : null);
        model.setFundingPercentage(dto.getFundingPercentage() != null ? dto.getFundingPercentage().doubleValue() : null);
        model.setCommitmentCount(dto.getCommitmentCount());
        model.setCreatedAt(dto.getCreatedAt());
        model.setUpdatedAt(dto.getUpdatedAt());
        return model;
    }

    public List<com.iaproject.agent.model.GiftResponse> toModelGiftList(List<com.iaproject.agent.service.dto.GiftResponse> dtos) {
        if (dtos == null) return null;
        return dtos.stream().map(this::toModel).collect(Collectors.toList());
    }

    public com.iaproject.agent.model.GiftSummaryResponse toModel(com.iaproject.agent.service.dto.GiftSummaryResponse dto) {
        if (dto == null) return null;
        com.iaproject.agent.model.GiftSummaryResponse model = new com.iaproject.agent.model.GiftSummaryResponse();
        model.setTotalGifts(dto.getTotalGifts());
        model.setAvailableGifts(dto.getAvailableGifts());
        model.setReservedGifts(dto.getReservedGifts());
        model.setPartiallyFundedGifts(dto.getPartiallyFundedGifts());
        model.setFullyFundedGifts(dto.getFullyFundedGifts());
        model.setTotalBudget(dto.getTotalBudget() != null ? dto.getTotalBudget().doubleValue() : null);
        model.setCoveredBudget(dto.getCoveredBudget() != null ? dto.getCoveredBudget().doubleValue() : null);
        model.setRemainingBudget(dto.getRemainingBudget() != null ? dto.getRemainingBudget().doubleValue() : null);
        model.setCoveragePercentage(dto.getCoveragePercentage() != null ? dto.getCoveragePercentage().doubleValue() : null);
        return model;
    }

    public com.iaproject.agent.service.dto.ReserveGiftRequest toServiceDto(com.iaproject.agent.model.ReserveGiftRequest model) {
        if (model == null) return null;
        return com.iaproject.agent.service.dto.ReserveGiftRequest.builder()
                .userId(model.getUserId())
                .guestName(model.getGuestName())
                .guestEmail(model.getGuestEmail())
                .guestPhone(model.getGuestPhone())
                .notes(model.getNotes())
                .build();
    }

    public com.iaproject.agent.service.dto.ContributeGiftRequest toServiceDto(com.iaproject.agent.model.ContributeGiftRequest model) {
        if (model == null) return null;
        return com.iaproject.agent.service.dto.ContributeGiftRequest.builder()
                .userId(model.getUserId())
                .guestName(model.getGuestName())
                .guestEmail(model.getGuestEmail())
                .guestPhone(model.getGuestPhone())
                .contributionAmount(model.getContributionAmount() != null ? BigDecimal.valueOf(model.getContributionAmount()) : null)
                .notes(model.getNotes())
                .build();
    }

    // ============================================================================
    // COMMITMENTS
    // ============================================================================

    public com.iaproject.agent.model.CommitmentResponse toModel(com.iaproject.agent.service.dto.CommitmentResponse dto) {
        if (dto == null) return null;
        com.iaproject.agent.model.CommitmentResponse model = new com.iaproject.agent.model.CommitmentResponse();
        model.setId(dto.getId());
        model.setGiftId(dto.getGiftId());
        model.setGiftName(dto.getGiftName());
        model.setUserId(dto.getUserId());
        model.setGuestName(dto.getGuestName());
        model.setGuestEmail(dto.getGuestEmail());
        model.setGuestPhone(dto.getGuestPhone());
        model.setCommitmentType(dto.getCommitmentType() != null ? com.iaproject.agent.model.CommitmentResponse.CommitmentTypeEnum.valueOf(dto.getCommitmentType().name()) : null);
        model.setContributionAmount(dto.getContributionAmount() != null ? dto.getContributionAmount().doubleValue() : null);
        model.setToken(dto.getToken());
        model.setIsActive(dto.getIsActive());
        model.setNotes(dto.getNotes());
        model.setCreatedAt(dto.getCreatedAt());
        model.setCancelledAt(dto.getCancelledAt());
        return model;
    }

    // ============================================================================
    // IDEAS
    // ============================================================================

    public com.iaproject.agent.service.dto.CreateIdeaRequest toServiceDto(com.iaproject.agent.model.CreateIdeaRequest model) {
        if (model == null) return null;
        return com.iaproject.agent.service.dto.CreateIdeaRequest.builder()
                .userId(model.getUserId())
                .guestName(model.getGuestName())
                .description(model.getDescription())
                .build();
    }

    public com.iaproject.agent.model.IdeaResponse toModel(com.iaproject.agent.service.dto.IdeaResponse dto) {
        if (dto == null) return null;
        com.iaproject.agent.model.IdeaResponse model = new com.iaproject.agent.model.IdeaResponse();
        model.setId(dto.getId());
        model.setEventId(dto.getEventId());
        model.setUserId(dto.getUserId());
        model.setGuestName(dto.getGuestName());
        model.setDescription(dto.getDescription());
        model.setIsApproved(dto.getIsApproved());
        model.setOrganizerComment(dto.getOrganizerComment());
        model.setCreatedAt(dto.getCreatedAt());
        return model;
    }

    public List<com.iaproject.agent.model.IdeaResponse> toModelIdeaList(List<com.iaproject.agent.service.dto.IdeaResponse> dtos) {
        if (dtos == null) return null;
        return dtos.stream().map(this::toModel).collect(Collectors.toList());
    }

    // ============================================================================
    // BABY MESSAGES
    // ============================================================================

    public com.iaproject.agent.service.dto.CreateBabyMessageRequest toServiceDto(com.iaproject.agent.model.CreateBabyMessageRequest model) {
        if (model == null) return null;
        return com.iaproject.agent.service.dto.CreateBabyMessageRequest.builder()
                .userId(model.getUserId())
                .guestName(model.getGuestName())
                .messageText(model.getMessageText())
                .audioUrl(model.getAudioUrl())
                .build();
    }

    public com.iaproject.agent.service.dto.UpdateBabyMessageRequest toServiceDto(com.iaproject.agent.model.UpdateBabyMessageRequest model) {
        if (model == null) return null;
        return com.iaproject.agent.service.dto.UpdateBabyMessageRequest.builder()
                .isPublished(model.getIsPublished())
                .build();
    }

    public com.iaproject.agent.model.BabyMessageResponse toModel(com.iaproject.agent.service.dto.BabyMessageResponse dto) {
        if (dto == null) return null;
        com.iaproject.agent.model.BabyMessageResponse model = new com.iaproject.agent.model.BabyMessageResponse();
        model.setId(dto.getId());
        model.setEventId(dto.getEventId());
        model.setUserId(dto.getUserId());
        model.setGuestName(dto.getGuestName());
        model.setMessageText(dto.getMessageText());
        model.setAudioUrl(dto.getAudioUrl());
        model.setIsPublished(dto.getIsPublished());
        model.setCreatedAt(dto.getCreatedAt());
        return model;
    }

    public List<com.iaproject.agent.model.BabyMessageResponse> toModelBabyMessageList(List<com.iaproject.agent.service.dto.BabyMessageResponse> dtos) {
        if (dtos == null) return null;
        return dtos.stream().map(this::toModel).collect(Collectors.toList());
    }
}
