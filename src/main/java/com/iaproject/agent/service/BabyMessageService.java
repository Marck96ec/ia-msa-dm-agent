package com.iaproject.agent.service;

import com.iaproject.agent.domain.BabyMessage;
import com.iaproject.agent.domain.Event;
import com.iaproject.agent.repository.BabyMessageRepository;
import com.iaproject.agent.service.dto.BabyMessageResponse;
import com.iaproject.agent.service.dto.CreateBabyMessageRequest;
import com.iaproject.agent.service.dto.UpdateBabyMessageRequest;
import com.iaproject.agent.service.mapper.EventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para gestionar mensajes para el bebé.
 * Responsabilidades:
 * - Crear mensajes
 * - Consultar mensajes por evento
 * - Moderar mensajes (publicar/ocultar)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BabyMessageService {

    private final BabyMessageRepository babyMessageRepository;
    private final EventService eventService;
    private final EventMapper eventMapper;

    /**
     * Crea un nuevo mensaje para el bebé.
     */
    @Transactional
    public BabyMessageResponse createMessage(Long eventId, CreateBabyMessageRequest request) {
        log.info("Creando mensaje para bebé: eventId={}, userId={}", eventId, request.getUserId());
        
        Event event = eventService.getEventById(eventId);
        
        if (!event.getAllowBabyMessages()) {
            throw new IllegalStateException("Este evento no permite mensajes para el bebé");
        }

        BabyMessage message = BabyMessage.builder()
                .event(event)
                .userId(request.getUserId())
                .guestName(request.getGuestName())
                .messageText(request.getMessageText())
                .audioUrl(request.getAudioUrl())
                .isPublished(true) // Por defecto publicado
                .build();

        BabyMessage saved = babyMessageRepository.save(message);
        log.info("✅ Mensaje creado: id={}", saved.getId());
        
        return eventMapper.toBabyMessageResponse(saved);
    }

    /**
     * Crea un nuevo mensaje para el bebé usando slug.
     */
    @Transactional
    public BabyMessageResponse createMessageBySlug(String eventSlug, CreateBabyMessageRequest request) {
        Event event = eventService.getEventEntityBySlug(eventSlug);
        return createMessage(event.getId(), request);
    }

    /**
     * Obtiene todos los mensajes publicados de un evento.
     */
    @Transactional(readOnly = true)
    public List<BabyMessageResponse> getPublishedMessages(Long eventId) {
        Event event = eventService.getEventById(eventId);
        List<BabyMessage> messages = babyMessageRepository.findByEventAndIsPublishedTrueOrderByCreatedAtDesc(event);
        
        return eventMapper.toBabyMessageResponseList(messages);
    }

    /**
     * Obtiene todos los mensajes publicados de un evento usando slug.
     */
    @Transactional(readOnly = true)
    public List<BabyMessageResponse> getPublishedMessagesBySlug(String eventSlug) {
        Event event = eventService.getEventEntityBySlug(eventSlug);
        return getPublishedMessages(event.getId());
    }

    /**
     * Obtiene todos los mensajes de un evento (incluidos no publicados) - solo organizadores.
     */
    @Transactional(readOnly = true)
    public List<BabyMessageResponse> getAllMessages(Long eventId) {
        Event event = eventService.getEventById(eventId);
        List<BabyMessage> messages = babyMessageRepository.findByEvent(event);
        
        return eventMapper.toBabyMessageResponseList(messages);
    }

    /**
     * Obtiene todos los mensajes de un evento usando slug (incluidos no publicados).
     */
    @Transactional(readOnly = true)
    public List<BabyMessageResponse> getAllMessagesBySlug(String eventSlug) {
        Event event = eventService.getEventEntityBySlug(eventSlug);
        return getAllMessages(event.getId());
    }

    /**
     * Actualiza el estado de publicación de un mensaje.
     */
    @Transactional
    public BabyMessageResponse updateMessageStatus(Long messageId, UpdateBabyMessageRequest request) {
        log.info("Actualizando estado de mensaje: id={}, isPublished={}", messageId, request.getIsPublished());
        
        BabyMessage message = babyMessageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Mensaje no encontrado: " + messageId));
        
        if (request.getIsPublished() != null) {
            message.setIsPublished(request.getIsPublished());
        }

        BabyMessage saved = babyMessageRepository.save(message);
        log.info("✅ Mensaje actualizado: id={}", saved.getId());
        
        return eventMapper.toBabyMessageResponse(saved);
    }
}
