-- ============================================================================
-- Script de Datos Iniciales (SEED)
-- ============================================================================
-- Proyecto: IA MSA DM Agent
-- Descripción: Inserta datos de ejemplo para pruebas y desarrollo
-- Uso: Ejecutar después de schema.sql
-- ============================================================================

\c mydb;

-- ============================================================================
-- 1. DATOS DE EJEMPLO: conversation_history
-- ============================================================================

-- Limpiar datos existentes (solo para desarrollo)
-- TRUNCATE TABLE conversation_history RESTART IDENTITY CASCADE;

-- Conversación 1: Ejemplo básico de chat
INSERT INTO conversation_history 
    (conversation_id, user_message, ai_response, model_used, temperature, prompt_tokens, completion_tokens, total_tokens, created_at)
VALUES 
    ('conv-demo-001', 
     '¿Qué es Spring AI?', 
     'Spring AI es un framework de Spring diseñado específicamente para simplificar el desarrollo de aplicaciones que integran Inteligencia Artificial. Proporciona abstracción unificada para múltiples proveedores de IA como OpenAI, Azure OpenAI, Anthropic y Ollama.',
     'gpt-4o-mini',
     0.7,
     15,
     87,
     102,
     CURRENT_TIMESTAMP - INTERVAL '2 hours');

-- Conversación 1: Mensaje de seguimiento
INSERT INTO conversation_history 
    (conversation_id, user_message, ai_response, model_used, temperature, prompt_tokens, completion_tokens, total_tokens, created_at)
VALUES 
    ('conv-demo-001', 
     '¿Puedes darme un ejemplo de uso?', 
     'Claro, aquí tienes un ejemplo básico:\n\n```java\n@Service\npublic class ChatService {\n    private final ChatClient chatClient;\n    \n    public String chat(String message) {\n        return chatClient.prompt()\n            .user(message)\n            .call()\n            .content();\n    }\n}\n```',
     'gpt-4o-mini',
     0.7,
     25,
     145,
     170,
     CURRENT_TIMESTAMP - INTERVAL '1 hour 50 minutes');

-- Conversación 2: Generación creativa
INSERT INTO conversation_history 
    (conversation_id, user_message, ai_response, model_used, temperature, prompt_tokens, completion_tokens, total_tokens, created_at)
VALUES 
    ('conv-demo-002', 
     'Escribe un poema corto sobre programación', 
     'En líneas de código escribo mi arte,\nfunciones y clases que toman parte.\nDebug tras debug, busco el error,\nhasta que compile sin ningún dolor.',
     'gpt-4o-mini',
     1.2,
     18,
     65,
     83,
     CURRENT_TIMESTAMP - INTERVAL '1 hour 30 minutes');

-- Conversación 3: Consulta técnica
INSERT INTO conversation_history 
    (conversation_id, user_message, ai_response, model_used, temperature, prompt_tokens, completion_tokens, total_tokens, created_at)
VALUES 
    ('conv-demo-003', 
     '¿Cómo funciona JPA en Spring Boot?', 
     'JPA (Java Persistence API) en Spring Boot funciona mediante:\n\n1. **Entities**: Clases anotadas con @Entity que mapean a tablas\n2. **Repositories**: Interfaces que extienden JpaRepository\n3. **Auto-configuración**: Spring Boot configura automáticamente el EntityManager\n4. **Hibernate**: Implementación por defecto de JPA\n\nEjemplo:\n```java\n@Entity\npublic class User {\n    @Id\n    @GeneratedValue\n    private Long id;\n    private String name;\n}\n\npublic interface UserRepository extends JpaRepository<User, Long> {}\n```',
     'gpt-4o-mini',
     0.5,
     28,
     210,
     238,
     CURRENT_TIMESTAMP - INTERVAL '1 hour');

-- Conversación 4: Pregunta sobre arquitectura
INSERT INTO conversation_history 
    (conversation_id, user_message, ai_response, model_used, temperature, prompt_tokens, completion_tokens, total_tokens, created_at)
