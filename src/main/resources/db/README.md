# Scripts de Base de Datos PostgreSQL

Este directorio contiene los scripts SQL necesarios para configurar la base de datos del proyecto.

## 📁 Estructura de Scripts

```
db/
├── init-database.sql    # Creación de base de datos y usuario
├── schema.sql          # Definición de tablas, índices y constraints
└── seed-data.sql       # Datos de ejemplo (opcional)
```

## 🚀 Ejecución de Scripts

### 1. Inicialización de Base de Datos

Ejecutar como superusuario de PostgreSQL:

```bash
# Con psql (local)
psql -U postgres -f src/main/resources/db/init-database.sql

# Con Docker
docker exec -i postgres-db psql -U postgres < src/main/resources/db/init-database.sql
```

Este script:
- ✅ Crea la base de datos `mydb`
- ✅ Crea el usuario `admin` con contraseña `admin123`
- ✅ Otorga todos los privilegios necesarios

### 2. Creación de Schema

Ejecutar después de init-database.sql:

```bash
# Con psql (local)
psql -U admin -d mydb -f src/main/resources/db/schema.sql

# Con Docker
docker exec -i postgres-db psql -U admin -d mydb < src/main/resources/db/schema.sql
```

Este script:
- ✅ Crea la tabla `conversation_history`
- ✅ Crea índices para optimización de consultas
- ✅ Configura triggers para auditoría automática
- ✅ Crea vistas para análisis de datos

### 3. Datos de Ejemplo (Opcional)

Para desarrollo y pruebas:

```bash
# Con psql (local)
psql -U admin -d mydb -f src/main/resources/db/seed-data.sql

# Con Docker
docker exec -i postgres-db psql -U admin -d mydb < src/main/resources/db/seed-data.sql
```

Este script:
- ✅ Inserta 7 conversaciones de ejemplo
- ✅ Demuestra diferentes modelos y configuraciones
- ✅ Útil para pruebas y desarrollo

### 4. Ejecución Completa (Todo en uno)

```bash
# Local
psql -U postgres -f src/main/resources/db/init-database.sql
psql -U admin -d mydb -f src/main/resources/db/schema.sql
psql -U admin -d mydb -f src/main/resources/db/seed-data.sql

# Docker
docker exec -i postgres-db psql -U postgres < src/main/resources/db/init-database.sql
docker exec -i postgres-db psql -U admin -d mydb < src/main/resources/db/schema.sql
docker exec -i postgres-db psql -U admin -d mydb < src/main/resources/db/seed-data.sql
```

## 📊 Esquema de Base de Datos

### Tabla: `conversation_history`

```sql
conversation_history
├── id (BIGSERIAL PK)              -- ID único autoincremental
├── conversation_id (VARCHAR)      -- Agrupa mensajes de la misma conversación
├── user_message (TEXT)            -- Mensaje del usuario
├── ai_response (TEXT)             -- Respuesta de la IA
├── model_used (VARCHAR)           -- Modelo usado (gpt-4o-mini, etc.)
├── temperature (DOUBLE)           -- Parámetro de creatividad (0.0-2.0)
├── prompt_tokens (INTEGER)        -- Tokens del prompt
├── completion_tokens (INTEGER)    -- Tokens de la respuesta
├── total_tokens (INTEGER)         -- Total de tokens
├── created_at (TIMESTAMP)         -- Fecha de creación
└── updated_at (TIMESTAMP)         -- Fecha de actualización
```

### Índices

- `idx_conversation_id` - Búsqueda por ID de conversación
- `idx_created_at` - Ordenamiento y filtrado por fecha
- `idx_model_used` - Análisis de uso por modelo
- `idx_created_tokens` - Análisis de consumo de tokens

### Vistas

- `v_usage_by_model` - Estadísticas agrupadas por modelo
- `v_recent_conversations` - Últimas 100 conversaciones

### Triggers

- `update_conversation_history_updated_at` - Actualiza `updated_at` automáticamente

## 🔍 Consultas Útiles

```sql
-- Ver todas las conversaciones
SELECT * FROM conversation_history ORDER BY created_at DESC;

-- Ver uso por modelo
SELECT * FROM v_usage_by_model;

-- Conversaciones de las últimas 24 horas
SELECT * FROM conversation_history 
WHERE created_at > NOW() - INTERVAL '24 hours';

-- Tokens totales usados
SELECT SUM(total_tokens) as total FROM conversation_history;

-- Conversación más larga
SELECT * FROM conversation_history 
ORDER BY total_tokens DESC LIMIT 1;
```

## ⚠️ Notas Importantes

1. **Desarrollo vs Producción**: Los scripts incluyen comandos de limpieza (`DROP`, `TRUNCATE`) comentados. Descomentar solo en desarrollo.

2. **Spring Boot DDL**: El proyecto está configurado con `spring.jpa.hibernate.ddl-auto=update`, que crea/actualiza tablas automáticamente. Los scripts SQL son opcionales pero recomendados para control explícito.

3. **Migraciones**: Para producción, considera usar Flyway o Liquibase para gestionar versiones de schema.

4. **Seguridad**: Cambia las credenciales por defecto (`admin`/`admin123`) en producción.

## 🔐 Credenciales por Defecto

```
Host:     localhost (o postgres-db en Docker)
Puerto:   5432
Database: mydb
Usuario:  admin
Password: admin123
```

## 📝 Cambiar Credenciales

Editar `init-database.sql` y modificar:

```sql
CREATE USER tu_usuario WITH PASSWORD 'tu_password_segura';
```

Luego actualizar variables de entorno:

```env
DB_USERNAME=tu_usuario
DB_PASSWORD=tu_password_segura
```
