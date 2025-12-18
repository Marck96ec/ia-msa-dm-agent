package com.iaproject.agent.service;

import com.iaproject.agent.domain.GiftCommitment;
import com.iaproject.agent.repository.GiftCommitmentRepository;
import com.iaproject.agent.service.dto.CommitmentResponse;
import com.iaproject.agent.service.mapper.EventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

/**
 * Servicio para gestionar compromisos de regalos (reservas y aportes).
 * Responsabilidades:
 * - Consultar commitments por token
 * - Cancelar commitments
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommitmentService {

    private final GiftCommitmentRepository commitmentRepository;
    private final EventMapper eventMapper;

    /**
     * Obtiene un commitment por su token.
     */
    @Transactional(readOnly = true)
    public CommitmentResponse getCommitmentByToken(String token) {
        GiftCommitment commitment = commitmentRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Compromiso no encontrado"));
        
        return eventMapper.toCommitmentResponse(commitment);
    }

    /**
     * Cancela un commitment.
     */
    @Transactional
    public void cancelCommitment(String token) {
        log.info("Cancelando commitment: token={}", token);
        
        GiftCommitment commitment = commitmentRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Compromiso no encontrado"));
        
        if (!commitment.getIsActive()) {
            throw new IllegalStateException("Este compromiso ya está cancelado");
        }

        commitment.setIsActive(false);
        commitment.setCancelledAt(OffsetDateTime.now());
        commitmentRepository.save(commitment);
        
        log.info("✅ Commitment cancelado: id={}", commitment.getId());
    }
}
