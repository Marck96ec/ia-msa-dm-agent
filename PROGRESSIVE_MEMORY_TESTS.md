# 🧪 Pruebas de Memoria Progresiva

Este archivo contiene ejemplos de pruebas para validar el sistema de "Memoria Progresiva Sin Interrogatorio".

---

## 📋 Prerequisitos

```bash
# Asegurarse de que la aplicación esté corriendo
./gradlew bootRun

# O verificar que el puerto 8080 esté activo
curl http://localhost:8080/actuator/health
```

---

## 🎯 Test 1: Detección de Formato Preferido (STEPS)

### Llamada
```bash
curl -X POST http://localhost:8080/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Explícame en pasos cómo organizar un baby shower exitoso",
    "metadata": {
      "userId": "test-progressive-001",
      "mode": "EVENT"
    }
  }' | jq
```

### Validación
```sql
-- Verificar que se guardó el formato preferido
SELECT user_id, preferred_format, response_speed, current_objective 
FROM user_profile 
WHERE user_id = 'test-progressive-001';
```

**Esperado**: `preferred_format = 'STEPS'`

---

## 🎯 Test 2: Detección de Ritmo Rápido (QUICK)

### Llamada
```bash
curl -X POST http://localhost:8080/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Rápido, dime 3 juegos para baby shower",
    "metadata": {
      "userId": "test-progressive-002",
      "mode": "EVENT"
    }
  }' | jq
```

### Validación
```sql
SELECT user_id, response_speed 
FROM user_profile 
WHERE user_id = 'test-progressive-002';
```

**Esperado**: `response_speed = 'QUICK'`

---

## 🎯 Test 3: Detección de Formato Lista (LIST)

### Llamada
```bash
curl -X POST http://localhost:8080/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Dame ideas de decoración en lista",
    "metadata": {
      "userId": "test-progressive-003",
      "mode": "EVENT"
    }
  }' | jq
```

### Validación
```sql
SELECT user_id, preferred_format 
FROM user_profile 
WHERE user_id = 'test-progressive-003';
```

**Esperado**: `preferred_format = 'LIST'`

---

## 🎯 Test 4: Detección de Formato Directo (DIRECT)

### Llamada
```bash
curl -X POST http://localhost:8080/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Vamos al grano, ¿cuánto cuesta un baby shower promedio?",
    "metadata": {
      "userId": "test-progressive-004",
      "mode": "EVENT"
    }
  }' | jq
```

### Validación
```sql
SELECT user_id, preferred_format 
FROM user_profile 
WHERE user_id = 'test-progressive-004';
```

**Esperado**: `preferred_format = 'DIRECT'`

---

## 🎯 Test 5: Detección de Ritmo Explicado (EXPLAINED)

### Llamada
```bash
curl -X POST http://localhost:8080/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Explícame con ejemplos cómo elegir el tema del baby shower",
    "metadata": {
      "userId": "test-progressive-005",
      "mode": "EVENT"
    }
  }' | jq
```

### Validación
```sql
SELECT user_id, response_speed 
FROM user_profile 
WHERE user_id = 'test-progressive-005';
```

**Esperado**: `response_speed = 'EXPLAINED'`

---

## 🎯 Test 6: Combinación de Preferencias

### Llamada
```bash
curl -X POST http://localhost:8080/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Quiero que me respondas de forma más formal y detallada, en pasos",
    "metadata": {
      "userId": "test-progressive-006"
    }
  }' | jq
```

### Validación
```sql
SELECT user_id, tone, verbosity, preferred_format 
FROM user_profile 
WHERE user_id = 'test-progressive-006';
```

**Esperado**: 
- `tone = 'FORMAL'`
- `verbosity = 'DETAILED'`
- `preferred_format = 'STEPS'`

---

## 🎯 Test 7: Verificación del System Prompt

Este test verifica que el System Prompt se construya correctamente con las preferencias.

### Setup
```bash
# Primera llamada: establecer preferencias
curl -X POST http://localhost:8080/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Háblame formal y en pasos, sin emojis",
    "metadata": {
      "userId": "test-progressive-007",
      "mode": "EVENT"
    }
  }' | jq
```

### Segunda llamada (debe usar el perfil guardado)
```bash
curl -X POST http://localhost:8080/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "¿Qué regalos son populares para baby shower?",
    "metadata": {
      "userId": "test-progressive-007",
      "mode": "EVENT"
    }
  }' | jq
```

### Validación Manual
**Esperado**:
- Respuesta en tono formal (sin "¡", sin lenguaje coloquial)
- Respuesta en pasos numerados (1., 2., 3., ...)
- Sin emojis en la respuesta

---

## 🎯 Test 8: Verificación de Logs (System Prompt en Logs)

Para ver el System Prompt que se está enviando a OpenAI:

```bash
# Habilitar logging detallado (si no está activo)
# Buscar en logs: "Calling AI with system prompt"

# Hacer una llamada con perfil configurado
curl -X POST http://localhost:8080/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Ayúdame a planear mi baby shower",
    "metadata": {
      "userId": "test-progressive-008",
      "mode": "EVENT"
    }
  }' | jq

# Revisar logs en la consola donde corre la app
# Buscar: "# PERFIL DEL USUARIO"
```

**Esperado en logs**:
```
# PERFIL DEL USUARIO (aplica de forma natural, sin mencionarlo)
- Idioma preferido: es-EC
- Tono conversacional: cercano y amigable
- Nivel de detalle: equilibrado entre brevedad y detalle
- Uso de emojis: usar emojis ocasionalmente para énfasis
```

