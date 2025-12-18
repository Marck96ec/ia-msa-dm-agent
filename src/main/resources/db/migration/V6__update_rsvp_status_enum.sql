-- =========================================================================
-- Migración V6: Actualizar enum de status en tabla rsvps
-- =========================================================================
-- Cambia los valores del enum de status de YES/NO/PENDING a CONFIRMED/DECLINED/PENDING
-- para mayor claridad semántica
-- =========================================================================

-- Actualizar registros existentes
UPDATE rsvps SET status = 'CONFIRMED' WHERE status = 'YES';
UPDATE rsvps SET status = 'DECLINED' WHERE status = 'NO';

-- Eliminar constraint antiguo
ALTER TABLE rsvps DROP CONSTRAINT IF EXISTS chk_status;

-- Crear nuevo constraint con valores actualizados
ALTER TABLE rsvps ADD CONSTRAINT chk_status CHECK (status IN ('CONFIRMED', 'DECLINED', 'PENDING'));
