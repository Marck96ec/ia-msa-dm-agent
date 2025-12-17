package com.iaproject.agent.service;

import com.iaproject.agent.model.ModelInfo;
import com.iaproject.agent.model.ModelsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Servicio para interactuar con la API de OpenAI y obtener información de modelos disponibles.
 * Utiliza las credenciales configuradas (API Key, Organization ID, Project ID) para autenticarse.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiModelService {

    private static final String OPENAI_MODELS_URL = "https://api.openai.com/v1/models";

    private final RestTemplate restTemplate;

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Value("${spring.ai.openai.organization-id:#{null}}")
    private String organizationId;

    @Value("${spring.ai.openai.project-id:#{null}}")
    private String projectId;

    /**
     * Obtiene la lista de modelos disponibles desde la API de OpenAI.
     *
     * @return ModelsResponse con la lista de modelos y el total
     * @throws RuntimeException si ocurre un error al obtener los modelos
     */
    public ModelsResponse getAvailableModels() {
        log.debug("Obteniendo modelos disponibles de OpenAI");

        try {
            HttpHeaders headers = createAuthHeaders();
            HttpEntity<Void> request = new HttpEntity<>(headers);

            log.debug("Realizando petición a: {}", OPENAI_MODELS_URL);
            ResponseEntity<OpenAiModelsApiResponse> response = restTemplate.exchange(
                    OPENAI_MODELS_URL,
                    HttpMethod.GET,
                    request,
                    OpenAiModelsApiResponse.class
            );

            if (response.getBody() == null || response.getBody().data() == null) {
                log.warn("Respuesta vacía de la API de OpenAI");
                return new ModelsResponse(List.of(), 0);
            }

            List<ModelInfo> models = response.getBody().data().stream()
                    .map(this::mapToModelInfo)
                    .toList();

            log.info("Se obtuvieron {} modelos disponibles", models.size());
            return new ModelsResponse(models, models.size());

        } catch (HttpClientErrorException.Unauthorized e) {
            log.error("Error de autenticación con OpenAI: credenciales inválidas", e);
            throw new RuntimeException("API Key, Organization ID o Project ID inválidos", e);
        } catch (HttpClientErrorException e) {
            log.error("Error HTTP al obtener modelos: {} - {}", e.getStatusCode(), e.getMessage());
            throw new RuntimeException("Error al obtener modelos de OpenAI: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error inesperado al obtener modelos de OpenAI", e);
            throw new RuntimeException("Error inesperado al obtener modelos", e);
        }
    }

    /**
     * Crea los headers de autenticación necesarios para la API de OpenAI.
     *
     * @return HttpHeaders configurados con Authorization, Organization y Project
     */
    private HttpHeaders createAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        if (organizationId != null && !organizationId.isBlank()) {
            headers.set("OpenAI-Organization", organizationId);
            log.debug("Header OpenAI-Organization agregado");
        }

        if (projectId != null && !projectId.isBlank()) {
            headers.set("OpenAI-Project", projectId);
            log.debug("Header OpenAI-Project agregado");
        }

        return headers;
    }

    /**
     * Mapea el modelo de respuesta de la API de OpenAI a nuestro modelo de dominio.
     *
     * @param apiModel Modelo de la API de OpenAI
     * @return ModelInfo con la información mapeada
     */
    private ModelInfo mapToModelInfo(OpenAiModelData apiModel) {
        ModelInfo model = new ModelInfo();
        model.setId(apiModel.id());
        model.setObject(apiModel.object());
        model.setCreated(apiModel.created());
        model.setOwnedBy(apiModel.ownedBy());
        return model;
    }

    /**
     * Record para mapear la respuesta completa de la API de OpenAI.
     *
     * @param object Tipo de objeto (siempre "list")
     * @param data   Lista de modelos disponibles
     */
    private record OpenAiModelsApiResponse(
            String object,
            List<OpenAiModelData> data
    ) {
    }

    /**
     * Record para mapear cada modelo individual de la respuesta de OpenAI.
     *
     * @param id      ID del modelo
     * @param object  Tipo de objeto (siempre "model")
     * @param created Timestamp de creación (Unix epoch)
     * @param ownedBy Propietario del modelo
     */
    private record OpenAiModelData(
            String id,
            String object,
            Long created,
            String ownedBy
    ) {
    }
}
