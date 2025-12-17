# IA MSA DM Agent - API REST con Spring AI

## 📋 Descripción

**IA MSA DM Agent** es una API REST construida con Spring Boot 3.3.0 y **Spring AI 1.1.2** que facilita la integración de modelos de lenguaje (LLM) en aplicaciones Java empresariales.

### 🎯 Enfoque API-First

Este proyecto sigue el patrón **API-First**, donde:

1. **La especificación OpenAPI es la fuente de verdad** (`src/main/resources/openapi/api-spec.yaml`)
2. **Los modelos y controladores se generan automáticamente** mediante OpenAPI Generator
3. **El contrato de la API se define antes del código**, garantizando consistencia
4. **Documentación Swagger UI** disponible automáticamente en `/swagger-ui.html`

**Beneficios:**
- ✅ Contrato de API versionado y documentado
- ✅ Generación automática de modelos con validación
- ✅ Interfaces de controladores type-safe
- ✅ Documentación siempre sincronizada con el código
- ✅ Fácil integración con clientes (generación de SDKs)

### ¿Qué es Spring AI?

**Spring AI** es un framework de Spring diseñado específicamente para simplificar el desarrollo de aplicaciones que integran Inteligencia Artificial. Proporciona:

- **Abstracción unificada** para múltiples proveedores de IA (OpenAI, Azure OpenAI, Anthropic, Ollama, etc.)
- **Integración nativa** con el ecosistema Spring (inyección de dependencias, configuración, logging)
- **Gestión automática** de prompts, conversaciones y contextos
- **Portabilidad**: cambia de proveedor sin modificar tu código de negocio
- **RAG (Retrieval Augmented Generation)** integrado para documentos
- **Function Calling**: permite que el modelo ejecute funciones de tu aplicación

## 🎯 ¿Para qué sirve este proyecto?

Este proyecto es un **starter template** que te permite:

1. **Chatear con modelos de IA** a través de una API REST profesional
2. **Integrar IA en microservicios** de forma estandarizada
3. **Construir agentes conversacionales** con memoria de contexto
4. **Experimentar con diferentes modelos** (GPT-4, Azure OpenAI, Ollama local)
5. **Escalar hacia soluciones empresariales** con arquitectura mantenible

### Casos de uso típicos:

- ✅ Asistentes virtuales para aplicaciones empresariales
- ✅ Análisis y procesamiento de documentos
- ✅ Generación de contenido automatizado
- ✅ Chatbots con lógica de negocio integrada
- ✅ Sistemas de recomendación inteligentes
- ✅ Procesamiento de lenguaje natural (NLP)

## 🏗️ Arquitectura

```
ia-msa-dm-agent/
├── src/main/
│   ├── java/com/iaproject/agent/
│   │   ├── IaMsaDmAgentApplication.java    # Clase principal
│   │   ├── config/
│   │   │   └── SpringAiConfig.java         # Configuración de Spring AI
│   │   ├── controller/
│   │   │   ├── ChatController.java         # Implementa ChatApi (generada)
│   │   │   └── GlobalExceptionHandler.java # Manejo de errores
│   │   └── service/
│   │       └── ChatService.java            # Lógica de negocio
│   └── resources/
│       ├── openapi/
│       │   └── api-spec.yaml               # ⭐ Especificación OpenAPI
│       └── application.yml                 # Configuración
├── build/generated/                        # Código generado (Git ignored)
│   └── src/main/java/com/iaproject/agent/
│       ├── api/ChatApi.java                # Interfaz generada
│       └── model/                          # Modelos generados
│           ├── ChatRequest.java
│           ├── ChatResponse.java
│           ├── TokenUsage.java
│           └── ErrorResponse.java
└── build.gradle                        # Configuración Gradle + OpenAPI Generator
```

### Flujo API-First:

1. **Diseñar API** → Editar `api-spec.yaml`
2. **Generar código** → `./gradlew openApiGenerate`
3. **Implementar** → Controladores implementan interfaces generadas
4. **Compilar** → `./gradlew build` (genera código automáticamente)
5. **Documentar** → Swagger UI en `/swagger-ui.html`

