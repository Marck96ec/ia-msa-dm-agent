package com.iaproject.agent.service;

import com.iaproject.agent.domain.Event;
import com.iaproject.agent.repository.EventRepository;
import com.iaproject.agent.service.dto.CreateEventRequest;
import com.iaproject.agent.service.dto.EventPublicResponse;
import com.iaproject.agent.service.dto.UpdateEventRequest;
import com.iaproject.agent.service.mapper.EventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para gestionar eventos.
 * Responsabilidades:
 * - Crear y actualizar eventos
 * - Consultar eventos por slug o ID
 * - Validar permisos de organizador
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    /**
     * Obtiene un evento por su slug (acceso público).
     */
    @Transactional(readOnly = true)
    public EventPublicResponse getEventBySlug(String slug) {
        log.info("Consultando evento por slug: {}", slug);
        Event event = getEventEntityBySlug(slug);
        return eventMapper.toPublicResponse(event);
    }

    /**
     * Obtiene la entidad Event por su slug (uso interno).
     */
    @Transactional(readOnly = true)
    public Event getEventEntityBySlug(String slug) {
        Event event = eventRepository.findBySlug(slug)
                .orElseThrow(() -> new IllegalArgumentException("Evento no encontrado: " + slug));
        
        if (!event.getIsActive()) {
            throw new IllegalStateException("El evento no está activo");
        }
        
        return event;
    }

    /**
     * Obtiene un evento por su ID (validando que exista).
     */
    @Transactional(readOnly = true)
    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Evento no encontrado: " + eventId));
    }

    /**
     * Crea un nuevo evento.
     */
    @Transactional
    public EventPublicResponse createEvent(CreateEventRequest request) {
        log.info("Creando nuevo evento: slug={}, organizador={}", request.getSlug(), request.getOrganizerUserId());
        
        if (eventRepository.existsBySlug(request.getSlug())) {
            throw new IllegalArgumentException("Ya existe un evento con el slug: " + request.getSlug());
        }

        Event event = Event.builder()
                .slug(request.getSlug())
                .name(request.getName())
                .description(request.getDescription())
                .eventDate(request.getEventDate())
                .location(request.getLocation())
                .locationUrl(request.getLocationUrl())
                .welcomeMessage(request.getWelcomeMessage())
                .closingMessage(request.getClosingMessage())
                .chatbotInstructions(request.getChatbotInstructions())
                .isActive(true)
                .maxAttendees(request.getMaxAttendees())
                .giftBudget(request.getGiftBudget())
                .organizerUserId(request.getOrganizerUserId())
                .organizerName(request.getOrganizerName())
                .organizerEmail(request.getOrganizerEmail())
                .organizerPhone(request.getOrganizerPhone())
                .allowSharedGifts(request.getAllowSharedGifts() != null ? request.getAllowSharedGifts() : true)
                .allowBabyMessages(request.getAllowBabyMessages() != null ? request.getAllowBabyMessages() : true)
                .allowIdeas(request.getAllowIdeas() != null ? request.getAllowIdeas() : true)
                .imageUrl(request.getImageUrl())
                .build();

        Event saved = eventRepository.save(event);
        log.info("✅ Evento creado: id={}, slug={}", saved.getId(), saved.getSlug());
        
        return eventMapper.toPublicResponse(saved);
    }

    /**
     * Actualiza un evento existente (solo organizador).
     */
    @Transactional
    public EventPublicResponse updateEvent(Long eventId, UpdateEventRequest request) {
        log.info("Actualizando evento: id={}", eventId);
        
        Event event = getEventById(eventId);
        
        if (request.getName() != null) event.setName(request.getName());
        if (request.getDescription() != null) event.setDescription(request.getDescription());
        if (request.getEventDate() != null) event.setEventDate(request.getEventDate());
        if (request.getLocation() != null) event.setLocation(request.getLocation());
        if (request.getLocationUrl() != null) event.setLocationUrl(request.getLocationUrl());
        if (request.getWelcomeMessage() != null) event.setWelcomeMessage(request.getWelcomeMessage());
        if (request.getClosingMessage() != null) event.setClosingMessage(request.getClosingMessage());
        if (request.getChatbotInstructions() != null) event.setChatbotInstructions(request.getChatbotInstructions());
        if (request.getIsActive() != null) event.setIsActive(request.getIsActive());
        if (request.getMaxAttendees() != null) event.setMaxAttendees(request.getMaxAttendees());
        if (request.getGiftBudget() != null) event.setGiftBudget(request.getGiftBudget());
        if (request.getOrganizerName() != null) event.setOrganizerName(request.getOrganizerName());
        if (request.getOrganizerEmail() != null) event.setOrganizerEmail(request.getOrganizerEmail());
        if (request.getOrganizerPhone() != null) event.setOrganizerPhone(request.getOrganizerPhone());
        if (request.getAllowSharedGifts() != null) event.setAllowSharedGifts(request.getAllowSharedGifts());
        if (request.getAllowBabyMessages() != null) event.setAllowBabyMessages(request.getAllowBabyMessages());
        if (request.getAllowIdeas() != null) event.setAllowIdeas(request.getAllowIdeas());
        if (request.getImageUrl() != null) event.setImageUrl(request.getImageUrl());

        Event saved = eventRepository.save(event);
        log.info("✅ Evento actualizado: id={}", saved.getId());
        
        return eventMapper.toPublicResponse(saved);
    }

    /**
     * Actualiza un evento existente usando slug.
     */
    @Transactional
    public EventPublicResponse updateEventBySlug(String slug, UpdateEventRequest request) {
        Event event = getEventEntityBySlug(slug);
        return updateEvent(event.getId(), request);
    }
}
