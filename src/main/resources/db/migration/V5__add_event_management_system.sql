-- ============================================================================
-- Migration V5: Sistema de gestión de eventos (Baby Shower)
-- ============================================================================
-- Descripción: Agrega tablas para gestionar eventos, RSVPs, regalos,
--              compromisos, ideas y mensajes para el bebé.
-- Fecha: 2025-12-17
-- ============================================================================

-- ============================================================================
-- 1. TABLA: events
-- ============================================================================

CREATE TABLE IF NOT EXISTS events (
    -- Identificador único autoincremental
    id BIGSERIAL PRIMARY KEY,
    
    -- Slug único para acceso público (ej: baby-shower-maria-2025)
    slug VARCHAR(100) NOT NULL UNIQUE,
    
    -- Información básica del evento
    name VARCHAR(200) NOT NULL,
    description TEXT,
    event_date TIMESTAMP WITH TIME ZONE NOT NULL,
    location TEXT,
    location_url VARCHAR(500),
    
    -- Mensajes personalizados para el chatbot
    welcome_message TEXT,
    closing_message TEXT,
    chatbot_instructions TEXT,
    
    -- Estado y configuración
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    max_attendees INTEGER,
    gift_budget DECIMAL(10, 2),
    
    -- Información del organizador
    organizer_user_id VARCHAR(100) NOT NULL,
    organizer_name VARCHAR(200),
    organizer_email VARCHAR(150),
    organizer_phone VARCHAR(50),
    
    -- Configuraciones de funcionalidades
    allow_shared_gifts BOOLEAN NOT NULL DEFAULT TRUE,
    allow_baby_messages BOOLEAN NOT NULL DEFAULT TRUE,
    allow_ideas BOOLEAN NOT NULL DEFAULT TRUE,
    
    -- Imagen del evento
    image_url VARCHAR(500),
    
    -- Auditoría
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Índices para events
CREATE INDEX idx_event_slug ON events(slug);
CREATE INDEX idx_event_date ON events(event_date);
CREATE INDEX idx_is_active ON events(is_active);

COMMENT ON TABLE events IS 'Eventos (baby shower, cumpleaños, etc.)';
COMMENT ON COLUMN events.slug IS 'Identificador único amigable para URLs';
COMMENT ON COLUMN events.organizer_user_id IS 'ID del usuario organizador';

-- ============================================================================
-- 2. TABLA: rsvps
-- ============================================================================

CREATE TABLE IF NOT EXISTS rsvps (
    -- Identificador único autoincremental
    id BIGSERIAL PRIMARY KEY,
    
    -- Relación con evento
    event_id BIGINT NOT NULL,
    
    -- Información del invitado
    user_id VARCHAR(100) NOT NULL,
    guest_name VARCHAR(200),
    guest_email VARCHAR(150),
    guest_phone VARCHAR(50),
    
    -- Estado de confirmación
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    
    -- Detalles de asistencia
    guests_count INTEGER,
    notes TEXT,
    
    -- Auditoría
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT fk_rsvp_event FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
    CONSTRAINT uk_event_user UNIQUE (event_id, user_id),
    CONSTRAINT chk_status CHECK (status IN ('YES', 'NO', 'PENDING'))
);

-- Índices para rsvps
CREATE INDEX idx_rsvp_event ON rsvps(event_id);
CREATE INDEX idx_rsvp_user ON rsvps(user_id);
CREATE INDEX idx_rsvp_status ON rsvps(status);

COMMENT ON TABLE rsvps IS 'Confirmaciones de asistencia (RSVP)';
COMMENT ON COLUMN rsvps.status IS 'Estado: YES, NO, PENDING';

-- ============================================================================
-- 3. TABLA: gifts
-- ============================================================================

CREATE TABLE IF NOT EXISTS gifts (
    -- Identificador único autoincremental
    id BIGSERIAL PRIMARY KEY,
    
    -- Relación con evento
    event_id BIGINT NOT NULL,
    
    -- Información del regalo
    name VARCHAR(200) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2),
    image_url VARCHAR(500),
    
    -- Configuración
    allow_split BOOLEAN NOT NULL DEFAULT FALSE,
    priority INTEGER,
    quantity INTEGER DEFAULT 1,
    purchase_url VARCHAR(500),
    
    -- Estado
    status VARCHAR(30) NOT NULL DEFAULT 'AVAILABLE',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    
    -- Auditoría
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT fk_gift_event FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
    CONSTRAINT chk_gift_status CHECK (status IN ('AVAILABLE', 'RESERVED', 'PARTIALLY_FUNDED', 'FULLY_FUNDED', 'INACTIVE'))
);

-- Índices para gifts
CREATE INDEX idx_gift_event ON gifts(event_id);
CREATE INDEX idx_gift_status ON gifts(status);
CREATE INDEX idx_gift_active ON gifts(is_active);

