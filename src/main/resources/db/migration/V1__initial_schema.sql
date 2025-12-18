-- ============================================================================
-- Migration V1: Initial Schema
-- ============================================================================
-- Description: Creates the base conversation_history table
-- Date: 2025-01-XX
-- ============================================================================

-- ============================================================================
-- 1. EXTENSIONS
-- ============================================================================

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ============================================================================
-- 2. TABLA: conversation_history
-- ============================================================================

CREATE TABLE IF NOT EXISTS conversation_history (
    id BIGSERIAL PRIMARY KEY,
    conversation_id VARCHAR(100) NOT NULL,
    user_message TEXT NOT NULL,
    ai_response TEXT NOT NULL,
    model_used VARCHAR(50),
    temperature DOUBLE PRECISION,
    prompt_tokens INTEGER,
    completion_tokens INTEGER,
    total_tokens INTEGER,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT chk_temperature CHECK (temperature IS NULL OR (temperature >= 0 AND temperature <= 2)),
    CONSTRAINT chk_prompt_tokens CHECK (prompt_tokens IS NULL OR prompt_tokens >= 0),
    CONSTRAINT chk_completion_tokens CHECK (completion_tokens IS NULL OR completion_tokens >= 0),
    CONSTRAINT chk_total_tokens CHECK (total_tokens IS NULL OR total_tokens >= 0)
);

-- ============================================================================
-- 3. ÍNDICES
-- ============================================================================

CREATE INDEX IF NOT EXISTS idx_conversation_id 
    ON conversation_history(conversation_id);

CREATE INDEX IF NOT EXISTS idx_created_at 
    ON conversation_history(created_at DESC);

CREATE INDEX IF NOT EXISTS idx_model_used 
    ON conversation_history(model_used);

CREATE INDEX IF NOT EXISTS idx_created_tokens 
    ON conversation_history(created_at, total_tokens);

-- ============================================================================
-- 4. TRIGGER: Actualización automática de updated_at
-- ============================================================================

CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_conversation_history_updated_at
    BEFORE UPDATE ON conversation_history
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();
