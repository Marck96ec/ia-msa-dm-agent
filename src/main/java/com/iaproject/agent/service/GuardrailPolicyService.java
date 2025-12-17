package com.iaproject.agent.service;

import com.iaproject.agent.domain.ConversationHistory;
import com.iaproject.agent.domain.UserProfile;
import com.iaproject.agent.domain.enums.GuardrailAction;
import com.iaproject.agent.domain.enums.GuardrailReason;
import com.iaproject.agent.model.ChatRequest;
import com.iaproject.agent.service.dto.GuardrailEvaluationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Servicio de evaluación de guardrails (reglas de seguridad y límites).
 * Implementa validaciones pre-IA para proteger el sistema de:
 * - Mensajes excesivamente largos
 * - Intentos de prompt injection
 * - Solicitudes fuera de alcance (validación contra dominios en BD)
 * - Contenido inseguro
 * 
 * Principio: Las validaciones son reglas duras que se ejecutan ANTES de llamar a la IA.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GuardrailPolicyService {

    private final AllowedDomainService allowedDomainService;

    // Límites configurables
    private static final int MAX_MESSAGE_LENGTH = 800;

    // Patrones para detección de prompt injection
    private static final List<Pattern> INJECTION_PATTERNS = List.of(
            Pattern.compile("ignore.*instruction", Pattern.CASE_INSENSITIVE),
            Pattern.compile("system\\s+prompt", Pattern.CASE_INSENSITIVE),
            Pattern.compile("act\\s+as", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE),
            Pattern.compile("actúa\\s+como", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE),
            Pattern.compile("revela.*prompt", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE),
            Pattern.compile("developer\\s+message", Pattern.CASE_INSENSITIVE),
            Pattern.compile("mensaje\\s+del\\s+desarrollador", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE),
            Pattern.compile("api\\s+key", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\btoken\\b", Pattern.CASE_INSENSITIVE),
            Pattern.compile("credenciales", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE),
            Pattern.compile("forget.*previous", Pattern.CASE_INSENSITIVE),
            Pattern.compile("olvida.*anterior", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE),
            Pattern.compile("override\\s+instructions", Pattern.CASE_INSENSITIVE),
            Pattern.compile("bypass\\s+rules", Pattern.CASE_INSENSITIVE)
    );

    // Palabras clave de contenido prohibido (unsafe)
    private static final List<Pattern> UNSAFE_PATTERNS = List.of(
            Pattern.compile("\\b(hack|exploit|vulnerability)\\b", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\b(spam|phishing|scam)\\b", Pattern.CASE_INSENSITIVE)
            // Agregar más según políticas específicas
    );

    /**
     * Evalúa una solicitud contra todas las políticas de guardrails.
     *
     * @param request solicitud del usuario
     * @param profile perfil del usuario (puede ser null)
     * @param history historial de conversación (puede ser vacío)
     * @return resultado de la evaluación con acción y razón
     */
    public GuardrailEvaluationResult evaluate(
            ChatRequest request,
            UserProfile profile,
            List<ConversationHistory> history) {

        String message = request.getMessage();
        log.debug("Evaluando guardrails para mensaje de longitud: {}", message.length());

        // 1. Validar longitud del mensaje
        GuardrailEvaluationResult lengthCheck = checkMessageLength(message);
        if (!lengthCheck.isAllowed()) {
            log.warn("Mensaje bloqueado: TOO_LONG ({} caracteres)", message.length());
            return lengthCheck;
        }

        // 2. Detectar prompt injection
        GuardrailEvaluationResult injectionCheck = checkInjectionAttempts(message);
        if (!injectionCheck.isAllowed()) {
            log.warn("Mensaje bloqueado: INJECTION detectado");
            return injectionCheck;
        }

        // 3. Detectar contenido inseguro
        GuardrailEvaluationResult unsafeCheck = checkUnsafeContent(message);
        if (!unsafeCheck.isAllowed()) {
            log.warn("Mensaje bloqueado: UNSAFE contenido detectado");
            return unsafeCheck;
        }

        // 4. Validar alcance (solo si mode=EVENT o domainId/eventId presente)
        GuardrailEvaluationResult scopeCheck = checkScope(request, message);
        if (!scopeCheck.isAllowed()) {
            log.warn("Mensaje redirigido: OUT_OF_SCOPE");
            return scopeCheck;
        }

        // Todas las validaciones pasaron
        log.debug("Guardrails: mensaje permitido");
        return GuardrailEvaluationResult.builder()
                .action(GuardrailAction.ALLOW)
                .reason(GuardrailReason.NONE)
                .build();
    }

    /**
     * Valida que el mensaje no exceda el límite de longitud.
     */
    private GuardrailEvaluationResult checkMessageLength(String message) {
        if (message.length() > MAX_MESSAGE_LENGTH) {
            return GuardrailEvaluationResult.builder()
                    .action(GuardrailAction.BLOCK)
                    .reason(GuardrailReason.TOO_LONG)
                    .predefinedResponse(String.format(
                            "Tu mensaje es demasiado largo (%d caracteres). " +
                            "Por favor, envía un mensaje de máximo %d caracteres.",
                            message.length(), MAX_MESSAGE_LENGTH))
                    .quickReplies(List.of(
                            "Resumir mi pregunta",
                            "Dividir en partes",
                            "Ayuda"
                    ))
                    .build();
        }
        return GuardrailEvaluationResult.builder()
                .action(GuardrailAction.ALLOW)
                .reason(GuardrailReason.NONE)
                .build();
    }

    /**
     * Detecta intentos de prompt injection o manipulación del sistema.
     */
    private GuardrailEvaluationResult checkInjectionAttempts(String message) {
        for (Pattern pattern : INJECTION_PATTERNS) {
            if (pattern.matcher(message).find()) {
                return GuardrailEvaluationResult.builder()
                        .action(GuardrailAction.BLOCK)
                        .reason(GuardrailReason.INJECTION)
                        .predefinedResponse(
                                "No puedo procesar tu solicitud. " +
                                "Por favor, reformula tu pregunta de manera natural.")
                        .quickReplies(List.of(
                                "¿Cómo puedo ayudarte?",
                                "Ver opciones",
                                "Hablar con soporte"
                        ))
                        .build();
            }
        }
        return GuardrailEvaluationResult.builder()
                .action(GuardrailAction.ALLOW)
                .reason(GuardrailReason.NONE)
                .build();
    }

    /**
     * Detecta contenido inseguro o prohibido.
     */
    private GuardrailEvaluationResult checkUnsafeContent(String message) {
        for (Pattern pattern : UNSAFE_PATTERNS) {
            if (pattern.matcher(message).find()) {
                return GuardrailEvaluationResult.builder()
                        .action(GuardrailAction.BLOCK)
                        .reason(GuardrailReason.UNSAFE)
                        .predefinedResponse(
                                "Tu mensaje contiene contenido que no puedo procesar. " +
                                "Por favor, reformula tu pregunta.")
                        .quickReplies(List.of(
                                "Ver temas disponibles",
                                "Ayuda",
                                "Contactar soporte"
                        ))
                        .build();
            }
        }
        return GuardrailEvaluationResult.builder()
                .action(GuardrailAction.ALLOW)
                .reason(GuardrailReason.NONE)
                .build();
    }

    /**
     * Valida que la solicitud esté dentro del alcance permitido.
     * Solo se aplica si el request tiene mode=EVENT o domainId/eventId.
     * Los dominios permitidos se cargan dinámicamente desde la base de datos.
     */
    private GuardrailEvaluationResult checkScope(ChatRequest request, String message) {
        // Verificar si el contexto requiere validación de alcance
        boolean requiresScopeValidation = false;
        String domainContext = null;
        
        if (request.getMetadata() != null) {
            String mode = request.getMetadata().getMode() != null ? request.getMetadata().getMode().toString() : null;
            String domainId = request.getMetadata().getDomainId();
            String eventId = request.getMetadata().getEventId();
            
            requiresScopeValidation = "EVENT".equals(mode) || domainId != null || eventId != null;
            domainContext = domainId;
        }

        if (!requiresScopeValidation) {
            // No se requiere validación de alcance
            return GuardrailEvaluationResult.builder()
                    .action(GuardrailAction.ALLOW)
                    .reason(GuardrailReason.NONE)
                    .build();
        }

        // Cargar dominios permitidos desde BD (cacheados)
        List<String> allowedKeywords = allowedDomainService.getAllowedKeywords();
        
        if (allowedKeywords.isEmpty()) {
            log.warn("⚠️ No hay dominios permitidos configurados en BD, permitiendo mensaje");
            return GuardrailEvaluationResult.builder()
                    .action(GuardrailAction.ALLOW)
                    .reason(GuardrailReason.NONE)
                    .build();
        }

        // Verificar si el mensaje menciona algún dominio permitido
        String messageLower = message.toLowerCase();
        boolean inScope = allowedKeywords.stream()
                .anyMatch(messageLower::contains);

        if (!inScope) {
            // Generar mensaje y quick replies dinámicos basados en el contexto
            String redirectMessage = buildRedirectMessage(domainContext, allowedKeywords);
            List<String> quickReplies = buildRedirectQuickReplies(domainContext);
            
            return GuardrailEvaluationResult.builder()
                    .action(GuardrailAction.REDIRECT)
                    .reason(GuardrailReason.OUT_OF_SCOPE)
                    .predefinedResponse(redirectMessage)
                    .quickReplies(quickReplies)
                    .build();
        }

        return GuardrailEvaluationResult.builder()
                .action(GuardrailAction.ALLOW)
                .reason(GuardrailReason.NONE)
                .build();
    }

    /**
     * Construye mensaje de redirección dinámico según el contexto.
     */
    private String buildRedirectMessage(String domainContext, List<String> allowedKeywords) {
        if (domainContext != null && !domainContext.isBlank()) {
            return String.format(
                    "Estoy aquí para ayudarte con temas relacionados a %s. " +
                    "¿Tienes alguna pregunta relacionada?", domainContext);
        }
        
        // Mensaje genérico si no hay contexto específico
        return "Estoy aquí para ayudarte con los temas permitidos. " +
               "¿En qué puedo asistirte?";
    }

    /**
     * Construye quick replies dinámicos según el dominio.
     */
    private List<String> buildRedirectQuickReplies(String domainContext) {
        // Quick replies genéricos para redirección
        return List.of(
                "Ver temas disponibles",
                "¿Qué puedes hacer?",
                "Ayuda"
        );
    }
}