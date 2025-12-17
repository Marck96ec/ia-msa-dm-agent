-- ============================================================================
-- Migration V4__extend_user_profile_progressive_memory.sql
-- ============================================================================
-- Descripción: Extiende user_profile para soportar "Memoria Progresiva Sin Interrogatorio"
--              Agrega campos para objetivo actual, formato preferido, velocidad de respuesta
--              y decisiones pasadas para evitar repetir preguntas.
-- ============================================================================

-- ============================================================================
-- 1. AGREGAR COLUMNAS A user_profile
-- ============================================================================

-- Objetivo actual de la conversación
ALTER TABLE user_profile 
ADD COLUMN IF NOT EXISTS current_objective TEXT;

-- Formato de respuesta preferido (STEPS, LIST, DIRECT)
ALTER TABLE user_profile 
ADD COLUMN IF NOT EXISTS preferred_format VARCHAR(20);

-- Ritmo de respuesta (QUICK, EXPLAINED)
ALTER TABLE user_profile 
ADD COLUMN IF NOT EXISTS response_speed VARCHAR(20);

-- Decisiones ya tomadas (JSONB array)
ALTER TABLE user_profile 
ADD COLUMN IF NOT EXISTS past_decisions JSONB;

-- ============================================================================
-- 2. COMENTARIOS
-- ============================================================================

COMMENT ON COLUMN user_profile.current_objective IS 'Objetivo actual de la conversación (ej: "planear baby shower")';
COMMENT ON COLUMN user_profile.preferred_format IS 'Formato preferido: STEPS (pasos), LIST (lista), DIRECT (directo)';
COMMENT ON COLUMN user_profile.response_speed IS 'Ritmo preferido: QUICK (rápidas), EXPLAINED (explicadas)';
COMMENT ON COLUMN user_profile.past_decisions IS 'Array JSON de decisiones importantes ya tomadas';

-- ============================================================================
-- FIN DE LA MIGRACIÓN
-- ============================================================================
