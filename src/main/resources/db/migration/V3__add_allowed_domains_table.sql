-- ============================================================================
-- Migration V3__add_allowed_domains_table.sql
-- ============================================================================
-- Descripción: Crea tabla de dominios permitidos para validación dinámica
--              de alcance en guardrails (OUT_OF_SCOPE).
-- 
-- Permite configurar dominios sin hardcodear en código Java.
-- Cada keyword representa una palabra/frase que debe estar presente en el
-- mensaje del usuario cuando mode=EVENT.
-- ============================================================================

-- ============================================================================
-- 1. TABLA: allowed_domain
-- ============================================================================

CREATE TABLE IF NOT EXISTS allowed_domain (
    -- Identificador único autoincremental
    id BIGSERIAL PRIMARY KEY,
    
    -- Palabra clave o frase del dominio permitido
    keyword VARCHAR(100) NOT NULL UNIQUE,
    
    -- Categoría o agrupación (ej: "baby-shower", "automotive", "wedding")
    category VARCHAR(50) NOT NULL,
    
    -- Descripción opcional del dominio
    description TEXT,
    
    -- Indica si el dominio está activo
    active BOOLEAN NOT NULL DEFAULT true,
    
    -- Timestamps de auditoría
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índices
CREATE INDEX idx_allowed_domain_category ON allowed_domain(category);
CREATE INDEX idx_allowed_domain_active ON allowed_domain(active);

-- Comentarios
COMMENT ON TABLE allowed_domain IS 'Dominios permitidos para validación de alcance (OUT_OF_SCOPE)';
COMMENT ON COLUMN allowed_domain.keyword IS 'Palabra clave del dominio (en minúsculas)';
COMMENT ON COLUMN allowed_domain.category IS 'Categoría para agrupar keywords relacionadas';
COMMENT ON COLUMN allowed_domain.active IS 'Si false, el dominio no se usa en validaciones';

-- ============================================================================
-- 2. DATOS INICIALES - EJEMPLO: BABY SHOWER
-- ============================================================================
-- Puedes cambiar estos datos según tu caso de uso

INSERT INTO allowed_domain (keyword, category, description, active) VALUES
    ('baby shower', 'baby-shower', 'Evento principal', true),
    ('babyshower', 'baby-shower', 'Variante sin espacio', true),
    ('baby-shower', 'baby-shower', 'Variante con guion', true),
    ('evento', 'baby-shower', 'Palabra genérica de evento', true),
    ('celebración', 'baby-shower', 'Sinónimo de evento', true),
    ('fiesta', 'baby-shower', 'Tipo de celebración', true),
    ('invitados', 'baby-shower', 'Relacionado con planificación', true),
    ('regalos', 'baby-shower', 'Aspecto del baby shower', true),
    ('juegos', 'baby-shower', 'Actividades del evento', true),
    ('decoración', 'baby-shower', 'Aspecto visual del evento', true),
    ('planificación', 'baby-shower', 'Proceso de organización', true),
    ('bebé', 'baby-shower', 'Tema central', true),
    ('mamá', 'baby-shower', 'Protagonista del evento', true),
    ('embarazo', 'baby-shower', 'Contexto del evento', true)
ON CONFLICT (keyword) DO NOTHING;

-- ============================================================================
-- 3. DATOS ALTERNATIVOS - EJEMPLO: CARROS (comentado por defecto)
-- ============================================================================
-- Descomenta y ajusta según necesites cambiar a otro dominio

-- DELETE FROM allowed_domain WHERE category = 'baby-shower';
-- 
-- INSERT INTO allowed_domain (keyword, category, description, active) VALUES
--     ('carros', 'automotive', 'Tema principal', true),
--     ('autos', 'automotive', 'Sinónimo de carros', true),
--     ('vehículos', 'automotive', 'Término formal', true),
--     ('automóviles', 'automotive', 'Término técnico', true),
--     ('coches', 'automotive', 'Variante española', true),
--     ('automotriz', 'automotive', 'Industria', true),
--     ('mecánica', 'automotive', 'Aspecto técnico', true),
--     ('motor', 'automotive', 'Componente', true),
--     ('mantenimiento', 'automotive', 'Cuidado del vehículo', true),
--     ('conducción', 'automotive', 'Actividad', true),
--     ('velocidad', 'automotive', 'Característica', true),
--     ('marca', 'automotive', 'Fabricante', true),
--     ('modelo', 'automotive', 'Tipo de vehículo', true)
-- ON CONFLICT (keyword) DO NOTHING;

-- ============================================================================
-- FIN DE LA MIGRACIÓN
-- ============================================================================