### Principios aplicados:

- **Separación de capas** (Controller → Service → AI Client)
- **Inyección de dependencias** para testabilidad
- **Configuración externalizada** (variables de entorno)
- **Manejo centralizado de excepciones**
- **Logging estructurado**
- **Validación de entrada** con Jakarta Bean Validation

## 🚀 Requisitos Previos

- **Java 17** o superior
- **Gradle 8.5+** (incluido Gradle Wrapper)
- **API Key** de OpenAI, Azure OpenAI o Ollama instalado localmente

## ⚙️ Configuración

### 1. Clonar y configurar variables de entorno

```bash
# Copiar el archivo de ejemplo
cp .env.example .env

# Editar .env con tu API key
OPENAI_API_KEY=sk-tu-api-key-real-aqui
```

### 2. Opciones de proveedor de IA

#### Opción A: OpenAI (Recomendado para empezar)

```yaml
spring:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY}
      chat:
        options:
          model: gpt-4o-mini  # o gpt-4, gpt-3.5-turbo
```

#### Opción B: Azure OpenAI

```yaml
spring:
  ai:
    azure:
      openai:
        api-key: ${AZURE_OPENAI_API_KEY}
        endpoint: ${AZURE_OPENAI_ENDPOINT}
```

#### Opción C: Ollama (Local, gratuito)

```bash
# Instalar Ollama
# https://ollama.ai

# Descargar un modelo
ollama pull llama2
```

```yaml
spring:
  ai:
    ollama:
      base-url: http://localhost:11434
      chat:
        options:
          model: llama2
```

## 🔧 Ejecución

### Generar código desde OpenAPI (opcional)

```bash
# Windows
.\gradlew.bat openApiGenerate

# Linux/Mac
./gradlew openApiGenerate
```

**Nota:** El código se genera automáticamente al compilar.

### Con Gradle Wrapper (Recomendado)

```bash
# Windows
.\gradlew.bat bootRun

# Linux/Mac
./gradlew bootRun
```

### Compilar y ejecutar JAR

```bash
# Windows
.\gradlew.bat build
java -jar build/libs/ia-msa-dm-agent-1.0.0.jar

# Linux/Mac
./gradlew build
java -jar build/libs/ia-msa-dm-agent-1.0.0.jar
```

La aplicación estará disponible en `http://localhost:8080`

### Acceder a Swagger UI

```
http://localhost:8080/swagger-ui.html
```

Swagger UI proporciona:
- 📖 Documentación interactiva de la API
- 🧪 Pruebas en vivo de endpoints
- 📦 Esquemas de modelos
- ✅ Validación de requests/responses

## 📡 Endpoints de la API

### 1. Chat completo (POST)

**Endpoint:** `POST /api/v1/chat`

**Request Body:**
```json
{
  "message": "Explícame qué es Spring AI en 3 párrafos",
  "conversationId": "conv-123",
  "temperature": 0.7,
  "maxTokens": 500
}
```

**Response:**
```json
{
  "response": "Spring AI es un framework...",
  "conversationId": "conv-123",
  "timestamp": "2025-12-17T10:30:00",
  "tokenUsage": {
    "promptTokens": 15,
    "completionTokens": 120,
    "totalTokens": 135
  }
}
```

**Parámetros:**
- `message` (required): Tu pregunta o prompt
- `conversationId` (optional): ID para mantener contexto entre llamadas
- `temperature` (optional): Creatividad (0.0-2.0, default 0.7)
- `maxTokens` (optional): Límite de tokens en la respuesta

### 2. Chat simple (GET)

**Endpoint:** `GET /api/v1/chat/simple?message=Hola`

**Response:** Texto plano con la respuesta del modelo

### 3. Health Check

**Endpoint:** `GET /actuator/health`

## 🧪 Pruebas con cURL

