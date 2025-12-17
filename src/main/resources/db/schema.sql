-- ============================================================================
-- Script DDL: Creación de tablas y esquema de base de datos
-- ============================================================================
-- Proyecto: IA MSA DM Agent
-- Descripción: Define el esquema completo de la base de datos
-- Versión: 1.0.0
-- Fecha: 2025-12-17
-- ============================================================================

-- Conectar a la base de datos
\c mydb;

-- ============================================================================
-- 1. EXTENSIONS
-- ============================================================================

-- Habilitar extensiones necesarias
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";  -- Para generación de UUIDs

COMMENT ON EXTENSION "uuid-ossp" IS 'Generación de identificadores únicos universales';

-- ============================================================================
-- 2. TABLA: conversation_history
-- ============================================================================

-- Eliminar tabla si existe (solo para desarrollo)
-- DROP TABLE IF EXISTS conversation_history CASCADE;

CREATE TABLE IF NOT EXISTS conversation_history (
    -- Identificador único autoincremental
    id BIGSERIAL PRIMARY KEY,
    
    -- Identificador de la conversación (puede agrupar múltiples mensajes)
    conversation_id VARCHAR(100) NOT NULL,
    
    -- Contenido del mensaje del usuario
    user_message TEXT NOT NULL,
    
    -- Respuesta generada por la IA
    ai_response TEXT NOT NULL,
    
    -- Modelo de IA utilizado (ej: gpt-4o-mini, gpt-4, llama2)
    model_used VARCHAR(50),
    
    -- Parámetro de temperatura usado (0.0 - 2.0)
    temperature DOUBLE PRECISION,
    
    -- Métricas de uso de tokens
    prompt_tokens INTEGER,           -- Tokens del prompt (entrada)
    completion_tokens INTEGER,       -- Tokens de la respuesta (salida)
    total_tokens INTEGER,            -- Total de tokens usados
    
    -- Auditoría automática
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT chk_temperature CHECK (temperature IS NULL OR (temperature >= 0 AND temperature <= 2)),
    CONSTRAINT chk_prompt_tokens CHECK (prompt_tokens IS NULL OR prompt_tokens >= 0),
    CONSTRAINT chk_completion_tokens CHECK (completion_tokens IS NULL OR completion_tokens >= 0),
    CONSTRAINT chk_total_tokens CHECK (total_tokens IS NULL OR total_tokens >= 0)
);

-- Comentarios de documentación
COMMENT ON TABLE conversation_history IS 'Historial de conversaciones con modelos de IA';
COMMENT ON COLUMN conversation_history.id IS 'Identificador único de la entrada';
COMMENT ON COLUMN conversation_history.conversation_id IS 'ID que agrupa mensajes de la misma conversación';
COMMENT ON COLUMN conversation_history.user_message IS 'Mensaje o pregunta del usuario';
COMMENT ON COLUMN conversation_history.ai_response IS 'Respuesta generada por el modelo de IA';
COMMENT ON COLUMN conversation_history.model_used IS 'Modelo de IA utilizado (gpt-4o-mini, gpt-4, etc.)';
COMMENT ON COLUMN conversation_history.temperature IS 'Parámetro de creatividad (0.0 = determinístico, 2.0 = muy creativo)';
COMMENT ON COLUMN conversation_history.prompt_tokens IS 'Cantidad de tokens del mensaje de entrada';
COMMENT ON COLUMN conversation_history.completion_tokens IS 'Cantidad de tokens de la respuesta generada';
COMMENT ON COLUMN conversation_history.total_tokens IS 'Total de tokens consumidos (prompt + completion)';
COMMENT ON COLUMN conversation_history.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN conversation_history.updated_at IS 'Fecha y hora de última actualización';

-- ============================================================================
-- 3. ÍNDICES
-- ============================================================================

-- Índice para búsqueda por conversation_id (búsqueda frecuente)
CREATE INDEX IF NOT EXISTS idx_conversation_id 
    ON conversation_history(conversation_id);

