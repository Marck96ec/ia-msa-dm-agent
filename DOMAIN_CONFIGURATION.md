# 🔧 Configuración de Dominios Permitidos

## 📖 Descripción

Los **dominios permitidos** son palabras clave que se usan para validar el alcance (scope) de las conversaciones cuando `mode=EVENT`. Si el mensaje del usuario no contiene ninguna keyword permitida, el sistema redirige al usuario con un mensaje contextual.

Los dominios se almacenan en la tabla `allowed_domain` en PostgreSQL y se cachean en memoria para optimizar el rendimiento.

---

## 🗄️ Estructura de la Tabla

### allowed_domain

| Columna | Tipo | Descripción |
|---------|------|-------------|
| `id` | BIGSERIAL | ID autoincremental |
| `keyword` | VARCHAR(100) | Palabra clave (ej: "carros", "baby shower") |
| `category` | VARCHAR(50) | Categoría para agrupar (ej: "automotive", "baby-shower") |
| `description` | TEXT | Descripción opcional |
| `active` | BOOLEAN | Si está activo para validación |
| `created_at` | TIMESTAMP | Fecha de creación |
| `updated_at` | TIMESTAMP | Última actualización |

---

## 🚀 Cambiar de Baby Shower a Carros

### Opción 1: SQL directo (más rápido)

```sql
-- 1. Desactivar todos los dominios de baby-shower
UPDATE allowed_domain 
SET active = false 
WHERE category = 'baby-shower';

-- 2. Insertar dominios de carros/automotive
INSERT INTO allowed_domain (keyword, category, description, active) VALUES
    ('carros', 'automotive', 'Tema principal', true),
    ('autos', 'automotive', 'Sinónimo de carros', true),
    ('vehículos', 'automotive', 'Término formal', true),
    ('automóviles', 'automotive', 'Término técnico', true),
    ('coches', 'automotive', 'Variante española', true),
    ('automotriz', 'automotive', 'Industria', true),
    ('mecánica', 'automotive', 'Aspecto técnico', true),
    ('motor', 'automotive', 'Componente', true),
    ('mantenimiento', 'automotive', 'Cuidado del vehículo', true),
    ('conducción', 'automotive', 'Actividad', true),
    ('velocidad', 'automotive', 'Característica', true),
    ('marca', 'automotive', 'Fabricante', true),
    ('modelo', 'automotive', 'Tipo de vehículo', true),
    ('reparación', 'automotive', 'Servicio', true),
    ('repuestos', 'automotive', 'Piezas', true)
ON CONFLICT (keyword) DO NOTHING;
```

### Opción 2: Usar AllowedDomainService (programático)

```java
@Service
@RequiredArgsConstructor
public class DomainMigrationService {
    
    private final AllowedDomainService allowedDomainService;
    
    public void switchToBabyShowerToAutomotive() {
        // Desactivar baby-shower
        allowedDomainService.getByCategory("baby-shower")
            .forEach(domain -> allowedDomainService.setActive(domain.getId(), false));
        
        // Crear dominios de automotive
        allowedDomainService.create("carros", "automotive", "Tema principal");
        allowedDomainService.create("autos", "automotive", "Sinónimo de carros");
        allowedDomainService.create("vehículos", "automotive", "Término formal");
        // ... etc
        
        // Limpiar caché para aplicar cambios inmediatamente
        allowedDomainService.clearCache();
    }
}
```

---

## 🔄 Gestión de Dominios en Runtime

### Agregar un nuevo dominio

```java
allowedDomainService.create("híbrido", "automotive", "Tipo de motor");
```

### Desactivar temporalmente un dominio

```java
// Obtener ID del dominio
Long domainId = 15L;
allowedDomainService.setActive(domainId, false);
```

### Reactivar un dominio

```java
allowedDomainService.setActive(domainId, true);
```

### Eliminar permanentemente

```java
allowedDomainService.delete(domainId);
```

### Limpiar caché manualmente

```java
// Útil después de cambios masivos directos en BD
allowedDomainService.clearCache();
```

---

## 📊 Consultas Útiles

### Ver todos los dominios activos

```sql
SELECT * FROM allowed_domain 
WHERE active = true 
ORDER BY category, keyword;
```

### Contar dominios por categoría

```sql
SELECT category, COUNT(*) as total 
FROM allowed_domain 
WHERE active = true 
GROUP BY category;
```