COMMENT ON TABLE gifts IS 'Regalos de la lista de cada evento';
COMMENT ON COLUMN gifts.allow_split IS 'Permite aportes compartidos';
COMMENT ON COLUMN gifts.status IS 'Estado: AVAILABLE, RESERVED, PARTIALLY_FUNDED, FULLY_FUNDED, INACTIVE';

-- ============================================================================
-- 4. TABLA: gift_commitments
-- ============================================================================

CREATE TABLE IF NOT EXISTS gift_commitments (
    -- Identificador único autoincremental
    id BIGSERIAL PRIMARY KEY,
    
    -- Relación con regalo
    gift_id BIGINT NOT NULL,
    
    -- Información del invitado
    user_id VARCHAR(100) NOT NULL,
    guest_name VARCHAR(200),
    guest_email VARCHAR(150),
    guest_phone VARCHAR(50),
    
    -- Detalles del compromiso
    commitment_type VARCHAR(30) NOT NULL,
    contribution_amount DECIMAL(10, 2),
    
    -- Token para consulta sin login
    token VARCHAR(100) NOT NULL UNIQUE,
    
    -- Estado
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    notes TEXT,
    cancelled_at TIMESTAMP WITH TIME ZONE,
    
    -- Auditoría
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT fk_commitment_gift FOREIGN KEY (gift_id) REFERENCES gifts(id) ON DELETE CASCADE,
    CONSTRAINT chk_commitment_type CHECK (commitment_type IN ('FULL_RESERVATION', 'PARTIAL_CONTRIBUTION'))
);

-- Índices para gift_commitments
CREATE INDEX idx_commitment_gift ON gift_commitments(gift_id);
CREATE INDEX idx_commitment_user ON gift_commitments(user_id);
CREATE INDEX idx_commitment_token ON gift_commitments(token);
CREATE INDEX idx_commitment_active ON gift_commitments(is_active);

COMMENT ON TABLE gift_commitments IS 'Compromisos de regalos (reservas y aportes)';
COMMENT ON COLUMN gift_commitments.token IS 'Token único para consulta/cancelación sin login';
COMMENT ON COLUMN gift_commitments.commitment_type IS 'Tipo: FULL_RESERVATION, PARTIAL_CONTRIBUTION';

-- ============================================================================
-- 5. TABLA: ideas
-- ============================================================================

CREATE TABLE IF NOT EXISTS ideas (
    -- Identificador único autoincremental
    id BIGSERIAL PRIMARY KEY,
    
    -- Relación con evento
    event_id BIGINT NOT NULL,
    
    -- Información del invitado
    user_id VARCHAR(100) NOT NULL,
    guest_name VARCHAR(200),
    
    -- Contenido de la idea
    description TEXT NOT NULL,
    
    -- Moderación
    is_approved BOOLEAN DEFAULT FALSE,
    organizer_comment TEXT,
    
    -- Auditoría
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT fk_idea_event FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE
);

-- Índices para ideas
CREATE INDEX idx_idea_event ON ideas(event_id);
CREATE INDEX idx_idea_user ON ideas(user_id);
CREATE INDEX idx_idea_approved ON ideas(is_approved);

COMMENT ON TABLE ideas IS 'Ideas de apoyo propuestas por invitados';

-- ============================================================================
-- 6. TABLA: baby_messages
-- ============================================================================

CREATE TABLE IF NOT EXISTS baby_messages (
    -- Identificador único autoincremental
    id BIGSERIAL PRIMARY KEY,
    
    -- Relación con evento
    event_id BIGINT NOT NULL,
    
    -- Información del invitado
    user_id VARCHAR(100) NOT NULL,
    guest_name VARCHAR(200),
    
    -- Contenido del mensaje
    message_text TEXT NOT NULL,
    audio_url VARCHAR(500),
    
    -- Moderación
    is_published BOOLEAN NOT NULL DEFAULT TRUE,
    
    -- Auditoría
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT fk_baby_message_event FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE
);

-- Índices para baby_messages
CREATE INDEX idx_baby_message_event ON baby_messages(event_id);
CREATE INDEX idx_baby_message_user ON baby_messages(user_id);
CREATE INDEX idx_baby_message_published ON baby_messages(is_published);

COMMENT ON TABLE baby_messages IS 'Mensajes para el bebé dejados por invitados';
COMMENT ON COLUMN baby_messages.is_published IS 'Indica si el mensaje está visible públicamente';

-- ============================================================================
-- FINALIZACIÓN
-- ============================================================================

-- Mensaje de confirmación
DO $$ 
BEGIN
    RAISE NOTICE '✅ Migration V5 completada exitosamente';
    RAISE NOTICE '📦 Tablas creadas: events, rsvps, gifts, gift_commitments, ideas, baby_messages';
END $$;
