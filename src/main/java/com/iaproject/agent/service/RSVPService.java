package com.iaproject.agent.service;

import com.iaproject.agent.domain.Event;
import com.iaproject.agent.domain.RSVP;
import com.iaproject.agent.domain.enums.RSVPStatus;
import com.iaproject.agent.repository.RSVPRepository;
import com.iaproject.agent.service.dto.RSVPListResponse;
import com.iaproject.agent.service.dto.RSVPRequest;
import com.iaproject.agent.service.dto.RSVPResponse;
import com.iaproject.agent.service.mapper.EventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para gestionar confirmaciones de asistencia (RSVPs).
 * Responsabilidades:
 * - Registrar y actualizar RSVPs
 * - Consultar RSVPs por evento o usuario
 * - Generar resúmenes y estadísticas
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RSVPService {

    private final RSVPRepository rsvpRepository;
    private final EventService eventService;
    private final EventMapper eventMapper;

    /**
     * Registra o actualiza el RSVP de un usuario para un evento.
     */
    @Transactional
    public RSVPResponse createOrUpdateRSVP(Long eventId, RSVPRequest request) {
        log.info("Registrando RSVP: eventId={}, userId={}, status={}", eventId, request.getUserId(), request.getStatus());
        
        Event event = eventService.getEventById(eventId);
        
        RSVP rsvp = rsvpRepository.findByEventAndUserId(event, request.getUserId())
                .orElse(RSVP.builder()
                        .event(event)
                        .userId(request.getUserId())
                        .build());
        
        rsvp.setGuestName(request.getGuestName());
        rsvp.setGuestEmail(request.getGuestEmail());
        rsvp.setGuestPhone(request.getGuestPhone());
        rsvp.setStatus(request.getStatus());
        rsvp.setGuestsCount(request.getGuestsCount());
        rsvp.setNotes(request.getNotes());

        RSVP saved = rsvpRepository.save(rsvp);
        log.info("✅ RSVP guardado: id={}, status={}", saved.getId(), saved.getStatus());
        
        return eventMapper.toRSVPResponse(saved);
    }

    /**
     * Registra o actualiza el RSVP de un usuario para un evento usando slug.
     */
    @Transactional
    public RSVPResponse createOrUpdateRSVPBySlug(String eventSlug, RSVPRequest request) {
        log.info("Registrando RSVP: eventSlug={}, userId={}, status={}", eventSlug, request.getUserId(), request.getStatus());
        Event event = eventService.getEventEntityBySlug(eventSlug);
        return createOrUpdateRSVP(event.getId(), request);
    }

    /**
     * Obtiene el RSVP de un usuario para un evento.
     */
    @Transactional(readOnly = true)
    public RSVPResponse getRSVPByUser(Long eventId, String userId) {
        Event event = eventService.getEventById(eventId);
        
        RSVP rsvp = rsvpRepository.findByEventAndUserId(event, userId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró RSVP para este usuario"));
        
        return eventMapper.toRSVPResponse(rsvp);
    }

    /**
     * Obtiene el RSVP de un usuario para un evento usando slug.
     */
    @Transactional(readOnly = true)
    public RSVPResponse getRSVPByUserAndSlug(String eventSlug, String userId) {
        Event event = eventService.getEventEntityBySlug(eventSlug);
        return getRSVPByUser(event.getId(), userId);
    }

    /**
     * Obtiene todos los RSVPs de un evento.
     */
    @Transactional(readOnly = true)
    public RSVPListResponse getAllRSVPs(Long eventId) {
        Event event = eventService.getEventById(eventId);
        List<RSVP> rsvps = rsvpRepository.findByEvent(event);
        
        RSVPListResponse.RSVPSummary summary = buildSummary(event, rsvps);
        
        return RSVPListResponse.builder()
                .rsvps(eventMapper.toRSVPResponseList(rsvps))
                .summary(summary)
                .build();
    }

    /**
     * Obtiene todos los RSVPs de un evento usando slug.
     */
    @Transactional(readOnly = true)
    public RSVPListResponse getAllRSVPsBySlug(String eventSlug) {
        Event event = eventService.getEventEntityBySlug(eventSlug);
        return getAllRSVPs(event.getId());
    }

    /**
     * Obtiene los asistentes confirmados (status = CONFIRMED).
     */
    @Transactional(readOnly = true)
    public List<RSVPResponse> getAttendees(Long eventId) {
        Event event = eventService.getEventById(eventId);
        List<RSVP> attendees = rsvpRepository.findByEventAndStatus(event, RSVPStatus.CONFIRMED);
        
        return eventMapper.toRSVPResponseList(attendees);
    }

    /**
     * Obtiene los asistentes confirmados usando slug.
     */
    @Transactional(readOnly = true)
    public List<RSVPResponse> getAttendeesBySlug(String eventSlug) {
        Event event = eventService.getEventEntityBySlug(eventSlug);
        return getAttendees(event.getId());
    }

    /**
     * Construye el resumen de RSVPs.
     */
    private RSVPListResponse.RSVPSummary buildSummary(Event event, List<RSVP> rsvps) {
        long totalYes = rsvpRepository.countByEventAndStatus(event, RSVPStatus.CONFIRMED);
        long totalNo = rsvpRepository.countByEventAndStatus(event, RSVPStatus.DECLINED);
        long totalPending = rsvpRepository.countByEventAndStatus(event, RSVPStatus.PENDING);
        
        long totalGuests = rsvps.stream()
                .filter(r -> r.getStatus() == RSVPStatus.CONFIRMED)
                .mapToInt(r -> r.getGuestsCount() != null ? r.getGuestsCount() : 0)
                .sum();
        
        return RSVPListResponse.RSVPSummary.builder()
                .totalYes(totalYes)
                .totalNo(totalNo)
                .totalPending(totalPending)
                .totalGuests(totalGuests)
                .build();
    }
}
