package com.iaproject.agent.service;

import com.iaproject.agent.domain.Event;
import com.iaproject.agent.service.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para generar el dashboard completo del evento.
 * Consolida información de múltiples fuentes en un solo payload.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final EventService eventService;
    private final RSVPService rsvpService;
    private final GiftService giftService;
    private final IdeaService ideaService;
    private final BabyMessageService babyMessageService;

    /**
     * Genera el dashboard completo de un evento.
     */
    @Transactional(readOnly = true)
    public EventDashboardResponse getEventDashboard(Long eventId) {
        log.info("Generando dashboard para evento: id={}", eventId);
        
        Event event = eventService.getEventById(eventId);
        EventPublicResponse eventResponse = eventService.getEventBySlug(event.getSlug());
        
        RSVPListResponse rsvpList = rsvpService.getAllRSVPs(eventId);
        GiftSummaryResponse giftSummary = giftService.getGiftSummary(eventId);
        List<IdeaResponse> recentIdeas = ideaService.getIdeasByEvent(eventId);
        List<BabyMessageResponse> babyMessages = babyMessageService.getAllMessages(eventId);
        
        long totalAttendees = rsvpList.getSummary().getTotalYes();
        long pendingRSVPs = rsvpList.getSummary().getTotalPending();
        
        return EventDashboardResponse.builder()
                .event(eventResponse)
                .rsvpSummary(rsvpList.getSummary())
                .giftSummary(giftSummary)
                .recentIdeas(recentIdeas)
                .totalBabyMessages((long) babyMessages.size())
                .totalAttendees(totalAttendees)
                .pendingRSVPs(pendingRSVPs)
                .build();
    }

    /**
     * Genera el dashboard completo de un evento usando slug.
     */
    @Transactional(readOnly = true)
    public EventDashboardResponse getEventDashboard(String eventSlug) {
        Event event = eventService.getEventEntityBySlug(eventSlug);
        return getEventDashboard(event.getId());
    }
}
