package com.iaproject.agent.controller;

import com.iaproject.agent.api.CommitmentsApi;
import com.iaproject.agent.model.CommitmentResponse;
import com.iaproject.agent.service.CommitmentService;
import com.iaproject.agent.service.mapper.ModelToDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Implementación del controlador REST para gestionar compromisos.
 * Implementa la interfaz generada desde OpenAPI (API-First).
 * 
 * NO contiene lógica de negocio - delega a CommitmentService.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class CommitmentController implements CommitmentsApi {

    private final CommitmentService commitmentService;
    private final ModelToDtoMapper mapper;

    @Override
    public ResponseEntity<CommitmentResponse> getCommitmentByToken(String token) {
        log.info("GET /api/v1/commitments/{}", token);
        com.iaproject.agent.service.dto.CommitmentResponse dto = commitmentService.getCommitmentByToken(token);
        return ResponseEntity.ok(mapper.toModel(dto));
    }

    @Override
    public ResponseEntity<Void> cancelCommitment(String token) {
        log.info("DELETE /api/v1/commitments/{}", token);
        commitmentService.cancelCommitment(token);
        return ResponseEntity.noContent().build();
    }
}