VALUES 
    ('conv-demo-004', 
     'Explica el patrón API-First', 
     'API-First es un enfoque de desarrollo donde:\n\n1. La especificación de la API se define ANTES del código\n2. Se usa OpenAPI/Swagger como "fuente de verdad"\n3. El código (modelos, controladores) se genera automáticamente\n4. Garantiza que documentación y código estén siempre sincronizados\n\nBeneficios:\n- Contrato claro entre frontend y backend\n- Documentación automática\n- Generación de SDKs para clientes\n- Detección temprana de problemas de diseño',
     'gpt-4o-mini',
     0.7,
     22,
     156,
     178,
     CURRENT_TIMESTAMP - INTERVAL '45 minutes');

-- Conversación 5: Consulta de base de datos
INSERT INTO conversation_history 
    (conversation_id, user_message, ai_response, model_used, temperature, prompt_tokens, completion_tokens, total_tokens, created_at)
VALUES 
    ('conv-demo-005', 
     '¿Cuáles son las mejores prácticas para PostgreSQL?', 
     'Mejores prácticas para PostgreSQL:\n\n1. **Índices**: Crear índices en columnas frecuentemente consultadas\n2. **Pool de conexiones**: Usar HikariCP con configuración adecuada\n3. **Particionamiento**: Para tablas muy grandes\n4. **VACUUM**: Ejecutar mantenimiento regular\n5. **Constraints**: Usar CHECK, UNIQUE, FOREIGN KEY\n6. **Transacciones**: Mantenerlas cortas y específicas\n7. **Tipos de datos**: Elegir el tipo más específico posible\n8. **Normalización**: Aplicar formas normales apropiadas',
     'gpt-4o-mini',
     0.6,
     30,
     195,
     225,
     CURRENT_TIMESTAMP - INTERVAL '30 minutes');

-- Conversación 6: Diferentes modelos (simulación)
INSERT INTO conversation_history 
    (conversation_id, user_message, ai_response, model_used, temperature, prompt_tokens, completion_tokens, total_tokens, created_at)
VALUES 
    ('conv-demo-006', 
     'Resume los principios SOLID', 
     'SOLID:\n\nS - Single Responsibility: Una clase, una responsabilidad\nO - Open/Closed: Abierto a extensión, cerrado a modificación\nL - Liskov Substitution: Las subclases deben ser sustituibles\nI - Interface Segregation: Interfaces específicas, no genéricas\nD - Dependency Inversion: Depender de abstracciones, no de implementaciones',
     'gpt-4',
     0.3,
     20,
     98,
     118,
     CURRENT_TIMESTAMP - INTERVAL '15 minutes');

-- Conversación 7: Uso reciente
INSERT INTO conversation_history 
    (conversation_id, user_message, ai_response, model_used, temperature, prompt_tokens, completion_tokens, total_tokens, created_at)
VALUES 
    ('conv-demo-007', 
     'Hola, ¿cómo estás?', 
     '¡Hola! Estoy funcionando perfectamente y listo para ayudarte. Soy un asistente de IA especializado en desarrollo de software, Spring Framework y arquitectura de aplicaciones. ¿En qué puedo ayudarte hoy?',
     'gpt-4o-mini',
     0.8,
     12,
     52,
     64,
     CURRENT_TIMESTAMP - INTERVAL '5 minutes');

-- ============================================================================
-- 2. VERIFICACIÓN DE DATOS
-- ============================================================================

-- Mostrar resumen de datos insertados
SELECT 
    COUNT(*) as total_conversaciones,
    COUNT(DISTINCT conversation_id) as conversaciones_unicas,
    SUM(total_tokens) as tokens_totales,
    AVG(total_tokens) as promedio_tokens,
    MIN(created_at) as primera_conversacion,
    MAX(created_at) as ultima_conversacion
FROM conversation_history;

-- Mostrar datos de ejemplo
SELECT 
    id,
    conversation_id,
    LEFT(user_message, 50) || '...' as mensaje,
    model_used,
    total_tokens,
    created_at
FROM conversation_history
ORDER BY created_at DESC;

-- ============================================================================
-- RESUMEN
-- ============================================================================

\echo ''
\echo '========================================='
\echo 'Datos de ejemplo insertados'
\echo '========================================='
\echo ''
\echo 'Se insertaron 7 conversaciones de ejemplo'
\echo 'con diferentes modelos y configuraciones'
\echo ''
\echo 'Puedes consultar:'
\echo '  SELECT * FROM conversation_history;'
\echo '  SELECT * FROM v_usage_by_model;'
\echo '  SELECT * FROM v_recent_conversations;'
\echo ''
\echo '========================================='