```bash
# Chat simple
curl "http://localhost:8080/api/v1/chat/simple?message=Hola,%20cómo%20estás?"

# Chat completo
curl -X POST http://localhost:8080/api/v1/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Genera un poema corto sobre la programación",
    "temperature": 0.9
  }'
```

## 🧪 Pruebas con Postman/Insomnia

1. **Importar colección** (puedes crear una con los ejemplos de arriba)
2. **Configurar variable de entorno** `BASE_URL=http://localhost:8080`
3. **Ejecutar requests** contra `/api/v1/chat`

## 📊 Monitoreo y Observabilidad

El proyecto incluye **Spring Boot Actuator** para métricas:

```bash
# Health check
curl http://localhost:8080/actuator/health

# Métricas
curl http://localhost:8080/actuator/metrics
```

Los logs incluyen:
- ✅ Nivel de logs configurable por paquete
- ✅ Trazabilidad de requests
- ✅ Información de uso de tokens
- ✅ Errores detallados

## 🔒 Seguridad

### Buenas prácticas implementadas:

- ✅ **API Keys en variables de entorno** (nunca en código)
- ✅ **Validación de entrada** con Jakarta Validation
- ✅ **Manejo seguro de excepciones** (no expone detalles internos)
- ✅ **HTTPS recomendado en producción**

### Para producción, considera agregar:

- 🔐 Spring Security (OAuth2, JWT)
- 🛡️ Rate limiting
- 🔍 API Gateway
- 📝 Auditoría de requests

## 🧩 Extensiones Comunes

### 1. Agregar memoria conversacional

```java
@Service
public class ConversationMemoryService {
    private final Map<String, List<Message>> conversations = new ConcurrentHashMap<>();
    
    public void addMessage(String conversationId, Message message) {
        conversations.computeIfAbsent(conversationId, k -> new ArrayList<>()).add(message);
    }
}
```

### 2. Implementar Function Calling

```java
@Configuration
public class FunctionConfig {
    @Bean
    @Description("Obtiene el clima actual de una ciudad")
    public Function<WeatherRequest, WeatherResponse> weatherFunction() {
        return request -> weatherService.getWeather(request.getCity());
    }
}
```

### 3. Agregar RAG (Retrieval Augmented Generation)

```gradle
dependencies {
    implementation 'org.springframework.ai:spring-ai-pdf-document-reader'
}
```

## 📚 Recursos Adicionales

- [Documentación oficial de Spring AI](https://docs.spring.io/spring-ai/reference/)
- [GitHub de Spring AI](https://github.com/spring-projects/spring-ai)
- [Ejemplos oficiales](https://github.com/spring-projects/spring-ai-examples)
- [OpenAI API Reference](https://platform.openai.com/docs/api-reference)

## 🤝 Contribuir

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -m 'Agrega nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.

## 👨‍💻 Autor

Proyecto creado como template para integración de Spring AI en aplicaciones empresariales.

---

## 🎓 Conceptos Clave de Spring AI

### ChatClient

El `ChatClient` es la abstracción principal de Spring AI:

```java
ChatClient chatClient = ChatClient.builder()
    .defaultSystem("Eres un asistente útil")
    .build();

String response = chatClient
    .prompt()
    .user("¿Qué es Spring Boot?")
    .call()
    .content();
```

### Options (Configuración de modelos)

```java
OpenAiChatOptions options = OpenAiChatOptions.builder()
    .model("gpt-4o-mini")
    .temperature(0.7)      // Creatividad (0-2)
    .maxTokens(1000)       // Límite de tokens
    .topP(0.9)             // Nucleus sampling
    .frequencyPenalty(0.5) // Penalización de repetición
    .build();
```

### Portabilidad

Cambiar de OpenAI a Azure OpenAI solo requiere cambiar la configuración:

```yaml
# Antes: OpenAI
spring.ai.openai.api-key=sk-...

# Después: Azure OpenAI
spring.ai.azure.openai.api-key=...
spring.ai.azure.openai.endpoint=https://...
```

**El código de tu aplicación NO cambia** ✨

---

¿Preguntas? Abre un issue en el repositorio.
