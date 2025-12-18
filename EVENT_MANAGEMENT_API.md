# 🎉 API de Gestión de Eventos - Documentación

Sistema completo para gestionar eventos tipo Baby Shower con 29 endpoints REST.

## 📋 Tabla de Contenidos

- [Eventos](#eventos)
- [RSVPs (Confirmaciones)](#rsvps)
- [Regalos](#regalos)
- [Compromisos](#compromisos)
- [Ideas de Apoyo](#ideas)
- [Mensajes para el Bebé](#mensajes-bebé)
- [Dashboard](#dashboard)

---

## 🎪 Eventos

### 1️⃣ Obtener información pública de un evento
```http
GET /api/v1/events/{slug}
```

**Respuesta:**
```json
{
  "id": 1,
  "slug": "baby-shower-maria-2025",
  "name": "Baby Shower de María",
  "description": "Celebración del próximo bebé",
  "eventDate": "2025-12-25T15:00:00Z",
  "location": "Salón de Eventos Paradise",
  "locationUrl": "https://maps.google.com/...",
  "welcomeMessage": "¡Bienvenido al Baby Shower de María!",
  "imageUrl": "https://...",
  "allowSharedGifts": true,
  "allowBabyMessages": true,
  "allowIdeas": true
}
```

### 28️⃣ Crear evento (Organizador)
```http
POST /api/v1/events
Content-Type: application/json

{
  "slug": "baby-shower-maria-2025",
  "name": "Baby Shower de María",
  "description": "Celebración del próximo bebé",
  "eventDate": "2025-12-25T15:00:00Z",
  "location": "Salón de Eventos Paradise",
  "organizerUserId": "user123",
  "organizerName": "Carlos Pérez",
  "organizerEmail": "carlos@example.com",
  "allowSharedGifts": true,
  "allowBabyMessages": true,
  "allowIdeas": true
}
```

### 28️⃣ Actualizar evento (Organizador)
```http
PUT /api/v1/events/{eventId}
Content-Type: application/json

{
  "name": "Baby Shower de María - Actualizado",
  "eventDate": "2025-12-26T15:00:00Z",
  "isActive": true
}
```

---

## ✅ RSVPs (Confirmaciones de Asistencia)

### 4️⃣ Registrar confirmación de asistencia
```http
POST /api/v1/events/{eventId}/rsvp
Content-Type: application/json

{
  "userId": "user-phone-593999123456",
  "guestName": "Ana García",
  "guestEmail": "ana@example.com",
  "status": "YES",
  "guestsCount": 2,
  "notes": "Llego un poco tarde"
}
```

**Status posibles:** `YES`, `NO`, `PENDING`

### 5️⃣ Consultar mi RSVP
```http
GET /api/v1/events/{eventId}/rsvp/{userId}
```

### 6️⃣ Actualizar mi RSVP
```http
PUT /api/v1/events/{eventId}/rsvp/{userId}
Content-Type: application/json

{
  "status": "NO",
  "notes": "No podré asistir, disculpas"
}
```

### 7️⃣ Lista de confirmados (Organizador)
```http
GET /api/v1/events/{eventId}/attendees
```

### 8️⃣ Listado completo de RSVPs (Organizador)
```http
GET /api/v1/events/{eventId}/rsvps
```

**Respuesta:**
```json
{
  "rsvps": [...],
  "summary": {
    "totalYes": 25,
    "totalNo": 3,
    "totalPending": 5,
    "totalGuests": 45
  }
}
```

---

## 🎁 Regalos

### 🔟 Ver lista de regalos
```http
GET /api/v1/events/{eventId}/gifts
```

### 1️⃣1️⃣ Ver detalle de un regalo
```http
GET /api/v1/gifts/{giftId}
```

**Respuesta:**
```json
{
  "id": 1,
  "eventId": 1,
  "name": "Cuna de madera",
  "description": "Cuna convertible 3 en 1",
  "price": 350.00,
  "imageUrl": "https://...",
  "allowSplit": true,
  "priority": 1,
  "status": "PARTIALLY_FUNDED",
  "currentFunding": 150.00,
  "fundingPercentage": 42.86,
  "commitmentCount": 3
}
```

**Estados posibles:**
- `AVAILABLE`: Disponible
- `RESERVED`: Reservado completamente
- `PARTIALLY_FUNDED`: Parcialmente financiado
- `FULLY_FUNDED`: Totalmente financiado
- `INACTIVE`: Desactivado

### 1️⃣2️⃣ Reservar regalo completo
```http
POST /api/v1/gifts/{giftId}/reserve
Content-Type: application/json

{
  "userId": "user-593999123456",
  "guestName": "Pedro López",
  "guestEmail": "pedro@example.com",
  "notes": "Lo compraré en Amazon"
}
```

**Respuesta:**
```json
{
  "id": 1,
  "giftId": 5,
  "giftName": "Cuna de madera",
  "userId": "user-593999123456",
  "guestName": "Pedro López",
  "commitmentType": "FULL_RESERVATION",
  "contributionAmount": 350.00,
  "token": "abc123-def456-ghi789",
  "isActive": true,
  "createdAt": "2025-12-17T10:00:00Z"
}
```

### 1️⃣3️⃣ Aportar a regalo compartido
```http
POST /api/v1/gifts/{giftId}/contribute
Content-Type: application/json

{
  "userId": "user-593999654321",
  "guestName": "Laura Martínez",
  "contributionAmount": 50.00,
  "notes": "Aporte parcial"
}
```

### 1️⃣4️⃣ Consultar mi compromiso (por token)
```http
GET /api/v1/commitments/{token}
```

### 1️⃣5️⃣ Cancelar mi compromiso
```http
DELETE /api/v1/commitments/{token}
```

### 1️⃣6️⃣ Crear regalo (Organizador)
```http
POST /api/v1/events/{eventId}/gifts
Content-Type: application/json

{
  "name": "Pañales Huggies",
  "description": "Pack de pañales tamaño M",
  "price": 45.00,
  "allowSplit": true,
  "priority": 2,
  "quantity": 1
}
```

### 1️⃣8️⃣ Actualizar regalo (Organizador)
```http
PUT /api/v1/gifts/{giftId}
Content-Type: application/json

{
  "price": 50.00,
  "description": "Actualización de precio",
  "isActive": true
}
```

### 1️⃣9️⃣ Eliminar regalo (Organizador)
```http
DELETE /api/v1/gifts/{giftId}
```

### 2️⃣0️⃣ Resumen de regalos (Organizador)
```http
GET /api/v1/events/{eventId}/gifts/summary
```

**Respuesta:**
```json
{
  "totalGifts": 20,
  "availableGifts": 12,
  "reservedGifts": 5,
  "partiallyFundedGifts": 2,
  "fullyFundedGifts": 1,
  "totalBudget": 2500.00,
  "coveredBudget": 1200.00,
  "remainingBudget": 1300.00,
  "coveragePercentage": 48.00
}
```

---

## 💡 Ideas de Apoyo

### 2️⃣1️⃣ Proponer idea de apoyo
```http
POST /api/v1/events/{eventId}/ideas
Content-Type: application/json

{
  "userId": "user-593999123456",
  "guestName": "Sofía Rodríguez",
  "description": "Puedo ayudar con la decoración del salón"
}
```

### 2️⃣2️⃣ Ver ideas propuestas (Organizador)
```http
GET /api/v1/events/{eventId}/ideas
```

---

## 💬 Mensajes para el Bebé

### 2️⃣3️⃣ Enviar mensaje
```http
POST /api/v1/events/{eventId}/baby-messages
Content-Type: application/json

{
  "userId": "user-593999123456",
  "guestName": "Roberto Castro",
  "messageText": "¡Que seas muy feliz pequeñito! 👶❤️"
}
```

### 2️⃣4️⃣ Ver mensajes publicados
```http
GET /api/v1/events/{eventId}/baby-messages
```

### 2️⃣4️⃣ Ver todos los mensajes (Organizador)
```http
GET /api/v1/events/{eventId}/baby-messages?includeUnpublished=true
```

### 2️⃣5️⃣ Moderar mensaje (Organizador)
```http
PATCH /api/v1/baby-messages/{messageId}
Content-Type: application/json

{
  "isPublished": false
}
```

---

## 📊 Dashboard

### 2️⃣9️⃣ Dashboard completo (Organizador)
```http
GET /api/v1/events/{eventId}/dashboard
```

**Respuesta consolidada:**
```json
{
  "event": { ... },
  "rsvpSummary": {
    "totalYes": 25,
    "totalNo": 3,
    "totalPending": 5,
    "totalGuests": 45
  },
  "giftSummary": {
    "totalGifts": 20,
    "availableGifts": 12,
    "coveredBudget": 1200.00,
    "coveragePercentage": 48.00
  },
  "recentIdeas": [...],
  "totalBabyMessages": 15,
  "totalAttendees": 25,
  "pendingRSVPs": 5
}
```

---

## 🔒 Seguridad y Validaciones

### Validaciones implementadas:

- **RSVP**: Solo un RSVP por usuario por evento
- **Reservas**: No permitir reservar regalos ya reservados (excepto si allowSplit=true)
- **Aportes**: No exceder el precio del regalo
- **Tokens**: Validación de tokens únicos para commitments
- **Estados**: Actualización automática de estados de regalos según financiamiento

### Reglas de negocio:

1. Un usuario solo puede tener un RSVP por evento
2. Un usuario puede tener múltiples commitments (diferentes regalos)
3. Los regalos con `allowSplit=false` solo permiten una reserva completa
4. Los regalos con `allowSplit=true` permiten múltiples aportes hasta alcanzar el precio
5. Los mensajes para el bebé son públicos por defecto (moderación opcional)
6. Las ideas de apoyo requieren aprobación del organizador (opcional)

---

## 🚀 Flujo de Uso Completo

### Para Invitados:

1. **Escanear QR** → `GET /api/v1/events/{slug}` - Ver info del evento
2. **Chat MCG** → `POST /api/v1/chat` - Interacción conversacional
3. **Confirmar asistencia** → `POST /api/v1/events/{eventId}/rsvp`
4. **Ver regalos** → `GET /api/v1/events/{eventId}/gifts`
5. **Reservar/Aportar** → `POST /api/v1/gifts/{giftId}/reserve` o `/contribute`
6. **Dejar mensaje** → `POST /api/v1/events/{eventId}/baby-messages`
7. **Proponer idea** → `POST /api/v1/events/{eventId}/ideas`

### Para Organizadores:

1. **Crear evento** → `POST /api/v1/events`
2. **Configurar regalos** → `POST /api/v1/events/{eventId}/gifts`
3. **Monitorear RSVPs** → `GET /api/v1/events/{eventId}/rsvps`
4. **Ver resumen** → `GET /api/v1/events/{eventId}/dashboard`
5. **Moderar mensajes** → `PATCH /api/v1/baby-messages/{messageId}`

---

## 📝 Notas de Implementación

- Todas las fechas usan ISO 8601 con timezone (OffsetDateTime)
- Los precios usan DECIMAL(10,2) para evitar errores de redondeo
- Los tokens son UUID únicos para seguridad sin autenticación
- Las validaciones usan Jakarta Validation (`@Valid`, `@NotBlank`, etc.)
- Todos los endpoints tienen logging estructurado
- Los servicios son transaccionales (`@Transactional`)
- Los controladores NO tienen lógica de negocio (solo orquestación)

---

**✨ Sistema completo con 29 endpoints implementados siguiendo principios SOLID y Clean Architecture.**
