-- ============================================================================
-- Script de inicialización de base de datos PostgreSQL
-- ============================================================================
-- Proyecto: IA MSA DM Agent
-- Descripción: Crea la base de datos y el usuario necesarios
-- Uso: Ejecutar como superusuario de PostgreSQL (postgres)
-- ============================================================================

-- Conectar a la base de datos por defecto
\c postgres;

-- Verificar si la base de datos ya existe
SELECT 'Database already exists' AS message 
WHERE EXISTS (SELECT FROM pg_database WHERE datname = 'mydb');

-- Crear base de datos si no existe
CREATE DATABASE mydb
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.utf8'
    LC_CTYPE = 'en_US.utf8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    TEMPLATE = template0;

COMMENT ON DATABASE mydb IS 'Base de datos para IA MSA DM Agent - API REST con Spring AI';

-- Crear usuario si no existe
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_user WHERE usename = 'admin') THEN
        CREATE USER admin WITH 
            LOGIN
            NOSUPERUSER
            NOCREATEDB
            NOCREATEROLE
            NOINHERIT
            NOREPLICATION
            CONNECTION LIMIT -1
            PASSWORD 'admin123';
    END IF;
END
$$;

-- Conectar a la nueva base de datos
\c mydb;

-- Otorgar todos los privilegios al usuario admin
GRANT ALL PRIVILEGES ON DATABASE mydb TO admin;
GRANT ALL PRIVILEGES ON SCHEMA public TO admin;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO admin;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO admin;

-- Mostrar mensaje de confirmación
\echo '✓ Base de datos "mydb" creada exitosamente'
\echo '✓ Usuario "admin" configurado correctamente'
\echo '✓ Privilegios otorgados'
\echo ''
\echo 'Credenciales:'
\echo '  Host:     localhost (o postgres-db en Docker)'
\echo '  Puerto:   5432'
\echo '  Database: mydb'
\echo '  Usuario:  admin'
\echo '  Password: admin123'