### Buscar keyword específica

```sql
SELECT * FROM allowed_domain 
WHERE keyword ILIKE '%carro%';
```

---

## 🎯 Ejemplos de Categorías

### 1. Baby Shower (evento infantil)

```
Keywords: baby shower, babyshower, bebé, mamá, embarazo, invitados, 
          regalos, juegos, decoración, planificación, celebración
Category: baby-shower
```

### 2. Automotive (carros/vehículos)

```
Keywords: carros, autos, vehículos, mecánica, motor, mantenimiento,
          conducción, velocidad, marca, modelo, reparación, repuestos
Category: automotive
```

### 3. Wedding (bodas)

```
Keywords: boda, wedding, matrimonio, novia, novio, ceremonia, recepción,
          invitaciones, vestido, traje, decoración, banquete, luna de miel
Category: wedding
```

### 4. Real Estate (bienes raíces)

```
Keywords: casa, departamento, propiedad, inmueble, venta, alquiler,
          hipoteca, inversión, ubicación, metros cuadrados, habitaciones
Category: real-estate
```

---

## ⚡ Performance y Caching

### Cache Configuration

Los dominios permitidos se cachean automáticamente usando Spring Cache:

```java
@Cacheable(value = "allowedDomains", key = "'all'")
public List<String> getAllowedKeywords() {
    // Resultado cacheado en memoria
}
```

**Ventajas:**
- ✅ Solo se consulta BD la primera vez
- ✅ Consultas subsecuentes usan memoria (mucho más rápido)
- ✅ Cache se invalida automáticamente al crear/actualizar/eliminar dominios

**Cache eviction:**

```java
@CacheEvict(value = "allowedDomains", allEntries = true)
public void create(...) {
    // Limpia cache después de crear
}
```

---

## 🧪 Testing con Diferentes Dominios

### Test 1: Baby Shower (dominio activo)

```json
POST /api/v1/chat
{
  "message": "¿Qué juegos recomiendas para el baby shower?",
  "metadata": {
    "mode": "EVENT",
    "domainId": "baby-shower"
  }
}
```

**Resultado:** ✅ ALLOW (contiene "baby shower")

---

### Test 2: Fuera de alcance (baby-shower activo)

```json
POST /api/v1/chat
{
  "message": "¿Cuál es la capital de Francia?",
  "metadata": {
    "mode": "EVENT",
    "domainId": "baby-shower"
  }
}
```

**Resultado:** 🔀 REDIRECT

---

### Test 3: Automotive (después de cambiar dominios)

```json
POST /api/v1/chat
{
  "message": "¿Qué mantenimiento necesita mi carro?",
  "metadata": {
    "mode": "EVENT",
    "domainId": "automotive"
  }
}
```

**Resultado:** ✅ ALLOW (contiene "carro" y "mantenimiento")

---

## 🛠️ API REST para Gestión de Dominios (Futuro)

Puedes crear un controller para gestionar dominios vía API:

```java
@RestController
@RequestMapping("/api/v1/admin/domains")
public class DomainManagementController {
    
    @PostMapping
    public AllowedDomain create(@RequestBody CreateDomainRequest request) {
        return allowedDomainService.create(
            request.keyword(), 
            request.category(), 
            request.description()
        );
    }
    
    @PutMapping("/{id}/active")
    public void setActive(@PathVariable Long id, @RequestParam boolean active) {
        allowedDomainService.setActive(id, active);
    }
    
    @GetMapping
    public List<AllowedDomain> getAll() {
        return allowedDomainService.getAll();
    }
}
```

---

## 📝 Checklist para Cambiar de Dominio

- [ ] Decidir nueva categoría (ej: "automotive")
- [ ] Listar keywords relevantes (mínimo 10-15)
- [ ] Ejecutar SQL para desactivar dominio anterior
- [ ] Ejecutar SQL para insertar nuevo dominio
- [ ] Verificar con: `SELECT * FROM allowed_domain WHERE active = true`
- [ ] Limpiar caché: `allowedDomainService.clearCache()`
- [ ] Probar con request de prueba (mensaje dentro/fuera de alcance)
- [ ] Verificar logs: `Cargadas X keywords de dominios permitidos`

---

**Autor**: IA Project Team  
**Fecha**: 2025-12-17  
**Versión**: 1.0.0