-- Índice para búsqueda por fecha de creación (análisis temporal)
CREATE INDEX IF NOT EXISTS idx_created_at 
    ON conversation_history(created_at DESC);

-- Índice para búsqueda por modelo usado (análisis de uso)
CREATE INDEX IF NOT EXISTS idx_model_used 
    ON conversation_history(model_used);

-- Índice compuesto para consultas de análisis de tokens por fecha
CREATE INDEX IF NOT EXISTS idx_created_tokens 
    ON conversation_history(created_at, total_tokens);

COMMENT ON INDEX idx_conversation_id IS 'Índice para búsqueda rápida por ID de conversación';
COMMENT ON INDEX idx_created_at IS 'Índice para ordenamiento y filtrado por fecha';
COMMENT ON INDEX idx_model_used IS 'Índice para análisis de uso por modelo';
COMMENT ON INDEX idx_created_tokens IS 'Índice compuesto para análisis de consumo de tokens';

-- ============================================================================
-- 4. TRIGGER: Actualización automática de updated_at
-- ============================================================================

-- Función para actualizar timestamp automáticamente
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

COMMENT ON FUNCTION update_updated_at_column() IS 'Actualiza automáticamente el campo updated_at';

-- Trigger que ejecuta la función en cada UPDATE
DROP TRIGGER IF EXISTS update_conversation_history_updated_at ON conversation_history;

CREATE TRIGGER update_conversation_history_updated_at
    BEFORE UPDATE ON conversation_history
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

COMMENT ON TRIGGER update_conversation_history_updated_at ON conversation_history 
    IS 'Actualiza updated_at automáticamente en cada modificación';

-- ============================================================================
-- 5. VISTAS (Opcional - para análisis)
-- ============================================================================

-- Vista: Resumen de uso por modelo
CREATE OR REPLACE VIEW v_usage_by_model AS
SELECT 
    model_used,
    COUNT(*) as total_conversations,
    SUM(total_tokens) as total_tokens_used,
    AVG(total_tokens) as avg_tokens_per_conversation,
    AVG(temperature) as avg_temperature,
    MIN(created_at) as first_use,
    MAX(created_at) as last_use
FROM conversation_history
WHERE model_used IS NOT NULL
GROUP BY model_used
ORDER BY total_conversations DESC;

COMMENT ON VIEW v_usage_by_model IS 'Estadísticas de uso agrupadas por modelo de IA';

-- Vista: Conversaciones recientes
CREATE OR REPLACE VIEW v_recent_conversations AS
SELECT 
    id,
    conversation_id,
    LEFT(user_message, 100) || '...' as user_message_preview,
    LEFT(ai_response, 100) || '...' as ai_response_preview,
    model_used,
    total_tokens,
    created_at
FROM conversation_history
ORDER BY created_at DESC
LIMIT 100;

COMMENT ON VIEW v_recent_conversations IS 'Últimas 100 conversaciones con preview de mensajes';

-- ============================================================================
-- 6. PERMISOS
-- ============================================================================

-- Otorgar permisos al usuario admin
GRANT SELECT, INSERT, UPDATE, DELETE ON conversation_history TO admin;
GRANT USAGE, SELECT ON SEQUENCE conversation_history_id_seq TO admin;
GRANT SELECT ON v_usage_by_model TO admin;
GRANT SELECT ON v_recent_conversations TO admin;

-- ============================================================================
-- RESUMEN
-- ============================================================================

\echo ''
\echo '========================================='
\echo 'Schema creado exitosamente'
\echo '========================================='
\echo ''
\echo 'Tablas creadas:'
\echo '  ✓ conversation_history'
\echo ''
\echo 'Índices creados:'
\echo '  ✓ idx_conversation_id'
\echo '  ✓ idx_created_at'
\echo '  ✓ idx_model_used'
\echo '  ✓ idx_created_tokens'
\echo ''
\echo 'Vistas creadas:'
\echo '  ✓ v_usage_by_model'
\echo '  ✓ v_recent_conversations'
\echo ''
\echo 'Triggers creados:'
\echo '  ✓ update_conversation_history_updated_at'
\echo ''
\echo '========================================='
