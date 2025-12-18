package com.iaproject.agent.service;

import com.iaproject.agent.domain.Event;
import com.iaproject.agent.domain.Idea;
import com.iaproject.agent.repository.IdeaRepository;
import com.iaproject.agent.service.dto.CreateIdeaRequest;
import com.iaproject.agent.service.dto.IdeaResponse;
import com.iaproject.agent.service.mapper.EventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para gestionar ideas de apoyo propuestas por invitados.
 * Responsabilidades:
 * - Crear ideas
 * - Consultar ideas por evento
 * - Aprobar/moderar ideas (futuro)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IdeaService {

    private final IdeaRepository ideaRepository;
    private final EventService eventService;
    private final EventMapper eventMapper;

    /**
     * Crea una nueva idea de apoyo.
     */
    @Transactional
    public IdeaResponse createIdea(Long eventId, CreateIdeaRequest request) {
        log.info("Creando idea: eventId={}, userId={}", eventId, request.getUserId());
        
        Event event = eventService.getEventById(eventId);
        
        if (!event.getAllowIdeas()) {
            throw new IllegalStateException("Este evento no permite ideas de apoyo");
        }

        Idea idea = Idea.builder()
                .event(event)
                .userId(request.getUserId())
                .guestName(request.getGuestName())
                .description(request.getDescription())
                .isApproved(false)
                .build();

        Idea saved = ideaRepository.save(idea);
        log.info("✅ Idea creada: id={}", saved.getId());
        
        return eventMapper.toIdeaResponse(saved);
    }

    /**
     * Crea una nueva idea de apoyo usando slug.
     */
    @Transactional
    public IdeaResponse createIdeaBySlug(String eventSlug, CreateIdeaRequest request) {
        Event event = eventService.getEventEntityBySlug(eventSlug);
        return createIdea(event.getId(), request);
    }

    /**
     * Obtiene todas las ideas de un evento.
     */
    @Transactional(readOnly = true)
    public List<IdeaResponse> getIdeasByEvent(Long eventId) {
        Event event = eventService.getEventById(eventId);
        List<Idea> ideas = ideaRepository.findByEventOrderByCreatedAtDesc(event);
        
        return eventMapper.toIdeaResponseList(ideas);
    }

    /**
     * Obtiene todas las ideas de un evento usando slug.
     */
    @Transactional(readOnly = true)
    public List<IdeaResponse> getIdeasByEventSlug(String eventSlug) {
        Event event = eventService.getEventEntityBySlug(eventSlug);
        return getIdeasByEvent(event.getId());
    }
}
