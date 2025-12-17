-- ============================================================================
-- Migration V2__add_guardrails_and_profile_support.sql
-- ============================================================================
-- Descripción: Agrega soporte para Guardrails y Memoria por Personalidad
-- 
-- Cambios:
-- 1. Crear tabla user_profile con índices
-- 2. Agregar columnas a conversation_history: userId, domainId, eventId, 
--    intent, guardrailAction, guardrailReason, quickReplies
-- 3. Crear índices adicionales en conversation_history
-- ============================================================================

-- ============================================================================
-- 1. TABLA: user_profile
-- ============================================================================

CREATE TABLE IF NOT EXISTS user_profile (
    -- Identificador único autoincremental
    id BIGSERIAL PRIMARY KEY,
    
    -- Identificador único del usuario (phone, sessionId, anonymousId)
    user_id VARCHAR(100) NOT NULL UNIQUE,
    
    -- Idioma preferido (default: es-EC)
    preferred_language VARCHAR(10) NOT NULL DEFAULT 'es-EC',
    
    -- Tono conversacional
    tone VARCHAR(20) NOT NULL DEFAULT 'WARM'
        CHECK (tone IN ('WARM', 'NEUTRAL', 'FORMAL', 'FUNNY')),
    
    -- Nivel de detalle en respuestas
    verbosity VARCHAR(20) NOT NULL DEFAULT 'MEDIUM'
        CHECK (verbosity IN ('SHORT', 'MEDIUM', 'DETAILED')),
    
    -- Preferencia de uso de emojis
    emoji_preference VARCHAR(20) NOT NULL DEFAULT 'LIGHT'
        CHECK (emoji_preference IN ('NONE', 'LIGHT', 'HEAVY')),
    
    -- Notas de estilo personalizadas (máx 500 caracteres)
    style_notes TEXT,
    
    -- Última actualización del perfil
    last_updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    
    -- Versión para optimistic locking
    version BIGINT NOT NULL DEFAULT 0,
    
    -- Timestamps de auditoría
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT style_notes_length CHECK (LENGTH(style_notes) <= 500)
);

-- Índices para user_profile
CREATE INDEX idx_user_profile_user_id ON user_profile(user_id);
CREATE INDEX idx_user_profile_last_updated ON user_profile(last_updated_at);

-- Comentarios
COMMENT ON TABLE user_profile IS 'Perfil conversacional del usuario con preferencias de personalidad';
COMMENT ON COLUMN user_profile.user_id IS 'ID único del usuario (phone, sessionId, anonymousId)';
COMMENT ON COLUMN user_profile.tone IS 'Tono conversacional: WARM, NEUTRAL, FORMAL, FUNNY';
COMMENT ON COLUMN user_profile.verbosity IS 'Nivel de detalle: SHORT, MEDIUM, DETAILED';
COMMENT ON COLUMN user_profile.emoji_preference IS 'Uso de emojis: NONE, LIGHT, HEAVY';
COMMENT ON COLUMN user_profile.style_notes IS 'Notas de estilo inferidas de la conversación';

-- ============================================================================
-- 2. AGREGAR COLUMNAS A conversation_history
-- ============================================================================

-- Agregar userId para vincular con perfil
ALTER TABLE conversation_history 
ADD COLUMN IF NOT EXISTS user_id VARCHAR(100);

-- Agregar domainId para contexto
ALTER TABLE conversation_history 
ADD COLUMN IF NOT EXISTS domain_id VARCHAR(100);

-- Agregar eventId para contexto específico
ALTER TABLE conversation_history 
ADD COLUMN IF NOT EXISTS event_id VARCHAR(100);

-- Agregar intent (reservado para futuras mejoras)
ALTER TABLE conversation_history 
ADD COLUMN IF NOT EXISTS intent VARCHAR(50);

-- Agregar guardrailAction
ALTER TABLE conversation_history 
ADD COLUMN IF NOT EXISTS guardrail_action VARCHAR(20)
    CHECK (guardrail_action IN ('ALLOW', 'BLOCK', 'REDIRECT'));

-- Agregar guardrailReason
ALTER TABLE conversation_history 
ADD COLUMN IF NOT EXISTS guardrail_reason VARCHAR(20)
    CHECK (guardrail_reason IN ('NONE', 'TOO_LONG', 'INJECTION', 'OUT_OF_SCOPE', 'UNSAFE'));

-- Agregar quickReplies (JSONB)
ALTER TABLE conversation_history 
ADD COLUMN IF NOT EXISTS quick_replies JSONB;

-- Comentarios para nuevas columnas
COMMENT ON COLUMN conversation_history.user_id IS 'ID del usuario (vincula con user_profile)';
COMMENT ON COLUMN conversation_history.domain_id IS 'ID del dominio (ej: baby-shower)';
COMMENT ON COLUMN conversation_history.event_id IS 'ID del evento específico';
COMMENT ON COLUMN conversation_history.intent IS 'Intención detectada (futuro)';
COMMENT ON COLUMN conversation_history.guardrail_action IS 'Acción de guardrail: ALLOW, BLOCK, REDIRECT';
COMMENT ON COLUMN conversation_history.guardrail_reason IS 'Razón del guardrail: NONE, TOO_LONG, INJECTION, OUT_OF_SCOPE, UNSAFE';
COMMENT ON COLUMN conversation_history.quick_replies IS 'Quick replies mostrados (formato JSON)';

-- ============================================================================
-- 3. ÍNDICES ADICIONALES EN conversation_history
-- ============================================================================

-- Índice compuesto para búsquedas por usuario y fecha
CREATE INDEX IF NOT EXISTS idx_conversation_history_user_created 
ON conversation_history(user_id, created_at DESC);

-- Índice compuesto para búsquedas por dominio y fecha
CREATE INDEX IF NOT EXISTS idx_conversation_history_domain_created 
ON conversation_history(domain_id, created_at DESC);

-- Índice para guardrail_action (análisis de guardrails)
CREATE INDEX IF NOT EXISTS idx_conversation_history_guardrail_action 
ON conversation_history(guardrail_action) 
WHERE guardrail_action IS NOT NULL;

-- ============================================================================
-- 4. DATOS INICIALES (OPCIONAL)
-- ============================================================================

-- Aquí se pueden insertar perfiles por defecto si es necesario
-- Por ejemplo, un perfil genérico para usuarios anónimos

-- ============================================================================
-- FIN DE LA MIGRACIÓN
-- ============================================================================