---

## 🎯 Test 9: Detección de Múltiples Comandos en un Mensaje

### Llamada
```bash
curl -X POST http://localhost:8080/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Quiero respuestas cortas, sin emojis y en lista",
    "metadata": {
      "userId": "test-progressive-009"
    }
  }' | jq
```

### Validación
```sql
SELECT 
  user_id, 
  verbosity, 
  emoji_preference, 
  preferred_format 
FROM user_profile 
WHERE user_id = 'test-progressive-009';
```

**Esperado**:
- `verbosity = 'SHORT'`
- `emoji_preference = 'NONE'`
- `preferred_format = 'LIST'`

---

## 🎯 Test 10: Persistencia del Perfil Entre Sesiones

### Primera sesión
```bash
curl -X POST http://localhost:8080/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Quiero que me respondas de forma más formal",
    "metadata": {
      "userId": "test-progressive-010"
    }
  }' | jq
```

### Segunda sesión (horas/días después)
```bash
curl -X POST http://localhost:8080/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Hola, ¿qué tal?",
    "metadata": {
      "userId": "test-progressive-010"
    }
  }' | jq
```

**Esperado**:
- La segunda respuesta debe mantener el tono FORMAL
- No debe preguntar de nuevo cómo quiere que le respondan

---

## 🧹 Limpieza de Datos de Prueba

```sql
-- Eliminar perfiles de prueba
DELETE FROM user_profile WHERE user_id LIKE 'test-progressive-%';

-- Eliminar historial de conversaciones de prueba
DELETE FROM conversation_history WHERE user_id LIKE 'test-progressive-%';

-- Verificar limpieza
SELECT COUNT(*) FROM user_profile WHERE user_id LIKE 'test-progressive-%';
```

---

## 📊 Script Bash para Ejecutar Todas las Pruebas

```bash
#!/bin/bash
# test-progressive-memory.sh

echo "🧪 Iniciando tests de Memoria Progresiva..."

BASE_URL="http://localhost:8080/chat"

# Test 1: Formato STEPS
echo ""
echo "Test 1: Formato STEPS"
curl -s -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Explícame en pasos cómo organizar un baby shower",
    "metadata": {"userId": "test-prog-001", "mode": "EVENT"}
  }' | jq -r '.aiResponse' | head -n 5

# Test 2: Ritmo QUICK
echo ""
echo "Test 2: Ritmo QUICK"
curl -s -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Rápido, dime 3 juegos para baby shower",
    "metadata": {"userId": "test-prog-002", "mode": "EVENT"}
  }' | jq -r '.aiResponse' | head -n 5

# Test 3: Formato LIST
echo ""
echo "Test 3: Formato LIST"
curl -s -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Dame ideas de decoración en lista",
    "metadata": {"userId": "test-prog-003", "mode": "EVENT"}
  }' | jq -r '.aiResponse' | head -n 5

# Test 4: Formato DIRECT
echo ""
echo "Test 4: Formato DIRECT"
curl -s -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Vamos al grano, ¿cuánto cuesta un baby shower?",
    "metadata": {"userId": "test-prog-004", "mode": "EVENT"}
  }' | jq -r '.aiResponse' | head -n 5

# Test 5: Ritmo EXPLAINED
echo ""
echo "Test 5: Ritmo EXPLAINED"
curl -s -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Explícame con ejemplos cómo elegir el tema",
    "metadata": {"userId": "test-prog-005", "mode": "EVENT"}
  }' | jq -r '.aiResponse' | head -n 5

# Test 6: Combinación
echo ""
echo "Test 6: Formal + Detallado + Pasos"
curl -s -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Quiero respuestas formales, detalladas y en pasos",
    "metadata": {"userId": "test-prog-006"}
  }' | jq -r '.aiResponse' | head -n 5

echo ""
echo "✅ Tests completados. Verificar perfiles en BD:"
echo "SELECT user_id, preferred_format, response_speed FROM user_profile WHERE user_id LIKE 'test-prog-%';"
```

### Uso del script
```bash
chmod +x test-progressive-memory.sh
./test-progressive-memory.sh
```

---

## 📈 Métricas de Éxito

Para cada test, verificar:

1. ✅ **Detección correcta**: El comando se detectó y guardó en BD
2. ✅ **Aplicación en System Prompt**: El perfil se inyecta en el prompt enviado a OpenAI
3. ✅ **Respuesta adaptada**: La IA responde según las preferencias (formato, tono, ritmo)
4. ✅ **Persistencia**: El perfil se mantiene entre sesiones

---

## 🐛 Debugging

### Ver logs de detección de comandos
```bash
# Buscar en logs:
"Comando detectado: preferredFormat=STEPS"
"Comando detectado: responseSpeed=QUICK"
"Comandos explícitos detectados en el mensaje, actualizando perfil"
```

### Ver perfiles creados
```sql
SELECT 
  user_id, 
  tone, 
  verbosity, 
  emoji_preference, 
  preferred_format, 
  response_speed,
  current_objective,
  past_decisions,
  last_updated_at
FROM user_profile 
ORDER BY last_updated_at DESC 
LIMIT 10;
```

### Ver historial de conversaciones
```sql
SELECT 
  user_id, 
  user_message, 
  ai_response, 
  created_at 
FROM conversation_history 
WHERE user_id LIKE 'test-progressive-%' 
ORDER BY created_at DESC;
```

---

**🎯 Tests de Memoria Progresiva - IA MSA DM Agent v2.0**
