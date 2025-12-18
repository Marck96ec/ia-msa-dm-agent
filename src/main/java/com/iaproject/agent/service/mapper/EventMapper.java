package com.iaproject.agent.service.mapper;

import com.iaproject.agent.domain.*;
import com.iaproject.agent.service.dto.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entidades de dominio a DTOs y viceversa.
 * Centraliza la lógica de transformación entre capas.
 */
@Component
public class EventMapper {

    /**
     * Convierte OffsetDateTime (del dominio) a LocalDateTime (de los DTOs).
     */
    private LocalDateTime toLocalDateTime(OffsetDateTime offsetDateTime) {
        return offsetDateTime != null ? offsetDateTime.toLocalDateTime() : null;
    }

    public EventPublicResponse toPublicResponse(Event event) {
        return EventPublicResponse.builder()
                .id(event.getId())
                .slug(event.getSlug())
                .name(event.getName())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .location(event.getLocation())
                .locationUrl(event.getLocationUrl())
                .welcomeMessage(event.getWelcomeMessage())
                .imageUrl(event.getImageUrl())
                .allowSharedGifts(event.getAllowSharedGifts())
                .allowBabyMessages(event.getAllowBabyMessages())
                .allowIdeas(event.getAllowIdeas())
                .build();
    }

    public RSVPResponse toRSVPResponse(RSVP rsvp) {
        return RSVPResponse.builder()
                .id(rsvp.getId())
                .eventId(rsvp.getEvent().getId())
                .userId(rsvp.getUserId())
                .guestName(rsvp.getGuestName())
                .guestEmail(rsvp.getGuestEmail())
                .guestPhone(rsvp.getGuestPhone())
                .status(rsvp.getStatus())
                .guestsCount(rsvp.getGuestsCount())
                .notes(rsvp.getNotes())
                .createdAt(toLocalDateTime(rsvp.getCreatedAt()))
                .updatedAt(toLocalDateTime(rsvp.getUpdatedAt()))
                .build();
    }

    public GiftResponse toGiftResponse(Gift gift, java.math.BigDecimal currentFunding, Integer commitmentCount) {
        java.math.BigDecimal fundingPercentage = null;
        if (gift.getPrice() != null && gift.getPrice().compareTo(java.math.BigDecimal.ZERO) > 0) {
            fundingPercentage = currentFunding
                    .divide(gift.getPrice(), 2, java.math.RoundingMode.HALF_UP)
                    .multiply(java.math.BigDecimal.valueOf(100));
        }

        return GiftResponse.builder()
                .id(gift.getId())
                .eventId(gift.getEvent().getId())
                .name(gift.getName())
                .description(gift.getDescription())
                .price(gift.getPrice())
                .imageUrl(gift.getImageUrl())
                .allowSplit(gift.getAllowSplit())
                .priority(gift.getPriority())
                .status(gift.getStatus())
                .isActive(gift.getIsActive())
                .quantity(gift.getQuantity())
                .purchaseUrl(gift.getPurchaseUrl())
                .currentFunding(currentFunding)
                .fundingPercentage(fundingPercentage)
                .commitmentCount(commitmentCount)
                .createdAt(toLocalDateTime(gift.getCreatedAt()))
                .updatedAt(toLocalDateTime(gift.getUpdatedAt()))
                .build();
    }

    public CommitmentResponse toCommitmentResponse(GiftCommitment commitment) {
        return CommitmentResponse.builder()
                .id(commitment.getId())
                .giftId(commitment.getGift().getId())
                .giftName(commitment.getGift().getName())
                .userId(commitment.getUserId())
                .guestName(commitment.getGuestName())
                .guestEmail(commitment.getGuestEmail())
                .guestPhone(commitment.getGuestPhone())
                .commitmentType(commitment.getCommitmentType())
                .contributionAmount(commitment.getContributionAmount())
                .token(commitment.getToken())
                .isActive(commitment.getIsActive())
                .notes(commitment.getNotes())
                .createdAt(toLocalDateTime(commitment.getCreatedAt()))
                .cancelledAt(toLocalDateTime(commitment.getCancelledAt()))
                .build();
    }

    public IdeaResponse toIdeaResponse(Idea idea) {
        return IdeaResponse.builder()
                .id(idea.getId())
                .eventId(idea.getEvent().getId())
                .userId(idea.getUserId())
                .guestName(idea.getGuestName())
                .description(idea.getDescription())
                .isApproved(idea.getIsApproved())
                .organizerComment(idea.getOrganizerComment())
                .createdAt(toLocalDateTime(idea.getCreatedAt()))
                .build();
    }

    public BabyMessageResponse toBabyMessageResponse(BabyMessage message) {
        return BabyMessageResponse.builder()
                .id(message.getId())
                .eventId(message.getEvent().getId())
                .userId(message.getUserId())
                .guestName(message.getGuestName())
                .messageText(message.getMessageText())
                .audioUrl(message.getAudioUrl())
                .isPublished(message.getIsPublished())
                .createdAt(toLocalDateTime(message.getCreatedAt()))
                .build();
    }

    public List<RSVPResponse> toRSVPResponseList(List<RSVP> rsvps) {
        return rsvps.stream()
                .map(this::toRSVPResponse)
                .collect(Collectors.toList());
    }

    public List<IdeaResponse> toIdeaResponseList(List<Idea> ideas) {
        return ideas.stream()
                .map(this::toIdeaResponse)
                .collect(Collectors.toList());
    }

    public List<BabyMessageResponse> toBabyMessageResponseList(List<BabyMessage> messages) {
        return messages.stream()
                .map(this::toBabyMessageResponse)
                .collect(Collectors.toList());
    }
}
